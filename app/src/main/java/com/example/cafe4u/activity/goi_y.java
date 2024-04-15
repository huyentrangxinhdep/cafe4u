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
import java.util.SortedMap;
import java.util.TreeMap;

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
                        String shopId = document.getString("shopId");
                        // Truy vấn vào cơ sở dữ liệu để lấy thông tin chi tiết của từng cửa hàng
                        DocumentReference shopRef = database.collection("CafeShop").document(shopId);
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


    private void RecommendShop(String mostFrequentStylesId) {
        CollectionReference shopRef = database.collection("CafeShop");
        shopRef.whereEqualTo("styleId", mostFrequentStylesId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Shop> recommendedShops = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Shop shop = document.toObject(Shop.class);
                                recommendedShops.add(shop);
                            }
//                            displayRecommendedShops(recommendedShops);
                        } else {
                            Log.e(TAG, "Error getting recommended shops: ", task.getException());
                        }
                    }
                });
    }

    // Hàm tính khoảng cách giữa hai điểm dựa trên vị trí latitude và longitude
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính trái đất trong đơn vị kilometer

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance;
    }

    // Hàm tính khoảng cách từ vị trí của người dùng tới từng quán trong danh sách recommendedShops
    private void calculateDistancesToShops(List<Shop> recommendedShops, double userLatitude, double userLongitude) {
        SortedMap<Double, Shop> sortedShops = new TreeMap<>(); // Map lưu trữ khoảng cách của từng cửa hàng

        for (Shop shop : recommendedShops) {
            double shopLatitude = shop.getLocation().getLatitude();
            double shopLongitude = shop.getLocation().getLongitude();

            // Tính khoảng cách từ vị trí của người dùng tới quán hiện tại
            double distance = calculateDistance(userLatitude, userLongitude, shopLatitude, shopLongitude);

            // Lưu khoảng cách vào Map với key là ID của cửa hàng
            sortedShops.put(distance, shop);
        }

        ArrayList<Shop> sortedShopList = new ArrayList<>(sortedShops.values());
        displayRecommendedShops(sortedShopList);




    }
    private void displayRecommendedShops(ArrayList<Shop> shops) {
        mShopAdapter = new ShopAdapter(goi_y.this, shops);
        recyclerView.setAdapter(mShopAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(goi_y.this));
    }



}