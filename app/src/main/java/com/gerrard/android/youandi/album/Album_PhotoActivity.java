package com.gerrard.android.youandi.album;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gerrard.android.youandi.NavigationActivity;
import com.gerrard.android.youandi.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Album_PhotoActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;
    Button btn;
    ImageView imageView;
    TextView textView;
    Uri imageUri;
    Button cancel;
    private FirebaseAuth firebaseAuth;
    DatabaseReference mDatabaseRef;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album__photo);

        imageView = findViewById(R.id.newpic);
        btn = findViewById(R.id.pickPhoto);
        textView = findViewById(R.id.text);
        cancel = findViewById(R.id.cancel);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
            Picasso.with(this)
                    .load(imageUri)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }}

    public void finishEdit(View view){
         StorageReference fileRef =  mStorageRef.child(firebaseAuth.getCurrentUser().getUid()).child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");

        fileRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    Toast.makeText(Album_PhotoActivity.this, "Upload success", Toast.LENGTH_LONG).show();
                    Upload upload = new Upload(textView.getText().toString().trim(), downloadUri.toString());
                    String uploadId = dbRef.push().getKey();
                    dbRef.child(firebaseAuth.getCurrentUser().getUid()).child("album").child(uploadId).setValue(upload);
                }
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
