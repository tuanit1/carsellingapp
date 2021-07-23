package com.example.autosellingapp.items;

import java.util.ArrayList;

public class UserItem {
    private String username;
    private String password;
    private String address;
    private String phoneNumber;
    private String fullName;
    private String email;
    private String image;
    private ArrayList<String> favourite_ads;

    public UserItem(String username, String password, String address, String phoneNumber, String fullName, String email, String image , ArrayList<String> favourite_ads) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.email = email;
        this.image = image;
        this.favourite_ads = favourite_ads;
    }

    public UserItem(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
