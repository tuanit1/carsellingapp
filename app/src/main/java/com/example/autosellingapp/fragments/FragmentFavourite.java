package com.example.autosellingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autosellingapp.R;
import com.example.autosellingapp.activity.ActivityLogin;
import com.example.autosellingapp.adapters.AdsAdapter;
import com.example.autosellingapp.asynctasks.LoadCategory;
import com.example.autosellingapp.databinding.FragmentCategoryBinding;
import com.example.autosellingapp.databinding.FragmentFavouriteBinding;
import com.example.autosellingapp.databinding.FragmentSellingBinding;
import com.example.autosellingapp.interfaces.AdsDetailListener;
import com.example.autosellingapp.interfaces.LoadCategoryListener;
import com.example.autosellingapp.interfaces.ReloadFragmentListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;

import java.util.ArrayList;

public class FragmentFavourite extends Fragment {

    private FragmentFavouriteBinding binding;
    private Methods methods;
    private String errorMsg;
    private ArrayList<AdsItem> arrayList_ads;
    private ArrayList<CarItem> arrayList_car;
    private ArrayList<MyItem> arrayList_city;
    private ArrayList<UserItem> arrayList_user;
    private ArrayList<ModelItem> arrayList_model;
    private AdsAdapter adsAdapter;
    private FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false);

        methods = new Methods(getContext());

        arrayList_ads = new ArrayList<>();
        arrayList_car = new ArrayList<>();
        arrayList_city = new ArrayList<>();
        arrayList_user = new ArrayList<>();
        arrayList_model = new ArrayList<>();


        if(methods.isLogged()){
            LoadCategory();
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

    private void LoadCategory(){
        if(methods.isNetworkAvailable()){
            LoadCategory loadCategory = new LoadCategory(new LoadCategoryListener() {
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
                            arrayList_model.clear();

                            arrayList_car.addAll(carItemArrayList);
                            arrayList_ads.addAll(adsItemArrayList);
                            arrayList_city.addAll(cityItemArrayList);
                            arrayList_user.addAll(userItemArrayList);
                            arrayList_model.addAll(modelItemArrayList);

                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        setEmpty();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_USER_FAVOURITE, null, null));
            loadCategory.execute();
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
                    LoadCategory();
                }
            });
            binding.llEmpty.setVisibility(View.VISIBLE);
        }else if(arrayList_ads.size() == 0){
            binding.tvEmpty.setText(getString(R.string.favourite_list_empty));
            binding.btnTry.setVisibility(View.GONE);
            binding.llEmpty.setVisibility(View.VISIBLE);
        }else{

            adsAdapter = new AdsAdapter(methods, arrayList_ads, arrayList_car, arrayList_user, arrayList_city, new AdsDetailListener() {
                @Override
                public void onClick(AdsItem adsItem, CarItem carItem) {
                    FragmentAdsDetail fragment = new FragmentAdsDetail(new ReloadFragmentListener() {
                        @Override
                        public void reload() {
                            LoadCategory();
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
                    if(methods.isLogged()){
                        if(Constant.UID.equals(uid)){
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.PROFILE_MODE, Constant.MY_PROFILE);
                            FragmentProfile f = new FragmentProfile();
                            f.setArguments(bundle);
                            ReplaceFragment(f, getString(R.string.frag_profile));
                        }else {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.PROFILE_MODE, Constant.USER_PROFILE);
                            bundle.putString(Constant.TAG_UID, uid);
                            FragmentProfile f = new FragmentProfile();
                            f.setArguments(bundle);
                            ReplaceFragment(f, getString(R.string.frag_profile));
                        }
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constant.PROFILE_MODE, Constant.USER_PROFILE);
                        bundle.putString(Constant.TAG_UID, uid);
                        FragmentProfile f = new FragmentProfile();
                        f.setArguments(bundle);
                        ReplaceFragment(f, getString(R.string.frag_profile));
                    }
                }
            });
            binding.rvFavourite.setAdapter(adsAdapter);
            binding.rvFavourite.setLayoutManager(new GridLayoutManager(getContext(), 2));
            binding.rvFavourite.setHasFixedSize(true);
            binding.rlScrollView.setVisibility(View.VISIBLE);
        }
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
}