package com.example.autosellingapp.items;

public class ModelItem {
    private int model_id;
    private int manu_id;
    private String model_name;

    public ModelItem(int model_id, int manu_id, String model_name) {
        this.model_id = model_id;
        this.manu_id = manu_id;
        this.model_name = model_name;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public int getManu_id() {
        return manu_id;
    }

    public void setManu_id(int manu_id) {
        this.manu_id = manu_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    @Override
    public String toString() {
        return model_name;
    }
}
