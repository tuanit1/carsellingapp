package com.example.autosellingapp.asynctasks;

import android.os.AsyncTask;
import android.util.Base64;

import com.example.autosellingapp.interfaces.LoadSearchListener;
import com.example.autosellingapp.items.ColorItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.JsonUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;

public class LoadSearch extends AsyncTask<Void, String, String> {

    private LoadSearchListener listener;
    private RequestBody requestBody;
    private ArrayList<ManufacturerItem> arrayList_manu;
    private ArrayList<ModelItem> arrayList_model;
    private ArrayList<MyItem> arrayList_city;
    private ArrayList<MyItem> arrayList_bodytype;
    private ArrayList<MyItem> arrayList_fueltype;
    private ArrayList<MyItem> arrayList_trans;
    private ArrayList<ColorItem> arrayList_color;
    private ArrayList<EquipmentItem> arrayList_equip;
    private ArrayList<UserItem> arrayList_user;

    public LoadSearch(LoadSearchListener listener, RequestBody requestBody) {
        this.listener = listener;
        this.requestBody = requestBody;
    }

    @Override
    protected void onPreExecute() {
        arrayList_manu = new ArrayList<>();
        arrayList_model = new ArrayList<>();
        arrayList_city = new ArrayList<>();
        arrayList_bodytype = new ArrayList<>();
        arrayList_fueltype = new ArrayList<>();
        arrayList_trans = new ArrayList<>();
        arrayList_color = new ArrayList<>();
        arrayList_equip = new ArrayList<>();
        arrayList_user = new ArrayList<>();
        listener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {

            Gson gson = new Gson();
            String json = JsonUtils.okhttpPost(Constant.SERVER_URL+"api.php", requestBody);

            if(!json.equals("")){
                JSONObject mainJson = new JSONObject(json);
                JSONObject jsonArray = mainJson.getJSONObject(Constant.TAG_ROOT);

                JSONArray data_manu = jsonArray.getJSONArray(Constant.TAG_MANU);
                JSONArray data_model = jsonArray.getJSONArray(Constant.TAG_MODEL);
                JSONArray data_city = jsonArray.getJSONArray(Constant.TAG_CITY);
                JSONArray data_bodytype = jsonArray.getJSONArray(Constant.TAG_BODY_TYPE);
                JSONArray data_fueltype = jsonArray.getJSONArray(Constant.TAG_FUEL_TYPE);
                JSONArray data_trans = jsonArray.getJSONArray(Constant.TAG_TRANS);
                JSONArray data_color = jsonArray.getJSONArray(Constant.TAG_COLOR);
                JSONArray data_equip = jsonArray.getJSONArray(Constant.TAG_EQUIP);
                JSONArray data_user = jsonArray.getJSONArray(Constant.TAG_USER);


                for (int i = 0; i < data_manu.length(); i++) {
                    JSONObject obj = data_manu.getJSONObject(i);

                    int id = obj.getInt(Constant.TAG_MANU_ID);
                    String name = obj.getString(Constant.TAG_MANU_NAME);
                    String image = obj.getString(Constant.TAG_MANU_THUMB);

                    ManufacturerItem objItem = new ManufacturerItem(id, name, image);
                    arrayList_manu.add(objItem);
                }

                for (int i = 0; i < data_model.length(); i++) {
                    JSONObject obj = data_model.getJSONObject(i);

                    int model_id = obj.getInt(Constant.TAG_MODEL_ID);
                    int manu_id = obj.getInt(Constant.TAG_MANU_ID);
                    String model_name = obj.getString(Constant.TAG_MODEL_NAME);

                    ModelItem objItem = new ModelItem(model_id, manu_id, model_name);
                    arrayList_model.add(objItem);
                }

                for (int i = 0; i < data_city.length(); i++) {
                    JSONObject obj = data_city.getJSONObject(i);

                    int id = obj.getInt(Constant.TAG_CITY_ID);
                    String name = obj.getString(Constant.TAG_CITY_NAME);

                    MyItem objItem = new MyItem(id, name);
                    arrayList_city.add(objItem);
                }

                for (int i = 0; i < data_bodytype.length(); i++) {
                    JSONObject obj = data_bodytype.getJSONObject(i);

                    int id = obj.getInt(Constant.TAG_BODY_TYPE_ID);
                    String name = obj.getString(Constant.TAG_BODY_TYPE_NAME);

                    MyItem objItem = new MyItem(id, name);
                    arrayList_bodytype.add(objItem);
                }

                for (int i = 0; i < data_fueltype.length(); i++) {
                    JSONObject obj = data_fueltype.getJSONObject(i);

                    int id = obj.getInt(Constant.TAG_FUEL_TYPE_ID);
                    String name = obj.getString(Constant.TAG_FUEL_TYPE_NAME);

                    MyItem objItem = new MyItem(id, name);
                    arrayList_fueltype.add(objItem);
                }

                for (int i = 0; i < data_trans.length(); i++) {
                    JSONObject obj = data_trans.getJSONObject(i);

                    int id = obj.getInt(Constant.TAG_TRANS_ID);
                    String name = obj.getString(Constant.TAG_TRANS_NAME);

                    MyItem objItem = new MyItem(id, name);
                    arrayList_trans.add(objItem);
                }

                for (int i = 0; i < data_color.length(); i++) {
                    JSONObject obj = data_color.getJSONObject(i);

                    int id = obj.getInt(Constant.TAG_COLOR_ID);
                    String name = obj.getString(Constant.TAG_COLOR_NAME);
                    String code = obj.getString(Constant.TAG_COLOR_CODE);

                    ColorItem objItem = new ColorItem(id, name, code);
                    arrayList_color.add(objItem);
                }

                for (int i = 0; i < data_equip.length(); i++) {
                    JSONObject obj = data_equip.getJSONObject(i);

                    int id = obj.getInt(Constant.TAG_EQUIP_ID);
                    String name = obj.getString(Constant.TAG_EQUIP_NAME);

                    EquipmentItem objItem = new EquipmentItem(id, name);
                    arrayList_equip.add(objItem);
                }

                for (int i = 0; i < data_user.length(); i++){
                    JSONObject obj = data_user.getJSONObject(i);

                    String uid = obj.getString(Constant.TAG_UID);
                    String address;
                    String phoneNumber;
                    String fullName;
                    String email;
                    if(checkForEncode(obj.getString(Constant.TAG_ADDRESS))) {
                        address = Base64Decode(obj.getString(Constant.TAG_ADDRESS));
                    }else{
                        address = obj.getString(Constant.TAG_ADDRESS);
                    }

                    if(checkForEncode(obj.getString(Constant.TAG_PHONE))) {
                        phoneNumber = Base64Decode(obj.getString(Constant.TAG_PHONE));
                    }else{
                        phoneNumber = obj.getString(Constant.TAG_PHONE);
                    }

                    if(checkForEncode(obj.getString(Constant.TAG_FULLNAME))) {
                        fullName = Base64Decode(obj.getString(Constant.TAG_FULLNAME));
                    }else{
                        fullName = obj.getString(Constant.TAG_FULLNAME);
                    }

                    if(checkForEncode(obj.getString(Constant.TAG_EMAIL))) {
                        email = Base64Decode(obj.getString(Constant.TAG_EMAIL));
                    }else{
                        email = obj.getString(Constant.TAG_EMAIL);
                    }

                    String image = Base64Decode(obj.getString(Constant.TAG_USER_IMAGE));

//                    if(checkForEncode(obj.getString(Constant.TAG_USER_IMAGE))){
//                        image = Base64Decode(obj.getString(Constant.TAG_USER_IMAGE));
//                    }else {
//                        image = obj.getString(Constant.TAG_USER_IMAGE);
//                    }
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
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onEnd(s, arrayList_manu, arrayList_model, arrayList_city, arrayList_bodytype, arrayList_fueltype, arrayList_trans, arrayList_color, arrayList_equip, arrayList_user);
        super.onPostExecute(s);
    }

    public String Base64Decode(String input) throws UnsupportedEncodingException {
        byte[] encodeValue = Base64.decode(input, Base64.DEFAULT);
        return new String(encodeValue, "UTF-8");
    }
    public boolean checkForEncode(String string) {
        String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(string);
        return m.find();
    }
}
