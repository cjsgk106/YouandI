package com.gerrard.android.youandi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.gerrard.android.youandi.album.AlbumFragment;
import com.gerrard.android.youandi.calendar.CalendarFragment;
import com.gerrard.android.youandi.chat.ChatFragment;
import com.gerrard.android.youandi.location.LocationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavigationActivity extends AppCompatActivity implements LoginDialogFragment.DialogListener{

    private LoginDialogFragment loginDialogFragment;
    private DatabaseReference firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private String myuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        contextOfApplication = getApplicationContext();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        firebaseAuth = FirebaseAuth.getInstance();
        myuid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.child("users").child(myuid).orderByKey().equalTo("userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    loginDialogFragment = new LoginDialogFragment();
                    loginDialogFragment.setCancelable(false);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    loginDialogFragment.show(ft, "dialog");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment =  null;


            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_chat:
                    selectedFragment = new ChatFragment();
                    break;
                case R.id.nav_album:
                    selectedFragment = new AlbumFragment();
                    break;
                case R.id.nav_calendar:
                    selectedFragment = new CalendarFragment();
                    break;
                case R.id.nav_location:
                    selectedFragment = new LocationFragment();
                    break;
            }


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


    @Override
    public void onFinishEditDialog(String name, String email) {
        if (!TextUtils.isEmpty(name)) {
            firebaseDatabase.child("users").child(myuid).child("userName").setValue(name);
        }
        if (!TextUtils.isEmpty(email)) {
            firebaseDatabase.child("users").child(myuid).child("partnerEmail").setValue(email);
        }
    }
}
