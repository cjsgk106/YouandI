package com.example.andorid.youandi;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, SettingDialogFragment.DialogListener{

    private static final int PICK_FROM_ALBUM = 10;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    private DatabaseReference firebaseDatabase;
    private ImageView myPicture;
    private ImageView addIcon;
    private TextView myName;
    private TextView myEmail;
    private TextView partnerEmail;
    private TextView myPassword;
    private Button logout;
    private String currentUserImage;
    private SettingDialogFragment settingDialogFragment;
    private String myuid;
    private String myUsername;
    private Bundle bundle;
    private LinearLayout nameLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        myuid = firebaseAuth.getCurrentUser().getUid();

        nameLinearLayout = (LinearLayout) findViewById(R.id.settingActivity_linearlayout_namelayout);
        nameLinearLayout.setOnClickListener(this);

        myPicture = (ImageView) findViewById(R.id.settingActivity_imageview_picture);
        myPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        firebaseDatabase.child("users").child(myuid).child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot item = dataSnapshot;
                    currentUserImage = item.getValue().toString();
                    if (!currentUserImage.equals("")) {
                        Glide.with
                                (getApplicationContext())
                                .load(currentUserImage)
                                .apply(new RequestOptions().circleCrop())
                                .into(myPicture);
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

        myName = (TextView) findViewById(R.id.settingActivity_textview_name);
        firebaseDatabase.child("users").child(myuid).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myName.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myEmail = (TextView) findViewById(R.id.settingActivity_textview_myemail);
        firebaseDatabase.child("users").child(myuid).child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    myEmail.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        partnerEmail = (TextView) findViewById(R.id.settingActivity_textview_partneremail);
        firebaseDatabase.child("users").child(myuid).child("partnerEmail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    partnerEmail.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myPassword = (TextView) findViewById(R.id.settingActivity_textview_changepw);
        myPassword.setOnClickListener(this);

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
                uploadImage();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settingActivity_linearlayout_namelayout:
                settingDialogFragment = new SettingDialogFragment();
                bundle = new Bundle();
                firebaseDatabase.child("users").child(myuid).child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        myUsername = dataSnapshot.getValue().toString();
                        Log.d("ondatachange", myUsername);
                        bundle.putString("name", myUsername);
                        settingDialogFragment.setArguments(bundle);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);
                        settingDialogFragment.show(ft, "dialog");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;

            /*case R.id.:
                settingDialogFragment = new SettingDialogFragment();


                ft = getSupportFragmentManager().beginTransaction();
                prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);


                dialogFragment.show(ft, "dialog");
                break;       */
        }
    }

    @Override
    public void onFinishEditDialog(String input) {
        if (!TextUtils.isEmpty(input)) {
            firebaseDatabase.child("users").child(myuid).child("userName").setValue(input);
        }
    }

}
