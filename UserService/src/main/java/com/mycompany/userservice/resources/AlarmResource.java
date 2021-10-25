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
/**
 *
 * @author
 */
@Path("alarm")
@Stateless
public class AlarmResource {

    @PersistenceContext
    EntityManager em;

    @POST
    public Response setAlarm(@Context HttpHeaders httpHeaders,
            @QueryParam("start") String start,
            @QueryParam("song") String song,
            @QueryParam("repetition") Integer repetition,
            @QueryParam("status") String status) {
        
        
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
            javax.naming.Context context=new InitialContext();
            ConnectionFactory connectionFactory=(ConnectionFactory) context.lookup("Alarm");
            Queue queue=(Queue)context.lookup("AlarmQueue");
            
            JMSContext jmsContext=connectionFactory.createContext();
            JMSProducer producer=jmsContext.createProducer();
            
            //"31-02-2012 21:17:20";
            //"11-Jan-2002 21:12:20";
            SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
            Date date=formatter.parse(start);  
            if (repetition==0)
                repetition=1;
            int statusReq=0;
            if (status.equals("1"))
                statusReq=1;
            AlarmRequest request=new AlarmRequest(date, repetition, song, userId, statusReq,AlarmRequest.Type.RING);
            ObjectMessage objMsg=jmsContext.createObjectMessage(request);
            producer.send(queue, objMsg);
            return Response
                    .ok().entity("Alarm for user id=" + userId + " is set.")
                    .build();
        } catch (Exception e) {
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
