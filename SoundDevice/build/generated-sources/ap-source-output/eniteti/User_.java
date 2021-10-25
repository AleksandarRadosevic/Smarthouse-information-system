package eniteti;

import eniteti.Alarm;
import eniteti.Planner;
import eniteti.Playlist;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-09-19T22:50:10")
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