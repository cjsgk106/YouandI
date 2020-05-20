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


public class SettingDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText editText = view.findViewById(R.id.fragment_setting_dialog_edittext);
            Log.d("onviewcreated",getArguments().getString("name"));
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("name"))) {
            Log.d("getArguments", getArguments().getString("name"));
            editText.setText(getArguments().getString("name"));
        }

        TextView countText = view.findViewById(R.id.fragment_setting_dialog_textcount);
        countText.setText(editText.getText().toString().length()+"/20");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                countText.setText((start+count)+"/20");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button editButton = view.findViewById(R.id.fragment_setting_dialog_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogListener dialogListener = (DialogListener)getActivity();
                dialogListener.onFinishEditDialog(editText.getText().toString());
                dismiss();
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
        void onFinishEditDialog(String input);
    }
}
