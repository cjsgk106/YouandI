package com.example.andorid.youandi.location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.example.andorid.youandi.R;
import com.example.andorid.youandi.calendar.Calendar_EditActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LocationViewActivity extends FragmentActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private FusedLocationProviderClient mFusedLocationProviderClient;
//    private final LatLng mDestinationLatLng = new LatLng(43.0753918, -89.4047987);
        private final LatLng mDestinationLatLng = null;

    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            if(mDestinationLatLng == null){
                Toast.makeText(LocationViewActivity.this, "Cannot Find Partner's Location", Toast.LENGTH_LONG).show();
            }else{
                mMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destination"));
                displayMyLocation();
            }

        });

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
    private void displayMyLocation(){
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else{
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location mLastKnowLocation = task.getResult();

                if(task.isSuccessful() && mLastKnowLocation != null){
                    mMap.addPolyline(new PolylineOptions().add(new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude()), mDestinationLatLng));
                    LatLng current = new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(current).title("Current"));
                }
            });
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                displayMyLocation();
            }
        }
    }

}
