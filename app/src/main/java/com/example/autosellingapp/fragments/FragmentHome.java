package com.example.autosellingapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.activity.MainActivity;
import com.example.autosellingapp.adapters.AdsAdapter;
import com.example.autosellingapp.adapters.ManufacturerAdapter;
import com.example.autosellingapp.asynctasks.LoadHome;
import com.example.autosellingapp.asynctasks.LoadRecent;
import com.example.autosellingapp.databinding.FragmentContactBinding;
import com.example.autosellingapp.interfaces.AdsDetailListener;
import com.example.autosellingapp.interfaces.InterAdListener;
import com.example.autosellingapp.interfaces.LoadCategoryListener;
import com.example.autosellingapp.interfaces.LoadManuListener;
import com.example.autosellingapp.interfaces.ManuListener;
import com.example.autosellingapp.interfaces.ReloadFragmentListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.example.autosellingapp.utils.SharedPref;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FragmentHome extends Fragment {

    private View rootView;
    private ManufacturerAdapter manufacturerAdapter;
    private ArrayList<ManufacturerItem> arrayList_manu;
    private ArrayList<AdsItem> arrayList_ads;
    private ArrayList<CarItem> arrayList_car;
    private ArrayList<UserItem> arrayList_user;
    private ArrayList<MyItem> arrayList_city;
    private RecyclerView rv_manu, rv_recent;
    private CardView cv_search;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private Methods methods;
    private LinearLayout ll_empty_home, ll_manu, ll_recent, ll_location;
    private RelativeLayout rl_manu, rl_recent;
    private String errorMsg;
    private Button btn_try;
    private TextView tv_empty, tv_mylocation;
    private ImageView iv_search;
    private EditText edt_search;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FragmentTransaction ft;

    private int NOT_SET = -1;

    private SharedPref sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Hook();

        sharedPref = new SharedPref(getContext());

        arrayList_manu = new ArrayList<>();
        arrayList_ads = new ArrayList<>();
        arrayList_car = new ArrayList<>();
        arrayList_user = new ArrayList<>();
        arrayList_city = new ArrayList<>();

        methods = new Methods(getContext());
        rv_manu.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rv_manu.setHasFixedSize(true);

        rl_manu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("TYPE", "type_manu");
                bundle.putSerializable(Constant.TAG_MANU, arrayList_manu);

                FragmentViewMore f = new FragmentViewMore();
                f.setArguments(bundle);

                ReplaceFragment(f, "All Manufacturer");
            }
        });

        LoadManufacturers();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bottom_nav, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void Hook(){
        rv_manu = rootView.findViewById(R.id.rv_manu);
        rv_recent = rootView.findViewById(R.id.rv_recent);
        cv_search= rootView.findViewById(R.id.cv_search);
        scrollView = rootView.findViewById(R.id.home_scroll_view);
        progressBar = rootView.findViewById(R.id.progressBar_home);
        ll_empty_home = rootView.findViewById(R.id.ll_empty_home);
        ll_manu = rootView.findViewById(R.id.ll_manu);
        ll_recent = rootView.findViewById(R.id.ll_recent);
        rl_manu = rootView.findViewById(R.id.rl_manu);
        rl_recent = rootView.findViewById(R.id.rl_recent);
        btn_try = rootView.findViewById(R.id.btn_try_home);
        tv_empty = rootView.findViewById(R.id.tv_empty_home);
        iv_search = rootView.findViewById(R.id.iv_search);
        edt_search = rootView.findViewById(R.id.edt_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_search.getText().toString().isEmpty()){

                    new Methods(getContext(), new InterAdListener() {
                        @Override
                        public void onClick(int position) {
                            SearchCategory();
                        }
                    }).showInter(0);

                }
            }
        });
    }

    private void SearchCategory(){
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.manufacturers), NOT_SET);
        bundle.putInt(getString(R.string.model), NOT_SET);
        bundle.putString(getString(R.string.search_text), edt_search.getText().toString());
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

    private void LoadManufacturers(){
        if(methods.isNetworkAvailable()){
            LoadHome loadHome = new LoadHome(new LoadManuListener() {
                @Override
                public void onStart() {
                    if(getActivity()!=null){
                        errorMsg = "";
                        scrollView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        ll_empty_home.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onEnd(String success, ArrayList<ManufacturerItem> manuList) {
                    if(getActivity() != null){
                        if(success.equals("1")){
                            arrayList_manu.clear();
                            arrayList_manu.addAll(manuList);

                            ArrayList<ManufacturerItem> arrayList_manu1 = new ArrayList<>();

                            if(arrayList_ads.size() > 12){
                                for(int i = 0; i < 12; i++){
                                    arrayList_manu1.add(arrayList_manu.get(i));
                                }
                            }else {
                                arrayList_manu1.addAll(arrayList_manu);
                            }

                            manufacturerAdapter = new ManufacturerAdapter(getContext(),"home" ,arrayList_manu1, new ManuListener() {
                                @Override
                                public void onClick(int manu_id) {
                                    openCatFragment(manu_id);
                                }
                            });
                            rv_manu.setAdapter(manufacturerAdapter);
                            setRecent();
                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        progressBar.setVisibility(View.GONE);
                        setEmpty();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_HOME, null, null, null));
            loadHome.execute();
        }else{
            errorMsg = getString(R.string.internet_not_connect);
            setEmpty();
        }
    }

    private void setRecent() {

        Bundle bundle = new Bundle();
        String array_ads_json = sharedPref.getRecentAds();
        bundle.putString(Constant.TAG_RECENTADS, array_ads_json);

        LoadRecent loadRecent = new LoadRecent(new LoadCategoryListener() {
            @Override
            public void onStart() {
                arrayList_ads.clear();
                arrayList_car.clear();
                arrayList_user.clear();
                arrayList_city.clear();
                ll_recent.setVisibility(View.GONE);
            }

            @Override
            public void onEnd(String success, ArrayList<CarItem> carItemArrayList, ArrayList<AdsItem> adsItemArrayList, ArrayList<MyItem> cityItemArrayList, ArrayList<UserItem> userItemArrayList, ArrayList<ModelItem> modelItemArrayList) {
                if(getActivity() != null){
                    if(success.equals("1")){
                        if(!adsItemArrayList.isEmpty()){
                            LinearLayoutManager llm = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                            rv_recent.setLayoutManager(llm);
                            rv_recent.setHasFixedSize(true);

                            if (methods.isLogged()){
                                for(AdsItem adsItem : adsItemArrayList){
                                    if(!adsItem.getUid().equals(Constant.UID)){
                                        arrayList_ads.add(adsItem);
                                    }
                                }
                            }else {
                                arrayList_ads.addAll(adsItemArrayList);
                            }

                            if(arrayList_ads.size() < 3){
                                return;
                            }

                            arrayList_car.addAll(carItemArrayList);
                            arrayList_user.addAll(userItemArrayList);
                            arrayList_city.addAll(cityItemArrayList);

                            ArrayList<AdsItem> arrayList_ads1 = new ArrayList<>();

                            Collections.reverse(arrayList_ads);

                            if(arrayList_ads.size() > 15){
                                for(int i = 0; i < 15; i++){
                                    arrayList_ads1.add(arrayList_ads.get(i));
                                }
                            }else {
                                arrayList_ads1.addAll(arrayList_ads);
                            }

                            AdsAdapter adsAdapter = new AdsAdapter("horizontal", getContext(), arrayList_ads1, arrayList_car, arrayList_user, arrayList_city, new AdsDetailListener() {
                                @Override
                                public void onClick(AdsItem adsItem, CarItem carItem) {
                                    FragmentAdsDetail fragment = new FragmentAdsDetail(new ReloadFragmentListener() {
                                        @Override
                                        public void reload() {
                                            setRecent();
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
                            rv_recent.setAdapter(adsAdapter);

                            ll_recent.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Toast.makeText(getContext(), "Can't load recent ads, Something wrong happened!", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                    setEmpty();
                }
            }
        }, methods.getAPIRequest(Constant.METHOD_RECENT, bundle, null, null));

        if(!array_ads_json.equals("")){
            loadRecent.execute();

            rl_recent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("TYPE", "type_ads");
                    bundle.putSerializable(Constant.TAG_ADS, arrayList_ads);
                    bundle.putSerializable(Constant.TAG_CAR, arrayList_car);
                    bundle.putSerializable(Constant.TAG_USER, arrayList_user);
                    bundle.putSerializable(Constant.TAG_CITY, arrayList_city);

                    FragmentViewMore f = new FragmentViewMore();
                    f.setArguments(bundle);

                    ReplaceFragment(f, "Recent Ads");
                }
            });
        }
    }

    private void setEmpty() {
        if(!errorMsg.equals("")){
            tv_empty.setText(errorMsg);
            btn_try.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadManufacturers();
                }
            });
            ll_empty_home.setVisibility(View.VISIBLE);
        }else{
            scrollView.setVisibility(View.VISIBLE);
            if(arrayList_manu.size() < 3){
                ll_manu.setVisibility(View.GONE);
            }
        }
    }

    private void openCatFragment(int SELECTED_MANU_ID){
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.manufacturers), SELECTED_MANU_ID);
        bundle.putInt(getString(R.string.model), NOT_SET);
        bundle.putString(getString(R.string.search_text), "");
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

    public void ReplaceFragment(Fragment fragment, String name){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, fragment, name);
        ft.addToBackStack(name);
        ft.commit();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(name);
    }
}