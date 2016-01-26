package com.dw.userstatistics.db.entities;

import com.dw.userstatistics.json.CustomLoginAttemptDeserializer;
import com.dw.userstatistics.json.CustomLoginAttemptSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by Alberto on 2016-01-21.
 */
@Entity
@Table(name = "loginAttempt")
@NamedQueries(
        @NamedQuery(name = "findLoginAttemptsByUserId", query = "select a from LoginAttempt a where a.userId = ? order by dateTime desc")
)
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;
    @JsonIgnore
    private long userId;
    @Column(name="dateTime",nullable = false)
    @JsonSerialize(using = CustomLoginAttemptSerializer.class)
    @JsonDeserialize(using = CustomLoginAttemptDeserializer.class)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateTime;

    public LoginAttempt() {
    }

    public LoginAttempt(long userId, DateTime dateTime) {
        this.userId = userId;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginAttempt)) return false;

        LoginAttempt that = (LoginAttempt) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        return dateTime != null ? dateTime.equals(that.dateTime) : that.dateTime == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }
}
