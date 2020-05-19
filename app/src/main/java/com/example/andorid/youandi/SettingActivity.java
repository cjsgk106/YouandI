package com.example.andorid.youandi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.andorid.youandi.model.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    private DatabaseReference firebaseDatabase;
    private ImageView myPicture;
    private ImageView addIcon;
    private EditText myName;
    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        myPicture = (ImageView) findViewById(R.id.settingActivity_imageview_picture);
        myPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        String myuid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    UserModel userModel = item.getValue(UserModel.class);
                    if (userModel.uid.equals(myuid)) {
                        if (!userModel.image.equals("")) {
                            Glide.with
                                    (getApplicationContext())
                                    .load(userModel.image)
                                    .apply(new RequestOptions().circleCrop())
                                    .into(myPicture);
                        }
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addIcon = (ImageView) findViewById(R.id.settingActivity_imageview_addicon);
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        myName = (EditText) findViewById(R.id.settingActivity_edittext_name);

        edit = (Button) findViewById(R.id.settingActivity_button_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
                startActivity(new Intent(SettingActivity.this, NavigationActivity.class));
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_ALBUM
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            imageUri = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getApplicationContext().getContentResolver(),
                                imageUri
                        );
                myPicture.setImageBitmap(bitmap);
                //uploadImage();
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (imageUri != null) {

            StorageReference ref
                    = storageReference
                    .child("profileImages").child(firebaseAuth.getCurrentUser().getUid());

            // adding listeners on upload
            // or failure of image
            Task<Uri> urltask = ref.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Toast.makeText(getApplicationContext(),
                                "Image Uploaded!!",
                                Toast.LENGTH_SHORT).show();
                        if (downloadUri != null) {

                            String profileImageUrl  = downloadUri.toString();
                            firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("image").setValue(profileImageUrl);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Failed " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
