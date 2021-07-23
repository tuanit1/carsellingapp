package com.example.autosellingapp.items;

import java.util.ArrayList;

public class CarItem {
    private int car_id;
    private String car_name;
    private int model_id;
    private int bodyType_id;
    private int fuelType_id;
    private ArrayList<String> car_imageList;
    private int car_year;
    private boolean isNew;
    private int car_power;
    private int trans_id;
    private int car_doors;
    private int car_seats;
    private ArrayList<String> car_equipments;
    private int car_previousOwner;
    private int color_id;
    private int car_gears;
    private int car_engineSize;
    private int car_cylinder;
    private int car_kerbWeight;
    private double car_fuelConsumption;
    private int car_co2emission;

    public CarItem(int car_id, String car_name, int model_id, int bodyType_id, int fuelType_id, ArrayList<String> car_imageList, int car_year, boolean isNew, int car_power, int trans_id, int car_doors, int car_seats, ArrayList<String> car_equipments, int car_previousOwner, int color_id, int car_gears, int car_engineSize, int car_cylinder, int car_kerbWeight, double car_fuelConsumption, int car_co2emission) {
        this.car_id = car_id;
        this.car_name = car_name;
        this.model_id = model_id;
        this.bodyType_id = bodyType_id;
        this.fuelType_id = fuelType_id;
        this.car_imageList = car_imageList;
        this.car_year = car_year;
        this.isNew = isNew;
        this.car_power = car_power;
        this.trans_id = trans_id;
        this.car_doors = car_doors;
        this.car_seats = car_seats;
        this.car_equipments = car_equipments;
        this.car_previousOwner = car_previousOwner;
        this.color_id = color_id;
        this.car_gears = car_gears;
        this.car_engineSize = car_engineSize;
        this.car_cylinder = car_cylinder;
        this.car_kerbWeight = car_kerbWeight;
        this.car_fuelConsumption = car_fuelConsumption;
        this.car_co2emission = car_co2emission;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public int getBodyType_id() {
        return bodyType_id;
    }

    public void setBodyType_id(int bodyType_id) {
        this.bodyType_id = bodyType_id;
    }

    public int getFuelType_id() {
        return fuelType_id;
    }

    public void setFuelType_id(int fuelType_id) {
        this.fuelType_id = fuelType_id;
    }

    public ArrayList<String> getCar_imageList() {
        return car_imageList;
    }

    public void setCar_imageList(ArrayList<String> car_imageList) {
        this.car_imageList = car_imageList;
    }

    public int getCar_year() {
        return car_year;
    }

    public void setCar_year(int car_year) {
        this.car_year = car_year;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getCar_power() {
        return car_power;
    }

    public void setCar_power(int car_power) {
        this.car_power = car_power;
    }

    public int getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(int trans_id) {
        this.trans_id = trans_id;
    }

    public int getCar_doors() {
        return car_doors;
    }

    public void setCar_doors(int car_doors) {
        this.car_doors = car_doors;
    }

    public int getCar_seats() {
        return car_seats;
    }

    public void setCar_seats(int car_seats) {
        this.car_seats = car_seats;
    }

    public ArrayList<String> getCar_equipments() {
        return car_equipments;
    }

    public void setCar_equipments(ArrayList<String> car_equipments) {
        this.car_equipments = car_equipments;
    }

    public int getCar_previousOwner() {
        return car_previousOwner;
    }

    public void setCar_previousOwner(int car_previousOwner) {
        this.car_previousOwner = car_previousOwner;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public int getCar_gears() {
        return car_gears;
    }

    public void setCar_gears(int car_gears) {
        this.car_gears = car_gears;
    }

    public int getCar_engineSize() {
        return car_engineSize;
    }

    public void setCar_engineSize(int car_engineSize) {
        this.car_engineSize = car_engineSize;
    }

    public int getCar_cylinder() {
        return car_cylinder;
    }

    public void setCar_cylinder(int car_cylinder) {
        this.car_cylinder = car_cylinder;
    }

    public int getCar_kerbWeight() {
        return car_kerbWeight;
    }

    public void setCar_kerbWeight(int car_kerbWeight) {
        this.car_kerbWeight = car_kerbWeight;
    }

    public double getCar_fuelConsumption() {
        return car_fuelConsumption;
    }

    public void setCar_fuelConsumption(double car_fuelConsumption) {
        this.car_fuelConsumption = car_fuelConsumption;
    }

    public int getCar_co2emission() {
        return car_co2emission;
    }

    public void setCar_co2emission(int car_co2emission) {
        this.car_co2emission = car_co2emission;
    }

    @Override
    public String toString() {
        return car_name;
    }
}
