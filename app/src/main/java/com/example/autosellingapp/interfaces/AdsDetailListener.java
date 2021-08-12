package com.example.autosellingapp.interfaces;

import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;

public interface AdsDetailListener {
    void onClick(AdsItem adsitem, CarItem carItem);
    void onUserClick(String uid);
}
