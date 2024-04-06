package com.example.cafe4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cafe4u.databinding.DangNhapBinding;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class dang_nhap extends AppCompatActivity {
    private DangNhapBinding binding;



    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), home1.class);
            startActivity(intent);
            finish();
        }
        binding = DangNhapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();

    }

    private void setListener() {
        binding.btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), dang_ky.class));
        });

        binding.btnLogin.setOnClickListener(v -> {
            if (isValidSignInDetail()) {
                signIn();
            }
        });
    }

    private void signIn() {

        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.edtEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.edtPasswd.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(getApplicationContext(), home1.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        showToast("Login successful");
                    }
                    else {
                        loading(false);
                        showToast("Unable to sign in");

                    }


                });
    }
    private void loading(Boolean isloading) {
        if (isloading) {
            binding.btnLogin.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnLogin.setVisibility(View.VISIBLE);
        }
    }
    private void showToast(String notice) {
        Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_SHORT).show();
    }
    private Boolean isValidSignInDetail () {
        if (binding.edtEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        }
        else if (binding.edtPasswd.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        }
        else {
            return true;
        }
    }

}