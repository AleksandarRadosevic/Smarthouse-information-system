package entiteti;

import entiteti.Alarm;
import entiteti.Playlist;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-09-17T19:52:55")
@StaticMetamodel(Song.class)
public class Song_ { 

    public static volatile ListAttribute<Song, Playlist> playlistList;
    public static volatile SingularAttribute<Song, String> name;
    public static volatile ListAttribute<Song, Alarm> alarmList;
    public static volatile SingularAttribute<Song, Integer> id;
    public static volatile SingularAttribute<Song, String> url;

}