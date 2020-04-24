package com.example.andorid.youandi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        contextOfApplication = getApplicationContext();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
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

            /*if(item.getItemId() == R.id.nav_home){
                selectedFragment = new HomeFragment();
            }
            else if (item.getItemId() == R.id.nav_chat){
                selectedFragment = new ChatFragment();
            }
            else if (item.getItemId() == R.id.nav_album){
                selectedFragment = new AlbumFragment();
            }
            else if (item.getItemId() == R.id.nav_calendar){
                selectedFragment = new CalendarFragment();
            }
            else if (item.getItemId() == R.id.nav_location){
                selectedFragment = new LocationFragment();
            }
            else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                return true;
            }*/

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
}
