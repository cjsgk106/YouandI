package com.example.andorid.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Calendar_EventActivity extends AppCompatActivity {
    ArrayAdapter<String> listAdapter;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__event);

        listView = (ListView) findViewById(R.id.listview);
        String eventname = getIntent().getStringExtra("EventName");
        String eventdate = getIntent().getStringExtra("EventDate");
        String event = eventname + ": " + eventdate;
        arrayList.add(event);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);



    }


}
