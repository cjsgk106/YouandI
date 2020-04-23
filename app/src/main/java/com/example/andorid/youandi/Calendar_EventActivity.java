package com.example.andorid.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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

    public static final String SHARED_PREF = "sharedPref";
    public static final String text = "text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar__event);

        //load event
        SharedPreferences sharedPreferences = getSharedPreferences("YouandI", Context.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.listview);

        ////////////
        int j=0;
        String id = "Event" + j;
        String tmp = "0";
        while (!tmp.equals("")){
            tmp = sharedPreferences.getString(id, "");
            arrayList.add(tmp);
            j++;
        }
        ////////////
//        int j=0;
//        String id = "Event" + j;
//        String tmp = "0";
//        tmp = sharedPreferences.getString(id, "");
//        arrayList.add(tmp);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);



    }


}
