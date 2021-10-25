package entiteti;

import entiteti.Song;
import entiteti.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-09-20T20:45:50")
@StaticMetamodel(Playlist.class)
public class Playlist_ { 

    public static volatile SingularAttribute<Playlist, Integer> id;
    public static volatile SingularAttribute<Playlist, Song> songId;
    public static volatile SingularAttribute<Playlist, User> userId;

}