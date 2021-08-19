package com.example.autosellingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.activity.ActivityLogin;
import com.example.autosellingapp.adapters.MySellingAdapter;
import com.example.autosellingapp.asynctasks.LoadSelling;
import com.example.autosellingapp.asynctasks.PostAdsAsync;
import com.example.autosellingapp.databinding.FragmentSellingBinding;
import com.example.autosellingapp.interfaces.EditDeleteSellingListener;
import com.example.autosellingapp.interfaces.LoadSellingListener;
import com.example.autosellingapp.interfaces.PostAdsListener;
import com.example.autosellingapp.interfaces.ReloadFragmentListener;
import com.example.autosellingapp.items.AdsItem;
import com.example.autosellingapp.items.CarItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;

import java.util.ArrayList;


public class FragmentSelling extends Fragment {

    private FragmentSellingBinding binding;
    private FragmentTransaction ft;
    private Methods methods;
    private String errorMsg;
    private ArrayList<AdsItem> arrayList_ads;
    private ArrayList<CarItem> arrayList_car;
    private ReloadFragmentListener reload_listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSellingBinding.inflate(inflater, container, false);

        methods = new Methods(getContext());

        arrayList_ads = new ArrayList<>();
        arrayList_car = new ArrayList<>();

        reload_listener = new ReloadFragmentListener() {
            @Override
            public void reload() {
                LoadData();
            }
        };

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplaceFragment(new FragmentPostAds(reload_listener), getString(R.string.frag_postads));
            }
        });

        if(methods.isLogged()){
            LoadData();
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

    private void LoadData(){

        Bundle bundle = new Bundle();
        bundle.putString(Constant.TAG_UID, Constant.UID);

        if(methods.isNetworkAvailable()){
            LoadSelling loadSelling = new LoadSelling(new LoadSellingListener() {
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
                public void onEnd(String success, ArrayList<AdsItem> adsItemArrayList, ArrayList<CarItem> carItemArrayList) {
                    if(getActivity() != null){
                        if(success.equals("1")){
                            arrayList_ads.clear();
                            arrayList_car.clear();

                            arrayList_car.addAll(carItemArrayList);
                            arrayList_ads.addAll(adsItemArrayList);

                        }else{
                            errorMsg = getString(R.string.error_connect_server);
                        }
                        binding.progressBar.setVisibility(View.GONE);
                        setEmpty();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_SELLING, bundle, null));
            loadSelling.execute();
        }else {
            errorMsg = getString(R.string.internet_not_connect);
            setEmpty();
        }
    }

    private void setEmpty(){

        if(!errorMsg.equals(getString(R.string.internet_not_connect))){

            if(errorMsg.equals(getString(R.string.error_connect_server))){
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.tvEmpty.setText(errorMsg);
                binding.btnTry.setText("TRY AGAIN");
                binding.btnTry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadData();
                    }
                });
            }else {
                if(arrayList_ads.isEmpty()){
                    binding.llEmpty.setVisibility(View.VISIBLE);
                    binding.tvEmpty.setText(getString(R.string.array_selling_empty));
                    binding.btnTry.setText("CREATE YOUR ADS");
                    binding.btnTry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ReplaceFragment(new FragmentPostAds(reload_listener), getString(R.string.frag_postads));
                        }
                    });
                }else{
                    binding.rlScrollView.setVisibility(View.VISIBLE);

                    EditDeleteSellingListener listener = new EditDeleteSellingListener() {
                        @Override
                        public void onEdit(AdsItem ads) {
                            EditSelling(ads);
                        }

                        @Override
                        public void onDelete(AdsItem ads) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.TAG_ADS_ID, ads.getAds_id());
                            bundle.putInt(Constant.TAG_CAR_ID, ads.getCar_id());
                            SellingAction(Constant.METHOD_DELETE_SELLING, bundle);
                        }

                        @Override
                        public void onMark(AdsItem ads) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.TAG_ADS_ID, ads.getAds_id());
                            int isAvailable = (ads.isAds_isAvailable()) ? 0 : 1;
                            bundle.putInt(Constant.TAG_ADS_ISAVAILABLE, isAvailable);
                            SellingAction(Constant.METHOD_MARK_AVAILABLE, bundle);
                        }
                    };

                    MySellingAdapter adapter = new MySellingAdapter(methods, arrayList_ads, arrayList_car, listener);
                    binding.rvMyAds.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    binding.rvMyAds.setAdapter(adapter);
                }

            }
        }else{
            binding.llEmpty.setVisibility(View.VISIBLE);
            binding.tvEmpty.setText(errorMsg);
            binding.btnTry.setText("TRY AGAIN");
            binding.btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadData();
                }
            });
        }
    }

    public void ReplaceFragment(Fragment fragment, String name){
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main_content, fragment, name);
        ft.addToBackStack(name);
        ft.commit();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(name);
    }

    private void openLoginActivity(){
        Intent intent = new Intent(getContext(), ActivityLogin.class);
        startActivity(intent);
    }

    private void EditSelling(AdsItem ads){
        FragmentPostAds fragment = new FragmentPostAds(reload_listener);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TAG_ADS, ads);
        bundle.putSerializable(Constant.TAG_CAR, methods.getCarItemByID(arrayList_car, ads.getCar_id()));
        fragment.setArguments(bundle);
        ReplaceFragment(fragment, "Edit Selling");
    }

    private void SellingAction(String type, Bundle bundle){

        PostAdsAsync postAdsAsync = new PostAdsAsync(new PostAdsListener() {
            @Override
            public void onStart() {
                binding.rlScrollView.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(String success) {
                if(success.equals(Constant.SUCCESS)){
                    Toast.makeText(getContext(), Constant.SUCCESS, Toast.LENGTH_SHORT).show();
                    LoadData();
                }else{
                    Toast.makeText(getContext(), Constant.ERROR_CON_SERVER, Toast.LENGTH_SHORT).show();
                }
                binding.rlScrollView.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        }, methods.getAPIRequest(type, bundle, null));

        postAdsAsync.execute();
    }
}