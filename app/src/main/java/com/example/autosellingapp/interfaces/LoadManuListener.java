package com.example.autosellingapp.interfaces;

import com.example.autosellingapp.items.ManufacturerItem;

import java.util.ArrayList;

public interface LoadManuListener {
    void onStart();
    void onEnd(String success, ArrayList<ManufacturerItem> manuList);
}
