package com.bapoto.bapotodriver.models;

import java.io.Serializable;

public class User implements Serializable {
    public String name, image,token,email,id;
    public Boolean isAdmin;
    public Boolean isDriver;
    public int nbOfRides;
    public int account;
}
