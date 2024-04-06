package com.example.cafe4u.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.cafe4u.R;
import com.example.cafe4u.databinding.Home2Binding;

public class home2 extends AppCompatActivity {

    private Home2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Home2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        binding.hSearch.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), kqua_search.class));
        });
        binding.cancelHSearch.setOnClickListener(v -> finish());
        binding.btnHome.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), home1.class));
        });
    }
}