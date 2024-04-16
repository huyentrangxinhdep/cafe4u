package com.example.cafe4u.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.example.cafe4u.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class home1 extends FragmentActivity implements OnMapReadyCallback {



    GoogleMap mMap;
    SupportMapFragment mapFragment;
//    BottomNavigationView bottom_menu;

    AutoCompleteTextView search;
    ImageButton btnFilter, btnGroup, btnHome, btnProfile, btnLike, search_btn;

//    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home1);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        addControls();
        addEvent();



    }


    private void addEvent() {
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home1.this, kqua_search.class);
                startActivity(i);
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home1.this, home2.class);
                startActivity(i);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home1.this, tai_khoan.class);
                startActivity(i);
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home1.this, ds_yeuthich.class);
                startActivity(i);
            }
        });

        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home1.this, HomeChatActivity.class);
                startActivity(i);
            }
        });


    }

    private void addControls() {
        search_btn = findViewById(R.id.search_btn);
        btnFilter = findViewById(R.id.btnFilter);
        search =  findViewById(R.id.search);
//        bottom_menu = findViewById(R.id.bottom_menu);
//        bottom_menu =  findViewById(R.id.bottom_menu);
        btnGroup = findViewById(R.id.btnGroup);
        btnHome = findViewById(R.id.btnHome);
        btnLike = findViewById(R.id.btnLike);
        btnProfile = findViewById(R.id.btnProfile);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ptit = new LatLng(20.979660, 105.786820);
        mMap.addMarker(new MarkerOptions().position(ptit).title("PTIT"));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ptit, 10));


    }
}