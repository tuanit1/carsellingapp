package com.example.autosellingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.adapters.AdsAdapter;
import com.example.autosellingapp.adapters.ManufacturerAdapter;
import com.example.autosellingapp.asynctasks.LoadHome;
import com.example.autosellingapp.asynctasks.LoadRecent;
import com.example.autosellingapp.interfaces.AdsDetailListener;
import com.example.autosellingapp.interfaces.LoadCategoryListener;
import com.example.autosellingapp.interfaces.LoadManuListener;
import com.example.autosellingapp.interfaces.ReloadFragmentListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.items.ManufacturerItem;
import com.example.autosellingapp.items.ModelItem;
import com.example.autosellingapp.items.MyItem;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.example.autosellingapp.utils.SharedPref;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FragmentHome extends Fragment {

    private View rootView;
    private ManufacturerAdapter manufacturerAdapter;
    private ArrayList<ManufacturerItem> arrayList_manu;
    private RecyclerView rv_manu, rv_recent;
    private CardView cv_search;
    private NestedScrollView scrollView;
    private ProgressBar progressBar;
    private Methods methods;
    private LinearLayout ll_empty_home, ll_manu, ll_recent;
    private String errorMsg;
    private Button btn_try;
    private TextView tv_empty;
    private FragmentTransaction ft;

    private SharedPref sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Hook();

        sharedPref = new SharedPref(getContext());

        arrayList_manu = new ArrayList<>();
        methods = new Methods(getContext());
        rv_manu.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rv_manu.setHasFixedSize(true);

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
        btn_try = rootView.findViewById(R.id.btn_try_home);
        tv_empty = rootView.findViewById(R.id.tv_empty_home);
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
                            manufacturerAdapter = new ManufacturerAdapter(arrayList_manu);
                            rv_manu.setAdapter(manufacturerAdapter);
                            if(methods.isLogged()){
                                setRecent();
                            }
                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        progressBar.setVisibility(View.GONE);
                        setEmpty();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_HOME, null, null));
            loadHome.execute();
        }else{
            errorMsg = getString(R.string.internet_not_connect);
            setEmpty();
        }
    }

    private void setRecent() {
        LoadRecent loadRecent = new LoadRecent(new LoadCategoryListener() {
            @Override
            public void onStart() {
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

                            Collections.reverse(adsItemArrayList);

                            AdsAdapter adsAdapter = new AdsAdapter(methods, adsItemArrayList, carItemArrayList, userItemArrayList, cityItemArrayList, new AdsDetailListener() {
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
        }, methods.getAPIRequest(Constant.METHOD_RECENT, null, null));

        loadRecent.execute();
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

    public void ReplaceFragment(Fragment fragment, String name){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, fragment, name);
        ft.addToBackStack(name);
        ft.commit();
    }
}