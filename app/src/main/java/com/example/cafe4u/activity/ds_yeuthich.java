package com.example.cafe4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe4u.R;
import com.example.cafe4u.activity.goi_y;
import com.example.cafe4u.activity.home1;
import com.example.cafe4u.adapters.ShopAdapter;
import com.example.cafe4u.databinding.DsYeuthichBinding;
import com.example.cafe4u.models.Shop;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ds_yeuthich extends AppCompatActivity {
    //khai bao bien giao dien
    ImageButton btn_QuayLai;
    private ArrayList<Shop> favouriteCafe;
    private RecyclerView mRecycleCafe;
    private ShopAdapter mCafeAdapter;
    private DsYeuthichBinding binding;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DsYeuthichBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mRecycleCafe = findViewById(R.id.listFavourite);
        getShop();
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

    private void getShop() {
        favouriteCafe = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        String userID = preferenceManager.getString(Constants.KEY_USER_ID);
        database.collection("favoriteShop").whereEqualTo("userId", userID).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        String shopID = document.getString("shopId");
                        if (shopID != null) {
                            // Truy vấn Firestore để lấy thông tin chi tiết của quán
                            database.collection("CafeShop").document(shopID)
                                    .get()
                                    .addOnSuccessListener(shopDocument -> {
                                        Shop shop = shopDocument.toObject(Shop.class);
                                        favouriteCafe.add(shop);
                                        // Hiển thị danh sách yêu thích lên RecyclerView
                                        displayShopList();
                                    })
                                    .addOnFailureListener(e -> {
                                        e.printStackTrace();
                                        Toast.makeText(this, "Lỗi khi thực hiện ", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "Lỗi null ", Toast.LENGTH_SHORT).show();
                        }

                    }
                    // Hiển thị danh sách quán lên RecyclerView
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(this, "Lỗi khi thực hiện ", Toast.LENGTH_SHORT).show();
                });
    }

    private void displayShopList() {
        mRecycleCafe = binding.listFavourite;
        mRecycleCafe.setLayoutManager(new LinearLayoutManager(this));
        mCafeAdapter = new ShopAdapter(this, favouriteCafe);
        mRecycleCafe.setAdapter(mCafeAdapter);
//        mRecycleCafe.setLayoutManager(new LinearLayoutManager(this));
    }
//    private void addControl() {
//
//    }


    //laytt tu csdl
//    private void createCafeList() {
//        favouriteCafe.add(new Shop(0, "Thor",1.0F,"Cổ điển","Wakanda"));
//        favouriteCafe.add(new Shop(0,"IronMan",2.0F,"Hiện đại","Mondstadt"));
//        favouriteCafe.add(new Shop(0,"Hulk",3.0F,"Trung Hoa","Liyue"));
//        favouriteCafe.add(new Shop(0,"SpiderMan",4.0F,"Cổ điển","Inazuma"));
//        favouriteCafe.add(new Shop(0,"Thor",5.0F,"Bác học","Sumeru"));
//        favouriteCafe.add(new Shop(0,"IronMan",4.0F,"Thanh lịch","Fontaine"));
//        favouriteCafe.add(new Shop(0,"Hulk",3.0F,"Rock n' Roll","Natlan"));
//        favouriteCafe.add(new Shop(0,"SpiderMan",2.0F,"Nữ hoàng băng giá","Snezhnaya"));
//        favouriteCafe.add(new Shop(0,"Thor",1.0F,"Cổ điển","Wakanda"));
//        favouriteCafe.add(new Shop(0,"IronMan",2.0F,"Cổ điển","Wakanda"));
//    }
}