package com.example.autosellingapp.items;

import java.io.Serializable;
import java.util.Date;

public class AdsItem implements Serializable {
    private int ads_id;
    private int car_id;
    private String uid;
    private double ads_price;
    private int ads_mileage;
    private int city_id;
    private String ads_location;
    private String ads_description;
    private Date ads_posttime;
    private int ads_likes;
    private boolean ads_isAvailable;

    public AdsItem(int ads_id, int car_id, String uid, double ads_price, int ads_mileage, int city_id, String ads_location, String ads_description, Date ads_posttime, int ads_likes, boolean ads_isAvailable) {
        this.ads_id = ads_id;
        this.car_id = car_id;
        this.uid = uid;
        this.ads_price = ads_price;
        this.ads_mileage = ads_mileage;
        this.city_id = city_id;
        this.ads_location = ads_location;
        this.ads_description = ads_description;
        this.ads_posttime = ads_posttime;
        this.ads_likes = ads_likes;
        this.ads_isAvailable = ads_isAvailable;
    }

    public boolean isAds_isAvailable() {
        return ads_isAvailable;
    }

    public void setAds_isAvailable(boolean ads_isAvailable) {
        this.ads_isAvailable = ads_isAvailable;
    }

    public int getAds_id() {
        return ads_id;
    }

    public void setAds_id(int ads_id) {
        this.ads_id = ads_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getAds_price() {
        return ads_price;
    }

    public void setAds_price(double ads_price) {
        this.ads_price = ads_price;
    }

    public int getAds_mileage() {
        return ads_mileage;
    }

    public void setAds_mileage(int ads_mileage) {
        this.ads_mileage = ads_mileage;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getAds_location() {
        return ads_location;
    }

    public void setAds_location(String ads_location) {
        this.ads_location = ads_location;
    }

    public String getAds_description() {
        return ads_description;
    }

    public void setAds_description(String ads_description) {
        this.ads_description = ads_description;
    }

    public Date getAds_posttime() {
        return ads_posttime;
    }

    public void setAds_posttime(Date ads_posttime) {
        this.ads_posttime = ads_posttime;
    }

    public int getAds_likes() {
        return ads_likes;
    }

    public void setAds_likes(int ads_likes) {
        this.ads_likes = ads_likes;
    }
}
