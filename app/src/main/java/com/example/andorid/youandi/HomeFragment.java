package com.example.andorid.youandi;


import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.net.URI;


public class HomeFragment extends Fragment {
//    ImageButton btn;
    TextView textView;
    String shared = "Shared";
//    URI imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ImageButton button = (ImageButton) view.findViewById(R.id.imageButton6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(shared, Context.MODE_PRIVATE);
        textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(sharedPreferences.getString("DATE", "Choose Date"));
        String encoded = sharedPreferences.getString("image", "");
        byte[] images = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(images, 0, images.length);
        bitmap = getResizedBitmap(bitmap, 900, 900);
        button.setImageBitmap(bitmap);

//
//
//        button.setImageBitmap(bitmap);


//        imageUri = URI.create(sharedPreferences.getString("imageURI",""));
//        Log.w("kkkkkkkk", imageUri.toString());
//        btn.setImageURI(null);
//        btn.setImageURI(android.net.Uri.parse(imageUri.toString()));
//        if (sharedPreferences.getString("imageURI", "") != "") {
//            btn.setImageURI(Uri.parse(sharedPreferences.getString("imageURI", "")));
//        }
        return view;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();

        return resizedBitmap;
    }

}

