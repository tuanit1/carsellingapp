package com.example.autosellingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

public class SharedPref {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String TAG_RECENT_ADS = "recent_ads", SHARED_PREF_AUTOLOGIN = "autologin", PASSWORD = "password", EMAIL = "email", IS_REMEMBER = "isRemember",
    IS_DARK_MODE = "is_dark_mode";

    public SharedPref(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public String getRecentAds(){
        return sharedPreferences.getString(TAG_RECENT_ADS, "");
    }

    public void addRecentAds(int ads_id){

        String array_recent_json = sharedPreferences.getString(TAG_RECENT_ADS, "");

        ArrayList<String> array_recent;

        if(array_recent_json.equals("")){
            array_recent = new ArrayList<>();
        }else {
            array_recent = new Gson().fromJson(array_recent_json, ArrayList.class);
        }

        if(isAdsInArray(ads_id, array_recent)){
            for (int i = 0; i < array_recent.size(); i++){
                if(ads_id == Integer.parseInt(array_recent.get(i))){
                    String temp_id = array_recent.get(i);
                    array_recent.remove(i--);
                    array_recent.add(temp_id);
                    break;
                }
            }
        }else {
            array_recent.add(String.valueOf(ads_id));
        }

        editor.putString(TAG_RECENT_ADS, new Gson().toJson(array_recent));
        editor.commit();
    }

    private Boolean isAdsInArray(int ads_id, ArrayList<String> arrayList){
        for(String id : arrayList){
            if(ads_id == Integer.parseInt(id)){
                return true;
            }
        }
        return false;
    }

    public Boolean getIsAutoLogin() {
        return sharedPreferences.getBoolean(SHARED_PREF_AUTOLOGIN, false);
    }

    public void setIsAutoLogin(Boolean isAutoLogin) {
        editor.putBoolean(SHARED_PREF_AUTOLOGIN, isAutoLogin);
        editor.apply();
    }

    public void setPassword(String password){
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    public void setEmail(String email){
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, "");
    }

    public Boolean isRemember(){
        return sharedPreferences.getBoolean(IS_REMEMBER, false);
    }

    public void setRemember(Boolean isRemember){
        editor.putBoolean(IS_REMEMBER, isRemember);
        editor.apply();
    }

    public void setDarkMode(boolean isDarkMode){
        editor.putBoolean(IS_DARK_MODE, isDarkMode);
        editor.apply();
    }

    public boolean isDarkMode(){
        return sharedPreferences.getBoolean(IS_DARK_MODE, false);
    }
}
