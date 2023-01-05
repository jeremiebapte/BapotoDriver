package com.bapoto.bapotodriver.models;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.type.DateTime;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Reservation implements Serializable {

    String isoHourPattern = "HH:mm";
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleHourFormat = new SimpleDateFormat(isoHourPattern);

    private String name;
    private String telephone;
    @Nullable
    private String email;
    private String pickUp;
    private String dropOff;
    private String hour;
    // TODO REGLER SOUCI AVEC DATE POUR POUVOIR COMPARER LES DATES 

    @Nullable
    private String price;
    private Timestamp dayAccepted;
    private String dayShouldFinish;
    @Nullable
    private String date;
    @Nullable
    private String infos;
    private User sender;
    private String driver;
    private String driverId;
    public Boolean isDone;
    private long timestamp;

    public Reservation() {
    }




    public Reservation(String name, String telephone,long timestamp, @Nullable String email,@Nullable String date, String pickUp, String dropOff,
                       String hour, @Nullable String infos, Timestamp dayAccepted,
                       String dayShouldFinish, String driver, Boolean isDone, String driverId) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.pickUp = pickUp;
        this.dropOff = dropOff;
        this.hour = hour;
        this.date = date;
        this.infos = infos;
        this.dayAccepted = dayAccepted;
        this.driver = driver;
        this.isDone = isDone;
        this.dayShouldFinish = dayShouldFinish;
        this.timestamp = timestamp;
        this.driverId = driverId;
    }

    public Reservation(String nom, String tel, String desti, Integer price, String rdv, Date date, String hour,Boolean isDone, String infos, String dayAccepted, User sender, String driver) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getDropOff() {
        return dropOff;
    }

    public void setDropOff(String dropOff) {
        this.dropOff = dropOff;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    public void setDate(@Nullable String date) {
        this.date = date;
    }

    @Nullable
    public String getPrice() {
        return price;
    }

    public void setPrice(@Nullable String price) {
        this.price = price;
    }

    @Nullable
    public String getInfos() {
        return infos;
    }

    public void setInfos(@Nullable String infos) {
        this.infos = infos;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Timestamp getDayAccepted() {
        return dayAccepted;
    }

    public void setDayAccepted(@Nullable Timestamp dayAccepted) {
        this.dayAccepted = dayAccepted;
    }


    public String getDayShouldFinish() {
        return dayShouldFinish;
    }

    public void setDayShouldFinish(String dayShouldFinish) {
        this.dayShouldFinish = dayShouldFinish;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
