package com.example.cafe4u.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.cafe4u.R;


public class chitiet_quan extends AppCompatActivity {
    ImageButton btn_QuayLai, btn_yeuThich;
    Button btn_seeComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chitiet_quan);
        // Quay về trang trước (Trang kết quả)
        btn_QuayLai = findViewById(R.id.back_arr);
        btn_QuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Chuyển sang layout đọc đánh giá của khách hàng
        btn_seeComment = findViewById(R.id.btnSeeComment);
        btn_seeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chitiet_quan.this, nhan_xet_khach_hang.class);
                startActivity(intent);
            }
        });
        btn_yeuThich = findViewById(R.id.favorite);
        btn_yeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}