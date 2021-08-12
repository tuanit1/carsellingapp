package com.example.autosellingapp.interfaces;

import com.example.autosellingapp.items.UserItem;

public interface UserListener {
    void onClick(UserItem user);
    void onLongClick(String uid);
}
