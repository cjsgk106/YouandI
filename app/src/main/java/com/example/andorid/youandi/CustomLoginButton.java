package com.example.andorid.youandi;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomLoginButton extends LinearLayout {
    LinearLayout linearLayout;
    ImageView symbol;
    TextView text;

    public CustomLoginButton(Context context) {
        super(context);
        initView();
    }

    public CustomLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CustomLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_login, this, false);
        addView(v);
        linearLayout = (LinearLayout) findViewById(R.id.custom_login_linearlayout);
        symbol = (ImageView) findViewById(R.id.custom_login_imageview);
        text = (TextView) findViewById(R.id.custom_login_textview);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoginButton);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoginButton, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int bg_resID = typedArray.getResourceId(R.styleable.LoginButton_layout_bg, R.color.white);
        linearLayout.setBackgroundResource(bg_resID);

        int symbol_resID = typedArray.getResourceId(R.styleable.LoginButton_layout_symbol, R.drawable.login_google_symbol);
        symbol.setImageResource(symbol_resID);

        int textColor = typedArray.getColor(R.styleable.LoginButton_layout_textColor, 0);
        text.setTextColor(textColor);

        String text_string = typedArray.getString(R.styleable.LoginButton_layout_text);
        text.setText(text_string);

        typedArray.recycle();
    }

}
