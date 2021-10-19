package com.example.autosellingapp.activity;

import android.app.Activity;
import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.SharedPref;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

    private SharedPref sharedPref;

    @Override
    public void onCreate() {


        super.onCreate();

        FirebaseApp.initializeApp(this);

        sharedPref = new SharedPref(this);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        if(sharedPref.isDarkMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }
}
