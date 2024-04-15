package com.example.cafe4u.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.cafe4u.R;
import com.example.cafe4u.models.Shop;
import com.example.cafe4u.ultility.Constants;
import com.example.cafe4u.ultility.PreferenceManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class home1 extends FragmentActivity implements OnMapReadyCallback {

    private PreferenceManager preferenceManager;

    private final int FINE_PERMISSION_CODE = 100; //yeu cau truy nhap
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentUserLocationMarker;

    GoogleMap mMap;

    AutoCompleteTextView search;
    ImageButton btnFilter, btnGroup, btnHome, btnProfile, btnLike, search_btn;

    //truy van den csdl
    public FirebaseFirestore firestore= FirebaseFirestore.getInstance();
    final CollectionReference cafeShopFireStore = firestore.collection("CafeShop");;

    int runappFirstTime = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this); //location
        getLastLocation();//location

        preferenceManager = new PreferenceManager(getApplicationContext());
        getToken();

        addControls();
        addEvent();


    }


    private void addEvent() {
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

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = search.getText().toString();
//                performSearch(query);
                markerList.clear();
                mMap.setOnMarkerClickListener(null);
                mMap.clear();
                LatLng crlc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                MarkerOptions markcrlc = new MarkerOptions().position(crlc).title("You are here!");
                markcrlc.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mMap.addMarker(markcrlc);

                if(!query.isEmpty()){
                    getLocationFromName(query);
                }
                else{
                    getLocationNearby();
                }
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
//        btnTarget = findViewById(R.id.btnHome);
        search_btn = findViewById(R.id.search_btn);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mylc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()); //location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylc, 17));
        MarkerOptions options = new MarkerOptions().position(mylc).title("You are here");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        currentUserLocationMarker = mMap.addMarker(options);


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

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(home1.this);
                    if (currentUserLocationMarker != null)
                    {
                        currentUserLocationMarker.remove();
                    }
                    if(runappFirstTime==1){
                        laydulieu();
                        runappFirstTime = 0;
                    }

                    // Gửi dữ liệu vị trí sang goi_y
                    Intent intent = new Intent(home1.this, goi_y.class);
                    intent.putExtra("latitude", currentLocation.getLatitude());
                    intent.putExtra("longitude", currentLocation.getLongitude());
                    startActivity(intent);

                }
                else{
                    Toast.makeText(home1.this,"Hãy bật định vị", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //yeu cau truy cap
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else{
                Toast.makeText(this,"Cho phép truy cập vị trí", Toast.LENGTH_SHORT).show();
            }
        }
    }


    double range = 0.005;
    ArrayList<String> nearbyShops = new ArrayList<>();
    public void laydulieu(){
        double minLatitude = currentLocation.getLatitude() - range;
        double maxLatitude = currentLocation.getLatitude() + range;
        double minLongitude = currentLocation.getLongitude() - range;
        double maxLongitude = currentLocation.getLongitude() + range;
        cafeShopFireStore.whereGreaterThanOrEqualTo("location", new GeoPoint(minLatitude, minLongitude))
                .whereLessThanOrEqualTo("location", new GeoPoint(maxLatitude, maxLongitude))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            nearbyShops.add(name);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nearbyShops);
                        search.setAdapter(adapter);
                        search.setThreshold(1);
                    } else {
                        Log.e("FirestoreError", "Error getting documents: ", task.getException());
                    }
                });
    }

//    private Marker cafeLocation;

    private void getLocationFromName(String name) {
        cafeShopFireStore.whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy giá trị "location" từ tài liệu Firestore
                            GeoPoint location = document.getGeoPoint("location");
                            if (location != null) {
                                LatLng cafelc = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cafelc, 17));
                                MarkerOptions options = new MarkerOptions().position(cafelc).title(name);
                                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                Marker cfMarker = mMap.addMarker(options);
                                markerList.add(cfMarker);

                                markChange();
                            } else {
                                Log.e("Location", "Location is null");
                            }
                        }
                    } else {
                        Log.e("FirestoreError", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void getLocationNearby(){
        LatLng cafelc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cafelc, 15));
        for (String cafeName : nearbyShops ) {
            // Thực hiện truy vấn trong Firestore để lấy thông tin của quán cafe dựa trên tên
            cafeShopFireStore.whereEqualTo("name", cafeName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Lấy tọa độ của quán cafe từ Firestore
                                    GeoPoint location = document.getGeoPoint("location");
                                    if (location != null) {
                                        LatLng cafeLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        Marker cfMarker = mMap.addMarker(new MarkerOptions().position(cafeLatLng).title(cafeName));
                                        markerList.add(cfMarker);

                                        markChange();
                                    }
                                }
                            } else {
                                Log.e("FirestoreError", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    private ArrayList<Marker> markerList = new ArrayList<>();

    private void markChange() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String shopId;
                LatLng latLng = marker.getPosition();

                GeoPoint cfLocation = new GeoPoint(latLng.latitude, latLng.longitude);
                getShopId(cfLocation, new OnShopIdReceivedListener() {
                    @Override
                    public void onShopIdReceived(String shopId) {

                        checkHistory(shopId, preferenceManager.getString(Constants.KEY_USER_ID));

                        Intent i = new Intent(home1.this, chitiet_quan.class);
                        i.putExtra("shopId",shopId);
                        startActivity(i);

                    }
                });


                return false;
            }
        });

    }

    interface OnShopIdReceivedListener {
        void onShopIdReceived(String cafeId);
    }

    private void getShopId(GeoPoint cfLocation, OnShopIdReceivedListener listener){

        GeoPoint cafeLocation = new GeoPoint(cfLocation.getLatitude(), cfLocation.getLongitude());
        Query query = firestore.collection("CafeShop")
                .whereEqualTo("location", cafeLocation);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    // Lấy ID của quán cafe từ tài liệu đầu tiên trong kết quả truy vấn
                    String cafeId = task.getResult().getDocuments().get(0).getId();

                    listener.onShopIdReceived(cafeId);

                }
            }
        });
    }

    private void checkHistory(String shopId, String userId) {

        Query query = firestore.collection("historySearch")
                .whereEqualTo("userId", userId)
                .whereEqualTo("shopId", shopId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    HashMap<String, Object> history = new HashMap<>();
                    history.put("userID", userId);
                    history.put("shopID", shopId);
                    firestore.collection("historySearch").add(history);
                } else {

                }
            }
        });
    }

}

