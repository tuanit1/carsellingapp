package com.example.autosellingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.activity.ActivityLogin;
import com.example.autosellingapp.asynctasks.UpdateFavouriteAsync;
import com.example.autosellingapp.databinding.FragmentContactBinding;
import com.example.autosellingapp.interfaces.UpdateFavListener;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;

public class FragmentContact extends Fragment {

    private FragmentContactBinding binding;
    private Methods methods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactBinding.inflate(inflater, container, false);

        methods = new Methods(getContext());

        if(methods.isLogged()){
            binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Submit();
                }
            });
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

    private void Submit(){
        if(binding.edtMessage.getText().toString().isEmpty()){
            binding.edtMessage.setError(getString(R.string.cannot_empty));
            return;
        }

        if(methods.isNetworkAvailable()){
            Bundle bundle = new Bundle();
            bundle.putString(Constant.TAG_FEEDBACK_MESSAGE, binding.edtMessage.getText().toString());

            UpdateFavouriteAsync async = new UpdateFavouriteAsync(new UpdateFavListener() {
                @Override
                public void onEnd(String success) {
                    if (success.equals("1")) {
                        Toast.makeText(getContext(), getString(R.string.sent_contact_success), Toast.LENGTH_SHORT).show();
                        binding.edtMessage.setText("");
                    }else {
                        Toast.makeText(getContext(), getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                    }
                }
            }, methods.getAPIRequest(Constant.METHOD_CONTACT, bundle, null, null));
            async.execute();
        }else {
            Toast.makeText(getContext(), getString(R.string.internet_not_connect), Toast.LENGTH_SHORT).show();
        }
    }

    private void openLoginActivity(){
        Intent intent = new Intent(getContext(), ActivityLogin.class);
        startActivity(intent);
    }
}