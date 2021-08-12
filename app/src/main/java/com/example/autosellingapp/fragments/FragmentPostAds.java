package com.example.autosellingapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.adapters.ColorListAdapter;
import com.example.autosellingapp.adapters.EquipmentAdapter;
import com.example.autosellingapp.adapters.PickImageAdapter;
import com.example.autosellingapp.asynctasks.LoadSearch;
import com.example.autosellingapp.asynctasks.PostAdsAsync;
import com.example.autosellingapp.databinding.FragmentPostadsBinding;
import com.example.autosellingapp.interfaces.ColorListener;
import com.example.autosellingapp.interfaces.EquipmentListener;
import com.example.autosellingapp.interfaces.LoadSearchListener;
import com.example.autosellingapp.interfaces.PostAdsListener;
import com.example.autosellingapp.interfaces.ReloadFragmentListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.ColorItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

public class FragmentPostAds extends Fragment {

    private FragmentPostadsBinding binding;
    private Methods methods;
    private String errorMsg;
    private int MODE = POST_MODE;
    private boolean isChangeImage = false;
    private static final int EDIT_MODE = 886;
    private static final int POST_MODE = 699;
    private ArrayList<ManufacturerItem> array_manu;
    private ArrayList<ModelItem> array_model;
    private ArrayList<MyItem> array_city;
    private ArrayList<MyItem> array_bodytype;
    private ArrayList<MyItem> array_fueltype;
    private ArrayList<MyItem> array_trans;
    private ArrayList<ColorItem> array_color;
    private ArrayList<EquipmentItem> array_equip;
    private ArrayList<Uri> uriImageArrayList;

    private PickImageAdapter pickImageAdapter;

    private int NOT_SET = -1;
    private AdsItem EDIT_ADS;
    private int SELECTED_MANU_ID = NOT_SET;
    private int SELECTED_MODEL_ID = NOT_SET;
    private int SELECTED_BODY_TYPE_ID = NOT_SET;
    private int SELECTED_FUEL_TYPE_ID = NOT_SET;
    private int SELECTED_CITY_ID = NOT_SET;
    private int SELECTED_CONDITION = NOT_SET;
    private int CONDITION_USED = 0;
    private int CONDITION_NEW = 1;
    private int SELECTED_TRANS_ID = NOT_SET;
    private int SELECTED_COLOR_ID = NOT_SET;

    private String SELECTED_CARNAME = "";
    private String SELECTED_DESCRIPTION = "";
    private String SELECTED_ADDRESS = "";
    private double SELECTED_FUELCONSUMP = NOT_SET;

    private int SELECTED_YEAR = NOT_SET;
    private int SELECTED_DOOR = NOT_SET;
    private int SELECTED_SEAT = NOT_SET;
    private int SELECTED_PREUSER = NOT_SET;
    private int SELECTED_ENGINESIZE = NOT_SET;
    private int SELECTED_GEARS = NOT_SET;
    private int SELECTED_CYLINDER = NOT_SET;
    private int SELECTED_WEIGHT = NOT_SET;
    private int SELECTED_CO2EMISSION = NOT_SET;

    private int SELECTED_PRICE = NOT_SET;
    private int SELECTED_POWER = NOT_SET;
    private int SELECTED_MILEAGE = NOT_SET;

    private static final int PICK_IMAGE_CODE = 622;

    private ArrayList<EquipmentItem> SELECTED_EQUIP_LIST;

    private ReloadFragmentListener reloadFragmentListener;

