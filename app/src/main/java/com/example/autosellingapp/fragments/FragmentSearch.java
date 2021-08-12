package com.example.autosellingapp.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.adapters.ColorListAdapter;
import com.example.autosellingapp.adapters.EquipmentAdapter;
import com.example.autosellingapp.asynctasks.LoadSearch;
import com.example.autosellingapp.interfaces.ColorListener;
import com.example.autosellingapp.interfaces.EquipmentListener;
import com.example.autosellingapp.interfaces.LoadSearchListener;
import com.example.autosellingapp.items.ColorItem;
import com.example.autosellingapp.items.EquipmentItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;

import java.util.ArrayList;

public class FragmentSearch extends Fragment {

    private View rootView;
    private Methods methods;
    private String errorMsg;
    private RelativeLayout scrollView;
    private RecyclerView rv_dialog_list_color;
    private ProgressBar progressBar;
    private LinearLayout ll_empty;
    private TextView tv_empty, tv_dialog_list, tv_dialog_list_color, tv_dialog_number;
    private Button btn_try, btn_not_set, btn_ok;
    private NumberPicker numberPicker;
    private ArrayList<ManufacturerItem> array_manu;
    private ArrayList<ModelItem> array_model;
    private ArrayList<MyItem> array_city;
    private ArrayList<MyItem> array_bodytype;
    private ArrayList<MyItem> array_fueltype;
    private ArrayList<MyItem> array_trans;
    private ArrayList<EquipmentItem> array_equip;
    private ArrayList<ColorItem> array_color;
    private ListView lv_dialog_list;
    private CardView cv_manu, cv_model, cv_price, cv_power, cv_mileage,
                    cv_bodyType, cv_fuelType, cv_condition, cv_year, cv_transmission, cv_bodyColor, cv_seatNumber, cv_doorNumber,
                    cv_equipment, cv_previousUser, cv_city;
    private TextView tv_manu, tv_model, tv_price, tv_power, tv_mileage,
            tv_bodyType, tv_fuelType, tv_condition, tv_year, tv_transmission, tv_bodyColor, tv_seatNumber, tv_doorNumber,
            tv_equipment, tv_previousUser, tv_city;
    private Button btn_search;

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

    private FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        Hook();
        methods = new Methods(getContext());
        array_manu = new ArrayList<>();
        array_model= new ArrayList<>();
        array_city= new ArrayList<>();
        array_bodytype= new ArrayList<>();
        array_fueltype= new ArrayList<>();
        array_trans = new ArrayList<>();
        array_color = new ArrayList<>();
        array_equip = new ArrayList<>();
        SELECTED_EQUIP_LIST = new ArrayList<>();

        LoadSearch();

