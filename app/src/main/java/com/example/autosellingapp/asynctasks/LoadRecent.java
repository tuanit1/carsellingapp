package com.example.autosellingapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.autosellingapp.interfaces.LoadCategoryListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.JsonUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.RequestBody;

public class LoadRecent extends AsyncTask<Void, String, String> {

    private LoadCategoryListener listener;
    private RequestBody requestBody;
    private ArrayList<CarItem> arrayList_car;
    private ArrayList<AdsItem> arrayList_ads;
    private ArrayList<MyItem> arrayList_city;
    private ArrayList<UserItem> arrayList_user;

    public LoadRecent(LoadCategoryListener listener, RequestBody requestBody) {
        this.listener = listener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        arrayList_ads = new ArrayList<>();
        arrayList_car = new ArrayList<>();
        arrayList_city = new ArrayList<>();
        arrayList_user = new ArrayList<>();
        listener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String json = JsonUtils.okhttpPost(Constant.SERVER_URL+"api.php", requestBody);

            if(!json.equals("")){

                Gson gson = new Gson();

                JSONObject mainJson = new JSONObject(json);
                JSONObject jsonArray = mainJson.getJSONObject(Constant.TAG_ROOT);

                JSONArray data_ads = jsonArray.getJSONArray(Constant.TAG_ADS);
                JSONArray data_car = jsonArray.getJSONArray(Constant.TAG_CAR);
                JSONArray data_city = jsonArray.getJSONArray(Constant.TAG_CITY);
                JSONArray data_user = jsonArray.getJSONArray(Constant.TAG_USER);

                for (int i = 0; i < data_ads.length(); i++) {
                    JSONObject obj = data_ads.getJSONObject(i);
                    int ads_id = obj.getInt(Constant.TAG_ADS_ID);
                    int car_id = obj.getInt(Constant.TAG_CAR_ID);
                    String username = obj.getString(Constant.TAG_UID);
                    double ads_price = obj.getInt(Constant.TAG_ADS_PRICE);
                    int ads_mileage = obj.getInt(Constant.TAG_ADS_MILEAGE);
                    int city_id = obj.getInt(Constant.TAG_CITY_ID);
                    String ads_location = obj.getString(Constant.TAG_ADS_LOCATION);
                    String ads_description = obj.getString(Constant.TAG_ADS_DESCRIPTION);
                    String ads_posttime_str = obj.getString(Constant.TAG_ADS_POST_TIME);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date ads_posttime = sdf.parse(ads_posttime_str);
                    int ads_likes = obj.getInt(Constant.TAG_ADS_LIKE);
                    boolean ads_isAvailable = obj.getInt(Constant.TAG_ADS_ISAVAILABLE) == 1;

                    AdsItem objItem = new AdsItem(ads_id, car_id, username, ads_price, ads_mileage, city_id, ads_location, ads_description, ads_posttime, ads_likes, ads_isAvailable);
                    arrayList_ads.add(objItem);
                }

                for (int i = 0; i < data_car.length(); i++) {
                    JSONObject obj = data_car.getJSONObject(i);
                    int car_id = obj.getInt(Constant.TAG_CAR_ID);
                    String car_name = obj.getString(Constant.TAG_CAR_NAME);
                    int model_id = obj.getInt(Constant.TAG_MODEL_ID);
                    int bodyType_id = obj.getInt(Constant.TAG_BODY_TYPE_ID);
                    int fuelType_id = obj.getInt(Constant.TAG_FUEL_TYPE_ID);

                    String car_video = obj.getString(Constant.TAG_CAR_VIDEO);

                    ArrayList<String> car_imageList = new ArrayList<>();

                    if(!obj.getString(Constant.TAG_CAR_IMAGELIST).equals("")){
                        car_imageList =  gson.fromJson(obj.getString(Constant.TAG_CAR_IMAGELIST), ArrayList.class);
                    }

                    int car_year = obj.getInt(Constant.TAG_CAR_YEAR);
                    boolean isNew = obj.getInt(Constant.TAG_CAR_CONDITION) == 1;
                    int car_power = obj.getInt(Constant.TAG_CAR_POWER);
                    int trans_id = obj.getInt(Constant.TAG_TRANS_ID);
                    int car_doors = obj.getInt(Constant.TAG_CAR_DOORS);
                    int car_seats = obj.getInt(Constant.TAG_CAR_SEATS);

                    ArrayList<String> car_equipments = new ArrayList<>();
                    if(!obj.getString(Constant.TAG_CAR_EQUIP).equals("")){
                        car_equipments = gson.fromJson(obj.getString(Constant.TAG_CAR_EQUIP), ArrayList.class);
                    }

                    ArrayList<String> car_imagelist_link = new ArrayList<>();

                    if(!obj.getString(Constant.TAG_CAR_IMAGELIST).equals("")){
                        car_imagelist_link =  gson.fromJson(obj.getString(Constant.TAG_CAR_IMAGELIST_LINK), ArrayList.class);
                    }

                    String video_type = obj.getString(Constant.TAG_VIDEO_TYPE);

                    int car_previousOwner = obj.getInt(Constant.TAG_CAR_PREOWNER);
                    int color_id = obj.getInt(Constant.TAG_COLOR_ID);
                    int car_gears = obj.getInt(Constant.TAG_CAR_GEARS);
                    int car_engineSize = obj.getInt(Constant.TAG_CAR_ENGINESIZE);
                    int car_cylinder = obj.getInt(Constant.TAG_CAR_CYLINDER);
                    int car_kerbWeight = obj.getInt(Constant.TAG_CAR_KERBWEIGHT);
                    double car_fuelConsumption = obj.getInt(Constant.TAG_CAR_FUELCONSUMP);
                    int car_co2emission = obj.getInt(Constant.TAG_CAR_CO2EMISSION);

                    CarItem objItem = new CarItem(car_id, car_name, model_id, bodyType_id, fuelType_id, car_video, video_type, car_imageList, car_imagelist_link, car_year, isNew, car_power, trans_id, car_doors, car_seats, car_equipments, car_previousOwner, color_id, car_gears, car_engineSize, car_cylinder, car_kerbWeight, car_fuelConsumption, car_co2emission);
                    arrayList_car.add(objItem);
                }

                for (int i = 0; i < data_city.length(); i++) {
                    JSONObject obj = data_city.getJSONObject(i);

                    int id = obj.getInt(Constant.TAG_CITY_ID);
                    String name = obj.getString(Constant.TAG_CITY_NAME);

                    MyItem objItem = new MyItem(id, name);
                    arrayList_city.add(objItem);
                }

                for (int i = 0; i < data_user.length(); i++){
                    JSONObject obj = data_user.getJSONObject(i);

                    String uid = obj.getString(Constant.TAG_UID);
                    String address = obj.getString(Constant.TAG_ADDRESS);
                    String phoneNumber = obj.getString(Constant.TAG_PHONE);
                    String fullName = obj.getString(Constant.TAG_FULLNAME);
                    String email = obj.getString(Constant.TAG_EMAIL);
                    String image = obj.getString(Constant.TAG_USER_IMAGE);
                    ArrayList<String> favourite_ads = new ArrayList<>();
                    if(!obj.getString(Constant.TAG_FAVLIST).equals("")){
                        favourite_ads = gson.fromJson(obj.getString(Constant.TAG_FAVLIST), ArrayList.class);
                    }
                    ArrayList<String> chatlist = new ArrayList<>();
                    if(!obj.getString(Constant.TAG_CHATLIST).equals("")){
                        chatlist = gson.fromJson(obj.getString(Constant.TAG_CHATLIST), ArrayList.class);
                    }
                    ArrayList<String> followlist = new ArrayList<>();
                    if(!obj.getString(Constant.TAG_FOLLOWLIST).equals("")){
                        followlist = gson.fromJson(obj.getString(Constant.TAG_FOLLOWLIST), ArrayList.class);
                    }

                    UserItem objItem = new UserItem(uid, address, phoneNumber, fullName, email, image, chatlist, followlist, favourite_ads);
                    arrayList_user.add(objItem);
                }

                return "1";
            }else{
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onEnd(s, arrayList_car, arrayList_ads, arrayList_city, arrayList_user, null);
        super.onPostExecute(s);
    }
}
