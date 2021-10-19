package com.example.autosellingapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.example.autosellingapp.utils.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ActivityLogin extends AppCompatActivity {


    private AppCompatEditText edt_email, edt_password;
    private CheckBox ckb_remember;
    private Button btn_login, btn_skip;
    private TextView tv_forgotPass, tv_signUp;
    private ProgressBar progressBar;
    private Methods methods;
    private SharedPref sharedPref;
    private String from = "";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        methods = new Methods(this);
        sharedPref = new SharedPref(this);

        auth = FirebaseAuth.getInstance();
        Hook();

        if(sharedPref.isRemember()){
            edt_email.setText(sharedPref.getEmail());
            edt_password.setText(sharedPref.getPassword());
            ckb_remember.setChecked(true);
        }else {
            ckb_remember.setChecked(false);
        }
    }


    private void Hook(){
        edt_email = findViewById(R.id.et_login_email);
        edt_password = findViewById(R.id.et_login_password);
        ckb_remember = findViewById(R.id.cb_rememberme);
        btn_login = findViewById(R.id.button_login);
        btn_skip = findViewById(R.id.button_skip);
        tv_forgotPass = findViewById(R.id.tv_forgotpass);
        tv_forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ForgotPasswordActivity.class));
            }
        });
        tv_signUp = findViewById(R.id.tv_login_signup);
        progressBar = findViewById(R.id.progressBar);
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(methods.isNetworkAvailable()){
                    LoadLogin();
                }else{
                    Toast.makeText(ActivityLogin.this, getString(R.string.internet_not_connect), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    private void LoadLogin(){

        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(password.isEmpty()){
            edt_password.setError(getString(R.string.cannot_empty));
            focusView = edt_password;
            cancel = true;
        }

        if(password.contains(" ")){
            edt_password.setError(getString(R.string.passdontcontainspace));
            focusView = edt_password;
            cancel = true;
        }

        if(email.isEmpty()){
            edt_email.setError(getString(R.string.cannot_empty));
            focusView = edt_email;
            cancel = true;
        }
        if(email.contains(" ")){
            edt_email.setError(getString(R.string.emailcontainspace));
            focusView = edt_email;
            cancel = true;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email.setError(getString(R.string.invalid_email));
            focusView = edt_email;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        if(auth.getCurrentUser().isEmailVerified()){
                            Constant.isLogged = true;
                            Log.e("AAA", "Log in");
                            Constant.UID = auth.getCurrentUser().getUid();
                            sharedPref.setIsAutoLogin(true);
                            sharedPref.setEmail(email);
                            sharedPref.setPassword(password);

                            if(ckb_remember.isChecked()){
                                sharedPref.setRemember(true);
                            }else {
                                sharedPref.setRemember(false);
                            }

                            openMainActivity();
                        }else{
                            Toast.makeText(ActivityLogin.this, "Your email is not verified! Please verify your email!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ActivityLogin.this, "Failed to sign in! Make sure your email or password is correct!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void openMainActivity(){
        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
        startActivity(intent);
    }

}