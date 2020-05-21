package com.example.andorid.youandi;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class LoginDialogFragment extends DialogFragment {

    private FirebaseAuth firebaseAuth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        return inflater.inflate(R.layout.fragment_login_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText name = view.findViewById(R.id.fragment_login_dialog_edittext_name);

        TextView nameCountText = view.findViewById(R.id.fragment_login_dialog_textview_name_textcount);
        nameCountText.setText(name.getText().toString().length()+"/20");
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                nameCountText.setText((start+count)+"/20");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        final EditText email = view.findViewById(R.id.fragment_login_dialog_edittext_email);

        TextView emailCountText = view.findViewById(R.id.fragment_login_dialog_textview_email_textcount);
        emailCountText.setText(email.getText().toString().length()+"/100");
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                emailCountText.setText((start+count)+"/100");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button okButton = view.findViewById(R.id.fragment_login_dialog_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "You Must Submit This Information", Toast.LENGTH_LONG).show();
                }
                else if (!isEmailValid(email.getText().toString())) {
                    Toast.makeText(getContext(), "Enter a Valid Email Address", Toast.LENGTH_LONG).show();
                }
                else {
                    DialogListener dialogListener = (DialogListener) getActivity();
                    dialogListener.onFinishEditDialog(name.getText().toString(), email.getText().toString());
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface DialogListener {
        void onFinishEditDialog(String name, String email);
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
