package com.example.autosellingapp.interfaces;

import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;

import java.util.ArrayList;

public interface LoadSellingListener {
    void onStart();
    void onEnd(String success, ArrayList<AdsItem> arrayList_ads, ArrayList<CarItem> arrayList_car);
}
