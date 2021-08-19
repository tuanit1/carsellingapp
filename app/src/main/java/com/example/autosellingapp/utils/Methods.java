package com.example.autosellingapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;

import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.ColorItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Methods {
    private Context context;
    private boolean IS_CHANGE_IMAGE = false;

    public Methods(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isLogged(){
        return Constant.isLogged;
    }

    public UserItem getUserItemByUsername(ArrayList<UserItem> arrayList, String uid){
        for(UserItem item : arrayList){
            if(item.getUid().equals(uid)){
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

    public boolean isUserIdInUserChatList(String user_id, ArrayList<String> arrayList_chatlist){
        for(String uid : arrayList_chatlist){
            if(uid.equals(user_id)){
                return true;
            }
        }
        return false;
    }

    public boolean isFavourite(ArrayList<UserItem> arrayList_user, int id){
        if(isLogged()){
            UserItem user = getUserItemByUsername(arrayList_user, Constant.UID);
            for(String i : user.getFavourite_ads()){
                if(Integer.parseInt(i) == id){
                    return true;
                }
            }
        }
        return false;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public String getPathImage(Uri uri) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String filePath = "";
                String wholeID = DocumentsContract.getDocumentId(uri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = {MediaStore.Images.Media.DATA};

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return filePath;
            } else {

                if (uri == null) {
                    return null;
                }
                // try to retrieve the image from the media store first
                // this will only work for images selected from gallery
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String retunn = cursor.getString(column_index);
                    cursor.close();
                    return retunn;
                }
                // this is our fallback here
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (uri == null) {
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String returnn = cursor.getString(column_index);
                cursor.close();
                return returnn;
            }
            // this is our fallback here
            return uri.getPath();
        }
    }

    public RequestBody getAPIRequest(String method, Bundle bundle, ArrayList<File> arrayList_file){
        JsonObject jsObj = new JsonObject();
        jsObj.addProperty("method_name", method);
        jsObj.addProperty("API_KEY", Constant.API_KEY);
        if(method.equals(Constant.METHOD_UPDATELIKE)){

            String uid = bundle.getString(Constant.TAG_UID);
            String user_favlist = bundle.getString("user_favlist");
            Boolean is_favourite = bundle.getBoolean("is_favorite");
            int ads_id = bundle.getInt("ads_id");

            jsObj.addProperty(Constant.TAG_UID, uid);
            jsObj.addProperty("user_favlist", user_favlist);
            jsObj.addProperty("status", (is_favourite)?"remove":"add");
            jsObj.addProperty("ads_id", ads_id);
        }

        if(method.equals(Constant.METHOD_UPDATEFOLLOW)){
            jsObj.addProperty(Constant.TAG_UID, Constant.UID);
            jsObj.addProperty(Constant.TAG_FOLLOWLIST, bundle.getString(Constant.TAG_FOLLOWLIST));
        }

        if(method.equals(Constant.METHOD_SELLING)){
            String uid = bundle.getString(Constant.TAG_UID);
            jsObj.addProperty(Constant.TAG_UID, uid);
        }
        if(method.equals(Constant.METHOD_POST_ADS)){
            int SELECTED_MANU_ID = bundle.getInt(Constant.TAG_MANU_ID);
            int SELECTED_MODEL_ID = bundle.getInt(Constant.TAG_MODEL_ID);
            String SELECTED_CARNAME = bundle.getString(Constant.TAG_CAR_NAME);
            int SELECTED_PRICE = bundle.getInt(Constant.TAG_ADS_PRICE);
            int SELECTED_CITY_ID = bundle.getInt(Constant.TAG_CITY_ID);
            String SELECTED_ADDRESS = bundle.getString(Constant.TAG_ADS_LOCATION);
            int SELECTED_POWER = bundle.getInt(Constant.TAG_CAR_POWER);
            int SELECTED_ENGINESIZE = bundle.getInt(Constant.TAG_CAR_ENGINESIZE);
            int SELECTED_MILEAGE = bundle.getInt(Constant.TAG_ADS_MILEAGE);
            int SELECTED_BODY_TYPE_ID = bundle.getInt(Constant.TAG_BODY_TYPE_ID);
            int SELECTED_FUEL_TYPE_ID = bundle.getInt(Constant.TAG_FUEL_TYPE_ID);
            int SELECTED_CONDITION = bundle.getInt(Constant.TAG_CAR_CONDITION);
            int SELECTED_YEAR = bundle.getInt(Constant.TAG_CAR_YEAR);
            int SELECTED_TRANS_ID = bundle.getInt(Constant.TAG_TRANS_ID);
            int SELECTED_COLOR_ID = bundle.getInt(Constant.TAG_COLOR_ID);
            int SELECTED_SEAT = bundle.getInt(Constant.TAG_CAR_SEATS);
            int SELECTED_DOOR = bundle.getInt(Constant.TAG_CAR_DOORS);
            int SELECTED_PREUSER = bundle.getInt(Constant.TAG_CAR_PREOWNER);
            int SELECTED_GEARS = bundle.getInt(Constant.TAG_CAR_GEARS);
            int SELECTED_CYLINDER = bundle.getInt(Constant.TAG_CAR_CYLINDER);
            int SELECTED_WEIGHT = bundle.getInt(Constant.TAG_CAR_KERBWEIGHT);
            double SELECTED_FUELCONSUMP = bundle.getDouble(Constant.TAG_CAR_FUELCONSUMP);
            int SELECTED_CO2EMISSION = bundle.getInt(Constant.TAG_CAR_CO2EMISSION);
            String SELECTED_DESCRIPTION = bundle.getString(Constant.TAG_ADS_DESCRIPTION);
            ArrayList<EquipmentItem> arrayList_equip = (ArrayList<EquipmentItem>) bundle.getSerializable(Constant.TAG_EQUIP);
            ArrayList<String> arrayList_equip_id = new ArrayList<>();
            for (EquipmentItem item : arrayList_equip){
                arrayList_equip_id.add(String.valueOf(item.getEquip_id()));
            }
            String json = new Gson().toJson(arrayList_equip_id);

            jsObj.addProperty(Constant.TAG_MANU_ID, SELECTED_MANU_ID);
            jsObj.addProperty(Constant.TAG_MODEL_ID, SELECTED_MODEL_ID);
            jsObj.addProperty(Constant.TAG_CAR_NAME, SELECTED_CARNAME);
            jsObj.addProperty(Constant.TAG_ADS_PRICE, SELECTED_PRICE);
            jsObj.addProperty(Constant.TAG_CITY_ID, SELECTED_CITY_ID);
            jsObj.addProperty(Constant.TAG_ADS_LOCATION, SELECTED_ADDRESS);
            jsObj.addProperty(Constant.TAG_CAR_POWER, SELECTED_POWER);
            jsObj.addProperty(Constant.TAG_CAR_ENGINESIZE, SELECTED_ENGINESIZE);
            jsObj.addProperty(Constant.TAG_ADS_MILEAGE, SELECTED_MILEAGE);
            jsObj.addProperty(Constant.TAG_BODY_TYPE_ID, SELECTED_BODY_TYPE_ID);
            jsObj.addProperty(Constant.TAG_FUEL_TYPE_ID, SELECTED_FUEL_TYPE_ID);
            jsObj.addProperty(Constant.TAG_CAR_CONDITION, SELECTED_CONDITION);
            jsObj.addProperty(Constant.TAG_CAR_YEAR, SELECTED_YEAR);
            jsObj.addProperty(Constant.TAG_TRANS_ID, SELECTED_TRANS_ID);
            jsObj.addProperty(Constant.TAG_COLOR_ID, SELECTED_COLOR_ID);
            jsObj.addProperty(Constant.TAG_CAR_SEATS, SELECTED_SEAT);
            jsObj.addProperty(Constant.TAG_CAR_DOORS, SELECTED_DOOR);
            jsObj.addProperty(Constant.TAG_CAR_PREOWNER, SELECTED_PREUSER);
            jsObj.addProperty(Constant.TAG_CAR_GEARS, SELECTED_GEARS);
            jsObj.addProperty(Constant.TAG_CAR_CYLINDER, SELECTED_CYLINDER);
            jsObj.addProperty(Constant.TAG_CAR_KERBWEIGHT, SELECTED_WEIGHT);
            jsObj.addProperty(Constant.TAG_CAR_FUELCONSUMP, SELECTED_FUELCONSUMP);
            jsObj.addProperty(Constant.TAG_CAR_CO2EMISSION, SELECTED_CO2EMISSION);
            jsObj.addProperty(Constant.TAG_ADS_DESCRIPTION, SELECTED_DESCRIPTION);
            jsObj.addProperty(Constant.TAG_CAR_EQUIP, json);
            jsObj.addProperty(Constant.TAG_UID, Constant.UID);
            jsObj.addProperty("image_count", arrayList_file.size());

        }

        if(method.equals(Constant.METHOD_EDIT_SELLING)){

            IS_CHANGE_IMAGE = bundle.getBoolean("is_change_image");
            int SELECTED_ADS_ID = bundle.getInt(Constant.TAG_ADS_ID);
            int SELECTED_CAR_ID = bundle.getInt(Constant.TAG_CAR_ID);
            int SELECTED_MANU_ID = bundle.getInt(Constant.TAG_MANU_ID);
            int SELECTED_MODEL_ID = bundle.getInt(Constant.TAG_MODEL_ID);
            String SELECTED_CARNAME = bundle.getString(Constant.TAG_CAR_NAME);
            int SELECTED_PRICE = bundle.getInt(Constant.TAG_ADS_PRICE);
            int SELECTED_CITY_ID = bundle.getInt(Constant.TAG_CITY_ID);
            String SELECTED_ADDRESS = bundle.getString(Constant.TAG_ADS_LOCATION);
            int SELECTED_POWER = bundle.getInt(Constant.TAG_CAR_POWER);
            int SELECTED_ENGINESIZE = bundle.getInt(Constant.TAG_CAR_ENGINESIZE);
            int SELECTED_MILEAGE = bundle.getInt(Constant.TAG_ADS_MILEAGE);
            int SELECTED_BODY_TYPE_ID = bundle.getInt(Constant.TAG_BODY_TYPE_ID);
            int SELECTED_FUEL_TYPE_ID = bundle.getInt(Constant.TAG_FUEL_TYPE_ID);
            int SELECTED_CONDITION = bundle.getInt(Constant.TAG_CAR_CONDITION);
            int SELECTED_YEAR = bundle.getInt(Constant.TAG_CAR_YEAR);
            int SELECTED_TRANS_ID = bundle.getInt(Constant.TAG_TRANS_ID);
            int SELECTED_COLOR_ID = bundle.getInt(Constant.TAG_COLOR_ID);
            int SELECTED_SEAT = bundle.getInt(Constant.TAG_CAR_SEATS);
            int SELECTED_DOOR = bundle.getInt(Constant.TAG_CAR_DOORS);
            int SELECTED_PREUSER = bundle.getInt(Constant.TAG_CAR_PREOWNER);
            int SELECTED_GEARS = bundle.getInt(Constant.TAG_CAR_GEARS);
            int SELECTED_CYLINDER = bundle.getInt(Constant.TAG_CAR_CYLINDER);
            int SELECTED_WEIGHT = bundle.getInt(Constant.TAG_CAR_KERBWEIGHT);
            double SELECTED_FUELCONSUMP = bundle.getDouble(Constant.TAG_CAR_FUELCONSUMP);
            int SELECTED_CO2EMISSION = bundle.getInt(Constant.TAG_CAR_CO2EMISSION);
            String SELECTED_DESCRIPTION = bundle.getString(Constant.TAG_ADS_DESCRIPTION);
            ArrayList<EquipmentItem> arrayList_equip = (ArrayList<EquipmentItem>) bundle.getSerializable(Constant.TAG_EQUIP);
            ArrayList<String> arrayList_equip_id = new ArrayList<>();
            for (EquipmentItem item : arrayList_equip){
                arrayList_equip_id.add(String.valueOf(item.getEquip_id()));
            }
            String json = new Gson().toJson(arrayList_equip_id);

            jsObj.addProperty("is_change_image", (IS_CHANGE_IMAGE)?"true":"false");
            jsObj.addProperty(Constant.TAG_ADS_ID, SELECTED_ADS_ID);
            jsObj.addProperty(Constant.TAG_CAR_ID, SELECTED_CAR_ID);
            jsObj.addProperty(Constant.TAG_MANU_ID, SELECTED_MANU_ID);
            jsObj.addProperty(Constant.TAG_MODEL_ID, SELECTED_MODEL_ID);
            jsObj.addProperty(Constant.TAG_CAR_NAME, SELECTED_CARNAME);
            jsObj.addProperty(Constant.TAG_ADS_PRICE, SELECTED_PRICE);
            jsObj.addProperty(Constant.TAG_CITY_ID, SELECTED_CITY_ID);
            jsObj.addProperty(Constant.TAG_ADS_LOCATION, SELECTED_ADDRESS);
            jsObj.addProperty(Constant.TAG_CAR_POWER, SELECTED_POWER);
            jsObj.addProperty(Constant.TAG_CAR_ENGINESIZE, SELECTED_ENGINESIZE);
            jsObj.addProperty(Constant.TAG_ADS_MILEAGE, SELECTED_MILEAGE);
            jsObj.addProperty(Constant.TAG_BODY_TYPE_ID, SELECTED_BODY_TYPE_ID);
            jsObj.addProperty(Constant.TAG_FUEL_TYPE_ID, SELECTED_FUEL_TYPE_ID);
            jsObj.addProperty(Constant.TAG_CAR_CONDITION, SELECTED_CONDITION);
            jsObj.addProperty(Constant.TAG_CAR_YEAR, SELECTED_YEAR);
            jsObj.addProperty(Constant.TAG_TRANS_ID, SELECTED_TRANS_ID);
            jsObj.addProperty(Constant.TAG_COLOR_ID, SELECTED_COLOR_ID);
            jsObj.addProperty(Constant.TAG_CAR_SEATS, SELECTED_SEAT);
            jsObj.addProperty(Constant.TAG_CAR_DOORS, SELECTED_DOOR);
            jsObj.addProperty(Constant.TAG_CAR_PREOWNER, SELECTED_PREUSER);
            jsObj.addProperty(Constant.TAG_CAR_GEARS, SELECTED_GEARS);
            jsObj.addProperty(Constant.TAG_CAR_CYLINDER, SELECTED_CYLINDER);
            jsObj.addProperty(Constant.TAG_CAR_KERBWEIGHT, SELECTED_WEIGHT);
            jsObj.addProperty(Constant.TAG_CAR_FUELCONSUMP, SELECTED_FUELCONSUMP);
            jsObj.addProperty(Constant.TAG_CAR_CO2EMISSION, SELECTED_CO2EMISSION);
            jsObj.addProperty(Constant.TAG_ADS_DESCRIPTION, SELECTED_DESCRIPTION);
            jsObj.addProperty(Constant.TAG_CAR_EQUIP, json);
            jsObj.addProperty(Constant.TAG_UID, Constant.UID);
            jsObj.addProperty("image_count", arrayList_file.size());
        }

        if(method.equals(Constant.METHOD_DELETE_SELLING)){
            int SELECTED_ADS_ID = bundle.getInt(Constant.TAG_ADS_ID);
            int SELECTED_CAR_ID = bundle.getInt(Constant.TAG_CAR_ID);

            jsObj.addProperty(Constant.TAG_ADS_ID, SELECTED_ADS_ID);
            jsObj.addProperty(Constant.TAG_CAR_ID, SELECTED_CAR_ID);
        }

        if(method.equals(Constant.METHOD_MARK_AVAILABLE)){
            int SELECTED_ADS_ID = bundle.getInt(Constant.TAG_ADS_ID);
            int SELECTED_ADS_IS_AVAILABLE = bundle.getInt(Constant.TAG_ADS_ISAVAILABLE);

            jsObj.addProperty(Constant.TAG_ADS_ID, SELECTED_ADS_ID);
            jsObj.addProperty(Constant.TAG_ADS_ISAVAILABLE, SELECTED_ADS_IS_AVAILABLE);
        }

        if(method.equals(Constant.METHOD_USER_FAVOURITE)){
            jsObj.addProperty(Constant.TAG_UID, Constant.UID);
        }

        if(method.equals(Constant.MEDTHOD_SIGNUP)){
            jsObj.addProperty(Constant.TAG_UID, bundle.getString(Constant.TAG_UID));
            jsObj.addProperty(Constant.TAG_EMAIL, bundle.getString(Constant.TAG_EMAIL));
            jsObj.addProperty(Constant.TAG_PHONE, bundle.getString(Constant.TAG_PHONE));
            jsObj.addProperty(Constant.TAG_ADDRESS, bundle.getString(Constant.TAG_ADDRESS));
            jsObj.addProperty(Constant.TAG_FULLNAME, bundle.getString(Constant.TAG_FULLNAME));
        }

        if(method.equals(Constant.METHOD_UPDATE_CHATLIST)){
            jsObj.addProperty(Constant.TAG_UID, bundle.getString(Constant.TAG_UID));
            jsObj.addProperty(Constant.TAG_CHATLIST, bundle.getString(Constant.TAG_CHATLIST));
        }

        if(method.equals(Constant.METHOD_PROFILE)){
            jsObj.addProperty(Constant.TAG_UID, bundle.getString(Constant.TAG_UID));
        }

        if(method.equals(Constant.METHOD_UPDATE_USER)){
            IS_CHANGE_IMAGE = bundle.getBoolean("is_change_image");
            jsObj.addProperty("is_change_image", IS_CHANGE_IMAGE?"true":"false");
            jsObj.addProperty(Constant.TAG_UID, Constant.UID);
            jsObj.addProperty(Constant.TAG_FULLNAME, bundle.getString(Constant.TAG_FULLNAME));
            jsObj.addProperty(Constant.TAG_PHONE, bundle.getString(Constant.TAG_PHONE));
            jsObj.addProperty(Constant.TAG_ADDRESS, bundle.getString(Constant.TAG_ADDRESS));
        }

        if(method.equals(Constant.METHOD_RECENT)){
            jsObj.addProperty(Constant.TAG_RECENTADS, bundle.getString(Constant.TAG_RECENTADS));
        }

        if(method.equals(Constant.METHOD_POST_ADS)){
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            builder.addFormDataPart("data", jsObj.toString());

            for (int i = 0 ; i < arrayList_file.size(); i++){
                builder.addFormDataPart("car_image"+i, arrayList_file.get(i).getName(), RequestBody.create(MEDIA_TYPE_PNG, arrayList_file.get(i)));
            }

            return builder.build();
        }else if(method.equals(Constant.METHOD_EDIT_SELLING)){
            if(IS_CHANGE_IMAGE){
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);

                builder.addFormDataPart("data", jsObj.toString());

                for (int i = 0 ; i < arrayList_file.size(); i++){
                    builder.addFormDataPart("car_image"+i, arrayList_file.get(i).getName(), RequestBody.create(MEDIA_TYPE_PNG, arrayList_file.get(i)));
                }

                return builder.build();
            }else{
                return new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("data", jsObj.toString())
                        .build();
            }
        }else if(method.equals(Constant.METHOD_UPDATE_USER)){
            if(IS_CHANGE_IMAGE){
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);

                builder.addFormDataPart("data", jsObj.toString());

                builder.addFormDataPart("user_image", arrayList_file.get(0).getName(), RequestBody.create(MEDIA_TYPE_PNG, arrayList_file.get(0)));

                return builder.build();
            }else{
                return new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("data", jsObj.toString())
                        .build();
            }
        }else{
            return new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("data", jsObj.toString())
                    .build();
        }
    }
    public String ToBase64(String input){
        byte[] encodeValue = Base64.encode(input.getBytes(), Base64.DEFAULT);
        return new String(encodeValue);
    }

    public ColorItem getColorItemByID(ArrayList<ColorItem> array_color, int color_id) {
        for(ColorItem item : array_color){
            if (item.getColor_id() == color_id){
                return item;
            }
        }
        return null;
    }

    public ManufacturerItem getManuItemByID(ArrayList<ManufacturerItem> array_manu, int manu_id) {
        for(ManufacturerItem item : array_manu){
            if(item.getManu_id() == manu_id){
                return item;
            }
        }
        return null;
    }

    public void removeFavorite(ArrayList<String> arrayList_fav, int ads_id) {
        for(int i = 0; i < arrayList_fav.size(); i++){
            String str = arrayList_fav.get(i);
            if(Integer.parseInt(str) == ads_id){
                arrayList_fav.remove(i);
                break;
            }
        }
    }

    public void addFavorite(ArrayList<String> arrayList_fav, int ads_id){
        arrayList_fav.add(String.valueOf(ads_id));
    }

    public void follow(ArrayList<String> arrayList_follow, String uid){
        arrayList_follow.add(uid);
    }

    public void unfollow(ArrayList<String> arrayList_follow, String uid){
        for(int i = 0; i < arrayList_follow.size(); i++){
            String str = arrayList_follow.get(i);
            if(str.equals(uid)){
                arrayList_follow.remove(i);
                break;
            }
        }
    }

    public boolean isMyAds(AdsItem ads) {
        return ads.getUid().equals(Constant.UID);
    }

    public boolean isFollowing(ArrayList<String> followlist, String user_id) {
        for(String uid : followlist){
            if(uid.equals(user_id)){
                return true;
            }
        }
        return false;
    }
}
