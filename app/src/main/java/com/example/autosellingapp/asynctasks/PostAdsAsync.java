package com.example.autosellingapp.asynctasks;

import android.os.AsyncTask;

import com.example.autosellingapp.interfaces.PostAdsListener;
import com.example.autosellingapp.interfaces.UpdateFavListener;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.JsonUtils;

import okhttp3.RequestBody;

public class PostAdsAsync extends AsyncTask<Void, String, String> {

    private RequestBody requestBody;
    private PostAdsListener listener;

    public PostAdsAsync(PostAdsListener listener, RequestBody requestBody){
        this.listener = listener;
        this.requestBody = requestBody;
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

            if(json.equals("1")){
                return Constant.SUCCESS;
            }else{
                return Constant.FAIL;
            }
        } catch (Exception e){
            e.printStackTrace();
            return Constant.FAIL;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onEnd(s);
        super.onPostExecute(s);
    }
}
