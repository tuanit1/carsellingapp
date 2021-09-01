package com.example.autosellingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autosellingapp.R;
import com.example.autosellingapp.activity.ActivityLogin;
import com.example.autosellingapp.adapters.UserAdapter;
import com.example.autosellingapp.asynctasks.LoadUser;
import com.example.autosellingapp.databinding.FragmentMessageBinding;
import com.example.autosellingapp.interfaces.LoadUserListener;
import com.example.autosellingapp.interfaces.UserListener;
import com.example.autosellingapp.items.UserFirebase;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentMessage extends Fragment {

    private FragmentMessageBinding binding;
    private FirebaseAuth auth;
    private ArrayList<UserItem> arrayList_user;
    private UserAdapter userAdapter;
    private Methods methods;
    private FragmentTransaction ft;
    private String errorMsg;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        methods = new Methods(getContext());

        arrayList_user = new ArrayList<>();

        binding.rvChatList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        if(methods.isLogged()){
            LoadUser();
        }else {
            binding.llEmpty.setVisibility(View.VISIBLE);
            binding.tvEmpty.setText(Constant.NO_LOGIN);
            binding.btnTry.setText("LOGIN");
            binding.btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLoginActivity();
                }
            });
        }



        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            token = task.getResult();
                            SaveToken(token);
                        }
                    }
                });
    }

    private void SaveToken(String token){

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("token", token);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Tokens").document(userid).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

            }
        });
    }


    private void LoadUser(){
        if(methods.isNetworkAvailable()){
            LoadUser loadUser = new LoadUser(new LoadUserListener() {
                @Override
                public void onStart() {
                    if(getActivity() != null) {
                        errorMsg = "";
                        binding.rlScrollView.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.llEmpty.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onEnd(String success, ArrayList<UserItem> arrayList_userz) {
                    if(getActivity() != null){
                        if(success.equals(Constant.SUCCESS)){
                            arrayList_user.clear();
                            arrayList_user.addAll(arrayList_userz);
                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        setEmpty();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_USER, null, null, null));
            loadUser.execute();
        }else{
            errorMsg = getString(R.string.internet_not_connect);
            setEmpty();
        }
    }

    private void setEmpty() {
        if(!errorMsg.equals("")){
            binding.tvEmpty.setText(errorMsg);
            binding.btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadUser();
                }
            });
            binding.llEmpty.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }else{
            ArrayList<UserItem> arrayList = new ArrayList<>();

            UserItem USER = methods.getUserItemByUsername(arrayList_user, Constant.UID);

            for(String uid : USER.getChatlist()){
                arrayList.add(methods.getUserItemByUsername(arrayList_user, uid));
            }

            if(arrayList.isEmpty()){
                binding.tvEmpty.setText(getString(R.string.chatlist_empty));
                binding.btnTry.setText("REFRESH");
                binding.btnTry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadUser();
                    }
                });
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }else{

                ArrayList<UserFirebase> arrayList_ufb = new ArrayList<>();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        arrayList_ufb.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            UserFirebase userFirebase = dataSnapshot.getValue(UserFirebase.class);
                            arrayList_ufb.add(userFirebase);
                        }
                        for(UserItem user : arrayList_user){
                            for(UserFirebase ufb : arrayList_ufb){
                                if(user.getUid().equals(ufb.getUid())){
                                    user.setStatus(ufb.getStatus());
                                    break;
                                }
                            }
                        }
                        userAdapter = new UserAdapter(arrayList, new UserListener() {
                            @Override
                            public void onClick(UserItem user) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Constant.TAG_USER, user);
                                bundle.putSerializable("MY_USER", methods.getUserItemByUsername(arrayList_user, Constant.UID));
                                FragmentChat f = new FragmentChat();
                                f.setArguments(bundle);
                                ReplaceFragment(f, getString(R.string. frag_chat));
                            }

                            @Override
                            public void onLongClick(String uid) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(Constant.PROFILE_MODE, Constant.USER_PROFILE);
                                bundle.putString(Constant.TAG_UID, uid);
                                FragmentProfile f = new FragmentProfile();
                                f.setArguments(bundle);
                                ReplaceFragment(f, getString(R.string.frag_profile));
                            }
                        });
                        binding.rvChatList.setAdapter(userAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                binding.rlScrollView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void ReplaceFragment(Fragment fragment, String name){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, fragment, name);
        ft.addToBackStack(name);
        ft.commit();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(name);
    }
    private void openLoginActivity(){
        Intent intent = new Intent(getContext(), ActivityLogin.class);
        startActivity(intent);
    }

    private void setUserStatus(ArrayList<UserItem> arrayList_user){

        ArrayList<UserFirebase> arrayList_ufb = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                arrayList_ufb.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserFirebase userFirebase = dataSnapshot.getValue(UserFirebase.class);
                    arrayList_ufb.add(userFirebase);
                }
                for(UserItem user : arrayList_user){
                    for(UserFirebase ufb : arrayList_ufb){
                        if(user.getUid().equals(ufb.getUid())){
                            user.setStatus(ufb.getStatus());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}