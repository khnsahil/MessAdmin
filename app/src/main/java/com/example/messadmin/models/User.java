package com.example.messadmin.models;

public class User {

    private String user_id;
    private long phone_number;
    private String email;
    private String user_name,user_type;


    public User(String user_id, long phone_number, String email, String user_name, String user_type) {
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.email = email;
        this.user_name = user_name;
        this.user_type = user_type;
    }

    public User() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number=" + phone_number +
                ", email='" + email + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }



}
