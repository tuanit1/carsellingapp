package com.example.autosellingapp.activity;

import android.app.Activity;
import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.SharedPref;

public class MyApplication extends Application {

    private SharedPref sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = new SharedPref(this);

        if(sharedPref.isDarkMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
