/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

import alarmrequest.AlarmRequest;
import entiteti.Alarm;
import entiteti.Song;
import entiteti.User;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import timeCalculation.timeCalc;
import yt.YTBrowser;
import yt.YTSearchResult;

/**
 *
 * @author Aleksandar
 */
public class AlarmManager {

    static int refreshTime = 60000;
    static EntityManager em;

    @Resource(lookup = "Alarm")
    private static ConnectionFactory connectionFactory;

    @Resource(lookup = "AlarmQueue")
    private static Queue alarmQueue;

    static JMSConsumer consumer;
    static JMSContext context;

    public AlarmManager() {
        context = connectionFactory.createContext();
        consumer = context.createConsumer(alarmQueue);

        em = Persistence.createEntityManagerFactory("AlarmPU").createEntityManager();
        consumer.setMessageListener((message) -> {

            try {
                System.out.println("Poruka primljena");
                try {
                    em.getTransaction().begin();
                } catch (Exception e) {
                }
                ObjectMessage objMsg = (ObjectMessage) message;
                AlarmRequest request = (AlarmRequest) objMsg.getObject();

                //check user exists
                Query query = em.createNamedQuery("User.findById");
                query.setParameter("id", request.getUserId());
                List<User> users = query.getResultList();
                if (users.size() == 0) {
                    em.getTransaction().commit();
                    System.out.println("Vratio se");
                    return;
                }

                Song song = null;

                if (request.getSong() != null && !request.getSong().equals("")) {
                    YTSearchResult searchYT = YTBrowser.searchYT(request.getSong());
                    query = em.createNamedQuery("Song.findByUrl");
                    query.setParameter("url", searchYT.getUrl());
                    List<Song> songs = query.getResultList();
                    if (songs.size() == 0) {
                        song = new Song();
                        song.setName(request.getSong());
                        song.setUrl(searchYT.getUrl());
                        em.persist(song);
                        em.getTransaction().commit();
                        em.getTransaction().begin();
                    } else {
                        song = songs.get(0);
                    }
                }
                Alarm alarm = null;
                //set alarm in one moment
                if (request.getType() == AlarmRequest.Type.RING) {
                    Boolean status = Boolean.FALSE;
                    if (request.getStatus() == 1) {
                        status = Boolean.TRUE;
                    }
                    alarm = new Alarm(request.getName(), request.getTimeRing(), request.getRepetitionTotal(), status,
                            request.getRepetitionCount(), song, users.get(0));
                } //set ring of one alarm
                else if (request.getType() == AlarmRequest.Type.CHANGE) {
                    Query q = em.createQuery("Select a from Alarm a where a.id=:id", Alarm.class);
                    q.setParameter("id", request.getAlarmId());
                    List<Alarm> alarms = q.getResultList();
                    if (alarms.size() > 0) {
                        alarm = alarms.get(0);
                        alarm.setSongId(song);
                    } else {
                        System.err.println("Ne postoji dati alarm");
                        return;
                    }
                    em.merge(alarm);
                    em.getTransaction().commit();
                    return;
                }

                em.persist(alarm);
                em.getTransaction().commit();
                System.err.println("Upisan novi alarm");
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    private void ring(Alarm alarm) {
        if (alarm.getSongId() != null) {
            String urlAdress = alarm.getSongId().getUrl();
            URL url;
            try {
                System.out.println("ring");
                url = new URL(urlAdress);
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(url.toURI());
            } catch (MalformedURLException ex) {
                Logger.getLogger(AlarmManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(AlarmManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AlarmManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                File defaultSong = new File("default.mp3");
                Desktop.getDesktop().browse(defaultSong.toURI());
            } catch (IOException ex) {
                Logger.getLogger(AlarmManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void checkAlarms() {
        try {
            em.getTransaction().begin();
        } catch (Exception e) {
        }
        List<Alarm> alarms = em.createQuery("Select a from Alarm a WHERE a.status=1", Alarm.class).getResultList();
        System.out.println("provera ");
        //Date timeNow=
        for (Alarm alarm : alarms) {
            if (alarm.getRepetitionTotal() == 1) {
                //check if time is now
                LocalDateTime alarmTime = LocalDateTime.ofInstant(alarm.getTimeRing().toInstant(), ZoneId.systemDefault());
                if (!timeCalculation.timeCalc.isEqual(alarmTime)) {
                    continue;
                } else {
                    ring(alarm);
                }
                alarm.setStatus(Boolean.FALSE);
                em.merge(alarm);
                em.getTransaction().commit();
                em.getTransaction().begin();
            } else if (alarm.getRepetitionCount() != alarm.getRepetitionTotal()) {
                LocalDateTime alarmTime = LocalDateTime.ofInstant(alarm.getTimeRing().toInstant(), ZoneId.systemDefault());
                if (timeCalc.isEqual(alarmTime)) {
                    ring(alarm);
                    alarm.setRepetitionCount(alarm.getRepetitionCount() + 1);
                    em.merge(alarm);
                    em.getTransaction().commit();
                    em.getTransaction().begin();
                } else if (alarm.getRepetitionCount() > 0 && alarm.getRepetitionCount() < alarm.getRepetitionTotal()) {
                    ring(alarm);
                    alarm.setRepetitionCount(alarm.getRepetitionCount() + 1);
                    if (alarm.getRepetitionCount() == alarm.getRepetitionTotal()) {
                        alarm.setStatus(Boolean.FALSE);
                    }
                    em.merge(alarm);
                    em.getTransaction().commit();
                    em.getTransaction().begin();
                }

            }

        }
    }

    public static void main(String[] args) {

        AlarmManager alarm = new AlarmManager();
        while (true) {
            try {
                alarm.checkAlarms();
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(AlarmManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
