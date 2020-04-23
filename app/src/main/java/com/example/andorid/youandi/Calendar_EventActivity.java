package com.example.andorid.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Calendar_EventActivity extends AppCompatActivity {
    private ArrayAdapter<String> listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__event);

        ListView listView = (ListView) findViewById(R.id.List);
        String eventname = getIntent().getStringExtra("EventName");
        String eventdate = getIntent().getStringExtra("EventDate");

        listAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);


    }


}
