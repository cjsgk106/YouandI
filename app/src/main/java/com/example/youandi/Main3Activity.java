package com.example.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void clickFunction(View view){
        EditText et = (EditText) findViewById(R.id.editText);
        goToAct1();

    }

    public void goToAct1(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
