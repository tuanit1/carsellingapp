package com.example.autosellingapp.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.autosellingapp.R;
import com.example.autosellingapp.adapters.ManufacturerAdapter;
import com.example.autosellingapp.asynctasks.LoadManufacturers;
import com.example.autosellingapp.interfaces.LoadManuListener;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private View rootView;
    private ManufacturerAdapter manufacturerAdapter;
    private ArrayList<ManufacturerItem> arrayList_manu;
    private RecyclerView rv_manu;
    private CardView cv_search;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private Methods methods;
    private LinearLayout ll_empty_home, ll_manu;
    private String errorMsg;
    private Button btn_try;
    private TextView tv_empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Hook();


        arrayList_manu = new ArrayList<>();
        methods = new Methods(getContext());
        rv_manu.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rv_manu.setHasFixedSize(true);

        LoadManufacturers();

        return rootView;
    }

    private void Hook(){
        rv_manu = rootView.findViewById(R.id.rv_manu);
        cv_search= rootView.findViewById(R.id.cv_search);
        scrollView = rootView.findViewById(R.id.home_scroll_view);
        progressBar = rootView.findViewById(R.id.progressBar_home);
        ll_empty_home = rootView.findViewById(R.id.ll_empty_home);
        ll_manu = rootView.findViewById(R.id.ll_manu);
        btn_try = rootView.findViewById(R.id.btn_try_home);
        tv_empty = rootView.findViewById(R.id.tv_empty_home);
    }

    private void LoadManufacturers(){
        if(methods.isNetworkAvailable()){
            LoadManufacturers loadManufacturers = new LoadManufacturers(new LoadManuListener() {
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
                            manufacturerAdapter = new ManufacturerAdapter(arrayList_manu);
                            rv_manu.setAdapter(manufacturerAdapter);
                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        progressBar.setVisibility(View.GONE);
                        setEmpty();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_MANUFACTURER));
            loadManufacturers.execute();
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
}