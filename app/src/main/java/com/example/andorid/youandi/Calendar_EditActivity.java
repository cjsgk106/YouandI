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

import java.util.ArrayList;
import java.util.Calendar;

public class Calendar_EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView dateText;
    private static ArrayList<String> eventList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__edit);
        dateText = findViewById(R.id.dateText);



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

    public void clickAdd(View view){
        TextView eventNameView = findViewById(R.id.editText);
        String eventName = eventNameView.getText().toString();
        TextView eventDateView = findViewById(R.id.dateText);
        String eventDate = eventDateView.getText().toString();
        String event = eventName + ": " + eventDate;
        eventList.add(event);
        SharedPreferences sharedPreferences = getSharedPreferences("YouandI", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//////////////
        int j=0;
        String id = "Event" + j;
        for(int i = 0; i < eventList.size(); i++){
            editor.putString(id, event);
            j++;
        }
//        int j=0;
//        String id = "Event" + j;
//        editor.putString(id, event);
/////////////
        editor.commit();


        Intent intent = new Intent(this, Calendar_EventActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        dateText.setText(date);
    }
}
