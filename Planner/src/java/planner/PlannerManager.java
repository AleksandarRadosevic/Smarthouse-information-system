/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planner;

/**
 *
 * @author Aleksandar
 */
import entiteti.Alarm;
import entiteti.Planner;
import entiteti.User;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static planner.PlannerManager.topic;
import request.PlannerRequest;
import request.PlannerResponse;

public class PlannerManager {

    private static final String apiKey = "BSEZlkYr0nsOtvgKDvwv1FYvPm4kvaxmb9ZgpFxU01U";
    private static final String url_addressCoordinates = "https://geocode.search.hereapi.com/v1/geocode?";
    private static final String url_adressDistance = "https://router.hereapi.com/v8/routes?";

    static EntityManager em;

    @Resource(lookup = "Planner")
    static ConnectionFactory connectionFactory;

    @Resource(lookup = "PlannerQueue")
    static Queue queue;

    @Resource(lookup = "PlannerTopic")
    static Topic topic;

    @Resource(lookup = "PlannerQueueReport")
    static Queue queueReport;

    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public static Coordinates getCoords(String address) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("apiKey", apiKey);
            parameters.put("q", address);

            String paramsValue = PlannerManager.getParamsString(parameters);
            String addressNew = url_addressCoordinates + paramsValue;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(addressNew)
                    .build(); // defaults to GET
            Response response = client.newCall(request).execute();

            JSONObject jsonResponse = new JSONObject(response.body().string());
            JSONArray itemsArray = jsonResponse.getJSONArray("items");
            JSONObject firstItem = (JSONObject) itemsArray.getJSONObject(0);
            JSONObject position = firstItem.getJSONObject("position");
            Coordinates coordinates = new Coordinates(position.getDouble("lat"), position.getDouble("lng"));
            //System.out.println("Geografska sirina: " + coordinates.getWidth());
            //System.out.println("Geografska duzina: " + coordinates.getHeight());

            return coordinates;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PlannerManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlannerManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(PlannerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Integer getTravelDurationInMinutes(Coordinates startLocation, Coordinates endLocation) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("transportMode", "car");
            parameters.put("origin", startLocation.getWidth() + "," + startLocation.getHeight());
            parameters.put("destination", endLocation.getWidth() + "," + endLocation.getHeight());
            parameters.put("return", "summary");
            parameters.put("apiKey", apiKey);

            String paramsString = PlannerManager.getParamsString(parameters);
            String url_adress = url_adressDistance + paramsString;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url_adress)
                    .build(); // defaults to GET
            Response response = client.newCall(request).execute();

            String travelReport = response.body().string();
            System.out.println(travelReport);
            JSONObject jsonResponse = new JSONObject(travelReport);
            JSONArray routesArray = jsonResponse.getJSONArray("routes");
            JSONObject routeObject = (JSONObject) routesArray.getJSONObject(0);
            JSONArray selectionArray = routeObject.getJSONArray("sections");
            JSONObject selectionObject = (JSONObject) selectionArray.getJSONObject(0);

            JSONObject result = selectionObject.getJSONObject("summary");

            Double durationSecond = result.getDouble("duration");
            Double length = result.getDouble("length");
            System.out.println("Duzina puta je " + length / 1000 + " km.");

