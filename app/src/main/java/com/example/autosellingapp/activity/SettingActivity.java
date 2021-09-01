package com.example.autosellingapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.autosellingapp.R;
import com.example.autosellingapp.utils.SharedPref;

public class SettingActivity extends AppCompatActivity {

    private SwitchCompat sw_darkmode;
    private SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sw_darkmode = findViewById(R.id.sw_darkmode);

        sharedPref = new SharedPref(this);

        boolean isDarkMode = sharedPref.isDarkMode();

        sw_darkmode.setChecked(isDarkMode);

        sw_darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    }
}