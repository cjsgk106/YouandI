package com.example.andorid.youandi.album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andorid.youandi.NavigationActivity;
import com.example.andorid.youandi.R;
import com.example.andorid.youandi.album.Album_PhotoActivity;
import com.example.andorid.youandi.model.ImageAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment {
    RecyclerView mRecyclerView;
    albumAdapter mAdapter;
    DatabaseReference mDatabaseRef;
    List<Upload> mUploads;
    private FirebaseAuth firebaseAuth;

//    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_album, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUploads = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("album");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }
                mAdapter = new albumAdapter(getContext(), mUploads);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // test ro bring image from firebase storage
//        imageView = view.findViewById(R.id.newpic);
//        long ONE_MEGABYTE = 1024* 1024;
//        StorageReference storageRef =  FirebaseStorage.getInstance().getReference();
//        StorageReference photoRef = storageRef.child("photo/1");
//        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                bitmap = getResizedBitmap(bitmap, 200, 200);
//                imageView.setImageBitmap(bitmap);
//            }
//        });


        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Album_PhotoActivity.class);
                intent.putExtra("Data", "data");
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

//    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // CREATE A MATRIX FOR THE MANIPULATION
//        Matrix matrix = new Matrix();
//        // RESIZE THE BIT MAP
//        matrix.postScale(scaleWidth, scaleHeight);
//
//        // "RECREATE" THE NEW BITMAP
//        Bitmap resizedBitmap = Bitmap.createBitmap(
//                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
//
//        return resizedBitmap;
//    }
}
