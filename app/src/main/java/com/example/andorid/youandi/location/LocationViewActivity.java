package com.example.andorid.youandi.location;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andorid.youandi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class LocationViewActivity extends FragmentActivity implements OnMapReadyCallback {


    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<LatLng> locList = new ArrayList<>();
    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private double lat;
    private double lon;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        reference = firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("Location");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String tmp=  snapshot.getValue().toString();
                    arrayList.add(tmp);
                }
                for(int i=0; i < arrayList.size(); i++){
                    String[] tmp2 = arrayList.get(i).split(" ");
                    LatLng latLng = new LatLng(Double.valueOf(tmp2[0].substring(9, 26)), Double.valueOf(tmp2[1].substring(0,15)));
                    locList.add(latLng);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String latlon = Double.toString(lat)+ " " + Double.toString(lon);
                if(latlon.equals(" ")!= true ){
                    firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("Location").push().child("lat&lon").setValue(latlon);
                    LatLng tmp = new LatLng(lat, lon);
                    locList.add(tmp);
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
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.heart);
                Bitmap a = bitmapdraw.getBitmap();
                Bitmap resized = Bitmap.createScaledBitmap(a, 100, 100,false);
                for(int i = 0; i < locList.size(); i ++){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(locList.get(i));
                    markerOptions.title("visited");
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resized));
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
