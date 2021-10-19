package com.example.autosellingapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.interfaces.MyListener;
import com.example.autosellingapp.utils.AppOpenAdsManager;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.example.autosellingapp.utils.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class SplashActivity extends AppCompatActivity {

    private Methods methods;
    private SharedPref sharedPref;
    private FirebaseAuth auth;
    private AppOpenAdsManager appOpenAdsManager;
    private boolean isAutoLogin = false;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        methods = new Methods(this);
        sharedPref = new SharedPref(this);
        auth = FirebaseAuth.getInstance();

        appOpenAdsManager = new AppOpenAdsManager(this, new MyListener() {
            @Override
            public void onClick() {
                if(isAutoLogin){
                    openMainActivity();
                }else {
                    openLoginActivity();
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPref.getIsAutoLogin()) {
                    if(methods.isNetworkAvailable()){
                        isAutoLogin = true;
                        loadLogin();
                    }else {
                        openDialog(getString(R.string.internet_not_connect));
                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isAutoLogin = false;
                            //openLoginActivity();

                            appOpenAdsManager.showAdIfAvailable();
                        }
                    }, 2000);
                }
            }
        }, 500);
    }

    private void openLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, ActivityLogin.class);
        startActivity(intent);
    }

    private void loadLogin() {
        auth.signInWithEmailAndPassword(sharedPref.getEmail(), sharedPref.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(auth.getCurrentUser().isEmailVerified()){
                        Constant.isLogged = true;
                        Log.e("AAA", "Log in");
                        Constant.UID = auth.getCurrentUser().getUid();
                        //openMainActivity();

                        appOpenAdsManager.showAdIfAvailable();
                    }else{
                        Toast.makeText(getApplicationContext(), "Your email is not verified! Please verify your email!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    openDialog(getString(R.string.unauthorized_access));
                }
            }
        });
    }

    private void openMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void openDialog(String message) {
        AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        alert.setTitle("Message");
        alert.setMessage(message);

        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finishAffinity();
            }
        });
        alert.show();
    }
}