    public FragmentPostAds(ReloadFragmentListener reloadFragmentListener){
        this.reloadFragmentListener = reloadFragmentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPostadsBinding.inflate(inflater, container, false);

        methods = new Methods(getContext());

        array_manu = new ArrayList<>();
        array_model = new ArrayList<>();
        array_city = new ArrayList<>();;
        array_bodytype = new ArrayList<>();
        array_fueltype = new ArrayList<>();
        array_trans = new ArrayList<>();
        array_color = new ArrayList<>();
        array_equip = new ArrayList<>();
        SELECTED_EQUIP_LIST = new ArrayList<>();
        uriImageArrayList = new ArrayList<>();

        pickImageAdapter = new PickImageAdapter(uriImageArrayList);

        binding.cvModel.setEnabled(false);
        binding.cvManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_MANU_LIST);
            }
        });
        binding.cvModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_MODEL_LIST);
            }
        });
        binding.cvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_CITY_LIST);
            }
        });
        binding.cvBodytype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_BODY_TYPE_LIST);
            }
        });
        binding.cvFueltype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_FUEL_TYPE_LIST);
            }
        });
        binding.cvCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_CONDITION);
            }
        });
        binding.cvTransmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_TRANS);
            }
        });
        binding.cvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_YEAR);
            }
        });
        binding.cvSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_SEAT);
            }
        });
        binding.cvDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_DOOR);
            }
        });
        binding.cvPreuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_PREUSER);
            }
        });
        binding.cvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_PRICE);
            }
        });
        binding.cvPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_POWER);
            }
        });
        binding.cvEnginepower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_ENGINESIZE);
            }
        });
        binding.cvMileage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_MILEAGE);
            }
        });
        binding.cvGears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_GEARS);
            }
        });
        binding.cvCylinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_CYNLINDER);
            }
        });
        binding.cvWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_WEIGHT);
            }
        });
        binding.cvCo2emission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_CO2EMISSTION);
            }
        });
        binding.cvBodycolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogColor();
            }
        });
        binding.cvEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEquipment();
            }
        });
        binding.cvCarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogText(Constant.TEXT_CAR_NAME);
            }
        });
        binding.cvDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogText(Constant.TEXT_DESCRIPTION);
            }
        });
        binding.cvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogText(Constant.TEXT_ADDRESS);
            }
        });
        binding.cvFuelconsump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogText(Constant.TEXT_FUELCONSUMP);
            }
        });
        binding.cvImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogPickImage();
            }
        });
        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(methods.isNetworkAvailable()){
                    PostAd();
                }else {
                    Toast.makeText(getContext(), Constant.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });

        LoadData();

        return binding.getRoot();
    }

    private void LoadFromEdit(){
        Bundle bundle = getArguments();
        if(bundle != null){

            MODE = EDIT_MODE;
            binding.btnPost.setText("EDIT");

            EDIT_ADS = (AdsItem) bundle.getSerializable(Constant.TAG_ADS);
            CarItem car = (CarItem) bundle.getSerializable(Constant.TAG_CAR);
            ModelItem modelItem = methods.getModelItemByID(array_model, car.getModel_id());
            ManufacturerItem manu = methods.getManuItemByID(array_manu, modelItem.getManu_id());
            MyItem city = methods.getMyItemByID(array_city, EDIT_ADS.getCity_id());
            MyItem fuelType = methods.getMyItemByID(array_fueltype, car.getFuelType_id());
            MyItem bodyType = methods.getMyItemByID(array_bodytype, car.getBodyType_id());
            MyItem trans = methods.getMyItemByID(array_trans, car.getTrans_id());
            ColorItem color = methods.getColorItemByID(array_color, car.getColor_id());

            SELECTED_MANU_ID = modelItem.getManu_id();
            SELECTED_MODEL_ID = modelItem.getModel_id();
            SELECTED_BODY_TYPE_ID = car.getBodyType_id();
            SELECTED_FUEL_TYPE_ID = car.getFuelType_id();
            SELECTED_CITY_ID = EDIT_ADS.getCity_id();
            SELECTED_CONDITION = ( car.isNew() ) ? 1 : 0;
            SELECTED_TRANS_ID = car.getTrans_id();
            SELECTED_COLOR_ID = car.getColor_id();

            SELECTED_CARNAME = car.getCar_name();
            SELECTED_DESCRIPTION = EDIT_ADS.getAds_description();
            SELECTED_ADDRESS = EDIT_ADS.getAds_location();
            SELECTED_FUELCONSUMP = car.getCar_fuelConsumption();

            SELECTED_YEAR = car.getCar_year();
            SELECTED_DOOR = car.getCar_doors();
            SELECTED_SEAT = car.getCar_seats();
            SELECTED_PREUSER = car.getCar_previousOwner();
            SELECTED_ENGINESIZE = car.getCar_engineSize();
            SELECTED_GEARS = car.getCar_gears();
            SELECTED_CYLINDER = car.getCar_cylinder();
            SELECTED_WEIGHT = car.getCar_kerbWeight();
            SELECTED_CO2EMISSION = car.getCar_co2emission();

            SELECTED_PRICE = (int) EDIT_ADS.getAds_price();
            SELECTED_POWER = car.getCar_power();
            SELECTED_MILEAGE = EDIT_ADS.getAds_mileage();


            for(EquipmentItem item : array_equip){
                for(String id : car.getCar_equipments()){
                    if(item.getEquip_id() == Integer.parseInt(id)){
                        item.setChecked(true);
                        SELECTED_EQUIP_LIST.add(new EquipmentItem(item.getEquip_id(), item.getEquip_name(), item.isChecked()));
                    }
                }
            }

            for(String image : car.getCar_imageList()){
                String url = Constant.SERVER_URL + "images/car_image/" + image;
                uriImageArrayList.add(Uri.parse(url));
            }

            binding.tvManu.setText(manu.getManu_name());
            binding.tvManu.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvModel.setText(modelItem.getModel_name());
            binding.tvModel.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvCarName.setText(SELECTED_CARNAME);
            binding.tvCarName.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvImages.setText(uriImageArrayList.size() + " images is selected");
            binding.tvImages.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvPrice.setText("$ " + String.format("%1$,d",SELECTED_PRICE));
            binding.tvPrice.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvCity.setText(city.getName());
            binding.tvCity.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvAddress.setText(SELECTED_ADDRESS);
            binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvPower.setText(String.format("%1$,d",SELECTED_POWER) + " hp");
            binding.tvPower.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvEnginepower.setText(String.format("%1$,d",SELECTED_ENGINESIZE) + " cc");
            binding.tvEnginepower.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvMileage.setText(String.format("%1$,d",SELECTED_MILEAGE) + " km");
            binding.tvMileage.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvBodytype.setText(bodyType.getName());
            binding.tvBodytype.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvFueltype.setText(fuelType.getName());
            binding.tvFueltype.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvCondition.setText((SELECTED_CONDITION == 0)?"Used":"New");
            binding.tvCondition.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvYear.setText(String.valueOf(SELECTED_YEAR));
            binding.tvYear.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvTransmission.setText(trans.getName());
            binding.tvTransmission.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvBodycolor.setText(color.getColor_name());
            binding.tvBodycolor.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvSeat.setText(String.valueOf(SELECTED_SEAT));
            binding.tvSeat.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvDoor.setText(String.valueOf(SELECTED_DOOR));
            binding.tvDoor.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvEquipment.setText(SELECTED_EQUIP_LIST.size() + " equipments is selected");
            binding.tvEquipment.setTextColor(getResources().getColor(R.color.dark_blue));
            binding.tvPreuser.setText(String.valueOf(SELECTED_PREUSER));
            binding.tvPreuser.setTextColor(getResources().getColor(R.color.dark_blue));
            if(SELECTED_GEARS != NOT_SET){
                binding.tvGears.setText(String.valueOf(SELECTED_GEARS));
                binding.tvGears.setTextColor(getResources().getColor(R.color.dark_blue));
            }
            if(SELECTED_CYLINDER != NOT_SET){
                binding.tvCylinder.setText(String.valueOf(SELECTED_CYLINDER));
                binding.tvCylinder.setTextColor(getResources().getColor(R.color.dark_blue));
            }
            if(SELECTED_WEIGHT != NOT_SET){
                binding.tvWeight.setText(String.format("%1$,d",SELECTED_WEIGHT) + " kg");
                binding.tvWeight.setTextColor(getResources().getColor(R.color.dark_blue));
            }
            if(SELECTED_FUELCONSUMP != NOT_SET){
                binding.tvFuelconsump.setText(String.valueOf(SELECTED_FUELCONSUMP) + " liters/100 km");
                binding.tvFuelconsump.setTextColor(getResources().getColor(R.color.dark_blue));
            }
            if(SELECTED_CO2EMISSION != NOT_SET){
                binding.tvCo2emission.setText(String.valueOf(SELECTED_CO2EMISSION) + " g/km");
                binding.tvCo2emission.setTextColor(getResources().getColor(R.color.dark_blue));
            }
            if(!SELECTED_DESCRIPTION.equals("")){
                binding.tvDescription.setText(SELECTED_DESCRIPTION);
                binding.tvDescription.setTextColor(getResources().getColor(R.color.dark_blue));
            }
        }
    }

    private void LoadData(){
        if(methods.isNetworkAvailable()){
            LoadSearch loadSearch = new LoadSearch(new LoadSearchListener() {
                @Override
                public void onStart() {
                    if(getActivity()!=null){
                        errorMsg = "";
                        binding.rlScrollView.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        binding.llEmpty.setVisibility(View.GONE);
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

                            array_manu.addAll(arrayList_manu);
                            array_model.addAll(arrayList_model);
                            array_city.addAll(arrayList_city);
                            array_bodytype.addAll(arrayList_bodytype);
                            array_fueltype.addAll(arrayList_fueltype);
                            array_trans.addAll(arrayList_trans);
                            array_color.addAll(arrayList_color);
                            array_equip.addAll(arrayList_equip);

                            LoadFromEdit();

                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        binding.progressBar.setVisibility(View.GONE);
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
            binding.tvEmpty.setText(errorMsg);
            binding.btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadData();
                }
            });
            binding.llEmpty.setVisibility(View.VISIBLE);
        }else{
            binding.rlScrollView.setVisibility(View.VISIBLE);
        }
    }

    private void openDialogColor(){
        Dialog dialog1 = new Dialog(this.getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.layout_dialog_list_color);

        TextView tv_dialog_list_color = dialog1.findViewById(R.id.tv_dialog_list_color);
        RecyclerView rv_dialog_list_color = dialog1.findViewById(R.id.rv_dialog_list_color);


        tv_dialog_list_color.setText(Constant.TEXT_COLOR);

        ColorListAdapter adapter = new ColorListAdapter(array_color, new ColorListener() {
            @Override
            public void onClick(int color_id, String color_name) {
                binding.tvBodycolor.setText(color_name);
                SELECTED_COLOR_ID = color_id;
                binding.tvBodycolor.setTextColor(getResources().getColor(R.color.dark_blue));

                dialog1.dismiss();
            }
        });

        rv_dialog_list_color.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        rv_dialog_list_color.hasFixedSize();
        rv_dialog_list_color.setAdapter(adapter);

        dialog1.show();
    }

    private void openDialogNumber(String type){
        Dialog dialog = new Dialog(this.getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_dialog_number);

        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog_number);
        tv_dialog.setText(type);

        NumberPicker numberPicker = dialog.findViewById(R.id.num_picker);

        Button btn_back = dialog.findViewById(R.id.btn_not_set);

        btn_back.setText("BACK");

        switch (type){
            case Constant.TEXT_YEAR:
                numberPicker.setMinValue(2000);
                numberPicker.setMaxValue(2025);

                if(SELECTED_YEAR != NOT_SET){
                    numberPicker.setValue(SELECTED_YEAR);
                }

                break;
            case Constant.TEXT_SEAT:
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(12);

                if(SELECTED_SEAT != NOT_SET){
                    numberPicker.setValue(SELECTED_YEAR);
                }

                break;
            case Constant.TEXT_DOOR:
                numberPicker.setMinValue(2);
                numberPicker.setMaxValue(7);

                if(SELECTED_DOOR != NOT_SET){
                    numberPicker.setValue(SELECTED_DOOR);
                }

                break;
            case Constant.TEXT_PREUSER:
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(20);

                if(SELECTED_PREUSER != NOT_SET){
                    numberPicker.setValue(SELECTED_PREUSER);
                }

                break;
            case Constant.TEXT_PRICE:
                tv_dialog.setText(type + " ($)");
                numberPicker.setMinValue(500);
                numberPicker.setMaxValue(200000);

                if(SELECTED_PRICE != NOT_SET){
                    numberPicker.setValue(SELECTED_PRICE);
                }

                break;
            case Constant.TEXT_POWER:
                tv_dialog.setText(type + " (hp)");
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(300);

                if(SELECTED_POWER != NOT_SET){
                    numberPicker.setValue(SELECTED_POWER);
                }

                break;
            case Constant.TEXT_ENGINESIZE:
                tv_dialog.setText(type + " (cc)");
                numberPicker.setMinValue(50);
                numberPicker.setMaxValue(3000);

                if(SELECTED_ENGINESIZE != NOT_SET){
                    numberPicker.setValue(SELECTED_ENGINESIZE);
                }

                break;
            case Constant.TEXT_MILEAGE:
                tv_dialog.setText(type + " (km)");
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(200000);

                if(SELECTED_MILEAGE != NOT_SET){
                    numberPicker.setValue(SELECTED_MILEAGE);
                }

                break;
            case Constant.TEXT_GEARS:
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(8);

                if(SELECTED_GEARS != NOT_SET){
                    numberPicker.setValue(SELECTED_GEARS);
                }
                btn_back.setText("NOT SET");
                break;
            case Constant.TEXT_CYNLINDER:
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(10);

                if(SELECTED_CYLINDER != NOT_SET){
                    numberPicker.setValue(SELECTED_CYLINDER);
                }
                btn_back.setText("NOT SET");
                break;
            case Constant.TEXT_WEIGHT:
                tv_dialog.setText(type + " (kg)");
                numberPicker.setMinValue(500);
                numberPicker.setMaxValue(2000);

                if(SELECTED_WEIGHT != NOT_SET){
                    numberPicker.setValue(SELECTED_WEIGHT);
                }
                btn_back.setText("NOT SET");
                break;
            case Constant.TEXT_CO2EMISSTION:
                tv_dialog.setText(type + " (g/km)");
                numberPicker.setMinValue(50);
                numberPicker.setMaxValue(200);

                if(SELECTED_CO2EMISSION != NOT_SET){
                    numberPicker.setValue(SELECTED_CO2EMISSION);
                }
                btn_back.setText("NOT SET");
                break;
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (type){
                    case Constant.TEXT_GEARS:
                        SELECTED_GEARS = NOT_SET;
                        binding.tvGears.setText("");
                        break;
                    case Constant.TEXT_CYNLINDER:
                        SELECTED_CYLINDER = NOT_SET;
                        binding.tvCylinder.setText("");
                        break;
                    case Constant.TEXT_WEIGHT:
                        SELECTED_WEIGHT = NOT_SET;
                        binding.tvWeight.setText("");
                        break;
                    case Constant.TEXT_CO2EMISSTION:
                        SELECTED_CO2EMISSION = NOT_SET;
                        binding.tvCo2emission.setText("");
                        break;
                }
                dialog.dismiss();
            }
        });

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case Constant.TEXT_YEAR:
                        SELECTED_YEAR = numberPicker.getValue();
                        binding.tvYear.setText(String.valueOf(SELECTED_YEAR));
                        binding.tvYear.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_SEAT:
                        SELECTED_SEAT = numberPicker.getValue();
                        binding.tvSeat.setText(String.valueOf(SELECTED_SEAT));
                        binding.tvSeat.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_DOOR:
                        SELECTED_DOOR = numberPicker.getValue();
                        binding.tvDoor.setText(String.valueOf(SELECTED_DOOR));
                        binding.tvDoor.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_PREUSER:
                        SELECTED_PREUSER = numberPicker.getValue();
                        binding.tvPreuser.setText(String.valueOf(SELECTED_PREUSER));
                        binding.tvPreuser.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_PRICE:
                        SELECTED_PRICE = numberPicker.getValue();
                        binding.tvPrice.setText("$ " + String.format("%1$,d",SELECTED_PRICE));
                        binding.tvPrice.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_POWER:
                        SELECTED_POWER = numberPicker.getValue();
                        binding.tvPower.setText(String.valueOf(SELECTED_POWER) + " hp");
                        binding.tvPower.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_ENGINESIZE:
                        SELECTED_ENGINESIZE = numberPicker.getValue();
                        binding.tvEnginepower.setText(String.format("%1$,d",SELECTED_ENGINESIZE)+ " cc");
                        binding.tvEnginepower.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_MILEAGE:
                        SELECTED_MILEAGE = numberPicker.getValue();
                        binding.tvMileage.setText(String.format("%1$,d",SELECTED_MILEAGE) + " km");
                        binding.tvMileage.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_GEARS:
                        SELECTED_GEARS = numberPicker.getValue();
                        binding.tvGears.setText(String.valueOf(SELECTED_GEARS));
                        binding.tvGears.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_CYNLINDER:
                        SELECTED_CYLINDER = numberPicker.getValue();
                        binding.tvCylinder.setText(String.valueOf(SELECTED_CYLINDER));
                        binding.tvCylinder.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_WEIGHT:
                        SELECTED_WEIGHT = numberPicker.getValue();
                        binding.tvWeight.setText(String.format("%1$,d",SELECTED_WEIGHT )+ " kg");
                        binding.tvWeight.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_CO2EMISSTION:
                        SELECTED_CO2EMISSION = numberPicker.getValue();
                        binding.tvCo2emission.setText(String.valueOf(SELECTED_CO2EMISSION) + " g/km");
                        binding.tvCo2emission.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openDialogList(String type){
        Dialog dialog = new Dialog(this.getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_dialog_list);

        ListView lv_dialog_list = dialog.findViewById(R.id.lv_dialog_list);
        TextView tv_dialog_list = dialog.findViewById(R.id.tv_dialog_list);



        ArrayAdapter lv_Adapter;

        switch (type){
            case Constant.TEXT_MANU_LIST:
                tv_dialog_list.setText(type);

                lv_Adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, array_manu);
                lv_dialog_list.setAdapter(lv_Adapter);
                lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        binding.tvManu.setText(array_manu.get(position).getManu_name());
                        SELECTED_MANU_ID = array_manu.get(position).getManu_id();

                        binding.cvModel.setEnabled(true);
                        binding.tvManu.setTextColor(getResources().getColor(R.color.dark_blue));

                        SELECTED_MODEL_ID = NOT_SET;
                        binding.tvModel.setText("");
                        dialog.dismiss();
                    }
                });
                break;
            case Constant.TEXT_MODEL_LIST:
                tv_dialog_list.setText(type);

                lv_Adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1);
                ArrayList<ModelItem> arrayList = new ArrayList<>();

                if(SELECTED_MANU_ID != NOT_SET){
                    for (ModelItem item : array_model){
                        if(item.getManu_id() == SELECTED_MANU_ID){
                            arrayList.add(item);
                        }
                    }
                    lv_Adapter.addAll(arrayList);
                    lv_dialog_list.setAdapter(lv_Adapter);
                    lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            binding.tvModel.setText(arrayList.get(position).getModel_name());
                            SELECTED_MODEL_ID = arrayList.get(position).getModel_id();

                            if(SELECTED_MODEL_ID == NOT_SET){
                                binding.tvModel.setTextColor(getResources().getColor(R.color.default_text));
                            }else{
                                binding.tvModel.setTextColor(getResources().getColor(R.color.dark_blue));
                            }
                            dialog.dismiss();
                        }
                    });
                }
                break;
            case Constant.TEXT_BODY_TYPE_LIST:
                tv_dialog_list.setText(type);

                lv_Adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, array_bodytype);
                lv_dialog_list.setAdapter(lv_Adapter);
                lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        binding.tvBodytype.setText(array_bodytype.get(position).getName());
                        SELECTED_BODY_TYPE_ID = array_bodytype.get(position).getId();

                        binding.tvBodytype.setTextColor(getResources().getColor(R.color.dark_blue));

                        dialog.dismiss();
                    }
                });
                break;
            case Constant.TEXT_FUEL_TYPE_LIST:
                tv_dialog_list.setText(type);

                lv_Adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, array_fueltype);
                lv_dialog_list.setAdapter(lv_Adapter);
                lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        binding.tvFueltype.setText(array_fueltype.get(position).getName());
                        SELECTED_FUEL_TYPE_ID = array_fueltype.get(position).getId();

                        binding.tvFueltype.setTextColor(getResources().getColor(R.color.dark_blue));

                        dialog.dismiss();
                    }
                });
                break;
            case Constant.TEXT_CITY_LIST:
                tv_dialog_list.setText(type);

                lv_Adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, array_city);
                lv_dialog_list.setAdapter(lv_Adapter);
                lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        binding.tvCity.setText(array_city.get(position).getName());
                        SELECTED_CITY_ID = array_city.get(position).getId();

                        binding.tvCity.setTextColor(getResources().getColor(R.color.dark_blue));

                        dialog.dismiss();
                    }
                });
                break;
            case Constant.TEXT_CONDITION:
                tv_dialog_list.setText(type);

                ArrayList<String> list = new ArrayList<>();
                list.add(getString(R.string.used));
                list.add(getString(R.string.news));

                lv_Adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, list);
                lv_dialog_list.setAdapter(lv_Adapter);
                lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        binding.tvCondition.setText(list.get(position));

                        if(list.get(position).equals(getString(R.string.used))){
                            SELECTED_CONDITION = CONDITION_USED;
                            binding.tvCondition.setTextColor(getResources().getColor(R.color.dark_blue));
                        }else{
                            SELECTED_CONDITION = CONDITION_NEW;
                            binding.tvCondition.setTextColor(getResources().getColor(R.color.dark_blue));
                        }
                        dialog.dismiss();
                    }
                });
                break;
            case Constant.TEXT_TRANS:
                tv_dialog_list.setText(type);

                lv_Adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, array_trans);
                lv_dialog_list.setAdapter(lv_Adapter);
                lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        binding.tvTransmission.setText(array_trans.get(position).getName());
                        SELECTED_TRANS_ID = array_trans.get(position).getId();

                        binding.tvTransmission.setTextColor(getResources().getColor(R.color.dark_blue));

                        dialog.dismiss();
                    }
                });
                break;
        }
        dialog.show();
    }

    private void openDialogEquipment(){
        Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_equipment);
        dialog.setCancelable(false);

        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog_equip);
        EditText et_dialog = dialog.findViewById(R.id.et_dialog_equip);
        Button btn_ok = dialog.findViewById(R.id.btn_ok_equip);
        RecyclerView rv_dialog = dialog.findViewById(R.id.rv_dialog_equip);
        Button btn_back = dialog.findViewById(R.id.btn_cancel_equip);
        btn_back.setVisibility(View.GONE);

        tv_dialog.setText(Constant.TEXT_EQUIPMENT);

        ArrayList<EquipmentItem> arrayList_temp = new ArrayList<>();
        for(EquipmentItem item : array_equip){
            arrayList_temp.add(new EquipmentItem(item.getEquip_id(), item.getEquip_name(), item.isChecked()));
        }

        EquipmentAdapter adapter = new EquipmentAdapter(array_equip, getString(R.string.dialog), new EquipmentListener() {
            @Override
            public void onClick(int equip_id, boolean isChecked) {
                for (EquipmentItem item : arrayList_temp){
                    if(item.getEquip_id() == equip_id){
                        item.setChecked(isChecked);
                    }
                }
            }
        });
        rv_dialog.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rv_dialog.setHasFixedSize(true);
        rv_dialog.setAdapter(adapter);

        ArrayList<EquipmentItem> arrayList_search = new ArrayList<>();
        et_dialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                arrayList_search.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for(EquipmentItem item : arrayList_temp){
                    if(item.getEquip_name().toLowerCase().contains(s.toString().toLowerCase())){
                        arrayList_search.add(item);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.setAdapterData(arrayList_search);
                adapter.notifyDataSetChanged();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array_equip.clear();
                SELECTED_EQUIP_LIST.clear();
                array_equip.addAll(arrayList_temp);
                for(EquipmentItem item : array_equip){
                    if(item.isChecked()){
                        SELECTED_EQUIP_LIST.add(new EquipmentItem(item.getEquip_id(), item.getEquip_name(), item.isChecked()));
                    }
                }
                binding.tvEquipment.setText(SELECTED_EQUIP_LIST.size() +" equipments is selected");
                binding.tvEquipment.setTextColor(getResources().getColor(R.color.dark_blue));

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogText(String type){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(type.equals(Constant.TEXT_FUELCONSUMP)){
            dialog.setContentView(R.layout.layout_dialog_decimal);
        }else {
            dialog.setContentView(R.layout.layout_dialog_text);
        }

        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        Button btn_not_set = dialog.findViewById(R.id.btn_not_set);
        btn_not_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case Constant.TEXT_FUELCONSUMP:
                        SELECTED_FUELCONSUMP = NOT_SET;
                        binding.tvFuelconsump.setText("");
                        break;
                    case Constant.TEXT_DESCRIPTION:
                        SELECTED_DESCRIPTION = "";
                        binding.tvDescription.setText("");
                        break;
                }
                dialog.dismiss();
            }
        });
        EditText edt_dialog = dialog.findViewById(R.id.edt_dialog);
        tv_dialog.setText(type);

        switch (type){
            case Constant.TEXT_CAR_NAME:
                edt_dialog.setText(SELECTED_CARNAME);
                break;
            case Constant.TEXT_DESCRIPTION:
                edt_dialog.setText(SELECTED_DESCRIPTION);
                btn_not_set.setVisibility(View.VISIBLE);
                break;
            case Constant.TEXT_ADDRESS:
                edt_dialog.setText(SELECTED_ADDRESS);
                break;
            case Constant.TEXT_FUELCONSUMP:
                tv_dialog.setText(type + " (liters/100 km)");
                if (SELECTED_FUELCONSUMP != NOT_SET){
                    edt_dialog.setText(String.valueOf(SELECTED_FUELCONSUMP));
                }
                btn_not_set.setVisibility(View.VISIBLE);
                break;
        }


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case Constant.TEXT_CAR_NAME:
                        SELECTED_CARNAME = edt_dialog.getText().toString();
                        binding.tvCarName.setText(SELECTED_CARNAME);
                        binding.tvCarName.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_DESCRIPTION:
                        SELECTED_DESCRIPTION = edt_dialog.getText().toString();
                        binding.tvDescription.setText(SELECTED_DESCRIPTION);
                        binding.tvDescription.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_ADDRESS:
                        SELECTED_ADDRESS = edt_dialog.getText().toString();
                        binding.tvAddress.setText(SELECTED_ADDRESS);
                        binding.tvAddress.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_FUELCONSUMP:
                        if(!edt_dialog.getText().toString().equals("") && !edt_dialog.getText().toString().equals(".")){
                            SELECTED_FUELCONSUMP = Double.parseDouble(edt_dialog.getText().toString());
                            binding.tvFuelconsump.setText(String.valueOf(SELECTED_FUELCONSUMP) + " liters/100 km");
                            binding.tvFuelconsump.setTextColor(getResources().getColor(R.color.dark_blue));
                        }
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogPickImage(){
        Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_pick_image);
        dialog.setCancelable(false);

        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
        tv_dialog.setText("Images");
        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        Button btn_choose_img = dialog.findViewById(R.id.btn_choose_img);
        RecyclerView rv_dialog_image = dialog.findViewById(R.id.rv_dialog);
        rv_dialog_image.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        rv_dialog_image.setAdapter(pickImageAdapter);

        btn_choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setData(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGE_CODE);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvImages.setText(uriImageArrayList.size() + " images is selected");
                binding.tvImages.setTextColor(getResources().getColor(R.color.dark_blue));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode == PICK_IMAGE_CODE){
            if(resultCode == Activity.RESULT_OK){
                isChangeImage = true;
                uriImageArrayList.clear();
                if(data.getClipData() != null){
                    int count = data.getClipData().getItemCount();
                    for(int i = 0; i < count; i++){
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        uriImageArrayList.add(uri);
                        pickImageAdapter.notifyDataSetChanged();
                    }
                }else {
                    Uri uri = data.getData();
                    uriImageArrayList.add(uri);
                    pickImageAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void PostAd(){

        boolean cancel = false;
        View focusView = null;

        if(SELECTED_MANU_ID == NOT_SET){
            cancel = true;
            focusView = binding.cvManu;
            binding.tvManu.setText(getString(R.string.manufacturers) + " is not set");
            binding.tvManu.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_MODEL_ID == NOT_SET){
            cancel = true;
            focusView = binding.cvModel;
            binding.tvModel.setText(getString(R.string.model) + " is not set");
            binding.tvModel.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_CARNAME.equals("")){
            cancel = true;
            focusView = binding.cvCarName;
            binding.tvCarName.setText(getString(R.string.car_name) + " is not set");
            binding.tvCarName.setTextColor(getResources().getColor(R.color.red));
        }
        if(uriImageArrayList.isEmpty()){
            cancel = true;
            focusView = binding.cvImages;
            binding.tvImages.setText(getString(R.string.tv_images) + " is not set");
            binding.tvImages.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_PRICE == NOT_SET){
            cancel = true;
            focusView = binding.cvPrice;
            binding.tvPrice.setText(getString(R.string.price) + " is not set");
            binding.tvPrice.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_CITY_ID == NOT_SET){
            cancel = true;
            focusView = binding.cvCity;
            binding.tvCity.setText(getString(R.string.city) + " is not set");
            binding.tvCity.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_ADDRESS.equals("")){
            cancel = true;
            focusView = binding.cvAddress;
            binding.tvAddress.setText(getString(R.string.address) + " is not set");
            binding.tvAddress.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_POWER == NOT_SET){
            cancel = true;
            focusView = binding.cvPower;
            binding.tvPower.setText(getString(R.string.power) + " is not set");
            binding.tvPower.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_ENGINESIZE == NOT_SET){
            cancel = true;
            focusView = binding.cvEnginepower;
            binding.tvEnginepower.setText(getString(R.string.engine_power) + " is not set");
            binding.tvEnginepower.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_MILEAGE == NOT_SET){
            cancel = true;
            focusView = binding.cvMileage;
            binding.tvMileage.setText(getString(R.string.mileage) + " is not set");
            binding.tvMileage.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_BODY_TYPE_ID == NOT_SET){
            cancel = true;
            focusView = binding.cvBodytype;
            binding.tvBodytype.setText(getString(R.string.body_type) + " is not set");
            binding.tvBodytype.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_FUEL_TYPE_ID == NOT_SET){
            cancel = true;
            focusView = binding.cvFueltype;
            binding.tvFueltype.setText(getString(R.string.fuel_type) + " is not set");
            binding.tvFueltype.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_CONDITION == NOT_SET){
            cancel = true;
            focusView = binding.cvCondition;
            binding.tvCondition.setText(getString(R.string.condition) + " is not set");
            binding.tvCondition.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_YEAR == NOT_SET){
            cancel = true;
            focusView = binding.cvYear;
            binding.tvYear.setText(getString(R.string.year) + " is not set");
            binding.tvYear.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_TRANS_ID == NOT_SET){
            cancel = true;
            focusView = binding.cvTransmission;
            binding.tvTransmission.setText(getString(R.string.transmission) + " is not set");
            binding.tvTransmission.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_COLOR_ID == NOT_SET){
            cancel = true;
            focusView = binding.cvBodycolor;
            binding.tvBodycolor.setText(getString(R.string.body_color) + " is not set");
            binding.tvBodycolor.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_SEAT == NOT_SET){
            cancel = true;
            focusView = binding.cvSeat;
            binding.tvSeat.setText(getString(R.string.seat_number) + " is not set");
            binding.tvSeat.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_DOOR == NOT_SET){
            cancel = true;
            focusView = binding.cvDoor;
            binding.tvDoor.setText(getString(R.string.door_number) + " is not set");
            binding.tvDoor.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_EQUIP_LIST.isEmpty()){
            cancel = true;
            focusView = binding.cvEquipment;
            binding.tvEquipment.setText(getString(R.string.equipment) + " is not set");
            binding.tvEquipment.setTextColor(getResources().getColor(R.color.red));
        }
        if(SELECTED_PREUSER == NOT_SET){
            cancel = true;
            focusView = binding.cvPreuser;
            binding.tvPreuser.setText(getString(R.string.previous_users) + " is not set");
            binding.tvPreuser.setTextColor(getResources().getColor(R.color.red));
        }

        if(cancel){
            scrollToView(binding.scrollView, focusView);
        }else {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.TAG_MANU_ID, SELECTED_MANU_ID);
            bundle.putInt(Constant.TAG_MODEL_ID, SELECTED_MODEL_ID);
            bundle.putString(Constant.TAG_CAR_NAME, SELECTED_CARNAME);
            bundle.putInt(Constant.TAG_ADS_PRICE, SELECTED_PRICE);
            bundle.putInt(Constant.TAG_CITY_ID, SELECTED_CITY_ID);
            bundle.putString(Constant.TAG_ADS_LOCATION, SELECTED_ADDRESS);
            bundle.putInt(Constant.TAG_CAR_POWER, SELECTED_POWER);
            bundle.putInt(Constant.TAG_CAR_ENGINESIZE, SELECTED_ENGINESIZE);
            bundle.putInt(Constant.TAG_ADS_MILEAGE, SELECTED_MILEAGE);
            bundle.putInt(Constant.TAG_BODY_TYPE_ID, SELECTED_BODY_TYPE_ID);
            bundle.putInt(Constant.TAG_FUEL_TYPE_ID, SELECTED_FUEL_TYPE_ID);
            bundle.putInt(Constant.TAG_CAR_CONDITION, SELECTED_CONDITION);
            bundle.putInt(Constant.TAG_CAR_YEAR, SELECTED_YEAR);
            bundle.putInt(Constant.TAG_TRANS_ID, SELECTED_TRANS_ID);
            bundle.putInt(Constant.TAG_COLOR_ID, SELECTED_COLOR_ID);
            bundle.putInt(Constant.TAG_CAR_SEATS, SELECTED_SEAT);
            bundle.putInt(Constant.TAG_CAR_DOORS, SELECTED_DOOR);
            bundle.putInt(Constant.TAG_CAR_PREOWNER, SELECTED_PREUSER);
            bundle.putInt(Constant.TAG_CAR_GEARS, SELECTED_GEARS);
            bundle.putInt(Constant.TAG_CAR_CYLINDER, SELECTED_CYLINDER);
            bundle.putInt(Constant.TAG_CAR_KERBWEIGHT, SELECTED_WEIGHT);
            bundle.putDouble(Constant.TAG_CAR_FUELCONSUMP, SELECTED_FUELCONSUMP);
            bundle.putInt(Constant.TAG_CAR_CO2EMISSION, SELECTED_CO2EMISSION);
            bundle.putString(Constant.TAG_ADS_DESCRIPTION, SELECTED_DESCRIPTION);
            bundle.putSerializable(Constant.TAG_EQUIP, SELECTED_EQUIP_LIST);

            ArrayList<File> arrayList_file = new ArrayList<>();
            for(Uri uri : uriImageArrayList){
                String filePath = methods.getPathImage(uri);
                File file = new File(filePath);
                arrayList_file.add(file);
            }
            if(MODE == POST_MODE){
                AdsAction(bundle, Constant.METHOD_POST_ADS ,arrayList_file);
            }else {
                bundle.putBoolean("is_change_image", isChangeImage);
                bundle.putInt(Constant.TAG_ADS_ID, EDIT_ADS.getAds_id());
                bundle.putInt(Constant.TAG_CAR_ID, EDIT_ADS.getCar_id());
                AdsAction(bundle, Constant.METHOD_EDIT_SELLING, arrayList_file);
            }


        }
    }

    private void scrollToView(final NestedScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop() - 300;
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    private void AdsAction(Bundle bundle, String method , ArrayList<File> arrayList_file){
        PostAdsAsync postAdsAsync = new PostAdsAsync(new PostAdsListener() {
            @Override
            public void onStart() {
                binding.rlScrollView.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(String success) {
                if(success.equals(Constant.SUCCESS)){
                    getFragmentManager().popBackStack();
                    reloadFragmentListener.reload();
                }else{
                    Toast.makeText(getContext(), Constant.ERROR_CON_SERVER, Toast.LENGTH_SHORT).show();
                }
                binding.rlScrollView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        }, methods.getAPIRequest(method, bundle, arrayList_file));

        postAdsAsync.execute();
    }

}