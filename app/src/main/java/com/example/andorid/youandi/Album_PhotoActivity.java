package com.example.andorid.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.andorid.youandi.model.ImageAdapter;

public class Album_PhotoActivity extends AppCompatActivity {

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album__photo);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("Date") != null){
                Toast.makeText(getApplicationContext(),
                        "data" + bundle.getString("Date"),
                        Toast.LENGTH_SHORT).show();
            }
        }

        gridView = (GridView) findViewById(R.id.grid_view);

        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FullimageActivity.class);
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });
    }
}
