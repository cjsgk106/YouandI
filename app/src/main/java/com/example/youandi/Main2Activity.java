package com.example.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void clickFunction(View view){
        EditText et = (EditText) findViewById(R.id.editText);
        goToAct3();

    }

    public void goToAct3(){
        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);
    }
}
