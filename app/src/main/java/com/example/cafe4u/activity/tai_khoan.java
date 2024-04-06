package com.example.cafe4u.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cafe4u.databinding.TaiKhoanBinding;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class tai_khoan extends AppCompatActivity {

    private TaiKhoanBinding binding;
    private PreferenceManager preferenceManager;
    //khai bao bien giao dien

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TaiKhoanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetail();
//        getToken();
        setListener();

    }

    private void setListener() {

        binding.btnLogOut.setOnClickListener(v -> signOut());
        binding.btnQuaylai.setOnClickListener(v -> finish());
        binding.textChangePasswd.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), doi_mat_khau.class));
        });
        binding.textDsYeuThich.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ds_yeuthich.class));
        });
    }

    private void loadUserDetail() {
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imgProfile.setImageBitmap(bitmap);
        binding.textEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));
        binding.textPasswd.setText(preferenceManager.getString(Constants.KEY_PASSWORD));
    }

    private void showToast(String notice) {
        Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_SHORT).show();
    }

//    private void getToken() {
//        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
//    }
//    private void updateToken(String token) {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
//        documentReference.update(Constants.KEY_FCM_TOKEN, token)
//                .addOnSuccessListener(unused -> showToast("Token updated success"))
//                .addOnFailureListener(e -> showToast("Unable update"));
//    }

    private void signOut() {
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), dang_nhap.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable sign out"));
    }
}