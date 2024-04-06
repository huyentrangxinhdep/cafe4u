package com.example.cafe4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe4u.R;
import com.example.cafe4u.adapters.ShopAdapter;
import com.example.cafe4u.models.Shop;

import java.util.ArrayList;

public class goi_y extends AppCompatActivity {
    //khai bao bien giao dien
    ImageButton btn_Quaylai;
    private ArrayList<Shop> recommentCafe ;
    private RecyclerView mRecycleCafe;
    private ShopAdapter mCafeAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goi_y);
        mRecycleCafe = findViewById(R.id.listRcm);
        recommentCafe = new ArrayList<>();
        createCafeList();
        mCafeAdapter = new ShopAdapter(this,recommentCafe);
        mRecycleCafe.setAdapter(mCafeAdapter);
        mRecycleCafe.setLayoutManager(new LinearLayoutManager(this));

        addControl();
        addEvent();

    }

    private void addEvent() {
        btn_Quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(goi_y.this, home1.class);
                startActivity(back);
            }
        });
    }

    private void addControl() {
        btn_Quaylai = findViewById(R.id.btn_Quaylai);
    }

    private void createCafeList() {
        recommentCafe.add(new Shop(0, "Thor",1,"khong gian nho, phucj vu kem"));
        recommentCafe.add(new Shop(0,"IronMan",2,"thai do nhan vien tot nhg menu it mon"));
        recommentCafe.add(new Shop(0,"Hulk",3,"khong gian nho, phucj vu kem"));
        recommentCafe.add(new Shop(0,"SpiderMan",4,"khong gian nho, phucj vu kem"));
    }
}