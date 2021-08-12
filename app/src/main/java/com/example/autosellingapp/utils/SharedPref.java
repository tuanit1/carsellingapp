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
    private String TAG_RECENT_ADS = "recent_ads";

    public SharedPref(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void addRecentAds(AdsItem ads, CarItem car, UserItem user, MyItem city){

        if(isExistInRecent(ads)){
            return;
        }

        String ads_json = new Gson().toJson(ads);
        String car_json = new Gson().toJson(car);
        String user_json = new Gson().toJson(user);
        String city_json = new Gson().toJson(city);

        ArrayList<String> arrayList_set = new ArrayList<>();
        arrayList_set.add(ads_json);
        arrayList_set.add(car_json);
        arrayList_set.add(user_json);
        arrayList_set.add(city_json);

        String array_recent_json = sharedPreferences.getString(TAG_RECENT_ADS, "");

        ArrayList<String> array_recent;

        if(array_recent_json.isEmpty()){
            array_recent = new ArrayList<>();
        }else {
            array_recent = new Gson().fromJson(array_recent_json, ArrayList.class);
        }

        if(array_recent.size() >= 4){
            array_recent.remove(0);
        }

        array_recent.add(new Gson().toJson(arrayList_set));

        editor.putString(TAG_RECENT_ADS, new Gson().toJson(array_recent));
        editor.clear();
    }

    public ArrayList<String> getRecentAds(){
        String array_recent_json = sharedPreferences.getString(TAG_RECENT_ADS, "");

        ArrayList<String> array_recent;

        if(array_recent_json.isEmpty()){
            array_recent = new ArrayList<>();
        }else {
            array_recent = new Gson().fromJson(array_recent_json, ArrayList.class);
        }

        return array_recent;
    }

    private boolean isExistInRecent(AdsItem ads){
        int ads_id = ads.getAds_id();

        String array_recent_json = sharedPreferences.getString(TAG_RECENT_ADS, "");

        ArrayList<String> array_recent;

        if(array_recent_json.isEmpty()){
            return false;
        }else {
            array_recent = new Gson().fromJson(array_recent_json, ArrayList.class);

            for(String arrayList_set_json : array_recent){
                ArrayList<String> arrayList_set = new Gson().fromJson(arrayList_set_json, ArrayList.class);
                String ads_json = arrayList_set.get(0);
                AdsItem ads_item = new Gson().fromJson(ads_json, AdsItem.class);

                if(ads_id == ads_item.getAds_id()){
                    return true;
                }
            }

            return false;
        }
    }
}
