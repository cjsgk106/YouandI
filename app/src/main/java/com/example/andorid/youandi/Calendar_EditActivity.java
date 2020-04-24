package com.example.andorid.youandi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Calendar_EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    DatabaseHelper db;
    private TextView dateText;
    private static ArrayList<String> eventList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__edit);
        dateText = findViewById(R.id.dateText);
        final TextView eventNameView = findViewById(R.id.editText);
        final String eventName = eventNameView.getText().toString();
        final TextView eventDateView = findViewById(R.id.dateText);
        final String eventDate = eventDateView.getText().toString();
        final String event = eventName + ": " + eventDate;
        db = new DatabaseHelper(this);
        Button addbutton = findViewById(R.id.button3);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.insertDATA(event)){
                    Toast.makeText(Calendar_EditActivity.this, "Event added", Toast.LENGTH_SHORT).show();
                    eventNameView.setText("");
                    eventDateView.setText("");
                }else{
                    Toast.makeText(Calendar_EditActivity.this, "Event not added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    public void clickCancle(View view){
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        dateText.setText(date);
    }
}
