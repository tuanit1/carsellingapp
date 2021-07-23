package com.example.autosellingapp.interfaces;

import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;

import java.util.ArrayList;

public interface LoadCategoryListener {
    void onStart();
    void onEnd(String success, ArrayList<CarItem> carItemArrayList,
               ArrayList<AdsItem> adsItemArrayList, ArrayList<MyItem> cityItemArrayList,
               ArrayList<UserItem> userItemArrayList, ArrayList<ModelItem> modelItemArrayList);
}
