package com.example.messadmin.models;

public class Restaurant {
    private String res_name,res_type,timing,price,rating,profile_pic;

    public Restaurant() {
    }

    public Restaurant(String res_name, String res_type, String timing, String price, String rating, String profile_pic) {
        this.res_name = res_name;
        this.res_type = res_type;
        this.timing = timing;
        this.price = price;
        this.rating = rating;
        this.profile_pic = profile_pic;
    }

    public String getRes_name() {
        return res_name;
    }

    public void setRes_name(String res_name) {
        this.res_name = res_name;
    }

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String res_type) {
        this.res_type = res_type;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "res_name='" + res_name + '\'' +
                ", res_type='" + res_type + '\'' +
                ", timing='" + timing + '\'' +
                ", price='" + price + '\'' +
                ", rating='" + rating + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                '}';
    }
}
