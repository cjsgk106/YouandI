package com.example.andorid.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
public class Calendar_EventActivity extends AppCompatActivity {


    ListView listView;
    ArrayList<String> listItem ;
    DatabaseHelper db;
    ArrayAdapter adapter;

    public static final String SHARED_PREF = "sharedPref";
    public static final String text = "text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__event);

        listView = (ListView) findViewById(R.id.listview);
        listItem = new ArrayList<>();
        viewData();

    }

    private void viewData() {
        Cursor cursor = db.viewData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }else {
            while(cursor.moveToNext()){
                listItem.add(cursor.getString(0));
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
            listView.setAdapter(adapter);
        }
    }


}
