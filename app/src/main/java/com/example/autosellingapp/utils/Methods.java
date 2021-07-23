package com.example.autosellingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.widget.ArrayAdapter;

import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Methods {
    private Context context;

    public Methods(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public UserItem getUserItemByUsername(ArrayList<UserItem> arrayList, String username){
        for(UserItem item : arrayList){
            if(item.getUsername().equals(username)){
                return item;
            }
        }
        return null;
    }

    public CarItem getCarItemByID(ArrayList<CarItem> arrayList, int id){
        for(CarItem item : arrayList){
            if(id == item.getCar_id()){
                return item;
            }
        }
        return null;
    }

    public MyItem getMyItemByID(ArrayList<MyItem> arrayList, int id){
        for (MyItem item : arrayList){
            if(id == item.getId()){
                return item;
            }
        }
        return null;
    }

    public ModelItem getModelItemByID(ArrayList<ModelItem> arrayList, int id){
        for(ModelItem item : arrayList){
            if(item.getModel_id() == id){
                return item;
            }
        }
        return null;
    }
    public boolean isEquipItemInEquipArray(EquipmentItem item , ArrayList<String> arrayList_equipID){
        for (String id : arrayList_equipID){
            if(item.getEquip_id() == Integer.parseInt(id)){
                return true;
            }
        }
        return false;
    }

    public boolean isFavourite(ArrayList<UserItem> arrayList_user, int id){
        if(!Constant.USERNAME.equals("")){
            UserItem user = getUserItemByUsername(arrayList_user, Constant.USERNAME);
            for(String i : user.getFavourite_ads()){
                if(Integer.parseInt(i) == id){
                    return true;
                }
            }
        }
        return false;
    }

    public RequestBody getAPIRequest(String method){
        JsonObject jsObj = new JsonObject();
        jsObj.addProperty("method_name", method);
        jsObj.addProperty("API_KEY", Constant.API_KEY);
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", jsObj.toString())
                .build();
    }
    public String ToBase64(String input){
        byte[] encodeValue = Base64.encode(input.getBytes(), Base64.DEFAULT);
        return new String(encodeValue);
    }
}
