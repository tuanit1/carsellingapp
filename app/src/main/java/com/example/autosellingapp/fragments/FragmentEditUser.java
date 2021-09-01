package com.example.autosellingapp.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autosellingapp.R;
import com.example.autosellingapp.activity.ForgotPasswordActivity;
import com.example.autosellingapp.asynctasks.UpdateFavouriteAsync;
import com.example.autosellingapp.databinding.FragmentEditUserBinding;
import com.example.autosellingapp.interfaces.ReloadFragmentListener;
import com.example.autosellingapp.interfaces.UpdateFavListener;
import com.example.autosellingapp.items.UserItem;
import com.example.autosellingapp.utils.Constant;
import com.example.autosellingapp.utils.Methods;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class FragmentEditUser extends Fragment {

    private static final int PICK_IMAGE_CODE = 435;
    private FragmentEditUserBinding binding;
    private Methods methods;
    private UserItem USER;
    private String IMAGE = "";
    private String EMAIL = "";
    private String FULL_NAME = "";
    private String PHONE = "";
    private String ADDRESS = "";
    private boolean isChangeImage = false;
    private Uri IMAGE_PICK_URI;
    ReloadFragmentListener listener;

    public FragmentEditUser(ReloadFragmentListener listener){
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditUserBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();

        if(bundle != null){
            USER = (UserItem) bundle.getSerializable(Constant.TAG_USER);

            IMAGE = USER.getImage();
            EMAIL = USER.getEmail();
            FULL_NAME = USER.getFullName();
            PHONE = USER.getPhoneNumber();
            ADDRESS = USER.getAddress();
        }

        Picasso.get()
                .load(Constant.SERVER_URL + "images/user_image/" + IMAGE)
                .placeholder(R.drawable.user_ic)
                .into(binding.ivUser);

        methods = new Methods(getContext());

        binding.cvEmail.setEnabled(false);
        binding.tvEmail.setText(EMAIL);
        binding.tvFullname.setText(FULL_NAME);
        binding.tvPhone.setText(PHONE);
        binding.tvAddress.setText(ADDRESS);

        binding.cvFullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogText(Constant.TEXT_FULL_NAME);
            }
        });
        binding.cvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogText(Constant.TEXT_PHONE);
            }
        });
        binding.cvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogText(Constant.TEXT_ADDRESS);
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFalse = false;

                if(FULL_NAME.isEmpty()){
                    binding.tvFullname.setText(getString(R.string.full_name) + " is empty");
                    binding.tvFullname.setTextColor(getResources().getColor(R.color.red));
                    isFalse = true;
                }

                if(PHONE.isEmpty()){
                    binding.tvPhone.setText(getString(R.string.phone) + " is empty");
                    binding.tvPhone.setTextColor(getResources().getColor(R.color.red));
                    isFalse = true;;
                }

                if(ADDRESS.isEmpty()){
                    binding.tvAddress.setText(getString(R.string.address) + " is empty");
                    binding.tvAddress.setTextColor(getResources().getColor(R.color.red));
                    isFalse = true;;
                }

                if(isFalse){
                    return;
                }else {
                    UpdateUser();
                }

            }
        });

        binding.btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setData(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGE_CODE);
            }
        });

        binding.btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(EMAIL);
                Toast.makeText(getContext(), "Check your email to reset your password!", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if(requestCode == PICK_IMAGE_CODE){
            if(resultCode == Activity.RESULT_OK){
                isChangeImage = true;
                IMAGE_PICK_URI = data.getData();

                Picasso.get()
                        .load(IMAGE_PICK_URI)
                        .into(binding.ivUser);
            }
        }
    }

    private void openDialogText(String type){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_dialog_inputtext);

        TextView tv_dialog = dialog.findViewById(R.id.tv_dialog);
        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        EditText edt_dialog = dialog.findViewById(R.id.edt_dialog);
        tv_dialog.setText(type);

        switch (type){
            case Constant.TEXT_FULL_NAME:
                edt_dialog.setText(FULL_NAME);
                break;
            case Constant.TEXT_PHONE:
                edt_dialog.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                edt_dialog.setText(PHONE);
                break;
            case Constant.TEXT_ADDRESS:
                edt_dialog.setText(ADDRESS);
                break;
        }


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case Constant.TEXT_FULL_NAME:
                        FULL_NAME = edt_dialog.getText().toString();
                        binding.tvFullname.setText(FULL_NAME);
                        break;
                    case Constant.TEXT_PHONE:
                        PHONE = edt_dialog.getText().toString();
                        binding.tvPhone.setText(PHONE);
                        break;
                    case Constant.TEXT_ADDRESS:
                        ADDRESS = edt_dialog.getText().toString();
                        binding.tvAddress.setText(ADDRESS);
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void UpdateUser(){
        Bundle bundle = new Bundle();

        ArrayList<File> arrayList_file = new ArrayList<>();

        if(isChangeImage){
            String filePath = methods.getPathImage(IMAGE_PICK_URI);
            File file = new File(filePath);
            arrayList_file.add(file);
        }

        bundle.putBoolean("is_change_image", isChangeImage);
        bundle.putString(Constant.TAG_FULLNAME, FULL_NAME);
        bundle.putString(Constant.TAG_PHONE, PHONE);
        bundle.putString(Constant.TAG_ADDRESS, ADDRESS);

        UpdateFavouriteAsync updateFavouriteAsync = new UpdateFavouriteAsync(new UpdateFavListener() {
            @Override
            public void onEnd(String success) {
                if(success.equals("1")){
                    Toast.makeText(getContext(), "Information is updated!", Toast.LENGTH_SHORT).show();
                    listener.reload();
                }else{
                    Toast.makeText(getContext(), getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                }
            }
        }, methods.getAPIRequest(Constant.METHOD_UPDATE_USER, bundle, arrayList_file, null));
        updateFavouriteAsync.execute();
    }
}