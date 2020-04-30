package com.example.andorid.youandi.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andorid.youandi.DatabaseHelper;
import com.example.andorid.youandi.R;

import java.util.ArrayList;
public class Calendar_EventActivity extends AppCompatActivity {
    DatabaseHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__event);

        ListView listView =(ListView) findViewById(R.id.listview);
        mydb = new DatabaseHelper(this);

        ArrayList<String> thelist = new ArrayList<>();
        Cursor data = mydb.getListContents();

        if(data.getCount() == 0){
            Toast.makeText(Calendar_EventActivity.this, "There is no Event", Toast.LENGTH_LONG).show();
        }else{
            while (data.moveToNext()){
                thelist.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, thelist);
                listView.setAdapter(listAdapter);
            }
        }


    }





}
