package com.example.autosellingapp.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.autosellingapp.databinding.FragmentSettingBinding;
import com.example.autosellingapp.utils.SharedPref;

public class FragmentSetting extends Fragment {

    private FragmentSettingBinding binding;
    private SharedPref sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        sharedPref = new SharedPref(getContext());

        binding.swDarkmode.setChecked(sharedPref.isDarkMode());

        binding.swDarkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPref.setDarkMode(isChecked);
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        return binding.getRoot();
    }
}