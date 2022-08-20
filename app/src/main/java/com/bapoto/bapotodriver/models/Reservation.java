package com.bapoto.bapotodriver.models;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Reservation implements Serializable {


    private String name;
    private String telephone;
    @Nullable
    private String email;
    private String pickUp;
    private String dropOff;
    private String hour;
    private String date;
    private String price;
    @Nullable
    private Date dayAccepted;
    @Nullable
    private String infos;
    private User sender;
    private String driver;

    public Reservation() {
    }


    public Reservation(String name, String telephone, @Nullable String email, String pickUp, String dropOff,
                       String hour, String date, @Nullable String infos, @Nullable Date dayAccepted, String driver) {
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
    }

    public Reservation(String nom, String tel, String desti, String rdv, String date, String hour, String infos, Date dayAccepted, User sender, String driver) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Nullable
    public String getInfos() {
        return infos;
    }

    public void setInfos(@Nullable String infos) {
        this.infos = infos;
    }


    @Nullable
    public Date getDayAccepted() {
        return dayAccepted;
    }

    public void setDayAccepted(@Nullable Date dayAccepted) {
        this.dayAccepted = dayAccepted;
    }
}
