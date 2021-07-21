package com.example.autosellingapp.interfaces;

import android.widget.ArrayAdapter;

import com.example.autosellingapp.items.ColorItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;

import java.util.ArrayList;

public interface LoadSearchListener {
    void onStart();
    void onEnd(String success, ArrayList<ManufacturerItem> arrayList_manu,
               ArrayList<ModelItem> arrayList_model, ArrayList<MyItem> arrayList_city,
               ArrayList<MyItem> arrayList_bodytype, ArrayList<MyItem> arrayList_fueltype,
               ArrayList<MyItem> arrayList_trans, ArrayList<ColorItem> arrayList_color, ArrayList<EquipmentItem> arrayList_equip);
}
