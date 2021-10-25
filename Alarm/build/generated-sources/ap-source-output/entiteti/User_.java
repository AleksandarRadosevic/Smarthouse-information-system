package entiteti;

import entiteti.Alarm;
import entiteti.Planner;
import entiteti.Playlist;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-09-17T19:52:55")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile ListAttribute<User, Playlist> playlistList;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> address;
    public static volatile ListAttribute<User, Alarm> alarmList;
    public static volatile ListAttribute<User, Planner> plannerList;
    public static volatile SingularAttribute<User, Integer> id;
    public static volatile SingularAttribute<User, String> username;

}