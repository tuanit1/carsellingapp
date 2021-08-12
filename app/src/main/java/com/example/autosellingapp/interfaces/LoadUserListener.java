package com.example.autosellingapp.interfaces;

import com.example.autosellingapp.items.UserItem;

import java.util.ArrayList;

public interface LoadUserListener {
    void onStart();
    void onEnd(String success, ArrayList<UserItem> arrayList_user);
}
