package com.example.cafe4u.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe4u.R;
import com.example.cafe4u.adapters.ShopAdapter;
import com.example.cafe4u.databinding.DsYeuthichBinding;
import com.example.cafe4u.models.Shop;

public class ds_yeuthich extends AppCompatActivity {
    //khai bao bien giao dien
    ImageButton btn_QuayLai;
    private ArrayList<Shop> favouriteCafe ;
    private RecyclerView mRecycleCafe;
    private ShopAdapter mCafeAdapter ;
    private DsYeuthichBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DsYeuthichBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mRecycleCafe = findViewById(R.id.listFavourite);
        favouriteCafe = new ArrayList<>();
        createCafeList();
        mCafeAdapter = new ShopAdapter(this,favouriteCafe);
        mRecycleCafe.setAdapter(mCafeAdapter);
        mRecycleCafe.setLayoutManager(new LinearLayoutManager(this));
        //anh xa Id cho cac bien giao dien

        addControl();
        addEvent();
    }

    private void addEvent() {

        binding.btnQuaylai.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), home1.class));
        });
        binding.btnNewCoffe.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), goi_y.class));
        });
    }

    private void addControl() {

    }


    //laytt tu csdl
    private void createCafeList() {
        favouriteCafe.add(new Shop(0, "Thor",1,"khong gian nho, phucj vu kem"));
        favouriteCafe.add(new Shop(0,"IronMan",2,"thai do nhan vien tot nhg menu it mon"));
        favouriteCafe.add(new Shop(0,"Hulk",3,"khong gian nho, phucj vu kem"));
        favouriteCafe.add(new Shop(0,"SpiderMan",4,"khong gian nho, phucj vu kem"));
        favouriteCafe.add(new Shop(0,"Thor",5,"nhan vien phuc vu kem "));
        favouriteCafe.add(new Shop(0,"IronMan",4,"10 d k co nhung"));
        favouriteCafe.add(new Shop(0,"Hulk",3,"thai do nhan vien tot nhg menu it mon"));
        favouriteCafe.add(new Shop(0,"SpiderMan",2,"khong gian nho, phucj vu kem"));
        favouriteCafe.add(new Shop(0,"Thor",1,"thai do nhan vien tot nhg menu it mon"));
        favouriteCafe.add(new Shop(0,"IronMan",2,"khong gian nho, phucj vu kem"));
    }
}