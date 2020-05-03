package com.example.andorid.youandi.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andorid.youandi.R;
import com.example.andorid.youandi.calendar.Calendar_EditActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LocationViewActivity extends FragmentActivity implements OnMapReadyCallback {


    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private double lat;
    private double lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();


        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String lata = Double.toString(lat);
                String lona = Double.toString(lon);
                if(lona.length() !=0 && lata.length() != 0){
                    firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("Location").child("longitude").setValue(lona);
                    firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("Location").child("latitude").setValue(lata);
                    LatLng tmp = new LatLng(lat, lon);
                    arrayList.add(tmp);
//                    mMap.addMarker(new MarkerOptions().position(tmp).title("visited"));
                }else{
                    Toast.makeText(LocationViewActivity.this, "Need to pick a location", Toast.LENGTH_LONG).show();
                }

            }
        });

        Button inButton = (Button) findViewById(R.id.InButton);
        inButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        Button outButton = (Button) findViewById(R.id.OutButton);
        outButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        Button showButton = (Button) findViewById(R.id.showButton);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < arrayList.size(); i ++){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(arrayList.get(i));
                    markerOptions.title("visited");
                    Marker marker = mMap.addMarker(markerOptions);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Click Add");
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    Marker marker = mMap.addMarker(markerOptions);
//                    marker.remove();
                    lat = latLng.latitude;
                    lon = latLng.longitude;

                }
            });
    }
}
