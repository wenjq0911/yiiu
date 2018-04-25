package co.yiiu.module.log.model;

import co.yiiu.module.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "yiiu_visit_log")
public class VisitLog {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "ip")
    private String Ip;

    @Column(name = "in_time")
    private Date InTime;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    @JsonIgnore
    private User user;

    private String Address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public Date getInTime() {
        return InTime;
    }

    public void setInTime(Date inTime) {
        InTime = inTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
