package com.example.cafe4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cafe4u.R;
import com.example.cafe4u.databinding.Home2Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class home2 extends AppCompatActivity {

    private Home2Binding binding;

    // Khai báo Spinner và Adapter ở đây
    private Spinner styleSpinner, openTimeSpinner;

    private RatingBar ratingBar;
    private ArrayAdapter<String> adapter;
    Button hSearch, chSearch;
    ImageButton btnGroup, btnProfile, btnLike, btnHome;


    private ArrayAdapter<String> styleAdapter, openTimeAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    GeoPoint currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Home2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addControl();

        // Khởi tạo Adapter và đặt kiểu giao diện cho Spinner
        styleAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, styleList);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styleSpinner.setAdapter(styleAdapter);

        openTimeAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, openTimesList);
        openTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        openTimeSpinner.setAdapter(openTimeAdapter);

        //lay bien currentlocation
        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);
        currentLocation = new GeoPoint(latitude, longitude);

        getUniqueOpenTimes();

        addEvent();
    }

    private void addEvent() {
        chSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home2.this, home1.class);
                startActivity(i);
            }
        });
        hSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedOpenTime = openTimeSpinner.getSelectedItem().toString();
                String selectedStyle = styleSpinner.getSelectedItem().toString();
                float selectedRating = ratingBar.getRating();
                searchResult(selectedOpenTime, selectedStyle, selectedRating);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home2.this, tai_khoan.class);
                startActivity(i);
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home2.this, ds_yeuthich.class);
                startActivity(i);
            }
        });

        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home2.this, HomeChatActivity.class);
                startActivity(i);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            getLastLocation();
            }
        });
    }

    private void addControl() {
        styleSpinner = findViewById(R.id.spinnerStyle);
        openTimeSpinner = findViewById(R.id.openTimeSpinner);
        hSearch = findViewById(R.id.h_search);
        chSearch = findViewById(R.id.cancel_h_search);
        ratingBar = findViewById(R.id.ratingBar);

        btnGroup = findViewById(R.id.btnGroup);
        btnLike = findViewById(R.id.btnLike);
        btnProfile = findViewById(R.id.btnProfile);
        btnHome = findViewById(R.id.btnHome);
    }



//    private void getStyleList(){
//        CollectionReference stylesRef = db.collection("CafeShop");
//
//        // Lấy dữ liệu từ Firestore và cập nhật vào Adapter
//        stylesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        // Lấy dữ liệu từ mỗi document và thêm vào mảng
//                        String style = document.getString("style");
//                        styleList.add(style);
//                    }
//
//                    // Sắp xếp mảng styleList theo thứ tự bảng chữ cái
//                    Collections.sort(styleList);
//
//                    // Cập nhật dữ liệu mới vào Adapter sau khi lấy từ Firestore
//                    styleAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//    }

    private ArrayList<String> styleList = new ArrayList<>();
    private ArrayList<String> openTimesList = new ArrayList<>();
    private void getUniqueOpenTimes() {
        CollectionReference cafeShopRef = db.collection("CafeShop");

        cafeShopRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Set<String> openTimesSet = new HashSet<>(); // Sử dụng Set để loại bỏ các giá trị trùng lặp
                    Set<String> styleSet = new HashSet<>();

                    // Lặp qua các tài liệu và thêm openTime vào Set
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String openTime = document.getString("openTime");
                        String style = document.getString("style");
                        if (openTime != null && !openTime.isEmpty()) {
                            openTimesSet.add(openTime);
                        }
                        if (style != null && !style.isEmpty()) {
                            styleSet.add(style);
                        }
                    }

                    // Sắp xếp các giá trị trong Set theo thứ tự bảng chữ cái
                    ArrayList<String> sortedOpenTimes = new ArrayList<>(openTimesSet);
                    Collections.sort(sortedOpenTimes);

                    ArrayList<String> sortedStyle = new ArrayList<>(styleSet);
                    Collections.sort(sortedStyle);
                    // Cập nhật lại mảng openTimesList đã sắp xếp
                    openTimesList.clear();
                    openTimesList.addAll(sortedOpenTimes);

                    styleList.clear();
                    styleList.addAll(sortedStyle);

                    // Cập nhật dữ liệu mới vào Adapter sau khi lấy từ Firestore
                    openTimeAdapter.notifyDataSetChanged();
                    styleAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    double range = 0.005;
    private void searchResult(String selectedOpenTime, String selectedStyle, float selectedRating) {

        CollectionReference cafeShopRef = FirebaseFirestore.getInstance().collection("CafeShop");

        Query query = cafeShopRef;

        if (!selectedOpenTime.equals("-Không chọn-")) {
            query = query.whereEqualTo("openTime", selectedOpenTime);
        }
        if (selectedRating > 2 || selectedRating ==2) {
            query = query.whereGreaterThanOrEqualTo("rating", selectedRating);
        }
        if (!selectedStyle.equals("-Không chọn-")) {
            query = query.whereEqualTo("style", selectedStyle);
        }
        if(currentLocation != null){
            double minLatitude = currentLocation.getLatitude() - range;
            double maxLatitude = currentLocation.getLatitude() + range;
            double minLongitude = currentLocation.getLongitude() - range;
            double maxLongitude = currentLocation.getLongitude() + range;
            query = query.whereGreaterThanOrEqualTo("location", new GeoPoint(minLatitude, minLongitude))
                    .whereLessThanOrEqualTo("location", new GeoPoint(maxLatitude, maxLongitude));
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<String> cafeNames = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String cafeName = document.getString("name");
                    if (cafeName != null) {
                        cafeNames.add(cafeName);
                    }
                }
                Intent i = new Intent(home2.this, home1.class);
                i.putStringArrayListExtra("cafeNames", cafeNames);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(home2.this, "Không tìm thấy quán cafe", Toast.LENGTH_SHORT).show();
            }
        });
    }
}