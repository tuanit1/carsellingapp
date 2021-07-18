package com.example.autosellingapp.asynctasks;

import android.os.AsyncTask;

import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadManufacturers extends AsyncTask<String, String, String> {

    private RequestBody requestBody;
    private ArrayList<ManufacturerItem> arrayList = new ArrayList<>();

    @Override
    protected String doInBackground(String... strings) {
        try {
            String json = JsonUtils.okhttpPost(Constant.SERVER_URL, requestBody);

            JSONObject mainJson = new JSONObject(json);
            JSONArray jsonArray = mainJson.getJSONArray(Constant.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                int id = obj.getString(Constant.TAG_COUNTRY_ID);
                String name = obj.getString(Constant.TAG_COUNTRY_NAME);
                String image = obj.getString(Constant.TAG_COUNTRY_IMAGE);

                ManufacturerItem objItem = new ManufacturerItem(id, name, image);
                arrayList.add(objItem);
            }
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }
}
