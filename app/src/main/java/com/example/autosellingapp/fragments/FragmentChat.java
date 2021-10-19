package com.example.autosellingapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.autosellingapp.Notification.APIService;
import com.example.autosellingapp.Notification.Client;
import com.example.autosellingapp.Notification.Data;
import com.example.autosellingapp.Notification.MyResponse;
import com.example.autosellingapp.Notification.Sender;
import com.example.autosellingapp.Notification.Token;
import com.example.autosellingapp.R;
import com.example.autosellingapp.adapters.MessageAdapter;
import com.example.autosellingapp.databinding.FragmentChatBinding;
import com.example.autosellingapp.items.ChatItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class FragmentChat extends Fragment {

    private FragmentChatBinding binding;
    private Methods methods;
    private UserItem RECEIVER_USER;
    private UserItem MY_USER;
    private MessageAdapter messageAdapter;
    private ArrayList<ChatItem> arrayList_chat;
    private ValueEventListener seenListener;
    private DatabaseReference reference;
    private static final int GALLERY_PICK = 1;

    private String token;
    private String useridfortoken;
    private APIService apiService;
    private boolean notify = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        methods = new Methods(getContext());

        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        Bundle bundle = getArguments();

        arrayList_chat = new ArrayList<>();

        if(bundle != null){
            RECEIVER_USER = (UserItem) bundle.getSerializable(Constant.TAG_USER);
            MY_USER = (UserItem) bundle.getSerializable("MY_USER");

            if(!RECEIVER_USER.getImage().isEmpty()){
                Picasso.get().load(RECEIVER_USER.getImage())
                        .placeholder(R.drawable.user_ic)
                        .into(binding.ivUser);
            }


            binding.tvName.setText(RECEIVER_USER.getFullName());
        }

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        binding.rvChat.setHasFixedSize(true);
        binding.rvChat.setLayoutManager(llm);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(RECEIVER_USER.getUid());

        binding.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(methods.isNetworkAvailable()){
                    if(methods.isLogged()){
                        notify = true;
                        String msg = binding.edtType.getText().toString();
                        if(!msg.equals("")){
                            sendMessage(Constant.UID, RECEIVER_USER.getUid(), msg);
                        }
                        binding.edtType.setText("");
                    }
                }else {
                    Toast.makeText(getContext(), getString(R.string.internet_not_connect), Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.ivSendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(methods.isNetworkAvailable()){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALLERY_PICK);
                }else {
                    Toast.makeText(getContext(), getString(R.string.internet_not_connect), Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getFragmentManager().getFragments().get(getFragmentManager().getBackStackEntryCount() - 1).getTag());
            }
        });

        readMessage(Constant.UID, RECEIVER_USER.getUid());

        seenMessage();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        reference.removeEventListener(seenListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            Random rnd = new Random();
            int rand = 100000 + rnd.nextInt(900000);

            String file_name = methods.getFileName(imageUri);
            String image_name = "IMG_" + Constant.UID +"_"+ RECEIVER_USER.getUid() + "_" + rand + "_" +file_name;
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("MessageImage").child(image_name);

            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String downloadUrl = uri.toString();

                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            Date date = new Date(System.currentTimeMillis());
                            String currentTime = formatter.format(date);

                            reference = FirebaseDatabase.getInstance().getReference().child("Chats");
                            HashMap<String, Object> hashMap = new HashMap<>();

                            hashMap.put("sender_uid", Constant.UID);
                            hashMap.put("receiver_uid", RECEIVER_USER.getUid());
                            hashMap.put("type", "image");
                            hashMap.put("message", downloadUrl);
                            hashMap.put("time", currentTime);
                            hashMap.put("isseen", "false");

                            reference.push().setValue(hashMap);
                        }
                    });
                }
            });
        }
    }


    private void seenMessage(){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatItem chat = dataSnapshot.getValue(ChatItem.class);
                    if(chat.getReceiver_uid().equals(Constant.UID) && chat.getSender_uid().equals(RECEIVER_USER.getUid())){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", "true");
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender_uid, String receiver_uid, String message){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Chats");
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

        if(notify){
            sendNotification(RECEIVER_USER.getUid(), MY_USER.getFullName(), message);
        }
        notify = false;
    }

    private void sendNotification(String receiver_uid, String sender_name, String message) {
        useridfortoken = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Tokens").document(receiver_uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if(value != null){
                    Token objecttoken = value.toObject(Token.class);
                    token = objecttoken.getToken();

                    Data data = new Data(useridfortoken, R.mipmap.ic_launcher, message, "New Message From "+ sender_name, receiver_uid);

                    Sender sender = new Sender(data, token);

                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code() == 200){
                                if(response.body().success != 1){
                                    Toast.makeText(getContext(), "Failed to Send Notification", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }

            }
        });

    }


    private void readMessage(String sender_uid, String receiver_uid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                arrayList_chat.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ChatItem chat = dataSnapshot.getValue(ChatItem.class);
                    if(chat.getReceiver_uid().equals(sender_uid) && chat.getSender_uid().equals(receiver_uid) ||
                            chat.getReceiver_uid().equals(receiver_uid) && chat.getSender_uid().equals(sender_uid)){
                        arrayList_chat.add(chat);
                    }
                }
                messageAdapter = new MessageAdapter(arrayList_chat, RECEIVER_USER.getImage());
                binding.rvChat.setAdapter(messageAdapter);
                binding.rvChat.smoothScrollToPosition(binding.rvChat.getAdapter().getItemCount());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }
}