        return rootView;
    }

    private void Hook(){
        scrollView = rootView.findViewById(R.id.scroll_view_search);
        progressBar = rootView.findViewById(R.id.progressBar_search);
        ll_empty = rootView.findViewById(R.id.ll_empty);
        tv_empty = rootView.findViewById(R.id.tv_empty);
        btn_try = rootView.findViewById(R.id.btn_try);

        cv_manu = rootView.findViewById(R.id.cv_search_manu);
        cv_manu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_MANU_LIST);
            }
        });
        cv_model = rootView.findViewById(R.id.cv_search_model);
        cv_model.setEnabled(false);
        cv_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_MODEL_LIST);
            }
        });

        cv_price = rootView.findViewById(R.id.cv_search_price);
        cv_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog2Number(Constant.TEXT_PRICE);
            }
        });
        cv_power = rootView.findViewById(R.id.cv_search_power);
        cv_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog2Number(Constant.TEXT_POWER);
            }
        });
        cv_mileage = rootView.findViewById(R.id.cv_search_mileage);
        cv_mileage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog2Number(Constant.TEXT_MILEAGE);
            }
        });
        cv_bodyType = rootView.findViewById(R.id.cv_search_bodytype);
        cv_bodyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_BODY_TYPE_LIST);
            }
        });
        cv_fuelType = rootView.findViewById(R.id.cv_search_fueltype);
        cv_fuelType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_FUEL_TYPE_LIST);
            }
        });
        cv_condition = rootView.findViewById(R.id.cv_search_condition);
        cv_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_CONDITION);
            }
        });
        cv_year = rootView.findViewById(R.id.cv_search_year);
        cv_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_YEAR);
            }
        });
        cv_transmission = rootView.findViewById(R.id.cv_search_transmission);
        cv_transmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_TRANS);
            }
        });
        cv_bodyColor = rootView.findViewById(R.id.cv_search_bodycolor);
        cv_bodyColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogColor();
            }
        });
        cv_seatNumber = rootView.findViewById(R.id.cv_search_seat);
        cv_seatNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_SEAT);
            }
        });
        cv_doorNumber = rootView.findViewById(R.id.cv_search_door);
        cv_doorNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_DOOR);
            }
        });
        cv_equipment = rootView.findViewById(R.id.cv_search_equipment);
        cv_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEquipment();
            }
        });
        cv_previousUser = rootView.findViewById(R.id.cv_search_preuser);
        cv_previousUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNumber(Constant.TEXT_PREUSER);
            }
        });
        cv_city = rootView.findViewById(R.id.cv_search_city);
        cv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogList(Constant.TEXT_CITY_LIST);
            }
        });

        tv_manu = rootView.findViewById(R.id.tv_search_manu);
        tv_model = rootView.findViewById(R.id.tv_search_model);
        tv_price = rootView.findViewById(R.id.tv_search_price);
        tv_power = rootView.findViewById(R.id.tv_search_power);
        tv_mileage = rootView.findViewById(R.id.tv_search_mileage);
        tv_bodyType = rootView.findViewById(R.id.tv_search_bodytype);
        tv_fuelType = rootView.findViewById(R.id.tv_search_fueltype);
        tv_condition = rootView.findViewById(R.id.tv_search_condition);
        tv_year = rootView.findViewById(R.id.tv_search_year);
        tv_transmission = rootView.findViewById(R.id.tv_search_transmission);
        tv_bodyColor = rootView.findViewById(R.id.tv_search_bodycolor);
        tv_seatNumber = rootView.findViewById(R.id.tv_search_seat);
        tv_doorNumber = rootView.findViewById(R.id.tv_search_door);
        tv_equipment = rootView.findViewById(R.id.tv_search_equipment);
        tv_previousUser = rootView.findViewById(R.id.tv_search_preuser);
        tv_city = rootView.findViewById(R.id.tv_search_city);

        btn_search = rootView.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCatFragment();
            }
        });
    }

    private void LoadSearch(){
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

                            array_manu.add(new ManufacturerItem(NOT_SET, getString(R.string.not_set), null));
                            array_model.add(new ModelItem(NOT_SET, NOT_SET, getString(R.string.not_set)));
                            array_city.add(new MyItem(NOT_SET, getString(R.string.not_set)));
                            array_bodytype.add(new MyItem(NOT_SET, getString(R.string.not_set)));
                            array_fueltype.add(new MyItem(NOT_SET, getString(R.string.not_set)));
                            array_trans.add(new MyItem(NOT_SET, getString(R.string.not_set)));
                            array_color.add(new ColorItem(NOT_SET, getString(R.string.not_set), null));

                            array_manu.addAll(arrayList_manu);
                            array_model.addAll(arrayList_model);
                            array_city.addAll(arrayList_city);
                            array_bodytype.addAll(arrayList_bodytype);
                            array_fueltype.addAll(arrayList_fueltype);
                            array_trans.addAll(arrayList_trans);
                            array_color.addAll(arrayList_color);
                            array_equip.addAll(arrayList_equip);

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
                    LoadSearch();
                }
            });
            ll_empty.setVisibility(View.VISIBLE);
        }else{
            scrollView.setVisibility(View.VISIBLE);
//            if(array_manu.size() != 0 && array_model.size() != 0 && array_city.size() != 0 && array_bodytype.size() != 0 && array_fueltype.size() != 0) {
//
//            }
        }
    }

    private void openDialogList(String type){
        Dialog dialog = new Dialog(this.getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_dialog_list);


        lv_dialog_list = dialog.findViewById(R.id.lv_dialog_list);
        tv_dialog_list = dialog.findViewById(R.id.tv_dialog_list);



        ArrayAdapter lv_Adapter;

        switch (type){
            case Constant.TEXT_MANU_LIST:
                tv_dialog_list.setText(type);

                lv_Adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, array_manu);
                lv_dialog_list.setAdapter(lv_Adapter);
                lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tv_manu.setText(array_manu.get(position).getManu_name());
                        SELECTED_MANU_ID = array_manu.get(position).getManu_id();

                        if(SELECTED_MANU_ID == NOT_SET){
                            cv_model.setEnabled(false);
                            tv_manu.setTextColor(getResources().getColor(R.color.default_text));
                        }else {
                            cv_model.setEnabled(true);
                            tv_manu.setTextColor(getResources().getColor(R.color.dark_blue));
                        }

                        SELECTED_MODEL_ID = NOT_SET;
                        tv_model.setText(getString(R.string.not_set));
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
                        if(item.getManu_id() == NOT_SET || item.getManu_id() == SELECTED_MANU_ID){
                            arrayList.add(item);
                        }
                    }
                    lv_Adapter.addAll(arrayList);
                    lv_dialog_list.setAdapter(lv_Adapter);
                    lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            tv_model.setText(arrayList.get(position).getModel_name());
                            SELECTED_MODEL_ID = arrayList.get(position).getModel_id();

                            if(SELECTED_MODEL_ID == NOT_SET){
                                tv_model.setTextColor(getResources().getColor(R.color.default_text));
                            }else{
                                tv_model.setTextColor(getResources().getColor(R.color.dark_blue));
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
                        tv_bodyType.setText(array_bodytype.get(position).getName());
                        SELECTED_BODY_TYPE_ID = array_bodytype.get(position).getId();

                        if(SELECTED_BODY_TYPE_ID == NOT_SET){
                            tv_bodyType.setTextColor(getResources().getColor(R.color.default_text));
                        }else{
                            tv_bodyType.setTextColor(getResources().getColor(R.color.dark_blue));
                        }

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
                        tv_fuelType.setText(array_fueltype.get(position).getName());
                        SELECTED_FUEL_TYPE_ID = array_fueltype.get(position).getId();

                        if(SELECTED_FUEL_TYPE_ID == NOT_SET){
                            tv_fuelType.setTextColor(getResources().getColor(R.color.default_text));
                        }else{
                            tv_fuelType.setTextColor(getResources().getColor(R.color.dark_blue));
                        }

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
                        tv_city.setText(array_city.get(position).getName());
                        SELECTED_CITY_ID = array_city.get(position).getId();

                        if(SELECTED_CITY_ID == NOT_SET){
                            tv_city.setTextColor(getResources().getColor(R.color.default_text));
                        }else{
                            tv_city.setTextColor(getResources().getColor(R.color.dark_blue));
                        }

                        dialog.dismiss();
                    }
                });
                break;
            case Constant.TEXT_CONDITION:
                tv_dialog_list.setText(type);

                ArrayList<String> list = new ArrayList<>();
                list.add(getString(R.string.not_set));
                list.add(getString(R.string.used));
                list.add(getString(R.string.news));

                lv_Adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, list);
                lv_dialog_list.setAdapter(lv_Adapter);
                lv_dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tv_condition.setText(list.get(position));

                        if(list.get(position).equals(getString(R.string.not_set))){
                            SELECTED_CONDITION = NOT_SET;
                            tv_condition.setTextColor(getResources().getColor(R.color.default_text));
                        }else if(list.get(position).equals(getString(R.string.used))){
                            SELECTED_CONDITION = CONDITION_USED;
                            tv_condition.setTextColor(getResources().getColor(R.color.dark_blue));
                        }else{
                            SELECTED_CONDITION = CONDITION_NEW;
                            tv_condition.setTextColor(getResources().getColor(R.color.dark_blue));
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
                        tv_transmission.setText(array_trans.get(position).getName());
                        SELECTED_TRANS_ID = array_trans.get(position).getId();

                        if(SELECTED_TRANS_ID == NOT_SET){
                            tv_transmission.setTextColor(getResources().getColor(R.color.default_text));
                        }else{
                            tv_transmission.setTextColor(getResources().getColor(R.color.dark_blue));
                        }

                        dialog.dismiss();
                    }
                });
                break;
        }
        dialog.show();
    }

    private void openDialogColor(){
        Dialog dialog1 = new Dialog(this.getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.layout_dialog_list_color);

        tv_dialog_list_color = dialog1.findViewById(R.id.tv_dialog_list_color);
        rv_dialog_list_color = dialog1.findViewById(R.id.rv_dialog_list_color);


        tv_dialog_list_color.setText(Constant.TEXT_COLOR);

        ColorListAdapter adapter = new ColorListAdapter(array_color, new ColorListener() {
            @Override
            public void onClick(int color_id, String color_name) {
                tv_bodyColor.setText(color_name);
                SELECTED_COLOR = color_id;
                if(SELECTED_COLOR == NOT_SET){
                    tv_bodyColor.setTextColor(getResources().getColor(R.color.default_text));
                }else{
                    tv_bodyColor.setTextColor(getResources().getColor(R.color.dark_blue));
                }

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

        numberPicker = dialog.findViewById(R.id.num_picker);
        tv_dialog_number = dialog.findViewById(R.id.tv_dialog_number);
        btn_not_set = dialog.findViewById(R.id.btn_not_set);
        btn_ok = dialog.findViewById(R.id.btn_ok);

        switch (type){
            case Constant.TEXT_YEAR:
                numberPicker.setMinValue(2000);
                numberPicker.setMaxValue(2025);
                break;
            case Constant.TEXT_SEAT:
                numberPicker.setMinValue(1);
                numberPicker.setMaxValue(12);
                break;
            case Constant.TEXT_DOOR:
                numberPicker.setMinValue(2);
                numberPicker.setMaxValue(7);
                break;
            case Constant.TEXT_PREUSER:
                numberPicker.setMinValue(2);
                numberPicker.setMaxValue(10);
                break;

        }

        tv_dialog_number.setText(type);

        btn_not_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case Constant.TEXT_YEAR:
                        SELECTED_YEAR = NOT_SET;
                        tv_year.setText(getString(R.string.not_set));
                        tv_year.setTextColor(getResources().getColor(R.color.default_text));
                        break;
                    case Constant.TEXT_SEAT:
                        SELECTED_SEAT = NOT_SET;
                        tv_seatNumber.setText(getString(R.string.not_set));
                        tv_seatNumber.setTextColor(getResources().getColor(R.color.default_text));
                        break;
                    case Constant.TEXT_DOOR:
                        SELECTED_DOOR = NOT_SET;
                        tv_doorNumber.setText(getString(R.string.not_set));
                        tv_doorNumber.setTextColor(getResources().getColor(R.color.default_text));
                        break;
                    case Constant.TEXT_PREUSER:
                        SELECTED_PREUSER = NOT_SET;
                        tv_previousUser.setText(getString(R.string.not_set));
                        tv_previousUser.setTextColor(getResources().getColor(R.color.default_text));
                        break;

                }
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case Constant.TEXT_YEAR:
                        SELECTED_YEAR = numberPicker.getValue();
                        tv_year.setText(String.valueOf(SELECTED_YEAR));
                        tv_year.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_SEAT:
                        SELECTED_SEAT = numberPicker.getValue();
                        tv_seatNumber.setText(String.valueOf(SELECTED_SEAT));
                        tv_seatNumber.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_DOOR:
                        SELECTED_DOOR = numberPicker.getValue();
                        tv_doorNumber.setText(String.valueOf(SELECTED_DOOR));
                        tv_doorNumber.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_PREUSER:
                        SELECTED_PREUSER = numberPicker.getValue();
                        tv_previousUser.setText("< "+String.valueOf(SELECTED_PREUSER));
                        tv_previousUser.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialog2Number(String type){
        Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_2_number);

        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog_2);
        NumberPicker numberPicker_min = dialog.findViewById(R.id.num_picker_min);
        NumberPicker numberPicker_max = dialog.findViewById(R.id.num_picker_max);
        Button btn_not_set = dialog.findViewById(R.id.btn_not_set_2);
        Button btn_ok = dialog.findViewById(R.id.btn_ok_2);

        numberPicker_min.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal > numberPicker_max.getValue()){
                    numberPicker_max.setValue(newVal);
                }
            }
        });
        numberPicker_max.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal < numberPicker_min.getValue()){
                    numberPicker_min.setValue(newVal);
                }
            }
        });

        tv_dialog.setText(type);

        switch (type){
            case Constant.TEXT_PRICE:
                numberPicker_min.setMinValue(500);
                numberPicker_min.setMaxValue(100000);
                numberPicker_min.setWrapSelectorWheel(true);

                numberPicker_max.setMinValue(500);
                numberPicker_max.setMaxValue(100000);
                numberPicker_max.setWrapSelectorWheel(true);
                break;
            case Constant.TEXT_POWER:
                numberPicker_min.setMinValue(50);
                numberPicker_min.setMaxValue(400);
                numberPicker_min.setWrapSelectorWheel(true);

                numberPicker_max.setMinValue(50);
                numberPicker_max.setMaxValue(400);
                numberPicker_max.setWrapSelectorWheel(true);
                break;
            case Constant.TEXT_MILEAGE:
                numberPicker_min.setMinValue(5000);
                numberPicker_min.setMaxValue(300000);
                numberPicker_min.setWrapSelectorWheel(true);

                numberPicker_max.setMinValue(5000);
                numberPicker_max.setMaxValue(300000);
                numberPicker_max.setWrapSelectorWheel(true);
                break;
        }

        btn_not_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case Constant.TEXT_PRICE:
                        SELECTED_PRICE_MIN = NOT_SET;
                        SELECTED_PRICE_MAX = NOT_SET;
                        tv_price.setText(getString(R.string.not_set));
                        tv_price.setTextColor(getResources().getColor(R.color.default_text));
                        break;
                    case Constant.TEXT_POWER:
                        SELECTED_POWER_MIN = NOT_SET;
                        SELECTED_POWER_MAX = NOT_SET;
                        tv_power.setText(getString(R.string.not_set));
                        tv_power.setTextColor(getResources().getColor(R.color.default_text));
                        break;
                    case Constant.TEXT_MILEAGE:
                        SELECTED_MILEAGE_MIN = NOT_SET;
                        SELECTED_MILEAGE_MAX = NOT_SET;
                        tv_mileage.setText(getString(R.string.not_set));
                        tv_mileage.setTextColor(getResources().getColor(R.color.default_text));
                        break;
                }
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case Constant.TEXT_PRICE:
                        SELECTED_PRICE_MIN = numberPicker_min.getValue();
                        SELECTED_PRICE_MAX = numberPicker_max.getValue();
                        tv_price.setText(String.valueOf(SELECTED_PRICE_MIN+" - "+SELECTED_PRICE_MAX));
                        tv_price.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_POWER:
                        SELECTED_POWER_MIN = numberPicker_min.getValue();
                        SELECTED_POWER_MAX = numberPicker_max.getValue();
                        tv_power.setText(String.valueOf(SELECTED_POWER_MIN+" - "+SELECTED_POWER_MAX));
                        tv_power.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                    case Constant.TEXT_MILEAGE:
                        SELECTED_MILEAGE_MIN = numberPicker_min.getValue();
                        SELECTED_MILEAGE_MAX = numberPicker_max.getValue();
                        tv_mileage.setText(String.valueOf(SELECTED_MILEAGE_MIN+" - "+SELECTED_MILEAGE_MAX));
                        tv_mileage.setTextColor(getResources().getColor(R.color.dark_blue));
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogEquipment(){
        Dialog dialog = new Dialog(this.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_equipment);

        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog_equip);
        EditText et_dialog = dialog.findViewById(R.id.et_dialog_equip);
        Button btn_ok = dialog.findViewById(R.id.btn_ok_equip);
        RecyclerView rv_dialog = dialog.findViewById(R.id.rv_dialog_equip);
        Button btn_not_set_equip = dialog.findViewById(R.id.btn_cancel_equip);

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
                if(SELECTED_EQUIP_LIST.size() == 0){
                    tv_equipment.setText(getString(R.string.not_set));
                    tv_equipment.setTextColor(getResources().getColor(R.color.default_text));
                }else{
                    tv_equipment.setText(SELECTED_EQUIP_LIST.size() +" equipments is selected");
                    tv_equipment.setTextColor(getResources().getColor(R.color.dark_blue));
                }

                dialog.dismiss();
            }
        });

        btn_not_set_equip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED_EQUIP_LIST.clear();
                for (EquipmentItem item : array_equip){
                    item.setChecked(false);
                }
                tv_equipment.setText(getString(R.string.not_set));
                tv_equipment.setTextColor(getResources().getColor(R.color.default_text));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openCatFragment(){
//        String equip = "";
//        for(EquipmentItem item : SELECTED_EQUIP_LIST){
//            equip += item.getEquip_name() + " ";
//        }
//
//        String text = "Manufacturer: " + SELECTED_MANU_ID + "\n" +
//                "Model: " +SELECTED_MODEL_ID + "\n"+
//                "Price: " +SELECTED_PRICE_MIN + " - "+ SELECTED_PRICE_MAX + "\n"+
//                "Power: " +SELECTED_POWER_MIN + " - "+ SELECTED_POWER_MAX + "\n"+
//                "Mileage: " +SELECTED_MILEAGE_MIN + " - "+ SELECTED_MILEAGE_MAX + "\n"+
//                "Body type: " +SELECTED_BODY_TYPE_ID + "\n"+
//                "Fuel type: " +SELECTED_FUEL_TYPE_ID + "\n"+
//                "Year: " +SELECTED_YEAR + "\n"+
//                "Transmission: " +SELECTED_TRANS_ID + "\n"+
//                "Condition: " +SELECTED_CONDITION + "\n"+
//                "Color: " +SELECTED_COLOR + "\n"+
//                "City: " +SELECTED_CITY_ID + "\n"+
//                "Seat: " +SELECTED_SEAT + "\n"+
//                "Door: " +SELECTED_DOOR + "\n"+
//                "Pre users: " +SELECTED_PREUSER + "\n"+
//                "City: " +equip+ "\n";
//        Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.manufacturers), SELECTED_MANU_ID);
        bundle.putInt(getString(R.string.model), SELECTED_MODEL_ID);
        bundle.putInt(getString(R.string.price_min), SELECTED_PRICE_MIN);
        bundle.putInt(getString(R.string.price_max), SELECTED_PRICE_MAX);
        bundle.putInt(getString(R.string.power_min), SELECTED_POWER_MIN);
        bundle.putInt(getString(R.string.power_max), SELECTED_POWER_MAX);
        bundle.putInt(getString(R.string.mileage_min), SELECTED_MILEAGE_MIN);
        bundle.putInt(getString(R.string.mileage_max), SELECTED_MILEAGE_MAX);
        bundle.putInt(getString(R.string.body_type), SELECTED_BODY_TYPE_ID);
        bundle.putInt(getString(R.string.fuel_type), SELECTED_FUEL_TYPE_ID);
        bundle.putInt(getString(R.string.year), SELECTED_YEAR);
        bundle.putInt(getString(R.string.transmission), SELECTED_TRANS_ID);
        bundle.putInt(getString(R.string.condition), SELECTED_CONDITION);
        bundle.putInt(getString(R.string.body_color), SELECTED_COLOR);
        bundle.putInt(getString(R.string.city), SELECTED_CITY_ID);
        bundle.putInt(getString(R.string.seat_number), SELECTED_SEAT);
        bundle.putInt(getString(R.string.door_number), SELECTED_DOOR);
        bundle.putInt(getString(R.string.previous_users), SELECTED_PREUSER);
        bundle.putSerializable(getString(R.string.equipment), SELECTED_EQUIP_LIST);
        FragmentCategory fragment = new FragmentCategory();
        fragment.setArguments(bundle);
        ReplaceFragment(fragment, getString(R.string.frag_category));
    }

    public void ReplaceFragment(Fragment fragment, String name){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, fragment, name);
        ft.addToBackStack(name);
        ft.commit();
    }
}