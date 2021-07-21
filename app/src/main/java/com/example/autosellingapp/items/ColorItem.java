package com.example.autosellingapp.items;

public class ColorItem {
    private int color_id;
    private String color_name;
    private String color_code;

    public ColorItem(int color_id, String color_name, String color_code) {
        this.color_id = color_id;
        this.color_name = color_name;
        this.color_code = color_code;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    @Override
    public String toString() {
        return color_name;
    }
}
