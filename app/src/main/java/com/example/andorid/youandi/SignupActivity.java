package com.example.andorid.youandi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andorid.youandi.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText email;
    private EditText password;
    private EditText name;
    private Button signup;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = (EditText) findViewById(R.id.signupActivity_edittext_email);
        password = (EditText) findViewById(R.id.signupActivity_edittext_password);
        name = (EditText) findViewById(R.id.signupActivity_edittext_name);
        signup = (Button) findViewById(R.id.signupActivity_button_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateForm()) {
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signup:onComplete:" + task.isSuccessful());

                                if (task.isSuccessful()) {
                                    onAuthSuccess(task.getResult().getUser());
                                    //UserModel userModel = new UserModel(name.getText().toString(), email.getText().toString(), password.getText().toString());

                                    //String uid = task.getResult().getUser().getUid();
                                    //firebaseDatabase.getReference().child("users").setValue(userModel);
                                } else {
                                    Toast.makeText(SignupActivity.this, "Signup failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = name.getText().toString();
        // Write new user
        UserModel userModel = new UserModel(username, user.getEmail());
        firebaseDatabase.child("users").child(user.getUid()).setValue(userModel);

        // Go to NavigationActivity
        startActivity(new Intent(SignupActivity.this, NavigationActivity.class));
        finish();
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Required");
            result = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Required");
            result = false;
        } else {
            password.setError(null);
        }

        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Required");
            result = false;
        } else {
            name.setError(null);
        }

        return result;
    }
}
