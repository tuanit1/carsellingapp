package com.example.autosellingapp.asynctasks;

import android.os.AsyncTask;

import com.example.autosellingapp.interfaces.UpdateFavListener;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.JsonUtils;
import com.google.gson.Gson;

import okhttp3.RequestBody;

public class UpdateFavouriteAsync extends AsyncTask<Void, String, String> {

    private RequestBody requestBody;
    private UpdateFavListener listener;

    public UpdateFavouriteAsync(UpdateFavListener listener, RequestBody requestBody){
        this.listener = listener;
        this.requestBody = requestBody;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String json = JsonUtils.okhttpPost(Constant.SERVER_URL+"api.php", requestBody);

            if(json.equals("1")){
                return "1";
            }else{
                return "0";
            }
        } catch (Exception e){
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onEnd(s);
        super.onPostExecute(s);
    }
}
