package com.example.andorid.youandi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import static java.util.Base64.*;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final int PICK_FROM_ALBUM = 10;
    Button btn;
    ImageView imageView;
    TextView dateText;
    String shared = "Shared";
    Uri imageUri;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    byte[] bytesImage;
    DatabaseHelper mydb;
    Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView = findViewById(R.id.profile);
        btn = findViewById(R.id.pickPhoto);
        dateText = findViewById(R.id.dateText);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        String myuid = firebaseAuth.getCurrentUser().getUid();


        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "pick image"), GALLERY_REQUEST_CODE);
            }
        });

        findViewById(R.id.pickDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });




    }
    public void finishEdit(View view){
        uploadImage();
//        // convert bitmap to byte array
//        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//        ByteArrayOutputStream ba = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ba);
//        bytesImage = ba.toByteArray();
//
//        String encode = Base64.encodeToString(bytesImage, Base64.DEFAULT);
//
//        //keep date with sharedpreference
//        SharedPreferences sharedPreferences = getSharedPreferences(shared, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("DATE",dateText.getText().toString());
//        editor.putString("image", encode);
//        editor.commit();
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                bitmap = getResizedBitmap(bitmap, 450, 450);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK && data != null){
//                imageUri = data.getData();
//            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            bitmap = getResizedBitmap(bitmap, imageView.getWidth(), imageView.getHeight());
//            imageView.setImageBitmap(bitmap);
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
                        Toast.makeText(getApplicationContext(),"Image Uploaded!!", Toast.LENGTH_LONG).show();
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = month + "/" + dayOfMonth + "/" + year;
        dateText.setText(date);
    }

    public void clickFunction(View view){
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

//    public void goToAct1(){
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//    }
}
