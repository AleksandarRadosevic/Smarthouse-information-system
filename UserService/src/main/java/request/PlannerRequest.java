/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Aleksandar
 */
public class PlannerRequest implements Serializable {

    public enum Type {
        INSERT, UPDATE, DELETE, SELECT
    };
    private Type type;
    private Integer userId;
    private Date startTime;
    private String obligation;
    private Integer duration;
    private Boolean alarmId=Boolean.FALSE;
    private String destination;
    private Integer planerId;

    public Integer getPlanerId() {
        return planerId;
    }

    public PlannerRequest(Integer planerId) {
        this.planerId = planerId;
    }
    
    public void setPlanerId(Integer planerId) {
        this.planerId = planerId;
    }

    public PlannerRequest(Type type, Integer userId, Date startTime, Integer duration) {
        this.type = type;
        this.userId = userId;
        this.startTime = startTime;
        this.duration = duration;
        this.obligation = "obaveza";
    }

    public PlannerRequest(Type type, Integer userId) {
        this.type = type;
        this.userId = userId;
        this.obligation = "obaveza";
    }

    public PlannerRequest(Type type, Integer userId, Date startTime, String obligation, String destination, Integer duration) {
        this.type = type;
        this.userId = userId;
        this.startTime = startTime;
        this.obligation = obligation;
        this.destination = destination;
        this.duration=duration;
    }

    public PlannerRequest(Type type, Integer userId, Date startTime, String obligation, Integer duration, Boolean alarmId, String destination) {
        this.type = type;
        this.userId = userId;
        this.startTime = startTime;
        this.obligation = obligation;
        this.duration = duration;
        this.alarmId = alarmId;
        this.destination = destination;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getObligation() {
        return obligation;
    }

    public void setObligation(String obligation) {
        this.obligation = obligation;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getAlarm() {
        return alarmId;
    }

    public void setAlarm(Boolean alarmId) {
        this.alarmId = alarmId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

}
