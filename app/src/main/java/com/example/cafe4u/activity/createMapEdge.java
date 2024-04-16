package com.example.cafe4u.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cafe4u.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

public class createMapEdge extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;
    private DocumentReference firstDocumentRef = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_map_edge);

        db = FirebaseFirestore.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(createMapEdge.this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mylc = new LatLng(20, 105); //location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylc, 12));
        MarkerOptions options = new MarkerOptions().position(mylc).title("You are here");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Add a marker to the clicked location
                mMap.addMarker(new MarkerOptions().position(latLng).title("Clicked Location"));

                saveLocationToFirestore(latLng);
            }
        });


    }

    private void saveLocationToFirestore(LatLng latLng) {
        if (firstDocumentRef == null) {
            // Create a new document for the first location
            GeoPoint geoPoint = new GeoPoint(latLng.latitude, latLng.longitude);
            db.collection("map").add(new HashMap<String, Object>() {{
                        put("coordinates", geoPoint);
                    }})
                    .addOnSuccessListener(documentReference -> {
                        // Document successfully written
                        firstDocumentRef = documentReference;
                    })
                    .addOnFailureListener(e -> {
                        // Log the error message
                    });
        } else {
            // Update the first document with the edge field
            firstDocumentRef.update("edge", FieldValue.arrayUnion(new LatLng(latLng.latitude, latLng.longitude)))
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Edge updated successfully"))
                    .addOnFailureListener(e -> Log.e(TAG, "Error updating edge", e));
        }
    }
}