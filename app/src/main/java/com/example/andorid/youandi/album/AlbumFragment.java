package com.example.andorid.youandi.album;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.andorid.youandi.R;
import com.example.andorid.youandi.album.Album_PhotoActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AlbumFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_album, container, false);

        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Album_PhotoActivity.class);
                intent.putExtra("Date", "data");
                startActivity(intent);
            }
        });

//        Button addbutton = (Button) view.findViewById(R.id.add_button);
//        button.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//
//                }
//
//            }
//        });

        return view;
    }
}
