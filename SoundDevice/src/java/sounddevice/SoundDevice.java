/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sounddevice;

import deviceRequest.DeviceRequest;
import deviceRequest.DeviceSendRequest;
import eniteti.Playlist;
import eniteti.Song;
import eniteti.User;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import yt.YTBrowser;
import yt.YTSearchResult;

/**
 *
 * @author Aleksandar
 */
public class SoundDevice {

    static EntityManager em;

    @Resource(lookup = "SoundDevice")
    static ConnectionFactory connectionFactory;

    @Resource(lookup = "SoundDeviceTopic")
    static Topic topic;

    @Resource(lookup = "SoundDeviceQueue")
    static Queue queueReceive;

    @Resource(lookup = "SoundDeviceReportQueue")
    static Queue queueSend;

    static YTBrowser ytBrowser;

    private User userExists(DeviceRequest request) {

        Query userQuery = em.createNamedQuery("User.findById");
        userQuery.setParameter("id", request.getUserId());
        List<User> listUsers = userQuery.getResultList();
        if (listUsers.isEmpty()) {
            return null;
        }
        User user = listUsers.get(0);
        return user;
    }

    public SoundDevice() {
        em = Persistence.createEntityManagerFactory("SoundDevicePU").createEntityManager();
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queueReceive);
        JMSProducer producer = context.createProducer();
        consumer.setMessageListener((message) -> {

            try {
                System.out.println("prima");
                ObjectMessage obj = (ObjectMessage) message;
                DeviceRequest request = (DeviceRequest) obj.getObject();
                if (request.getType() == DeviceRequest.Type.PLAY_SONG) {
                    searchAndPlaySong(request);

                } else if (request.getType() == DeviceRequest.Type.PLAYLIST_USER) {
                    Integer id = request.getUserId();
                    em.getTransaction().begin();
                    System.out.println(id);
                    User user = em.createNamedQuery("User.findById", User.class).setParameter("id", id).getSingleResult();
                    Query query = em.createQuery("Select p.songId from Playlist p where p.userId=:id", Song.class);
                    query.setParameter("id", user);
                    List<Song> rs = query.getResultList();
                    List<String> finalResults = new ArrayList<>();
                    for (Song r : rs) {
                        finalResults.add(r.getName());
                    }
                    em.getTransaction().commit();
                    DeviceSendRequest sendMessage = new DeviceSendRequest(id, finalResults);
                    ObjectMessage objMsg = context.createObjectMessage(sendMessage);
                    //objMsg.setIntProperty("userId", user.getId());
                    //producer.send(topic, objMsg);
                    producer.send(queueSend,objMsg);
                    System.out.println("Poslat");
                }
            } catch (JMSException ex) {
                Logger.getLogger(SoundDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    private void searchAndPlaySong(DeviceRequest request) {
        //check if request is correct
        try {
            em.getTransaction().begin();
        } catch (Exception e) {
        }

        User user = userExists(request);
        if (user == null) {
            return;
        }
        YTSearchResult searchYT = YTBrowser.searchYT(request.getSongName());
        Query query = em.createNamedQuery("Song.findByUrl");
        query.setParameter("url", searchYT.getUrl());
        List<Song> songs = query.getResultList();
        Song song = null;
        if (songs.isEmpty()) {
            song = new Song();
            song.setName(request.getSongName());
            song.setUrl(searchYT.getUrl());
            em.persist(song);
            em.getTransaction().commit();
            em.getTransaction().begin();
        } else {
            song = songs.get(0);
        }
        Playlist playlist = new Playlist();
        playlist.setSongId(song);
        playlist.setUserId(user);
        em.persist(playlist);
        em.getTransaction().commit();
        //play song
        if (song != null) {
            URL url;
            try {
                url = new URL(song.getUrl());
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(url.toURI());
            } catch (MalformedURLException ex) {
                Logger.getLogger(SoundDevice.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(SoundDevice.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SoundDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                File defaultSong = new File("default.mp3");
                Desktop.getDesktop().browse(defaultSong.toURI());
            } catch (IOException ex) {
                Logger.getLogger(SoundDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        SoundDevice device = new SoundDevice();
        while (true) {
        }
    }

}
