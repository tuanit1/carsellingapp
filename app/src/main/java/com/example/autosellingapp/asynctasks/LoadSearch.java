package com.example.autosellingapp.asynctasks;

import android.graphics.Color;
import android.os.AsyncTask;

import com.example.autosellingapp.interfaces.LoadSearchListener;
import com.example.autosellingapp.items.ColorItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
        listener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
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
        listener.onEnd(s, arrayList_manu, arrayList_model, arrayList_city, arrayList_bodytype, arrayList_fueltype, arrayList_trans, arrayList_color, arrayList_equip);
        super.onPostExecute(s);
    }
}
