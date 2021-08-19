package com.example.autosellingapp.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autosellingapp.R;
import com.example.autosellingapp.adapters.AdsAdapter;
import com.example.autosellingapp.adapters.ManufacturerAdapter;
import com.example.autosellingapp.databinding.FragmentViewMoreBinding;
import com.example.autosellingapp.interfaces.AdsDetailListener;
import com.example.autosellingapp.interfaces.ManuListener;
import com.example.autosellingapp.interfaces.ReloadFragmentListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;

import java.util.ArrayList;

public class FragmentViewMore extends Fragment {

    private FragmentViewMoreBinding binding;
    private static final int MANUFACTURER = 2;
    private static final int ADS = 3;
    private FragmentTransaction ft;
    private ArrayList<ManufacturerItem> arrayList_manu;
    private ArrayList<AdsItem> arrayList_ads;
    private ArrayList<CarItem> arrayList_car;
    private ArrayList<UserItem> arrayList_user;
    private ArrayList<MyItem> arrayList_city;
    private Methods methods;
    private int ITEM_TYPE;
    private int NOT_SET = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewMoreBinding.inflate(inflater, container, false);

        methods = new Methods(getContext());

        Bundle bundle = getArguments();

        if(bundle != null){
            if(bundle.getString("TYPE").equals("type_ads")){
                ITEM_TYPE = ADS;
                arrayList_ads = (ArrayList<AdsItem>) bundle.getSerializable(Constant.TAG_ADS);
                arrayList_car = (ArrayList<CarItem>) bundle.getSerializable(Constant.TAG_CAR);
                arrayList_user = (ArrayList<UserItem>) bundle.getSerializable(Constant.TAG_USER);
                arrayList_city = (ArrayList<MyItem>) bundle.getSerializable(Constant.TAG_CITY);
            }else {
                ITEM_TYPE = MANUFACTURER;
                arrayList_manu = (ArrayList<ManufacturerItem>) bundle.getSerializable(Constant.TAG_MANU);
            }
        }

        switch (ITEM_TYPE){
            case ADS:
                binding.rlSearchManu.setVisibility(View.GONE);

                binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                binding.recyclerView.setHasFixedSize(true);

                AdsAdapter adsAdapter = new AdsAdapter("grid", methods, arrayList_ads, arrayList_car, arrayList_user, arrayList_city, new AdsDetailListener() {
                    @Override
                    public void onClick(AdsItem adsItem, CarItem carItem) {
                        FragmentAdsDetail fragment = new FragmentAdsDetail(new ReloadFragmentListener() {
                            @Override
                            public void reload() {

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

                binding.recyclerView.setAdapter(adsAdapter);

                break;
            case MANUFACTURER:
                binding.rlSearchManu.setVisibility(View.VISIBLE);

                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.recyclerView.setHasFixedSize(true);

                sortManu(arrayList_manu);

                ManufacturerAdapter manufacturerAdapter = new ManufacturerAdapter("view_more", arrayList_manu, new ManuListener() {
                    @Override
                    public void onClick(int manu_id) {
                        openCatFragment(manu_id);
                    }
                });

                binding.recyclerView.setAdapter(manufacturerAdapter);

                ArrayList<ManufacturerItem> arrayList_search = new ArrayList<>();
                binding.edtSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        arrayList_search.clear();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        for(ManufacturerItem item : arrayList_manu){
                            if(item.getManu_name().toLowerCase().contains(s.toString().toLowerCase())){
                                arrayList_search.add(item);
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        manufacturerAdapter.setAdapterData(arrayList_search);
                        manufacturerAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
        return binding.getRoot();
    }

    private void openCatFragment(int SELECTED_MANU_ID){
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.manufacturers), SELECTED_MANU_ID);
        bundle.putInt(getString(R.string.model), NOT_SET);
        bundle.putInt(getString(R.string.price_min), NOT_SET);
        bundle.putInt(getString(R.string.price_max), NOT_SET);
        bundle.putInt(getString(R.string.power_min), NOT_SET);
        bundle.putInt(getString(R.string.power_max), NOT_SET);
        bundle.putInt(getString(R.string.mileage_min), NOT_SET);
        bundle.putInt(getString(R.string.mileage_max), NOT_SET);
        bundle.putInt(getString(R.string.body_type), NOT_SET);
        bundle.putInt(getString(R.string.fuel_type), NOT_SET);
        bundle.putInt(getString(R.string.year), NOT_SET);
        bundle.putInt(getString(R.string.transmission), NOT_SET);
        bundle.putInt(getString(R.string.condition), NOT_SET);
        bundle.putInt(getString(R.string.body_color), NOT_SET);
        bundle.putInt(getString(R.string.city), NOT_SET);
        bundle.putInt(getString(R.string.seat_number), NOT_SET);
        bundle.putInt(getString(R.string.door_number), NOT_SET);
        bundle.putInt(getString(R.string.previous_users), NOT_SET);
        bundle.putSerializable(getString(R.string.equipment), new ArrayList<EquipmentItem>());
        FragmentCategory fragment = new FragmentCategory();
        fragment.setArguments(bundle);
        ReplaceFragment(fragment, getString(R.string.frag_category));
    }

    private void sortManu(ArrayList<ManufacturerItem> arr){
        for (int i = 0; i < arr.size(); i++) {
            int pos = i;
            for (int j = i; j < arr.size(); j++) {
                if (arr.get(j).getManu_name().compareTo(arr.get(pos).getManu_name()) < 0)
                    pos = j;
            }
            ManufacturerItem min = arr.get(pos);
            arr.set(pos, arr.get(i));
            arr.set(i, min);
        }
    }

    public void ReplaceFragment(Fragment fragment, String name){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, fragment, name);
        ft.addToBackStack(name);
        ft.commit();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(name);
    }
}