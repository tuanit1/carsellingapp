package com.example.autosellingapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.jetbrains.annotations.NotNull;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private AppCompatEditText edt_email;
    private Button btn_reset;
    private ProgressBar progressBar;

    //remove 'password' column in db table user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth = FirebaseAuth.getInstance();

        edt_email = findViewById(R.id.edt_email);
        progressBar = findViewById(R.id.progressBar);
        btn_reset = findViewById(R.id.button_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =  edt_email.getText().toString().trim();
                if(email.isEmpty()){
                    edt_email.setError("Email is required!");
                    edt_email.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edt_email.setError("Please provide valid email!");
                    edt_email.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<SignInMethodQueryResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if(task.getResult().getSignInMethods().isEmpty()){
                                    Toast.makeText(ForgotPasswordActivity.this, "This email have not been signed up yet!", Toast.LENGTH_SHORT).show();
                                }else{
                                    auth.sendPasswordResetEmail(email);
                                    Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}