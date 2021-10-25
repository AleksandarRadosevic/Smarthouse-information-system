package entiteti;

import entiteti.Planner;
import entiteti.Song;
import entiteti.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-09-19T16:21:17")
@StaticMetamodel(Alarm.class)
public class Alarm_ { 

    public static volatile SingularAttribute<Alarm, Date> timeRing;
    public static volatile SingularAttribute<Alarm, Integer> repetitionCount;
    public static volatile SingularAttribute<Alarm, String> name;
    public static volatile SingularAttribute<Alarm, Integer> repetitionTotal;
    public static volatile ListAttribute<Alarm, Planner> plannerList;
    public static volatile SingularAttribute<Alarm, Integer> id;
    public static volatile SingularAttribute<Alarm, Song> songId;
    public static volatile SingularAttribute<Alarm, User> userId;
    public static volatile SingularAttribute<Alarm, Boolean> status;

}