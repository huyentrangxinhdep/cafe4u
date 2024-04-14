package com.example.cafe4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cafe4u.R;
import com.example.cafe4u.databinding.DoiMatKhauBinding;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class doi_mat_khau extends AppCompatActivity {
    //Khai bao bien
    private DoiMatKhauBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DoiMatKhauBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        database = FirebaseFirestore.getInstance();

        setListener();

//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    private void setListener() {
        binding.btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), tai_khoan.class));
        });
        binding.btnSave.setOnClickListener(v -> {
            if (isValidChangePasswordDetail()) {
                changePasswd();
            }


        });
    }

    private void changePasswd() {
        String newPasswd = binding.edtNewPasswd.getText().toString().trim();

        // Kiểm tra mật khẩu mới không được rỗng
//        if (newPasswd.isEmpty()) {
//            showToast("Please enter new password");
//            return;
//        }

        loading(true);
        // Lấy ID của người dùng hiện tại
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        // Tạo tham chiếu đến tài liệu người dùng trong Firestore
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(userId);

        // Cập nhật mật khẩu mới trong cơ sở dữ liệu Firestore

        documentReference.update(Constants.KEY_PASSWORD, newPasswd)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showToast("Password changed successfully");
                                    preferenceManager.putString(Constants.KEY_PASSWORD, newPasswd); // Cập nhật mật khẩu mới vào SharedPreferences
                                    startActivity(new Intent(getApplicationContext(), tai_khoan.class));
                                    finish();
                                } else {
                                    loading(false);
                                    showToast("Failed to change password");
                                }
                            }
                        });


    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidChangePasswordDetail () {
        String oldPasswd = binding.edtOldPasswd.getText().toString().trim();
        String newPasswd = binding.edtNewPasswd.getText().toString().trim();
        String rewriteNewPasswd = binding.edtRewriteNewPasswd.getText().toString().trim();

        // Kiểm tra mật khẩu cũ không được rỗng
        if (oldPasswd.isEmpty()) {
            showToast("Please enter old password");
            return false;
        }

        // Kiểm tra mật khẩu mới không được rỗng
        if (newPasswd.isEmpty()) {
            showToast("Please enter new password");
            return false;
        }

        // Kiểm tra mật khẩu xác nhận mới không được rỗng
        if (rewriteNewPasswd.isEmpty()) {
            showToast("Please confirm new password");
            return false;
        }

        // Kiểm tra mật khẩu mới và mật khẩu xác nhận mới phải giống nhau
        if (!newPasswd.equals(rewriteNewPasswd)) {
            showToast("New password and confirm password must match");
            return false;
        }

        // Lấy mật khẩu hiện tại của người dùng từ SharedPreferences hoặc từ Firestore
        String currentPasswd = preferenceManager.getString(Constants.KEY_PASSWORD); // Lấy mật khẩu hiện tại của người dùng

        // Kiểm tra mật khẩu cũ nhập vào có trùng khớp với mật khẩu hiện tại không
        if (!oldPasswd.equals(currentPasswd)) {
            showToast("Old password does not match");
            return false;
        }

        return true;

    }

    private void loading(Boolean isloading) {
        if (isloading) {
            binding.btnSave.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnSave.setVisibility(View.VISIBLE);
        }
    }


}