/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "alarm")
@NamedQueries({
    @NamedQuery(name = "Alarm.findAll", query = "SELECT a FROM Alarm a"),
    @NamedQuery(name = "Alarm.findById", query = "SELECT a FROM Alarm a WHERE a.id = :id"),
    @NamedQuery(name = "Alarm.findByName", query = "SELECT a FROM Alarm a WHERE a.name = :name"),
    @NamedQuery(name = "Alarm.findByTimeRing", query = "SELECT a FROM Alarm a WHERE a.timeRing = :timeRing"),
    @NamedQuery(name = "Alarm.findByRepetitionTotal", query = "SELECT a FROM Alarm a WHERE a.repetitionTotal = :repetitionTotal"),
    @NamedQuery(name = "Alarm.findByStatus", query = "SELECT a FROM Alarm a WHERE a.status = :status"),
    @NamedQuery(name = "Alarm.findByRepetitionCount", query = "SELECT a FROM Alarm a WHERE a.repetitionCount = :repetitionCount")})
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    @Column(name = "timeRing")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeRing;
    @Column(name = "repetitionTotal")
    private Integer repetitionTotal;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "repetitionCount")
    private Integer repetitionCount;
    @JoinColumn(name = "songId", referencedColumnName = "id")
    @ManyToOne
    private Song songId;
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    @OneToMany(mappedBy = "alarmId")
    private List<Planner> plannerList;

    public Alarm() {
    }

    public Alarm(String name, Date timeRing, Integer repetitionTotal, Boolean status, Integer repetitionCount, Song songId, User userId) {
        this.name = name;
        this.timeRing = timeRing;
        this.repetitionTotal = repetitionTotal;
        this.status = status;
        this.repetitionCount = repetitionCount;
        this.songId = songId;
        this.userId = userId;
    }
    
    public Alarm(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getRepetitionTotal() {
        return repetitionTotal;
    }

    public void setRepetitionTotal(Integer repetitionTotal) {
        this.repetitionTotal = repetitionTotal;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getRepetitionCount() {
        return repetitionCount;
    }

    public void setRepetitionCount(Integer repetitionCount) {
        this.repetitionCount = repetitionCount;
    }

    public Song getSongId() {
        return songId;
    }

    public void setSongId(Song songId) {
        this.songId = songId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public List<Planner> getPlannerList() {
        return plannerList;
    }

    public void setPlannerList(List<Planner> plannerList) {
        this.plannerList = plannerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alarm)) {
            return false;
        }
        Alarm other = (Alarm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Alarm[ id=" + id + " ]";
    }
    
}
