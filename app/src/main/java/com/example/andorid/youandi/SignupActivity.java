package com.example.andorid.youandi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andorid.youandi.model.UserModel;
import com.example.andorid.youandi.model.UserModel1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText name;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = (EditText) findViewById(R.id.signupActivity_edittext_email);
        password = (EditText) findViewById(R.id.signupActivity_edittext_password);
        name = (EditText) findViewById(R.id.signupActivity_edittext_name);
        signup = (Button) findViewById(R.id.signupActivity_button_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString()==null || password.getText().toString()==null || name.getText().toString()==null) {
                    return;
                }

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // After Sign up
                                if (task.isSuccessful()) {
                                    UserModel userModel = new UserModel(name.getText().toString(), email.getText().toString(), password.getText().toString());

                                    String uid = task.getResult().getUser().getUid();
                                    FirebaseDatabase.getInstance().getReference().child("users").setValue(userModel);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
    }
}
