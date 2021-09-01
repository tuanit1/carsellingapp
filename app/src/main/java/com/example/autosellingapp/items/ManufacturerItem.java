package com.example.autosellingapp.items;

import java.io.Serializable;

public class ManufacturerItem implements Serializable {
    private int manu_id;
    private String manu_name;
    private String manu_thumb;

    public ManufacturerItem(int manu_id, String manu_name, String manu_thumb) {
        this.manu_id = manu_id;
        this.manu_name = manu_name;
        this.manu_thumb = manu_thumb;
    }

    public int getManu_id() {
        return manu_id;
    }

    public void setManu_id(int manu_id) {
        this.manu_id = manu_id;
    }

    public String getManu_name() {
        return manu_name;
    }

    public void setManu_name(String manu_name) {
        this.manu_name = manu_name;
    }

    public String getManu_thumb() {
        return manu_thumb;
    }

    public void setManu_thumb(String manu_thumb) {
        this.manu_thumb = manu_thumb;
    }

    @Override
    public String toString() {
        return manu_name;
    }
}
