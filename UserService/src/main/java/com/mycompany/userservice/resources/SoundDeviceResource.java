package com.mycompany.userservice.resources;

import entiteti.Alarm;
import entiteti.Song;
import entiteti.User;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import alarmrequest.AlarmRequest;
import deviceRequest.DeviceRequest;
import deviceRequest.DeviceSendRequest;
import javax.jms.JMSConsumer;
import javax.jms.Topic;

/**
 *
 * @author
 */
@Path("device")
@Stateless
public class SoundDeviceResource {

    @PersistenceContext
    EntityManager em;

    @GET
    public Response play(@Context HttpHeaders httpHeaders,
            @QueryParam("song") String song) {
        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        int userId = -1;
        if (authHeaderValues != null && authHeaderValues.size() > 0) {
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")), StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            //String password = stringTokenizer.nextToken();

            User user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();

            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            userId = user.getId();
        }
        try {
            javax.naming.Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("SoundDevice");
            Queue queue = (Queue) context.lookup("SoundDeviceQueue");

            JMSContext jmsContext = connectionFactory.createContext();
            JMSProducer producer = jmsContext.createProducer();

            DeviceRequest request = new DeviceRequest(userId, DeviceRequest.Type.PLAY_SONG);
            request.setSongName(song);
            ObjectMessage objMsg = jmsContext.createObjectMessage(request);
            producer.send(queue, objMsg);
            return Response
                    .ok().entity("Song: " + song)
                    .build();
        } catch (Exception e) {
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("playlist")
    public Response playlist(@Context HttpHeaders httpHeaders) {
        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        int userId = -1;
        if (authHeaderValues != null && authHeaderValues.size() > 0) {
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")), StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            //String password = stringTokenizer.nextToken();

            User user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();

            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            userId = user.getId();
        }
        try {
            javax.naming.Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("SoundDevice");
            Queue queue = (Queue) context.lookup("SoundDeviceQueue");
            //Topic topic = (Topic) context.lookup("SoundDeviceTopic");

            JMSContext jmsContext = connectionFactory.createContext();
            JMSProducer producer = jmsContext.createProducer();
            //JMSConsumer consumer = jmsContext.createSharedDurableConsumer(topic,"sub"+userId,"userId="+userId);

            DeviceRequest request = new DeviceRequest(userId, DeviceRequest.Type.PLAYLIST_USER);
            ObjectMessage objMsg = jmsContext.createObjectMessage(request);
            producer.send(queue, objMsg);
            //ObjectMessage msgRec=(ObjectMessage) consumer.receive();
            //DeviceSendRequest req=(DeviceSendRequest) msgRec.getObject();
            //List<String> results = req.getResults();
            //StringBuilder sb=new StringBuilder();
//            for (String s:results){
//                sb.append(s);
//                sb.append("\n");
//            }
//            return Response.ok().entity(sb.toString()).build();
            return Response
                    .ok().entity("Zahtev poslat ")
                    .build();
        } catch (Exception e) {
        }
        return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @GET
    @Path("result")
    public Response result(@Context HttpHeaders httpHeaders) {
        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        int userId = -1;
        if (authHeaderValues != null && authHeaderValues.size() > 0) {
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")), StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            //String password = stringTokenizer.nextToken();

            User user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();

            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            userId = user.getId();
        }
        try {
            javax.naming.Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("SoundDevice");
            //Topic topic = (Topic) context.lookup("SoundDeviceTopic");
            Queue topic=(Queue) context.lookup("SoundDeviceReportQueue");
            JMSContext jmsContext = connectionFactory.createContext();
            //JMSConsumer consumer = jmsContext.createSharedDurableConsumer(topic, "sub" + userId, "userId=" + userId);
            JMSConsumer consumer=jmsContext.createConsumer(topic);
            ObjectMessage msgRec = (ObjectMessage) consumer.receive();
            DeviceSendRequest req = (DeviceSendRequest) msgRec.getObject();
            List<String> results = req.getResults();
            StringBuilder sb = new StringBuilder();
            for (String s : results) {
                sb.append(s);
                sb.append("\n");
            }
            return Response.ok().entity(sb.toString()).build();

        } catch (Exception e) {
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
