package com.example.autosellingapp.asynctasks;

import android.os.AsyncTask;
import android.util.Base64;

import com.example.autosellingapp.interfaces.LoadUserListener;
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
