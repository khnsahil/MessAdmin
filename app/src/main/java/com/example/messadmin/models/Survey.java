package com.example.messadmin.models;

import java.util.HashMap;

public class Survey {

    String date;
    HashMap<String,String> map=new HashMap<>();

    public Survey(String date, HashMap<String, String> map) {
        this.date = date;
        this.map = map;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
