package com.example.autosellingapp.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.activity.ActivityLogin;
import com.example.autosellingapp.adapters.EquipmentAdapter;
import com.example.autosellingapp.asynctasks.LoadSearch;
import com.example.autosellingapp.asynctasks.UpdateFavouriteAsync;
import com.example.autosellingapp.interfaces.LoadSearchListener;
import com.example.autosellingapp.interfaces.ReloadFragmentListener;
import com.example.autosellingapp.interfaces.UpdateFavListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.ColorItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.adapters.CarImageAdapter;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class FragmentAdsDetail extends Fragment {

    private View rootView;
    private AdsItem ADS;
    private CarItem CAR;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private String errorMsg;
    private Methods methods;
    private LinearLayout ll_empty;
    private Button btn_try, btn_call, btn_chat;
    private TextView tv_empty;
    private RecyclerView rv_equipments;
    private CarImageAdapter carImageAdapter;
    private ImageButton btn_like;
    private ReloadFragmentListener reloadListener;
    private LinearLayout ll_buyer_function, ll_my_ads, ll_sold;
    private FragmentTransaction ft;

    private ArrayList<ManufacturerItem> array_manu;
    private ArrayList<ModelItem> array_model;
    private ArrayList<MyItem> array_city;
    private ArrayList<MyItem> array_bodytype;
    private ArrayList<MyItem> array_fueltype;
    private ArrayList<MyItem> array_trans;
    private ArrayList<EquipmentItem> array_equip;
    private ArrayList<ColorItem> array_color;
    private ArrayList<UserItem> array_user;

    private int NOT_SET = -1;
    private boolean IS_FAVORITE = false;

    private ViewPager viewPager;
    private TextView tv_price, tv_condition, tv_carName, tv_postTime, tv_sellerName, tv_mileAge, tv_enginePower, tv_trans, tv_bodyType, tv_fuelType,
                    tv_year, tv_manu, tv_model, tv_power, tv_preOwner, tv_door, tv_seat, tv_color, tv_gear, tv_cylinder, tv_weight, tv_consump, tv_emisssion, tv_descrip,
                    tv_email, tv_phone, tv_address;

    public FragmentAdsDetail(ReloadFragmentListener reloadFragmentListener){
        this.reloadListener = reloadFragmentListener;
    }

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
        rootView = inflater.inflate(R.layout.fragment_ads_detail, container, false);

        methods = new Methods(getContext());

        Bundle bundle = getArguments();
        if(bundle != null){
            ADS = (AdsItem) bundle.getSerializable(getString(R.string.adsItem));
            CAR = (CarItem) bundle.getSerializable(getString(R.string.carItem));
        }

        array_manu = new ArrayList<>();
        array_model= new ArrayList<>();
        array_city= new ArrayList<>();
        array_bodytype= new ArrayList<>();
        array_fueltype= new ArrayList<>();
        array_trans = new ArrayList<>();
        array_color = new ArrayList<>();
        array_equip = new ArrayList<>();
        array_user = new ArrayList<>();


        Hook();

        LoadData();
        return rootView;
    }

    private void Hook(){
        scrollView = rootView.findViewById(R.id.scroll_view);
        progressBar = rootView.findViewById(R.id.progressBar);
        ll_empty = rootView.findViewById(R.id.ll_empty);
        btn_try = rootView.findViewById(R.id.btn_try);
        tv_empty = rootView.findViewById(R.id.tv_empty);
        viewPager = rootView.findViewById(R.id.view_pager);
        rv_equipments = rootView.findViewById(R.id.rv_equipments);
        btn_like = rootView.findViewById(R.id.btn_like);
        btn_call = rootView.findViewById(R.id.btn_call);
        btn_chat = rootView.findViewById(R.id.btn_chat);
        tv_price = rootView.findViewById(R.id.tv_price);
        tv_condition = rootView.findViewById(R.id.tv_condition);
        tv_carName = rootView.findViewById(R.id.tv_car_name);
        tv_postTime = rootView.findViewById(R.id.tv_post_time);
        tv_sellerName = rootView.findViewById(R.id.tv_seller_name);
        tv_mileAge = rootView.findViewById(R.id.tv_mileage_value);
        tv_enginePower = rootView.findViewById(R.id.tv_enginepower_value);
        tv_trans = rootView.findViewById(R.id.tv_trans_value);
        tv_address = rootView.findViewById(R.id.tv_address_value);
        tv_bodyType = rootView.findViewById(R.id.tv_bodytype_value);
        tv_fuelType = rootView.findViewById(R.id.tv_fueltype_value);
        tv_year = rootView.findViewById(R.id.tv_year_value);
        tv_manu = rootView.findViewById(R.id.tv_manu_value);
        tv_model = rootView.findViewById(R.id.tv_model_value);
        tv_power = rootView.findViewById(R.id.tv_power_value);
        tv_preOwner = rootView.findViewById(R.id.tv_preowner_value);
        tv_door = rootView.findViewById(R.id.tv_door_value);
        tv_seat = rootView.findViewById(R.id.tv_seat_value);
        tv_color = rootView.findViewById(R.id.tv_color_value);
        tv_gear = rootView.findViewById(R.id.tv_gears_value);
        tv_cylinder = rootView.findViewById(R.id.tv_cylinder_value);
        tv_weight = rootView.findViewById(R.id.tv_weight_value);
        tv_consump = rootView.findViewById(R.id.tv_fuelconsump_value);
        tv_emisssion = rootView.findViewById(R.id.tv_emission_value);
        tv_descrip = rootView.findViewById(R.id.tv_description_value);
        tv_email = rootView.findViewById(R.id.tv_email_value);
        tv_phone = rootView.findViewById(R.id.tv_phone_value);
        ll_buyer_function = rootView.findViewById(R.id.ll_buyer_function);
        ll_my_ads = rootView.findViewById(R.id.ll_my_ads);
        ll_sold = rootView.findViewById(R.id.ll_sold);

    }

    private void BindData(){
        ModelItem modelItem = methods.getModelItemByID(array_model, CAR.getModel_id());
        ManufacturerItem manufacturerItem = methods.getManuItemByID(array_manu, modelItem.getManu_id());
        MyItem bodyType = methods.getMyItemByID(array_bodytype, CAR.getBodyType_id());
        MyItem fuelType = methods.getMyItemByID(array_fueltype, CAR.getFuelType_id());
        MyItem transmission = methods.getMyItemByID(array_trans, CAR.getTrans_id());
        ColorItem colorItem = methods.getColorItemByID(array_color, CAR.getColor_id());
        UserItem userItem = methods.getUserItemByUsername(array_user, ADS.getUid());

        if(methods.isLogged()){
            IS_FAVORITE = methods.isFavourite(array_user, ADS.getAds_id());
            if(!methods.isMyAds(ADS)){
                ll_buyer_function.setVisibility(View.VISIBLE);
                ll_my_ads.setVisibility(View.GONE);
            }else {
                ll_buyer_function.setVisibility(View.GONE);
                ll_my_ads.setVisibility(View.VISIBLE);
                tv_sellerName.setVisibility(View.GONE);
            }
        }else {
            ll_buyer_function.setVisibility(View.VISIBLE);
        }

        if(IS_FAVORITE){
            btn_like.setImageResource(R.drawable.heart2_checked_ic);
        }

        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(methods.isLogged()){
                    if(!methods.isMyAds(ADS)){
                        UpdateFavorite();
                    }
                }else{
                    openLoginActivity();
                    Toast.makeText(getContext(), Constant.NO_LOGIN, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(methods.isLogged()){

                    addToChatList(userItem);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.TAG_USER, userItem);
                    FragmentChat f = new FragmentChat();
                    f.setArguments(bundle);
                    ReplaceFragment(f, getString(R.string.frag_chat));
                }else{
                    openLoginActivity();
                    Toast.makeText(getContext(), Constant.NO_LOGIN, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(!ADS.isAds_isAvailable()){
            ll_sold.setVisibility(View.VISIBLE);
        }

        carImageAdapter = new CarImageAdapter(getContext(), CAR.getCar_imageList());
        viewPager.setAdapter(carImageAdapter);

        tv_price.setText("$ "+String.format("%1$,.0f", ADS.getAds_price()));
        tv_condition.setText((CAR.isNew())?"New":"Used");
        tv_carName.setText(CAR.getCar_name());

        Date currentDate = new Date();
        Date postDate = ADS.getAds_posttime();

        long diffInTime = currentDate.getTime() - postDate.getTime();
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInTime);
        long diffInHour = TimeUnit.MILLISECONDS.toHours(diffInTime);
        long diffInYear = TimeUnit.MILLISECONDS.toDays(diffInTime)/365l;
        long diffInMonth = TimeUnit.MILLISECONDS.toDays(diffInTime)/30l;
        long diffInDay = TimeUnit.MILLISECONDS.toDays(diffInTime);

        if(diffInYear < 1){
            if(diffInMonth < 1){
                if(diffInDay < 1){
                    if(diffInHour < 1){
                        if(diffInMinutes < 1){
                            tv_postTime.setText("Just now");
                        }else {
                            tv_postTime.setText(diffInMinutes + " minutes ago");
                        }
                    }else {
                        tv_postTime.setText(diffInHour + " hours ago");
                    }
                }else{
                    tv_postTime.setText(diffInDay + " days ago");
                }
            }else {
                tv_postTime.setText(diffInMonth + " months ago");
            }
        }else {
            tv_postTime.setText(diffInYear + " years ago");
        }
        tv_sellerName.setText(userItem.getFullName());
        tv_sellerName.setPaintFlags(tv_sellerName.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tv_sellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!methods.isMyAds(ADS)){
                    String uid = userItem.getUid();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.PROFILE_MODE, Constant.USER_PROFILE);
                    bundle.putString(Constant.TAG_UID, uid);
                    FragmentProfile f = new FragmentProfile();
                    f.setArguments(bundle);

                    ReplaceFragment(f, getString(R.string.frag_profile));
                }
            }
        });

        tv_mileAge.setText(String.format("%1$,d", ADS.getAds_mileage())+" km");
        tv_enginePower.setText((CAR.getCar_engineSize() != NOT_SET)?String.format("%1$,d", CAR.getCar_engineSize())+" cc":"N/A");
        tv_trans.setText(transmission.getName());
        tv_bodyType.setText(bodyType.getName());
        tv_fuelType.setText(fuelType.getName());
        tv_year.setText((CAR.getCar_year() != NOT_SET)?String.valueOf(CAR.getCar_year()):"N/A");
        tv_manu.setText(manufacturerItem.getManu_name());
        tv_model.setText(modelItem.getModel_name());
        tv_power.setText((CAR.getCar_power() != NOT_SET)?String.format("%1$,d", CAR.getCar_power())+" hp":"N/A");
        tv_preOwner.setText((CAR.getCar_previousOwner() != NOT_SET)?String.valueOf(CAR.getCar_previousOwner()):"N/A");
        tv_door.setText((CAR.getCar_doors() != NOT_SET)?String.valueOf(CAR.getCar_doors()): "N/A");
        tv_seat.setText((CAR.getCar_seats() != NOT_SET)?String.valueOf(CAR.getCar_seats()):"N/A");
        tv_color.setText(colorItem.getColor_name());
        tv_gear.setText((CAR.getCar_gears() != NOT_SET)?String.valueOf(CAR.getCar_gears()):"N/A");
        tv_cylinder.setText((CAR.getCar_cylinder() != NOT_SET)?String.valueOf(CAR.getCar_cylinder()):"N/A");
        tv_weight.setText((CAR.getCar_kerbWeight() != NOT_SET)?String.format("%1$,d", CAR.getCar_kerbWeight())+" kg":"N/A");
        tv_consump.setText((CAR.getCar_kerbWeight() != -1.0)? String.format("%1$,.0f", CAR.getCar_fuelConsumption()) +" liters/100 km":"N/A");
        tv_emisssion.setText((CAR.getCar_co2emission() != NOT_SET)?String.format("%1$,d", CAR.getCar_co2emission())+" g/km":"N/A");
        tv_descrip.setText((!ADS.getAds_description().equals(""))?(ADS.getAds_description()):"N/A");
        tv_address.setText((!ADS.getAds_location().equals(""))?(ADS.getAds_location()):"N/A");
        tv_email.setText((!userItem.getEmail().equals(""))?userItem.getEmail():"N/A");
        tv_phone.setText((!userItem.getPhoneNumber().equals(""))?userItem.getPhoneNumber():"N/A");

        ArrayList<EquipmentItem> arrayList = new ArrayList<>();
        for(String str : CAR.getCar_equipments()){
            for(EquipmentItem item : array_equip){
                if(Integer.parseInt(str) == item.getEquip_id()){
                    arrayList.add(item);
                    break;
                }
            }
        }
        rv_equipments.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rv_equipments.setAdapter(new EquipmentAdapter(arrayList, getString(R.string.ads), null));
    }

    private void LoadData(){
        if(methods.isNetworkAvailable()){
            LoadSearch loadSearch = new LoadSearch(new LoadSearchListener() {
                @Override
                public void onStart() {
                    if(getActivity()!=null){
                        errorMsg = "";
                        scrollView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        ll_empty.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onEnd(String success, ArrayList<ManufacturerItem> arrayList_manu, ArrayList<ModelItem> arrayList_model, ArrayList<MyItem> arrayList_city, ArrayList<MyItem> arrayList_bodytype, ArrayList<MyItem> arrayList_fueltype, ArrayList<MyItem> arrayList_trans, ArrayList<ColorItem> arrayList_color, ArrayList<EquipmentItem> arrayList_equip, ArrayList<UserItem> userItemArrayList) {
                    if(getActivity() != null){
                        if(success.equals("1")){
                            array_manu.clear();
                            array_model.clear();
                            array_city.clear();
                            array_bodytype.clear();
                            array_fueltype.clear();
                            array_trans.clear();
                            array_color.clear();
                            array_equip.clear();
                            array_user.clear();

                            array_manu.addAll(arrayList_manu);
                            array_model.addAll(arrayList_model);
                            array_city.addAll(arrayList_city);
                            array_bodytype.addAll(arrayList_bodytype);
                            array_fueltype.addAll(arrayList_fueltype);
                            array_trans.addAll(arrayList_trans);
                            array_color.addAll(arrayList_color);
                            array_equip.addAll(arrayList_equip);
                            array_user.addAll(userItemArrayList);

                            BindData();

                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        progressBar.setVisibility(View.GONE);
                        setEmpty();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_SEARCH, null, null));
            loadSearch.execute();
        }else{
            errorMsg = getString(R.string.internet_not_connect);
            setEmpty();
        }
    }
    private void setEmpty() {
        if(!errorMsg.equals("")){
            tv_empty.setText(errorMsg);
            btn_try.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadData();
                }
            });
            ll_empty.setVisibility(View.VISIBLE);
        }else{
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void UpdateFavorite(){
        UserItem USER_LOGIN = methods.getUserItemByUsername(array_user, Constant.UID);

        ArrayList<String> arrayList_fav_temp = new ArrayList<>(USER_LOGIN.getFavourite_ads());

        if(IS_FAVORITE){
            methods.removeFavorite(arrayList_fav_temp, ADS.getAds_id());
        }else{
            methods.addFavorite(arrayList_fav_temp, ADS.getAds_id());
        }

        String json = new Gson().toJson(arrayList_fav_temp);

        Bundle bundleUpdateFav = new Bundle();
        bundleUpdateFav.putString(Constant.TAG_UID, Constant.UID);
        bundleUpdateFav.putString("user_favlist", json);
        bundleUpdateFav.putBoolean("is_favorite", IS_FAVORITE);
        bundleUpdateFav.putInt("ads_id", ADS.getAds_id());

        UpdateFavouriteAsync updateFavouriteAsync = new UpdateFavouriteAsync(new UpdateFavListener() {
            @Override
            public void onEnd(String success) {
                if(success.equals("1")){
                    if(IS_FAVORITE){
                        //un favorite
                        IS_FAVORITE = false;
                        btn_like.setImageResource(R.drawable.heart2_ic);
                        methods.removeFavorite(USER_LOGIN.getFavourite_ads(), ADS.getAds_id());
                    }else{
                        // set favorite
                        IS_FAVORITE = true;
                        btn_like.setImageResource(R.drawable.heart2_checked_ic);
                        methods.addFavorite(USER_LOGIN.getFavourite_ads(), ADS.getAds_id());
                    }
                    reloadListener.reload();
                }else {
                    Toast.makeText(getContext(), getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                }
            }
        }, methods.getAPIRequest(Constant.METHOD_UPDATELIKE, bundleUpdateFav, null));
        updateFavouriteAsync.execute();
    }

    private void openLoginActivity(){
        Intent intent = new Intent(getContext(), ActivityLogin.class);
        startActivity(intent);
    }

    private void ReplaceFragment(Fragment fragment, String name){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, fragment, name);
        ft.addToBackStack(name);
        ft.commit();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(name);
    }

    private void addToChatList(UserItem addUser){
        UserItem SENDER = methods.getUserItemByUsername(array_user, Constant.UID);
        UserItem RECEIVER = methods.getUserItemByUsername(array_user, addUser.getUid());

        ArrayList<String> arrayList_sender = SENDER.getChatlist();
        ArrayList<String> arrayList_receiver = RECEIVER.getChatlist();

        //if receiver not exist in sender => add receiver to sender
        if(!methods.isUserIdInUserChatList(RECEIVER.getUid(), SENDER.getChatlist())){
            String uid = SENDER.getUid();
            arrayList_sender.add(RECEIVER.getUid());
            String json = new Gson().toJson(arrayList_sender);

            updateChatList(uid, json);
        }
        //if sender not exist in receiver => add sender to receiver
        if(!methods.isUserIdInUserChatList(SENDER.getUid(), RECEIVER.getChatlist())){
            String uid = RECEIVER.getUid();
            arrayList_receiver.add(SENDER.getUid());
            String json = new Gson().toJson(arrayList_receiver);

            updateChatList(uid, json);
        }

    }

    private void updateChatList(String uid, String json){

        Bundle bundle = new Bundle();
        bundle.putString(Constant.TAG_UID, uid);
        bundle.putString(Constant.TAG_CHATLIST, json);

        UpdateFavouriteAsync updateFavouriteAsync = new UpdateFavouriteAsync(new UpdateFavListener() {
            @Override
            public void onEnd(String success) {
                if(success.equals("1")){
                    //Toast.makeText(getContext(), Constant.SUCCESS, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                }
            }
        }, methods.getAPIRequest(Constant.METHOD_UPDATE_CHATLIST, bundle, null));
        updateFavouriteAsync.execute();

    }


}