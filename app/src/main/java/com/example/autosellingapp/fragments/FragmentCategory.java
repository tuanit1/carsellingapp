package com.example.autosellingapp.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.adapters.AdsAdapter;
import com.example.autosellingapp.asynctasks.LoadCategory;
import com.example.autosellingapp.interfaces.LoadCategoryListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FragmentCategory extends Fragment {

    private View rootView;
    private RecyclerView rv_ads;
    private Methods methods;
    private String errorMsg;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private LinearLayout ll_empty;
    private TextView tv_empty;
    private Button btn_try;
    private ArrayList<AdsItem> arrayList_ads;
    private ArrayList<CarItem> arrayList_car;
    private ArrayList<MyItem> arrayList_city;
    private ArrayList<UserItem> arrayList_user;
    private ArrayList<ModelItem> arrayList_model;
    private ArrayList<AdsItem> filteredAdsArray;
    private AdsAdapter adsAdapter;
    private CardView cv_no_found;
    private Button btn_no_found;

    private final int RADIO_STANDARD = 3;
    private final int RADIO_LATEST = 4;
    private final int RADIO_FAVORITE = 5;
    private final int RADIO_PRICE_LO_HI = 6;
    private final int RADIO_PRICE_HI_LO =7;
    private int RADIO_CURRENT = RADIO_STANDARD;

    private int NOT_SET = -1;
    private int CONDITION_USED = 0;
    private int CONDITION_NEW = 1;
    private int SELECTED_MANU_ID = NOT_SET;
    private int SELECTED_MODEL_ID = NOT_SET;
    private int SELECTED_BODY_TYPE_ID = NOT_SET;
    private int SELECTED_FUEL_TYPE_ID = NOT_SET;
    private int SELECTED_CITY_ID = NOT_SET;
    private int SELECTED_CONDITION = NOT_SET;
    private int SELECTED_TRANS_ID = NOT_SET;
    private int SELECTED_COLOR = NOT_SET;

    private int SELECTED_YEAR = NOT_SET;
    private int SELECTED_DOOR = NOT_SET;
    private int SELECTED_SEAT = NOT_SET;
    private int SELECTED_PREUSER = NOT_SET;

    private int SELECTED_PRICE_MIN = NOT_SET;
    private int SELECTED_PRICE_MAX = NOT_SET;
    private int SELECTED_POWER_MIN = NOT_SET;
    private int SELECTED_POWER_MAX = NOT_SET;
    private int SELECTED_MILEAGE_MIN = NOT_SET;
    private int SELECTED_MILEAGE_MAX = NOT_SET;

    private ArrayList<EquipmentItem> SELECTED_EQUIP_LIST;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        setHasOptionsMenu(true);
        methods = new Methods(getContext());
        Bundle bundle = getArguments();
        if(bundle != null){
            SELECTED_MANU_ID = bundle.getInt(getString(R.string.manufacturers));
            SELECTED_MODEL_ID = bundle.getInt(getString(R.string.model));
            SELECTED_BODY_TYPE_ID = bundle.getInt(getString(R.string.body_type));
            SELECTED_FUEL_TYPE_ID = bundle.getInt(getString(R.string.fuel_type));
            SELECTED_CITY_ID = bundle.getInt(getString(R.string.city));
            SELECTED_CONDITION = bundle.getInt(getString(R.string.condition));
            SELECTED_TRANS_ID = bundle.getInt(getString(R.string.transmission));
            SELECTED_COLOR = bundle.getInt(getString(R.string.body_color));
            SELECTED_YEAR = bundle.getInt(getString(R.string.year));
            SELECTED_DOOR = bundle.getInt(getString(R.string.door_number));
            SELECTED_SEAT = bundle.getInt(getString(R.string.seat_number));
            SELECTED_PREUSER = bundle.getInt(getString(R.string.previous_users));
            SELECTED_PRICE_MIN = bundle.getInt(getString(R.string.price_min));
            SELECTED_PRICE_MAX = bundle.getInt(getString(R.string.price_max));
            SELECTED_POWER_MIN = bundle.getInt(getString(R.string.power_min));
            SELECTED_POWER_MAX = bundle.getInt(getString(R.string.power_max));
            SELECTED_MILEAGE_MIN = bundle.getInt(getString(R.string.mileage_min));
            SELECTED_MILEAGE_MAX = bundle.getInt(getString(R.string.mileage_max));
            SELECTED_EQUIP_LIST = (ArrayList<EquipmentItem>) bundle.getSerializable(getString(R.string.equipment));
        }

        arrayList_car = new ArrayList<>();
        arrayList_ads = new ArrayList<>();
        arrayList_user = new ArrayList<>();
        arrayList_city = new ArrayList<>();
        arrayList_model = new ArrayList<>();
        filteredAdsArray = new ArrayList<>();

        Hook();

        LoadCategory();

        return rootView;
    }

    private void Hook(){
        rv_ads = rootView.findViewById(R.id.rv_ads);
        scrollView = rootView.findViewById(R.id.scroll_view);
        progressBar = rootView.findViewById(R.id.progressBar);
        ll_empty = rootView.findViewById(R.id.ll_empty);
        tv_empty = rootView.findViewById(R.id.tv_empty);
        btn_try = rootView.findViewById(R.id.btn_try);
        cv_no_found = rootView.findViewById(R.id.cv_no_found);
        btn_no_found = rootView.findViewById(R.id.btn_no_found);
        btn_no_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_sort:
                openDialogSort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void LoadCategory(){
        if(methods.isNetworkAvailable()){
            LoadCategory loadCategory = new LoadCategory(new LoadCategoryListener() {
                @Override
                public void onStart() {
                    if(getActivity() != null) {
                        errorMsg = "";
                        scrollView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        ll_empty.setVisibility(View.GONE);
                        cv_no_found.setVisibility(View.GONE);
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
                            filteredAdsArray.clear();

                            arrayList_car.addAll(carItemArrayList);
                            arrayList_ads.addAll(adsItemArrayList);
                            arrayList_city.addAll(cityItemArrayList);
                            arrayList_user.addAll(userItemArrayList);
                            arrayList_model.addAll(modelItemArrayList);

                            rv_ads.setLayoutManager(new GridLayoutManager(getContext(), 2));
                            rv_ads.setHasFixedSize(true);
                            filteredAdsArray = getFilteredAdsArray(arrayList_ads);
                            if(filteredAdsArray.size() != 0){
                                adsAdapter = new AdsAdapter(methods, filteredAdsArray, arrayList_car, arrayList_user, arrayList_city);
                                rv_ads.setAdapter(adsAdapter);
                            }
                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        progressBar.setVisibility(View.GONE);
                        setEmpty();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_CATEGORY));
            loadCategory.execute();
        }else {
            errorMsg = getString(R.string.internet_not_connect);
            setEmpty();
        }
    }

    private void setEmpty(){
        if(!errorMsg.equals("")){
            tv_empty.setText(errorMsg);
            btn_try.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadCategory();
                }
            });
            ll_empty.setVisibility(View.VISIBLE);
        }else if(filteredAdsArray.size() == 0){
            cv_no_found.setVisibility(View.VISIBLE);
        }else{
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<AdsItem> getFilteredAdsArray(ArrayList<AdsItem> arrayList_ads){
        ArrayList<AdsItem> filtered_array = new ArrayList<>();

        for(AdsItem ads : arrayList_ads){
            CarItem car = methods.getCarItemByID(arrayList_car, ads.getCar_id());
            ModelItem model = methods.getModelItemByID(arrayList_model, car.getModel_id());
            if(SELECTED_MANU_ID != NOT_SET){
                if(model.getManu_id() != SELECTED_MANU_ID){
                    continue;
                }
            }
            if(SELECTED_MODEL_ID != NOT_SET){
                if(car.getModel_id() != SELECTED_MODEL_ID){
                    continue;
                }
            }
            if(SELECTED_PRICE_MIN != NOT_SET && SELECTED_PRICE_MAX != NOT_SET){
                if(!(SELECTED_PRICE_MIN <= ads.getAds_price() && ads.getAds_price() <= SELECTED_PRICE_MAX)){
                    continue;
                }
            }
            if(SELECTED_POWER_MIN != NOT_SET && SELECTED_POWER_MAX != NOT_SET){
                if(!(SELECTED_POWER_MIN <= ads.getAds_price() && car.getCar_power() <= SELECTED_POWER_MAX)){
                    continue;
                }
            }
            if(SELECTED_MILEAGE_MIN != NOT_SET && SELECTED_MILEAGE_MAX != NOT_SET){
                if(!(SELECTED_MILEAGE_MIN <= ads.getAds_price() && ads.getAds_mileage() <= SELECTED_MILEAGE_MAX)){
                    continue;
                }
            }
            if(SELECTED_BODY_TYPE_ID != NOT_SET){
                if(car.getBodyType_id() != SELECTED_BODY_TYPE_ID){
                    continue;
                }
            }
            if(SELECTED_FUEL_TYPE_ID != NOT_SET){
                if(car.getFuelType_id() != SELECTED_FUEL_TYPE_ID){
                    continue;
                }
            }
            if(SELECTED_YEAR != NOT_SET){
                if(car.getCar_year() != SELECTED_YEAR){
                    continue;
                }
            }
            if(SELECTED_TRANS_ID != NOT_SET){
                if(car.getTrans_id() != SELECTED_TRANS_ID){
                    continue;
                }
            }
            if(SELECTED_CONDITION != NOT_SET){
                if(car.isNew() != (SELECTED_CONDITION == 0)?false:true){
                    continue;
                }
            }
            if(SELECTED_COLOR != NOT_SET){
                if(car.getColor_id() != SELECTED_COLOR){
                    continue;
                }
            }
            if(SELECTED_CITY_ID != NOT_SET){
                if(ads.getCity_id() != SELECTED_CITY_ID){
                    continue;
                }
            }
            if(SELECTED_SEAT != NOT_SET){
                if(car.getCar_seats() != SELECTED_SEAT){
                    continue;
                }
            }
            if(SELECTED_DOOR != NOT_SET){
                if(car.getCar_doors() != SELECTED_DOOR){
                    continue;
                }
            }
            if(SELECTED_PREUSER != NOT_SET){
                if(car.getCar_previousOwner() >= SELECTED_PREUSER){
                    continue;
                }
            }
            if(SELECTED_EQUIP_LIST.size() != 0){
                int index = 0;
                for(EquipmentItem equipmentItem : SELECTED_EQUIP_LIST){
                    if(methods.isEquipItemInEquipArray(equipmentItem, car.getCar_equipments())){
                        index++;
                        if(index == SELECTED_EQUIP_LIST.size()){
                            filtered_array.add(ads);
                            break;
                        }
                    }else {
                        break;
                    }
                }
            }else{
                filtered_array.add(ads);
            }

        }

        return filtered_array;
    }



    private void openDialogSort(){

        Dialog dialog_sort = new Dialog(getContext());
        dialog_sort.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_sort.setContentView(R.layout.layout_dialog_sort);

        RadioGroup radioGroup = dialog_sort.findViewById(R.id.radioGroup);
        RadioButton radio_standard = dialog_sort.findViewById(R.id.radio_standard);
        RadioButton radio_latest = dialog_sort.findViewById(R.id.radio_latest);
        RadioButton radio_favorite = dialog_sort.findViewById(R.id.radio_favorite);
        RadioButton radio_pricelowtohigh = dialog_sort.findViewById(R.id.radio_priceLowToHigh);
        RadioButton radio_pricehightolow = dialog_sort.findViewById(R.id.radio_priceHighToLow);
        TextView tv_dialog = dialog_sort.findViewById(R.id.tv_dialog_sort);

        switch (RADIO_CURRENT){
            case RADIO_STANDARD:
                radio_standard.setChecked(true);
                break;
            case RADIO_LATEST:
                radio_latest.setChecked(true);
                break;
            case RADIO_FAVORITE:
                radio_favorite.setChecked(true);
                break;
            case RADIO_PRICE_LO_HI:
                radio_pricelowtohigh.setChecked(true);
                break;
            case RADIO_PRICE_HI_LO:
                radio_pricehightolow.setChecked(true);
                break;
        }
        tv_dialog.setText(getString(R.string.sort));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_standard:
                        RADIO_CURRENT = RADIO_STANDARD;
                        LoadFilterSortAds();
                        dialog_sort.dismiss();
                        break;
                    case R.id.radio_latest:
                        RADIO_CURRENT = RADIO_LATEST;
                        LoadFilterSortAds();
                        dialog_sort.dismiss();
                        break;
                    case R.id.radio_favorite:
                        RADIO_CURRENT = RADIO_FAVORITE;
                        LoadFilterSortAds();
                        dialog_sort.dismiss();
                        break;
                    case R.id.radio_priceLowToHigh:
                        RADIO_CURRENT = RADIO_PRICE_LO_HI;
                        LoadFilterSortAds();
                        dialog_sort.dismiss();
                        break;
                    case R.id.radio_priceHighToLow:
                        RADIO_CURRENT = RADIO_PRICE_HI_LO;
                        LoadFilterSortAds();
                        dialog_sort.dismiss();
                        break;
                }
            }
        });
        dialog_sort.show();
    }

    private void LoadFilterSortAds(){
        ArrayList<AdsItem> tempArray = new ArrayList<>();
        for(AdsItem x : filteredAdsArray){
            tempArray.add(new AdsItem(x.getAds_id(), x.getCar_id(), x.getUsername(), x.getAds_price(), x.getAds_mileage(), x.getCity_id(), x.getAds_location(), x.getAds_description(), x.getAds_posttime(), x.getAds_likes()));
        }

        switch (RADIO_CURRENT){
            case RADIO_STANDARD:
                adsAdapter.setValue(filteredAdsArray);
                adsAdapter.notifyDataSetChanged();
                break;
            case RADIO_LATEST:
                sortByLatest(tempArray);
                adsAdapter.setValue(tempArray);
                adsAdapter.notifyDataSetChanged();
                break;
            case RADIO_FAVORITE:
                sortByFavorite(tempArray);
                adsAdapter.setValue(tempArray);
                adsAdapter.notifyDataSetChanged();
                break;
            case RADIO_PRICE_LO_HI:
                sortByPriceLowToHigh(tempArray);
                adsAdapter.setValue(tempArray);
                adsAdapter.notifyDataSetChanged();
                break;
            case RADIO_PRICE_HI_LO:
                sortByPriceHighToLow(tempArray);
                adsAdapter.setValue(tempArray);
                adsAdapter.notifyDataSetChanged();
                break;
        }
    }
    private void sortByPriceLowToHigh(ArrayList<AdsItem> arr){
        for (int i = 0; i < arr.size(); i++) {
            // find position of smallest num between (i + 1)th element and last element
            int pos = i;
            for (int j = i; j < arr.size(); j++) {
                if (arr.get(j).getAds_price() < arr.get(pos).getAds_price())
                    pos = j;
            }
            // Swap min (smallest num) to current position on array
            AdsItem min = arr.get(pos);
            arr.set(pos, arr.get(i));
            arr.set(i, min);
        }
    }
    private void sortByPriceHighToLow(ArrayList<AdsItem> arr){
        for (int i = 0; i < arr.size(); i++) {
            // find position of smallest num between (i + 1)th element and last element
            int pos = i;
            for (int j = i; j < arr.size(); j++) {
                if (arr.get(j).getAds_price() > arr.get(pos).getAds_price())
                    pos = j;
            }
            // Swap min (smallest num) to current position on array
            AdsItem min = arr.get(pos);
            arr.set(pos, arr.get(i));
            arr.set(i, min);
        }
    }
    private void sortByLatest(ArrayList<AdsItem> arr){
        for (int i = 0; i < arr.size(); i++) {
            // find position of smallest num between (i + 1)th element and last element
            int pos = i;
            for (int j = i; j < arr.size(); j++) {
                if (arr.get(j).getAds_posttime().compareTo(arr.get(pos).getAds_posttime()) > 0)
                    pos = j;
            }
            // Swap min (smallest num) to current position on array
            AdsItem min = arr.get(pos);
            arr.set(pos, arr.get(i));
            arr.set(i, min);
        }
    }
    private void sortByFavorite(ArrayList<AdsItem> arr){
        for (int i = 0; i < arr.size(); i++) {
            // find position of smallest num between (i + 1)th element and last element
            int pos = i;
            for (int j = i; j < arr.size(); j++) {
                if (arr.get(j).getAds_likes() > arr.get(pos).getAds_likes())
                    pos = j;
            }
            // Swap min (smallest num) to current position on array
            AdsItem min = arr.get(pos);
            arr.set(pos, arr.get(i));
            arr.set(i, min);
        }
    }
}