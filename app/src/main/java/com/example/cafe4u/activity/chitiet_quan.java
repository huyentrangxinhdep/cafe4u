package com.example.cafe4u.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cafe4u.R;
import com.example.cafe4u.activity.nhan_xet_khach_hang;
import com.example.cafe4u.models.Shop;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class chitiet_quan extends AppCompatActivity {
    private ImageButton btn_QuayLai, btn_yeuThich, imgbtn_write_cmt;
    private Button btn_seeComment;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String shopID;
    private TextView nameShop, styleShop, addressShop, openShop, contactShop;
    private RatingBar ratingShop;
    private ImageView imageShop;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_quan);
        getShop();
        addControl();
        addEvent();
    }

    public void getShop() {
        shopID = getIntent().getStringExtra("shopId");
        database.collection("CafeShop").document(shopID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Shop shop = documentSnapshot.toObject(Shop.class);
                        displayShopDetail(shop);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(chitiet_quan.this, "Cannot display shop detail!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void displayShopDetail(Shop shop) {
        shopID = getIntent().getStringExtra("shopId");

        nameShop = findViewById(R.id.Result_Layout_txt);
//        ratingShop = findViewById(R.id.ratingBar);
        styleShop = findViewById(R.id.styleShop);
        addressShop = findViewById(R.id.addressShop);
        openShop = findViewById(R.id.timeShop);
        contactShop = findViewById(R.id.contactShop);
        imageShop = findViewById(R.id.shop_img);

        nameShop.setText(shop.getName());
//        ratingShop.setRating(float(shop.getVote());
        styleShop.setText(shop.getStyle());
        addressShop.setText(shop.getAddress());
        openShop.setText(shop.getOpenTime());
        contactShop.setText(shop.getPhoneNumber());
        database.collection("CafeShop").document(shopID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String gsUrl = documentSnapshot.getString("imageShop");
                StorageReference imageRef = storage.getReferenceFromUrl(gsUrl);
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        Glide.with(chitiet_quan.this).
                                load(downloadUrl).
                                into(imageShop);
                    }
                });
            }
        });
    }

    public void addControl() {
        btn_QuayLai = findViewById(R.id.back_arr);
        imgbtn_write_cmt = findViewById(R.id.rate_btn);
        btn_yeuThich = findViewById(R.id.favorite);
        btn_seeComment = findViewById(R.id.btnSeeComment);
    }

    public void addEvent() {
        // Quay về trang trước (Trang kết quả)
        btn_QuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Chuyển đến giao diện viết nhận xét
//        imgbtn_write_cmt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent write_cmt = new Intent(chitiet_quan.this, Send_Feedback.class);
//                startActivity(write_cmt);
//            }
//        });
        // Thêm quán yêu thích
        btn_yeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceManager = new PreferenceManager(getApplicationContext());
                String userID = preferenceManager.getString(Constants.KEY_USER_ID);
                database.collection("favoriteShop").whereEqualTo("userId", userID).whereEqualTo("shopId", shopID).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            Map<String, String> favShop = new HashMap<>();
                            favShop.put("shopId", shopID);
                            favShop.put("userId", userID);
                            database.collection("favoriteShop").add(favShop);
                        } else {
                            Toast.makeText(chitiet_quan.this, "Quán này đã được thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        // Chuyển sang layout đọc đánh giá của khách hàng
        btn_seeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chitiet_quan.this, nhan_xet_khach_hang.class);
                startActivity(intent);
            }
        });
    }
}