package com.example.autosellingapp.items;

import java.io.Serializable;
import java.util.ArrayList;

public class UserItem implements Serializable {
    private String uid;
    private String address;
    private String phoneNumber;
    private String fullName;
    private String email;
    private String image;
    private ArrayList<String> chatlist;
    private ArrayList<String> followlist;
    private ArrayList<String> recentAds;
    private String status;

    private ArrayList<String> favourite_ads;

    public UserItem(String uid, String address, String phoneNumber, String fullName, String email, String image, ArrayList<String> chatlist, ArrayList<String> followlist, ArrayList<String> recentAds, ArrayList<String> favourite_ads) {
        this.uid = uid;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.email = email;
        this.image = image;
        this.chatlist = chatlist;
        this.followlist = followlist;
        this.recentAds = recentAds;
        this.favourite_ads = favourite_ads;
        this.status = "offline";
    }

    public ArrayList<String> getRecentAds() {
        return recentAds;
    }

    public void setRecentAds(ArrayList<String> recentAds) {
        this.recentAds = recentAds;
    }

    public ArrayList<String> getFollowlist() {
        return followlist;
    }

    public void setFollowlist(ArrayList<String> followlist) {
        this.followlist = followlist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserItem(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFavourite_ads() {
        return favourite_ads;
    }

    public void setFavourite_ads(ArrayList<String> favourite_ads) {
        this.favourite_ads = favourite_ads;
    }

    public ArrayList<String> getChatlist() {
        return chatlist;
    }

    public void setChatlist(ArrayList<String> chatlist) {
        this.chatlist = chatlist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
