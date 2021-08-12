package com.example.autosellingapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.autosellingapp.R;
import com.example.autosellingapp.activity.ActivityLogin;
import com.example.autosellingapp.adapters.AdsAdapter;
import com.example.autosellingapp.adapters.MessageAdapter;
import com.example.autosellingapp.adapters.UserFollowAdapter;
import com.example.autosellingapp.asynctasks.LoadProfile;
import com.example.autosellingapp.asynctasks.UpdateFavouriteAsync;
import com.example.autosellingapp.databinding.FragmentProfileBinding;
import com.example.autosellingapp.interfaces.AdsDetailListener;
import com.example.autosellingapp.interfaces.LoadCategoryListener;
import com.example.autosellingapp.interfaces.ReloadFragmentListener;
import com.example.autosellingapp.interfaces.UpdateFavListener;
import com.example.autosellingapp.interfaces.UserListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.ChatItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.RateItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentProfile extends Fragment {

    private FragmentProfileBinding binding;
    private Methods methods;
    private String errorMsg;
    private ArrayList<AdsItem> arrayList_ads;
    private ArrayList<CarItem> arrayList_car;
    private ArrayList<MyItem> arrayList_city;
    private ArrayList<UserItem> arrayList_user;
    private AdsAdapter adsAdapter;
    private ArrayList<RateItem> arrayList_rating;
    private FragmentTransaction ft;
    private int PROFILE_MODE;
    private String USER_ID;
    private boolean IS_FOLLOWING = false;
    private int FOLLOWERS_COUNT = 0;
    private int FOLLOWING_COUNT = 0;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        methods = new Methods(getContext());

        Bundle bundle = getArguments();
        if(bundle != null){
            PROFILE_MODE = bundle.getInt(Constant.PROFILE_MODE);
        }

        arrayList_ads = new ArrayList<>();
        arrayList_car = new ArrayList<>();
        arrayList_city = new ArrayList<>();
        arrayList_user = new ArrayList<>();
        arrayList_rating = new ArrayList<>();

        switch (PROFILE_MODE){
            case Constant.MY_PROFILE:
                if(methods.isLogged()){
                    LoadProfile();
                }else{
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
                break;
            case Constant.USER_PROFILE:
                assert bundle != null;
                USER_ID = bundle.getString(Constant.TAG_UID);
                LoadProfile();
                break;
        }


        return binding.getRoot();
    }

    private void BindingData(){
        switch (PROFILE_MODE){
            case Constant.MY_PROFILE:
                UserItem my_user = methods.getUserItemByUsername(arrayList_user, Constant.UID);
                binding.tvName.setText(my_user.getFullName());
                binding.tvEmail.setText("Email: " +my_user.getEmail());
                binding.tvPhone.setText("Phone: " +my_user.getPhoneNumber());

                FOLLOWING_COUNT = my_user.getFollowlist().size();

                binding.tvFollowingCount.setText("Following (" + FOLLOWING_COUNT +")");

                FOLLOWERS_COUNT = 0;
                for(UserItem user : arrayList_user){
                    if(!user.getUid().equals(my_user.getUid())){
                        for(String uid : user.getFollowlist()){
                            if(uid.equals(my_user.getUid())){
                                FOLLOWERS_COUNT++;
                            }
                        }
                    }
                }

                binding.tvFollowerCount.setText("Followers (" + FOLLOWERS_COUNT +")");

                Picasso.get()
                        .load(Constant.SERVER_URL + "images/user_image/" +my_user.getImage())
                        .placeholder(R.drawable.user_ic)
                        .into(binding.ivUser);

                binding.cvFollow.setVisibility(View.GONE);
                binding.btnEdit.setVisibility(View.VISIBLE);

                getRating(Constant.UID);

                binding.btnFollower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<UserItem> arrayList = new ArrayList<>();
                        for(UserItem user : arrayList_user){
                            if(!user.getUid().equals(my_user.getUid())){
                                for(String uid : user.getFollowlist()){
                                    if(uid.equals(my_user.getUid())){
                                        arrayList.add(user);
                                    }
                                }
                            }
                        }
                        openFollowingDialog(arrayList, getString(R.string.follower));
                    }
                });

                binding.btnFollowing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<UserItem> arrayList = new ArrayList<>();
                        for (String uid : my_user.getFollowlist()){
                            arrayList.add(methods.getUserItemByUsername(arrayList_user, uid));
                        }
                        openFollowingDialog(arrayList, getString(R.string.following));
                    }
                });

                break;

            case Constant.USER_PROFILE:
                UserItem user = methods.getUserItemByUsername(arrayList_user, USER_ID);

                if(methods.isLogged()){
                    UserItem log_user = methods.getUserItemByUsername(arrayList_user, Constant.UID);

                    IS_FOLLOWING = methods.isFollowing(log_user.getFollowlist(), USER_ID);

                    if(IS_FOLLOWING){
                        binding.cvFollow.setCardBackgroundColor(getResources().getColor(R.color.steel_blue));
                        binding.tvCvFollow.setText("Following");
                        binding.tvCvFollow.setTextColor(getResources().getColor(R.color.white));
                    }
                }

                binding.tvName.setText(user.getFullName());
                binding.tvEmail.setText("Email: " +user.getEmail());
                binding.tvPhone.setText("Phone: " +user.getPhoneNumber());

                FOLLOWING_COUNT = user.getFollowlist().size();

                binding.tvFollowingCount.setText("Following (" + FOLLOWING_COUNT +")");

                FOLLOWERS_COUNT = 0;
                for(UserItem u : arrayList_user){
                    if(!u.getUid().equals(user.getUid())){
                        for(String uid : u.getFollowlist()){
                            if(uid.equals(user.getUid())){
                                FOLLOWERS_COUNT++;
                            }
                        }
                    }
                }

                binding.tvFollowerCount.setText("Followers (" + FOLLOWERS_COUNT +")");

                Picasso.get()
                        .load(Constant.SERVER_URL + "images/user_image/" +user.getImage())
                        .placeholder(R.drawable.user_ic)
                        .into(binding.ivUser);

                binding.cvFollow.setVisibility(View.VISIBLE);
                binding.btnEdit.setVisibility(View.GONE);
                binding.cvFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                binding.btnFollower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<UserItem> arrayList = new ArrayList<>();
                        for(UserItem userItem : arrayList_user){
                            if(!userItem.getUid().equals(user.getUid())){
                                for(String uid : userItem.getFollowlist()){
                                    if(uid.equals(user.getUid())){
                                        arrayList.add(userItem);
                                    }
                                }
                            }
                        }
                        openFollowingDialog(arrayList, getString(R.string.follower));
                    }
                });

                binding.btnFollowing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<UserItem> arrayList = new ArrayList<>();
                        for (String uid : user.getFollowlist()){
                            arrayList.add(methods.getUserItemByUsername(arrayList_user, uid));
                        }
                        openFollowingDialog(arrayList, getString(R.string.following));
                    }
                });

                getRating(USER_ID);
                break;
        }

        binding.cvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(methods.isLogged()){
                    if(PROFILE_MODE == Constant.USER_PROFILE){
                        UpdateFollow();
                    }
                }else{
                    openLoginActivity();
                    Toast.makeText(getContext(), Constant.NO_LOGIN, Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PROFILE_MODE == Constant.MY_PROFILE){
                    FragmentEditUser f = new FragmentEditUser(new ReloadFragmentListener() {
                        @Override
                        public void reload() {
                            LoadProfile();
                        }
                    });
                    Bundle bundle = new Bundle();
                    UserItem userItem = methods.getUserItemByUsername(arrayList_user, Constant.UID);
                    bundle.putSerializable(Constant.TAG_USER, userItem);
                    f.setArguments(bundle);
                    ReplaceFragment(f, getString(R.string.frag_edituser));
                }
            }
        });

        binding.llRatingbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(methods.isLogged()){
                    if(PROFILE_MODE == Constant.USER_PROFILE){
                        openDialogRatingbar();
                    }
                }else {
                    openLoginActivity();
                    Toast.makeText(getContext(), Constant.NO_LOGIN, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UpdateFollow() {

        UserItem USER_LOGIN = methods.getUserItemByUsername(arrayList_user, Constant.UID);

        ArrayList<String> arrayList_followlist = new ArrayList<>(USER_LOGIN.getFollowlist());

        if(IS_FOLLOWING){
            methods.unfollow(arrayList_followlist, USER_ID);
        }else{
            methods.follow(arrayList_followlist, USER_ID);
        }

        String json = new Gson().toJson(arrayList_followlist);

        Bundle bundle = new Bundle();
        bundle.putString(Constant.TAG_FOLLOWLIST, json);

        UpdateFavouriteAsync updateFavouriteAsync = new UpdateFavouriteAsync(new UpdateFavListener() {
            @Override
            public void onEnd(String success) {
                if(success.equals("1")){
                    if(IS_FOLLOWING){
                        //un follow
                        IS_FOLLOWING = false;

                        binding.cvFollow.setCardBackgroundColor(getResources().getColor(R.color.white));
                        binding.tvCvFollow.setText("Follow");
                        binding.tvCvFollow.setTextColor(getResources().getColor(R.color.steel_blue));

                        binding.tvFollowerCount.setText("Followers (" + --FOLLOWERS_COUNT +")");

                        methods.unfollow(USER_LOGIN.getFollowlist(), USER_ID);
                    }else{
                        // follow
                        IS_FOLLOWING = true;

                        binding.cvFollow.setCardBackgroundColor(getResources().getColor(R.color.steel_blue));
                        binding.tvCvFollow.setText("Following");
                        binding.tvCvFollow.setTextColor(getResources().getColor(R.color.white));

                        binding.tvFollowerCount.setText("Followers (" + ++FOLLOWERS_COUNT +")");

                        methods.follow(USER_LOGIN.getFollowlist(), USER_ID);
                    }
                }else {
                    Toast.makeText(getContext(), getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                }
            }
        }, methods.getAPIRequest(Constant.METHOD_UPDATEFOLLOW, bundle, null));
        updateFavouriteAsync.execute();

    }

    private void LoadProfile(){

        Bundle bundle = new Bundle();

        if(PROFILE_MODE == Constant.MY_PROFILE){
            bundle.putString(Constant.TAG_UID, Constant.UID);
        }else {
            bundle.putString(Constant.TAG_UID, USER_ID);
        }

        if(methods.isNetworkAvailable()){
            LoadProfile loadProfile = new LoadProfile(new LoadCategoryListener() {
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
                public void onEnd(String success, ArrayList<CarItem> carItemArrayList, ArrayList<AdsItem> adsItemArrayList, ArrayList<MyItem> cityItemArrayList, ArrayList<UserItem> userItemArrayList, ArrayList<ModelItem> modelItemArrayList) {
                    if(getActivity() != null){
                        if(success.equals("1")){
                            arrayList_ads.clear();
                            arrayList_car.clear();
                            arrayList_city.clear();
                            arrayList_user.clear();

                            arrayList_car.addAll(carItemArrayList);
                            arrayList_ads.addAll(adsItemArrayList);
                            arrayList_city.addAll(cityItemArrayList);
                            arrayList_user.addAll(userItemArrayList);

                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        setEmpty();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_PROFILE, bundle, null));
            loadProfile.execute();
        }else {
            errorMsg = getString(R.string.internet_not_connect);
            setEmpty();
        }
    }

    private void setEmpty(){
        if(!errorMsg.equals("")){
            binding.tvEmpty.setText(errorMsg);
            binding.btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadProfile();
                }
            });
            binding.llEmpty.setVisibility(View.VISIBLE);
        }else{

            if(arrayList_ads.size() != 0){
                binding.rlScrollView.setVisibility(View.VISIBLE);

                binding.rvAds.setLayoutManager(new GridLayoutManager(getContext(),2));
                binding.rvAds.setHasFixedSize(true);

                adsAdapter = new AdsAdapter(methods, arrayList_ads, arrayList_car, arrayList_user, arrayList_city, new AdsDetailListener() {
                    @Override
                    public void onClick(AdsItem adsItem, CarItem carItem) {
                        FragmentAdsDetail fragment = new FragmentAdsDetail(new ReloadFragmentListener() {
                            @Override
                            public void reload() {
                                LoadProfile();
                            }
                        });
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(getString(R.string.adsItem), adsItem);
                        bundle.putSerializable(getString(R.string.carItem), carItem);
                        fragment.setArguments(bundle);
                        ReplaceFragment(fragment, getString(R.string.detailFragment));
                    }
                    @Override
                    public void onUserClick(String uid) {

                    }
                });
                binding.rvAds.setAdapter(adsAdapter);
            }else {
                binding.rlScrollView.setVisibility(View.VISIBLE);
                binding.rvAds.setVisibility(View.GONE);
                binding.tvAdsEmpty.setVisibility(View.VISIBLE);
            }
            BindingData();
        }
    }

    private void openDialogRatingbar(){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_dialog_ratingbar);

        Button btn_rate = dialog.findViewById(R.id.btn_rate);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = (int) ratingBar.getRating();

                if(score == 0){
                    Toast.makeText(getContext(), "Please rate as least one star!", Toast.LENGTH_SHORT).show();
                }else {
                    updateRating(USER_ID, Constant.UID, score);
                    dialog.dismiss();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openFollowingDialog(ArrayList<UserItem> arrayList, String text){
        Dialog dialogFollow = new Dialog(getContext());
        dialogFollow.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogFollow.setContentView(R.layout.layout_dialog_recycleview);

        Button btn_ok = dialogFollow.findViewById(R.id.btn_ok);
        RecyclerView rv_dialog = dialogFollow.findViewById(R.id.rv_dialog);
        TextView tv_dialog = dialogFollow.findViewById(R.id.tv_dialog);
        tv_dialog.setText(text);

        UserFollowAdapter userFollowAdapter = new UserFollowAdapter(arrayList, new UserListener() {
            @Override
            public void onClick(UserItem user) {
                String uid = user.getUid();
                if(methods.isLogged()){
                    Bundle bundle = new Bundle();
                    if(Constant.UID.equals(uid)){
                        bundle.putInt(Constant.PROFILE_MODE, Constant.MY_PROFILE);
                        FragmentProfile f = new FragmentProfile();
                        f.setArguments(bundle);

                        ReplaceFragment(f, getString(R.string.frag_profile));
                    }else {
                        bundle.putInt(Constant.PROFILE_MODE, Constant.USER_PROFILE);
                        bundle.putString(Constant.TAG_UID, uid);
                        FragmentProfile f = new FragmentProfile();
                        f.setArguments(bundle);

                        ReplaceFragment(f, getString(R.string.frag_profile));
                    }
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.PROFILE_MODE, Constant.USER_PROFILE);
                    bundle.putString(Constant.TAG_UID, uid);
                    FragmentProfile f = new FragmentProfile();
                    f.setArguments(bundle);

                    ReplaceFragment(f, getString(R.string.frag_profile));
                }
                dialogFollow.dismiss();
            }

            @Override
            public void onLongClick(String uid) {

            }
        });

        rv_dialog.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_dialog.setAdapter(userFollowAdapter);
        rv_dialog.setHasFixedSize(true);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFollow.dismiss();
            }
        });

        dialogFollow.show();
    }

    private void getRating(String uid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ratings").child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                arrayList_rating.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RateItem rateItem = dataSnapshot.getValue(RateItem.class);
                    arrayList_rating.add(rateItem);
                }

                int rating_count = arrayList_rating.size();
                float rating_score = 0;


                for(RateItem rate : arrayList_rating){
                    rating_score += rate.getScore();
                }

                float score = rating_score/rating_count;

                binding.tvRatingScore.setText((rating_count == 0)?"0":String.format("%1$,.1f", score));
                binding.tvRatingCount.setText("("+rating_count+")");
                binding.ratingBar.setRating(score);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    public void ReplaceFragment(Fragment fragment, String name){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, fragment, name);
        ft.addToBackStack(name);
        ft.commit();
    }

    private void openLoginActivity(){
        Intent intent = new Intent(getContext(), ActivityLogin.class);
        startActivity(intent);
    }

    private void updateRating(String user_id, String my_id, int score){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Ratings").child(user_id).child(my_id);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("from", my_id);
        hashMap.put("to", user_id);
        hashMap.put("score", score);

        reference.updateChildren(hashMap);
    }
}