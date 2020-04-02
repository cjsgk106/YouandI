package com.example.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickFunction(View view){
        EditText et = (EditText) findViewById(R.id.editText);
        goToAct2();

    }

    public void goToAct2(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
