package com.example.cafe4u.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.cafe4u.R;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class home1 extends FragmentActivity implements OnMapReadyCallback {

    private PreferenceManager preferenceManager;




    GoogleMap mMap;
    SupportMapFragment mapFragment;
//    BottomNavigationView bottom_menu;

    SearchView search;
    ImageButton btnFilter, btnGroup, btnHome, btnProfile, btnLike;

//    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home1);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        preferenceManager = new PreferenceManager(getApplicationContext());
        getToken();



        addControls();
        addEvent();



    }


    private void addEvent() {
        search.setOnClickListener(new View.OnClickListener() {
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
        search = findViewById(R.id.search);
        btnFilter = findViewById(R.id.btnFilter);
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


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ptit, 12));


    }

    private void showToast(String notice) {
        Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_SHORT).show();
    }
    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token)

                .addOnFailureListener(e -> showToast("Unable update"));
    }
}