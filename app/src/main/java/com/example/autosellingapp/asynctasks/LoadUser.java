package com.example.autosellingapp.asynctasks;

import android.os.AsyncTask;

import com.example.autosellingapp.interfaces.LoadUserListener;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.JsonUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;

public class LoadUser extends AsyncTask<Void, String, String> {

    private RequestBody requestBody;
    private LoadUserListener listener;
    private ArrayList<UserItem> arrayList_user;

    public LoadUser(LoadUserListener listener, RequestBody requestBody) {
        this.requestBody = requestBody;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
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

                JSONArray data_user = jsonArray.getJSONArray(Constant.TAG_USER);

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

                    ArrayList<String> recentAds = new ArrayList<>();
                    if(!obj.getString(Constant.TAG_RECENTADS).equals("")){
                        recentAds = gson.fromJson(obj.getString(Constant.TAG_RECENTADS), ArrayList.class);
                    }

                    UserItem objItem = new UserItem(uid, address, phoneNumber, fullName, email, image, chatlist, followlist, recentAds, favourite_ads);
                    arrayList_user.add(objItem);
                }
                return Constant.SUCCESS;
            }else {
                return Constant.FAIL;
            }
        } catch (Exception e){
            e.printStackTrace();
            return Constant.FAIL;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onEnd(s, arrayList_user);
        super.onPostExecute(s);
    }
}
