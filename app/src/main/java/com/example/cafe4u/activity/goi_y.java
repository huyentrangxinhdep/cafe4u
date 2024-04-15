package com.example.cafe4u.activity;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe4u.R;
import com.example.cafe4u.adapters.ShopAdapter;
import com.example.cafe4u.models.Shop;
import com.example.cafe4u.models.User;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class goi_y extends AppCompatActivity {
    private ShopAdapter mShopAdapter;

    //khai bao bien giao dien
    ImageButton btn_Quaylai;
    FirebaseFirestore database;
    RecyclerView recyclerView;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goi_y);
        database = FirebaseFirestore.getInstance();
        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);

        preferenceManager = new PreferenceManager(getApplicationContext());
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        recyclerView = findViewById(R.id.listRcm);
        btn_Quaylai = findViewById(R.id.btn_Quaylai);


        if (userId != null) {
            // Gọi phương thức để lấy thông tin lịch sử tìm kiếm
            getSearchHistory(userId);
        }
        addEvent();

    }

    private void getSearchHistory(String userId) {
        CollectionReference reference = database.collection("searchHistory").document(userId).collection("history");
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshots = task.getResult();
                    ArrayList<Shop> history = new ArrayList<>();
                    for (QueryDocumentSnapshot document : snapshots) {
                        String shopId = document.getString("shopID");
                        // Truy vấn vào cơ sở dữ liệu để lấy thông tin chi tiết của từng cửa hàng
                        DocumentReference shopRef = database.collection("shops").document(shopId);
                        shopRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot shopSnapshot = task.getResult();
                                    if (shopSnapshot.exists()) {
                                        Shop shop = shopSnapshot.toObject(Shop.class);
                                        if (shop != null) {
                                            // Thêm thông tin của cửa hàng vào danh sách lịch sử tìm kiếm
                                            history.add(shop);
                                        }
                                    }
                                } else {
                                    Log.e(TAG, "Error getting shop document", task.getException());
                                }
                            }
                        });
                    }

                    // Gọi hàm tính toán styleID xuất hiện nhiều nhất từ danh sách lịch sử tìm kiếm
                    String mostFrequentStyleId = getMostFrequentStylesId(history);
                    Log.d(TAG, "Most frequent style ID: " + mostFrequentStyleId);

                    // Hiển thị danh sách quán gợi ý
//                    displayRecommendedShops(history);
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    // Hàm tính toán và trả về styleID xuất hiện nhiều nhất
    public String getMostFrequentStylesId(List<Shop> history) {
        // Tạo một HashMap để lưu số lần xuất hiện của mỗi stylesID
        Map<String, Integer> stylesIdCount = new HashMap<>();

        // Lặp qua tất cả các cửa hàng trong lịch sử tìm kiếm
        for (Shop shop : history) {
            String stylesId = shop.getStyleId();
            // Kiểm tra xem stylesID này đã được đếm trước đó chưa
            if (stylesIdCount.containsKey(stylesId)) {
                // Nếu có, tăng giá trị đếm lên 1
                stylesIdCount.put(stylesId, stylesIdCount.get(stylesId) + 1);
            } else {
                // Nếu chưa, thêm vào HashMap với giá trị đếm là 1
                stylesIdCount.put(stylesId, 1);
            }
        }

        // Tìm stylesID xuất hiện nhiều nhất bằng cách lặp qua HashMap
        String mostFrequentStylesId = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : stylesIdCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequentStylesId = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        System.out.println(mostFrequentStylesId);
        return mostFrequentStylesId;
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



    // Hàm tính toán và trả về khung giờ mở cửa quán mà người dùng tìm nhiều nhất
    public String getMostFrequentOpenTime(List<Shop> history) {
        // Tạo một HashMap để lưu số lần xuất hiện của mỗi khung giờ mở cửa
        Map<String, Integer> openTimeCount = new HashMap<>();

        // Lặp qua từng quán trong lịch sử tìm kiếm
        for (Shop shop : history) {
            // Lấy khung giờ mở cửa của quán hiện tại
            String openTime = shop.getOpenTime();
            // Nếu khung giờ này đã tồn tại trong HashMap, tăng giá trị tương ứng lên 1
            if (openTimeCount.containsKey(openTime)) {
                int count = openTimeCount.get(openTime);
                openTimeCount.put(openTime, count + 1);
            } else {
                // Nếu không tồn tại, thêm khung giờ vào HashMap với số lần xuất hiện là 1
                openTimeCount.put(openTime, 1);
            }
        }

        // Tìm khung giờ mở cửa có số lần xuất hiện nhiều nhất
        String mostFrequentOpenTime = "";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : openTimeCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentOpenTime = entry.getKey();
            }
        }

        return mostFrequentOpenTime;
    }



}