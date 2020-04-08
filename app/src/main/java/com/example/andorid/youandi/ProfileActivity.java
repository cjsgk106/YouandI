package com.example.andorid.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void clickFunction(View view){
        goToAct1();

    }

    public void goToAct1(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
