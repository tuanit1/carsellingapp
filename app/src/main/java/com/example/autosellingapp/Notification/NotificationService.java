package com.example.autosellingapp.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.example.autosellingapp.Notification.OreoNotification.CHANNELID;

public class NotificationService extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager noti = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput!=null) {

            String replyText = remoteInput.getString("key_text_reply");

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String userid = firebaseAuth.getCurrentUser().getUid();


            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


            SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS", MODE_PRIVATE);
            String friendid = sharedPreferences.getString("friendid", "");


//            SimpleDateFormat formatter= new SimpleDateFormat( "HH:mm:ss");
//            Date date = new Date(System.currentTimeMillis());
//            String currenttime = formatter.format(date);
//
//
//
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("sender", userid);
//            hashMap.put("receiver", friendid);
//            hashMap.put("message", replyText);
//            hashMap.put("time", currenttime);
//
//            firebaseFirestore.collection("Messages").document(currenttime).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//
//                }
//            });

            sendMessage(userid, friendid, replyText);


            SharedPreferences shf = context.getSharedPreferences("NEWPREFS", MODE_PRIVATE);

            int shit = shf.getInt("values", 0);

            Notification repliedNotification =
                    new NotificationCompat.Builder(context, CHANNELID)
                            .setSmallIcon(
                                    android.R.drawable.ic_dialog_info)
                            .setContentText("Reply received")
                            .build();

            noti.notify(shit,
                    repliedNotification);

        }
    }

    private void sendMessage(String sender_uid, String receiver_uid, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        HashMap<String, Object> hashMap = new HashMap<>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String currentTime = formatter.format(date);

        hashMap.put("sender_uid", sender_uid);
        hashMap.put("receiver_uid", receiver_uid);
        hashMap.put("type", "message");
        hashMap.put("message", message);
        hashMap.put("isseen", "false");
        hashMap.put("time", currentTime);


        reference.push().setValue(hashMap);
    }
}
