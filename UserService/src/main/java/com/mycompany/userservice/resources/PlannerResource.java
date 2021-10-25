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
import javax.ws.rs.DELETE;
import request.PlannerRequest;
import request.PlannerResponse;

/**
 *
 * @author
 */
@Path("planer")
@Stateless
public class PlannerResource {

    @PersistenceContext
    EntityManager em;

    @POST
    @Path("insert")
    public Response insertPlanner(@Context HttpHeaders httpHeaders,
            @QueryParam("startTime") String start,
            @QueryParam("duration") int duration,
            @QueryParam("destination") String destination,
            @QueryParam("obligation") String obligation,
            @QueryParam("alarm") int alarm) {

        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        int userId = -1;
        User user = null;
        if (authHeaderValues != null && authHeaderValues.size() > 0) {
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")), StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            //String password = stringTokenizer.nextToken();

            user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();

            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            userId = user.getId();
        }
        try {
            javax.naming.Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("Planner");
            Queue queue = (Queue) context.lookup("PlannerQueue");

            JMSContext jmsContext = connectionFactory.createContext();
            JMSProducer producer = jmsContext.createProducer();

            //"31-02-2012 21:17:20";
            //"11-Jan-2002 21:12:20";
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = formatter.parse(start);
            Boolean setAlarm = false;
            if (alarm == 1) {
                setAlarm = true;
            }
            PlannerRequest request = new PlannerRequest(PlannerRequest.Type.INSERT, userId, date, obligation, duration, setAlarm, destination);
            ObjectMessage objMsg = jmsContext.createObjectMessage(request);
            producer.send(queue, objMsg);
            return Response
                    .ok().entity("Request is sent for user " + user.getUsername())
                    .build();
        } catch (Exception e) {
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("update")
    public Response updatePlanner(@Context HttpHeaders httpHeaders,
            @QueryParam("id") int planerId,
            @QueryParam("startTime") String start,
            @QueryParam("duration") int duration,
            @QueryParam("destination") String destination,
            @QueryParam("obligation") String obligation,
            @QueryParam("alarm") int alarm) {

        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        int userId = -1;
        User user = null;
        if (authHeaderValues != null && authHeaderValues.size() > 0) {
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")), StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            //String password = stringTokenizer.nextToken();

            user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();

            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            userId = user.getId();
        }
        try {
            javax.naming.Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("Planner");
            Queue queue = (Queue) context.lookup("PlannerQueue");

            JMSContext jmsContext = connectionFactory.createContext();
            JMSProducer producer = jmsContext.createProducer();

            //"31-02-2012 21:17:20";
            //"11-Jan-2002 21:12:20";
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = formatter.parse(start);
            Boolean setAlarm = false;
            if (alarm == 1) {
                setAlarm = true;
            }
            PlannerRequest request = new PlannerRequest(PlannerRequest.Type.UPDATE, userId, date, obligation, duration, setAlarm, destination);
            request.setPlanerId(planerId);
            ObjectMessage objMsg = jmsContext.createObjectMessage(request);
            producer.send(queue, objMsg);
            return Response
                    .ok().entity("Request is sent for user " + user.getUsername())
                    .build();
        } catch (Exception e) {
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("delete")
    public Response deletePlanner(@Context HttpHeaders httpHeaders,
            @QueryParam("id") int planerId) {
        List<String> authHeaderValues = httpHeaders.getRequestHeader("Authorization");
        int userId = -1;
        User user = null;
        if (authHeaderValues != null && authHeaderValues.size() > 0) {
            String authHeaderValue = authHeaderValues.get(0);
            String decodedAuthHeaderValue = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")), StandardCharsets.UTF_8);
            StringTokenizer stringTokenizer = new StringTokenizer(decodedAuthHeaderValue, ":");
            String username = stringTokenizer.nextToken();
            //String password = stringTokenizer.nextToken();

            user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();

            if (user == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            userId = user.getId();
        }
        try {
            javax.naming.Context context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("Planner");
            Queue queue = (Queue) context.lookup("PlannerQueue");

            JMSContext jmsContext = connectionFactory.createContext();
            JMSProducer producer = jmsContext.createProducer();

            PlannerRequest request = new PlannerRequest(PlannerRequest.Type.DELETE, userId);
            request.setPlanerId(planerId);
            ObjectMessage objMsg = jmsContext.createObjectMessage(request);
            producer.send(queue, objMsg);
            return Response
                    .ok().entity("Request is sent for user " + user.getUsername())
                    .build();
        } catch (Exception e) {
        }
        return Response.status(Response.Status.BAD_REQUEST).build();

    }

    @GET
    @Path("select")
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
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("Planner");
            Queue queue = (Queue) context.lookup("PlannerQueue");
            //Topic topic = (Topic) context.lookup("PlannerTopic");

            JMSContext jmsContext = connectionFactory.createContext();
            JMSProducer producer = jmsContext.createProducer();
            //JMSConsumer consumer = jmsContext.createSharedDurableConsumer(topic,"sub"+userId,"userId="+userId);

            PlannerRequest request = new PlannerRequest(PlannerRequest.Type.SELECT, userId);
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
                    .ok().entity("Zahtev za prikaz svih obaveza poslat. ")
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
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("Planner");
            //Topic topic = (Topic) context.lookup("PlannerTopic");
            Queue topic = (Queue) context.lookup("PlannerQueueReport");
            JMSContext jmsContext = connectionFactory.createContext();
            //JMSConsumer consumer = jmsContext.createSharedDurableConsumer(topic, "subP" + 1, "userId=" + 1);
            JMSConsumer consumer = jmsContext.createConsumer(topic);
            ObjectMessage msgRec = (ObjectMessage) consumer.receive();
            PlannerResponse req = (PlannerResponse) msgRec.getObject();
            StringBuilder sb = req.getObligationsBuilder();
            return Response.ok().entity(sb.toString()).build();

        } catch (Exception e) {
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
