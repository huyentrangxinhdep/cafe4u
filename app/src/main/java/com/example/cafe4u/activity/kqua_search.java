package com.example.cafe4u.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.cafe4u.R;
import com.example.cafe4u.adapters.ShopAdapter;
import com.example.cafe4u.databinding.ActivityKquaSearchBinding;
import com.example.cafe4u.models.Shop;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class kqua_search extends AppCompatActivity {


    ActivityKquaSearchBinding binding;
    PreferenceManager preferenceManager;
    RecyclerView recyclerView;
    FirebaseFirestore database;
    ShopAdapter shopAdapter;
    ArrayList<Shop> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKquaSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        getShop();
        setListener();


    }

    private void getShop() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();


    }

    private void setListener() {
        binding.btnQuaylai.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), home1.class));
        });
    }


}