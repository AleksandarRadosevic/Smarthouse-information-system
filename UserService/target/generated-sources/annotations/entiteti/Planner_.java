package entiteti;

import entiteti.Alarm;
import entiteti.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-09-20T20:45:50")
@StaticMetamodel(Planner.class)
public class Planner_ { 

    public static volatile SingularAttribute<Planner, Integer> duration;
    public static volatile SingularAttribute<Planner, String> obligation;
    public static volatile SingularAttribute<Planner, Date> start;
    public static volatile SingularAttribute<Planner, String> destination;
    public static volatile SingularAttribute<Planner, Alarm> alarmId;
    public static volatile SingularAttribute<Planner, Integer> id;
    public static volatile SingularAttribute<Planner, User> userId;

}