package com.example.andorid.youandi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.andorid.youandi.model.ImageAdapter;

public class FullimageActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullimage);

        imageView = (ImageView) findViewById(R.id.fullImage);
//        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Full Screen");

        Intent i =  getIntent();
        int position = i.getExtras().getInt("id");
        ImageAdapter imageAdapter = new ImageAdapter(this);

        imageView.setImageResource(imageAdapter.imageArray[position]);
    }
}
