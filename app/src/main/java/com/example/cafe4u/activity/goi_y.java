package com.example.cafe4u.activity;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe4u.R;
import com.example.cafe4u.adapters.ShopAdapter;
import com.example.cafe4u.databinding.GoiYBinding;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class goi_y extends AppCompatActivity {
    private GoiYBinding binding;
    private ShopAdapter mShopAdapter;
    private ArrayList<Shop> shopList, history;

    //khai bao bien giao dien
//    ImageButton btn_Quaylai;
    FirebaseFirestore database;
    RecyclerView recyclerView;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GoiYBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
        history = new ArrayList<>();
        shopList = new ArrayList<>();
        recyclerView = binding.listRcm;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(dividerItemDecoration);
//        mShopAdapter = new ShopAdapter(this ,recommentShop);
//        recyclerView.setAdapter(mShopAdapter);

        addEvent();
        getSearchHistory();
//        String mostFrequentStyle = getMostFrequentStylesId(history);
//        RecommendShop(mostFrequentStyle);
//        displayRecommendedShops(recommentShop);

    }

    private void getSearchHistory() {
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);
        database = FirebaseFirestore.getInstance();
        Map<String, Integer> styleCountMap = new HashMap<>();

        Query reference = database.collection("historySearch").whereEqualTo("userId", userId);
        CollectionReference cafeShop = database.collection("CafeShop");
        reference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                String shopID = documentSnapshot.getString("shopId");
                if (shopID != null) {
                    cafeShop.document(shopID).get()
                            .addOnSuccessListener(shopDocument -> {
                                Shop shop = shopDocument.toObject(Shop.class);
                                String style = shop.getStyle();
                                if (styleCountMap.containsKey(style)) {
                                    // Nếu đã tồn tại, tăng số lần xuất hiện lên 1
                                    styleCountMap.put(style, styleCountMap.get(style) + 1);
                                } else {
                                    // Nếu chưa tồn tại, thêm style vào map với số lần xuất hiện là 1
                                    styleCountMap.put(style, 1);
                                }

                                history.add(shop);

//                                displayRecommendedShops();



                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                                Toast.makeText(this, "execute", Toast.LENGTH_SHORT).show();
                            });
                }
                else {
                    Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                }
            }



        })


                        .addOnFailureListener(e -> {
                            e.printStackTrace();
                            Toast.makeText(this, "execute", Toast.LENGTH_SHORT).show();
                        });
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(styleCountMap.entrySet());

// Sắp xếp danh sách các cặp key-value theo số lần xuất hiện (value) giảm dần
        Collections.sort(entryList, (entry1, entry2) -> {
            int compare = entry2.getValue().compareTo(entry1.getValue()); // Sắp xếp giảm dần theo value
            if (compare == 0) { // Nếu số lần xuất hiện bằng nhau, sắp xếp theo thứ tự chữ cái của style (key)
                return entry1.getKey().compareTo(entry2.getKey());
            }
            return compare;
        });
        String mostStyle = entryList.get(0).getKey();
        cafeShop.whereEqualTo("style", mostStyle)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Shop shop = documentSnapshot.toObject(Shop.class);
                        shopList.add(shop);
                        displayRecommendedShops();
                    }


                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    Toast.makeText(this, "execute", Toast.LENGTH_SHORT).show();
                });


    }
    // Hàm tính toán và trả về style xuất hiện nhiều nhất
//    public String getMostFrequentStylesId(List<Shop> history) {
//        // Tạo một HashMap để lưu số lần xuất hiện của mỗi stylesID
//        Map<String, Integer> stylesCount = new HashMap<>();
//
//        // Lặp qua tất cả các cửa hàng trong lịch sử tìm kiếm
//        for (Shop shop : history) {
//            String styles = shop.getStyle();
//            // Kiểm tra xem stylesID này đã được đếm trước đó chưa
//            if (stylesCount.containsKey(styles)) {
//                // Nếu có, tăng giá trị đếm lên 1
//                stylesCount.put(styles, stylesCount.get(styles) + 1);
//            } else {
//                // Nếu chưa, thêm vào HashMap với giá trị đếm là 1
//                stylesCount.put(styles, 1);
//            }
//        }
//
//        // Tìm stylesID xuất hiện nhiều nhất bằng cách lặp qua HashMap
//        String mostStyle = null;
//        List<Map.Entry<String, Integer>> entries = new ArrayList<>(stylesCount.entrySet());
//        Comparator<Map.Entry<String, Integer>> comparator = new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
//                // So sánh theo Value (giảm dần)
//                int valueComparison = -entry1.getValue().compareTo(entry2.getValue());
//                if (valueComparison == 0) {
//                    // Nếu Value bằng nhau, so sánh theo Key (tăng dần)
//                    return entry1.getKey().compareTo(entry2.getKey());
//                }
//                return valueComparison;
//            }
//        };
//        Collections.sort(entries, comparator);
//        mostStyle = entries.get(0).getKey();
//
//
//        return mostStyle;
//
//    }
//
//

    private void addEvent() {
        binding.btnQuaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(goi_y.this, home1.class);
                startActivity(back);
            }
        });
    }


//    private void RecommendShop(String mostFrequentStyles) {
//        database = FirebaseFirestore.getInstance();
//
//        Query reference = database.collection("CafeShop").whereEqualTo("style", mostFrequentStyles);
//        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Shop shop = document.toObject(Shop.class);
//                        shopList.add(shop);
//                        displayRecommendedShops();
//                    }
//
//                }
//                else {
//                    Log.e(TAG, "Error getting recommended shops: ", task.getException());
//                }
//            }
//        });
////        calculateDistancesToShops(shopList);
//
//    }

    // Hàm tính khoảng cách giữa hai điểm dựa trên vị trí latitude và longitude
//    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
//        final int R = 6371; // Bán kính trái đất trong đơn vị kilometer
//
//        double latDistance = Math.toRadians(lat2 - lat1);
//        double lonDistance = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double distance = R * c;
//
//        return distance;
//    }

    // Hàm tính khoảng cách từ vị trí của người dùng tới từng quán trong danh sách recommendedShops
//    private void calculateDistancesToShops(List<Shop> recommendedShops) {
//        SortedMap<Double, Shop> sortedShops = new TreeMap<>(); // Map lưu trữ khoảng cách của từng cửa hàng
//
//        for (Shop shop : recommendedShops) {
//            double shopLatitude = shop.getLocation().getLatitude();
//            double shopLongitude = shop.getLocation().getLongitude();
//
//            // Tính khoảng cách từ vị trí của người dùng tới quán hiện tại
//            double distance = calculateDistance(20.979660, 105.786819, shopLatitude, shopLongitude);
//
//            // Lưu khoảng cách vào Map với key là ID của cửa hàng
//            sortedShops.put(distance, shop);
//        }
//
//        ArrayList<Shop> sortedShopList = new ArrayList<>(sortedShops.values());
//        mShopAdapter = new ShopAdapter(this ,sortedShopList);
//        recyclerView.setAdapter(mShopAdapter);
////        displayRecommendedShops(sortedShopList);
//
//
//
//
//    }
    private void displayRecommendedShops() {

        mShopAdapter = new ShopAdapter(this ,shopList);
        recyclerView.setAdapter(mShopAdapter);




    }



}