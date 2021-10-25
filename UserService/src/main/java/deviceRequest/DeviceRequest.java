/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deviceRequest;

import java.io.Serializable;

/**
 *
 * @author Aleksandar
 */
public class DeviceRequest implements Serializable {

    private Integer userId;
    private String songName;
    private Type type;
    public enum Type {
        PLAY_SONG, PLAYLIST_USER
    };

    public DeviceRequest(Integer userId, String songName,Type type) {
        this.userId = userId;
        this.songName = songName;
        this.type=type;
    }

    public DeviceRequest(Integer userId, Type type) {
        this.userId = userId;
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer id) {
        this.userId = userId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    
}
