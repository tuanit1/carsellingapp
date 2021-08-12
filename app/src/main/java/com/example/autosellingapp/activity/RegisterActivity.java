package com.example.autosellingapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.asynctasks.LoadSignUp;
import com.example.autosellingapp.databinding.ActivityRegisterBinding;
import com.example.autosellingapp.interfaces.LoadSignUpListener;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Methods methods;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        methods = new Methods(this);

        auth = FirebaseAuth.getInstance();

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });

    }

    private void RegisterUser(){
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();
        String fullname = binding.edtFullname.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String address = binding.edtAddress.getText().toString().trim();

        if(email.isEmpty()){
            binding.edtEmail.setError("Email is required!");
            binding.edtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.setError("Please provide valid email!");
            binding.edtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            binding.edtPassword.setError("Password is required!");
            binding.edtPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            binding.edtPassword.setError("Password must be at least 6 characters");
            binding.edtPassword.requestFocus();
            return;
        }

        if(fullname.isEmpty()){
            binding.edtFullname.setError("Full name is required!");
            binding.edtFullname.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            binding.edtPhone.setError("Phone is required!");
            binding.edtPhone.requestFocus();
            return;
        }

        if(address.isEmpty()){
            binding.edtAddress.setError("Address is required!");
            binding.edtAddress.requestFocus();
            return;
        }

        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<SignInMethodQueryResult> task) {
                        if(task.getResult().getSignInMethods().isEmpty()){

                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){

                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());
                                                HashMap<String, Object> hashMap = new HashMap<>();

                                                hashMap.put("uid", auth.getCurrentUser().getUid());
                                                hashMap.put("status", "offline");

                                                reference.setValue(hashMap);

                                                Bundle bundle = new Bundle();

                                                bundle.putString(Constant.TAG_UID, auth.getCurrentUser().getUid());
                                                bundle.putString(Constant.TAG_EMAIL, email);
                                                bundle.putString(Constant.TAG_FULLNAME, fullname);
                                                bundle.putString(Constant.TAG_PHONE, phone);
                                                bundle.putString(Constant.TAG_ADDRESS, address);

                                                LoadSignUp(bundle);
                                            }else{
                                                Toast.makeText(RegisterActivity.this, "Failed to sign up! Try again!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }else{
                            Toast.makeText(RegisterActivity.this, "This email have been used!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void LoadSignUp(Bundle bundle){
        LoadSignUp loadSignUp = new LoadSignUp(new LoadSignUpListener() {
            @Override
            public void onStart() {
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(String success) {
                binding.progressBar.setVisibility(View.GONE);
                if(success.equals(Constant.SUCCESS)){
                    auth.getCurrentUser().sendEmailVerification();
                    binding.edtEmail.setText("");
                    binding.edtPassword.setText("");
                    binding.edtFullname.setText("");
                    binding.edtPhone.setText("");
                    binding.edtAddress.setText("");
                    Toast.makeText(RegisterActivity.this, "Sign up successfully! Please verify your email!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, ActivityLogin.class));
                }else {
                    auth.getCurrentUser().delete();
                    Toast.makeText(RegisterActivity.this, "Failed to sign up! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }, methods.getAPIRequest(Constant.MEDTHOD_SIGNUP, bundle, null));

        loadSignUp.execute();
    }
}