package com.example.autosellingapp.asynctasks;

import android.os.AsyncTask;

import com.example.autosellingapp.interfaces.LoadManuListener;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadHome extends AsyncTask<Void, String, String> {

    private RequestBody requestBody;
    private ArrayList<ManufacturerItem> arrayList = new ArrayList<>();
    private LoadManuListener listener;

    public LoadHome(LoadManuListener listener, RequestBody requestBody){
        this.requestBody = requestBody;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String json = JsonUtils.okhttpPost(Constant.SERVER_URL+"api.php", requestBody);

            if(!json.equals("")){
                JSONObject mainJson = new JSONObject(json);
                JSONArray jsonArray = mainJson.getJSONArray(Constant.TAG_ROOT);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    int id = obj.getInt(Constant.TAG_MANU_ID);
                    String name = obj.getString(Constant.TAG_MANU_NAME);
                    String image = obj.getString(Constant.TAG_MANU_THUMB);

                    ManufacturerItem objItem = new ManufacturerItem(id, name, image);
                    arrayList.add(objItem);
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
        listener.onEnd(s, arrayList);
        super.onPostExecute(s);
    }
}
