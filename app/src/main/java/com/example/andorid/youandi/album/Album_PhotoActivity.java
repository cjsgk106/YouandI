package com.example.andorid.youandi.album;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andorid.youandi.NavigationActivity;
import com.example.andorid.youandi.R;
import com.example.andorid.youandi.model.ImageAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Album_PhotoActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;
    Button btn;
    ImageView imageView;
    TextView textView;
    Uri imageUri;
    byte[] bytesImage;
    Button cancel;

//    LinearLayout layout;

//    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album__photo);

        imageView = findViewById(R.id.newpic);
        btn = findViewById(R.id.pickPhoto);
        textView = findViewById(R.id.text);
        cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "pick image"), GALLERY_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = getResizedBitmap(bitmap, imageView.getWidth(), imageView.getHeight());
            imageView.setImageBitmap(bitmap);
        }}

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

    public void finishEdit(View view){
        StorageReference storageRef =  FirebaseStorage.getInstance().getReference("album");
        StorageReference photoRef = storageRef.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        // new from here
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("album");

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ba);
        bytesImage = ba.toByteArray();

        UploadTask uploadTask = photoRef.putBytes(bytesImage);
//        UploadTask uploadTask = photoRef.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Album_PhotoActivity.this, "Upload failed", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Album_PhotoActivity.this, "Upload success", Toast.LENGTH_LONG).show();
                Upload upload = new Upload(textView.getText().toString().trim(), taskSnapshot.getStorage().getDownloadUrl().toString());
                String uploadId = dbRef.push().getKey();
                dbRef.child(uploadId).setValue(upload);
            }
        });

        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void cancel(){
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }
}
