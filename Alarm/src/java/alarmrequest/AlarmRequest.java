/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarmrequest;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Aleksandar
 */
public class AlarmRequest implements Serializable {

    private String name;
    private Date timeRing;
    private Integer repetitionCount;
    private Integer repetitionTotal;
    private String song;
    private Integer userId;
    private Integer status;
    private Integer alarmId;

    public enum Type {
        RING, CHANGE
    }
    private Type type;

    public AlarmRequest(String name, Date timeRing, Integer repetitionTotal, String song, Integer userId, Integer status, Type type) {
        this.name = name;
        this.timeRing = timeRing;
        this.repetitionCount = 0;
        this.repetitionTotal = repetitionTotal;
        this.song = song;
        this.userId = userId;
        this.status = status;
        this.type = type;
    }

    public AlarmRequest(Integer alarmId, Type type, String song,Integer userId) {
        this.alarmId=alarmId;
        this.type=type;
        this.song=song;
        this.userId=userId;
    }

    public AlarmRequest(Date timeRing, Integer repetitionTotal, String song, Integer userId, Integer status, Type type) {
        this.name = "";
        this.timeRing = timeRing;
        this.repetitionCount = 0;
        this.repetitionTotal = repetitionTotal;
        this.song = song;
        this.userId = userId;
        this.status = 1;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimeRing() {
        return timeRing;
    }

    public void setTimeRing(Date timeRing) {
        this.timeRing = timeRing;
    }

    public Integer getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(Integer repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    public Integer getRepetitionTotal() {
        return repetitionTotal;
    }

    public void setRepetitionTotal(Integer repetitionTotal) {
        this.repetitionTotal = repetitionTotal;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
