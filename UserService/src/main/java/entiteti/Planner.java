/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entiteti;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Aleksandar
 */
@Entity
@Table(name = "planner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Planner.findAll", query = "SELECT p FROM Planner p"),
    @NamedQuery(name = "Planner.findById", query = "SELECT p FROM Planner p WHERE p.id = :id"),
    @NamedQuery(name = "Planner.findByStart", query = "SELECT p FROM Planner p WHERE p.start = :start"),
    @NamedQuery(name = "Planner.findByDuration", query = "SELECT p FROM Planner p WHERE p.duration = :duration"),
    @NamedQuery(name = "Planner.findByObligation", query = "SELECT p FROM Planner p WHERE p.obligation = :obligation"),
    @NamedQuery(name = "Planner.findByDestination", query = "SELECT p FROM Planner p WHERE p.destination = :destination")})
public class Planner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duration")
    private int duration;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "obligation")
    private String obligation;
    @Size(max = 100)
    @Column(name = "destination")
    private String destination;
    @JoinColumn(name = "alarmId", referencedColumnName = "id")
    @ManyToOne
    private Alarm alarmId;
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public Planner() {
    }

    public Planner(Integer id) {
        this.id = id;
    }

    public Planner(Integer id, Date start, int duration, String obligation) {
        this.id = id;
        this.start = start;
        this.duration = duration;
        this.obligation = obligation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getObligation() {
        return obligation;
    }

    public void setObligation(String obligation) {
        this.obligation = obligation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Alarm getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Alarm alarmId) {
        this.alarmId = alarmId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof Planner)) {
            return false;
        }
        Planner other = (Planner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entiteti.Planner[ id=" + id + " ]";
    }
    
}