            int durationInteger = (int) Math.round(durationSecond);
            int travelDuration = durationInteger / 60;
            if (durationInteger % 60 != 0) {
                travelDuration++;
            }
            return travelDuration;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PlannerManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlannerManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(PlannerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Planner prevObligation(Planner p) {
        Query query = em.createNamedQuery("Planner.findByUserId", Planner.class);
        query.setParameter("id", p.getUserId());
        List<Planner> result = query.getResultList();
        Planner prev = null;

        for (Planner temp : result) {
            if (temp.getStart().before(p.getStart())) {
                if (prev == null) {
                    prev = temp;
                } else if (temp.getStart().after(prev.getStart())) {
                    prev = temp;
                }
            }
        }

        return prev;
    }

    private Planner nextObligation(Planner p) {
        Query query = em.createNamedQuery("Planner.findByUserId", Planner.class);
        query.setParameter("id", p.getUserId());
        List<Planner> result = query.getResultList();
        Planner prev = null;

        for (Planner temp : result) {
            if (temp.getStart().after(p.getStart())) {
                if (prev == null) {
                    prev = temp;
                } else if (temp.getStart().before(prev.getStart())) {
                    prev = temp;
                }
            }
        }

        return prev;
    }

    //check if obligation can be added into calendar
    private boolean validObligationTime(Planner p) {

        Planner prev = prevObligation(p);
        Planner next = nextObligation(p);

        String locationPrev;
        LocalDateTime timePrev;

        String locationNow;
        LocalDateTime timeNow = LocalDateTime.ofInstant(p.getStart().toInstant(), ZoneId.systemDefault());

        //initialize location now and time now
        if (p.getDestination() == null) {
            locationNow = p.getUserId().getAddress();
            p.setDestination(locationNow);
        } else {
            locationNow = p.getDestination();
        }

        //initialize location prev and time prev
        if (prev == null) {
            locationPrev = p.getUserId().getAddress();
            timePrev = LocalDateTime.now();
        } else {
            locationPrev = prev.getDestination();
            timePrev = LocalDateTime.ofInstant(prev.getStart().toInstant(), ZoneId.systemDefault()).plusMinutes(prev.getDuration());
        }

        //get coordinates
        Coordinates first = getCoords(locationPrev);
        Coordinates second = getCoords(locationNow);

        Integer minutesFromFirstToSecond = getTravelDurationInMinutes(first, second);
        timePrev = timePrev.plusMinutes(minutesFromFirstToSecond);
        if (timePrev.isAfter(timeNow)) //check for overlapping
        {
            return false;
        }

        if (next != null) {
            //shift values because not to change condition at the end
            locationPrev = locationNow;
            timePrev = timeNow.plusMinutes(p.getDuration());
            locationNow = next.getDestination();
            timeNow = LocalDateTime.ofInstant(next.getStart().toInstant(), ZoneId.systemDefault());
            first = getCoords(locationPrev);
            second = getCoords(locationNow);
            minutesFromFirstToSecond = getTravelDurationInMinutes(first, second);
            timePrev = timePrev.plusMinutes(minutesFromFirstToSecond);
            if (timePrev.isAfter(timeNow)) //check for overlapping
            {
                return false;
            }
        }

        return true;
    }

    private boolean createObligation(PlannerRequest request) {
        try {
            em.getTransaction().begin();
        } catch (Exception e) {
        }
        Planner p = new Planner();
        p.setObligation(request.getObligation());
        p.setDestination(request.getDestination());
        p.setStart(request.getStartTime());
        User user = em.createNamedQuery("User.findById", User.class).setParameter("id", request.getUserId()).getSingleResult();
        p.setUserId(user);
        p.setDuration(request.getDuration());
        if (!validObligationTime(p)) {
            em.getTransaction().commit();
            return false;
        }
        em.persist(p);
        em.getTransaction().commit();
        em.getTransaction().begin();
        System.out.println(request.getAlarm());
        if (request.getAlarm() == true) {
            setAlarm(p, user);
        }

        System.out.println("Uspesno dodata obaveza");
        return true;
    }

    private void setAlarm(Planner p, User user) {
        System.out.println("dodaje alarm");
        String location = p.getDestination();
        LocalDateTime alarmTime = LocalDateTime.ofInstant(p.getStart().toInstant(), ZoneId.systemDefault()).plusMinutes(p.getDuration());
        Alarm alarm = new Alarm();
        alarm.setStatus(Boolean.TRUE);
        alarm.setUserId(user);
        alarm.setTimeRing(java.sql.Timestamp.valueOf(alarmTime));
        alarm.setRepetitionTotal(1);
        alarm.setRepetitionCount(0);
        alarm.setName("podsetnik za polazak");
        p.setAlarmId(alarm);
        em.persist(alarm);
        em.merge(p);
        em.getTransaction().commit();
        em.getTransaction().begin();
    }

    private boolean changeObligation(PlannerRequest request) {
        try {
            em.getTransaction().begin();
        } catch (Exception e) {
        }

        List<Planner> results = em.createNamedQuery("Planner.findById", Planner.class).setParameter("id", request.getPlanerId()).getResultList();
        if (results.size() == 0) {
            em.getTransaction().commit();
            return false;
        }
        Planner p = results.get(0);
        p.setDestination(request.getDestination());
        p.setDuration(request.getDuration());
        p.setObligation(request.getObligation());
        p.setStart(request.getStartTime());

        if (!validObligationTime(p)) {
            em.getTransaction().commit();
            return false;
        }

        if (p.getAlarmId() != null) {
            Alarm old = p.getAlarmId();
            p.setAlarmId(null);
            em.remove(old);
        }
        if (request.getAlarm() == true) {
            setAlarm(p, p.getUserId());
        }
        em.merge(p);
        em.getTransaction().commit();
        System.out.println("Uspesno promenjena obaveza");
        return true;
    }

    private boolean deleteObligation(PlannerRequest request) {
        Integer id = request.getPlanerId();
        if (id == null) {
            System.out.println("Obaveza sa ovim id-jem ne postoji.");
            return false;
        }
        try {
            em.getTransaction().begin();
        } catch (Exception e) {
        }
        Query query = em.createNamedQuery("Planner.findById", Planner.class);
        query.setParameter("id", id);
        List<Planner> resultList = query.getResultList();
        if (resultList.size() == 0) {
            System.out.println("Obaveza sa ovim id-jem ne postoji.");
            return false;
        }
        Planner planer = resultList.get(0);
        Alarm alarm = planer.getAlarmId();
        em.remove(planer);
        if (alarm != null) {
            em.remove(alarm);
        }
        em.getTransaction().commit();
        System.out.println("Uspesno obrisana obaveza");
        return true;
    }

    private List<Planner> getObligations(PlannerRequest request) {
        Integer userId = request.getUserId();
        User user = em.createNamedQuery("User.findById", User.class).setParameter("id", userId).getSingleResult();
        if (user == null) {
            return null;
        }
        try {
            em.getTransaction().begin();
        } catch (Exception e) {
        }

        Query query = em.createNamedQuery("Planner.findByUserId", Planner.class);
        query.setParameter("id", user);
        List<Planner> result = query.getResultList();
        em.getTransaction().commit();
        return result;
    }

    public PlannerManager() {
        em = Persistence.createEntityManagerFactory("PlannerPU").createEntityManager();
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queue);
        JMSProducer producer = context.createProducer();

        consumer.setMessageListener((message) -> {

            try {
                System.out.println("Prima poruke");
                ObjectMessage objMsg = (ObjectMessage) message;
                PlannerRequest request = (PlannerRequest) objMsg.getObject();

                if (request.getType() == PlannerRequest.Type.INSERT) {
                    createObligation(request);
                } else if (request.getType() == PlannerRequest.Type.UPDATE) {
                    changeObligation(request);
                } else if (request.getType() == PlannerRequest.Type.DELETE) {
                    deleteObligation(request);
                } else if (request.getType() == PlannerRequest.Type.SELECT) {
                    List<entiteti.Planner> list = getObligations(request);
                    PlannerResponse response = new PlannerResponse(list, request.getUserId());
                    ObjectMessage msg = context.createObjectMessage(response);
                    //msg.setIntProperty("userId", request.getUserId());
                    //producer.send(topic, msg);
                    producer.send(queueReport,msg);
                }
            } catch (JMSException ex) {
                Logger.getLogger(PlannerManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

    public static void main(String[] args) {
        PlannerManager planner = new PlannerManager();
        while (true) {
        }
    }
}
