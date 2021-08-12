package com.example.autosellingapp.interfaces;

import com.example.autosellingapp.items.AdsItem;

public interface EditDeleteSellingListener {
    void onEdit(AdsItem ads);
    void onDelete(AdsItem ads);
    void onMark(AdsItem ads);
}
