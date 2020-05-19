package com.example.andorid.youandi;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {
    private TextView datecount_textview;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    private static final int PICK_FROM_ALBUM = 10;
    private ImageView coupleImageView;
    private ImageView myImageView;
    private ImageView yourImageView;
    private DatabaseReference firebaseDatabase;
    private DatePickerDialog picker;
    private long startDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.fragment_home_toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#f7dce2"));
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        TextView toolbarTitle = (TextView) view.findViewById(R.id.fragment_hom_toolbartitle);
        toolbarTitle.setText("YOU & I");
        setHasOptionsMenu(true);

        datecount_textview = (TextView) view.findViewById(R.id.fragment_home_textview_datecount);
        datecount_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        cldr.set(year, month, day);
                        startDate = cldr.getTimeInMillis();
//                        firebaseDatabase.child("dates").setValue(startDate);
                        firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("dates").setValue(startDate);
                    }
                }, year, month, day);
                picker.getDatePicker().setMaxDate(System.currentTimeMillis());
                picker.show();
            }
        });

        firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("dates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot item = dataSnapshot;
                    startDate = Long.parseLong(item.getValue().toString());
                    Calendar today = Calendar.getInstance();
                    today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                    long endDate = today.getTimeInMillis();
                    long diff = endDate - startDate;
                    int diffdays = (int) (diff / (1000*60*60*24));
                    datecount_textview.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                    datecount_textview.setText(Integer.toString(diffdays+1));
                    datecount_textview.setTextSize(40);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        coupleImageView = (ImageView) view.findViewById(R.id.fragment_home_imagebutton);
    /*    String myuid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    UserModel userModel = item.getValue(UserModel.class);
                    if (userModel.uid.equals(myuid)) {
                        if (!userModel.image.equals("")) {
                            Glide.with
                                    (view)
                                    .load(userModel.image)
                                    .apply(new RequestOptions().circleCrop())
                                    .into(imageView);
                        }
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); */

        coupleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        myImageView = (ImageView) view.findViewById(R.id.fragment_home_mypicture);
        String myuid = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    UserModel userModel = item.getValue(UserModel.class);
                    if (userModel.uid.equals(myuid)) {
                        if (!userModel.image.equals("")) {
                            Glide.with
                                    (view)
                                    .load(userModel.image)
                                    .apply(new RequestOptions().circleCrop())
                                    .into(myImageView);
                        }
                        break;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
        myImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });*/

        return view;
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
                        .getBitmap(getActivity().getApplicationContext().getContentResolver(),
                                imageUri
                        );
                coupleImageView.setImageBitmap(bitmap);
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
                        Toast.makeText(getActivity(),
                                "Image Uploaded!!",
                                Toast.LENGTH_SHORT).show();
                        if (downloadUri != null) {

                            String profileImageUrl  = downloadUri.toString();
                            firebaseDatabase.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("image").setValue(profileImageUrl);
                        }

                    } else {
                        Toast.makeText(getActivity(),
                                "Failed " + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.setting_actionbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                firebaseAuth = firebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return true;
            case R.id.action_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                return true;
        }
        return false;
    }

}

