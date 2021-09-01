package com.example.autosellingapp.Notification;

import com.example.autosellingapp.Notification.MyResponse;
import com.example.autosellingapp.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAKWByDm8:APA91bHrMgVZTYGf_4N8BDGnVsZ8TYW9Kils_gwsfgOf4MLJwwbo4ZJgFuL0eG3f5Ym5Gam-_CfA2KOMaPCH3nd3gKJW7hr4uwaLxTyKSJaNmsZCIRH-OkCmgMb4Lq_CKz-jb-Jgbnah"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
