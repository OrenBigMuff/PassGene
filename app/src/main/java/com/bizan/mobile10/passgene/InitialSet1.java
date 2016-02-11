package com.bizan.mobile10.passgene;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class InitialSet1 extends AppCompatActivity
        implements View.OnClickListener {

    Button btn;
    TextInputLayout inputLayoutL;
    TextInputLayout inputLayoutF;
    EditText inputLastname;
    EditText inputFirstname;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_set1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = (Button) findViewById(R.id.btnInitialSet1);
        btn.setOnClickListener(this);

        inputLayoutL = (TextInputLayout) findViewById(R.id.inputUserLastname);
        inputLayoutL.setError("姓をアルファベットで入力して下さい。"); // show error
        inputLayoutL.setError(null); // hide error

        inputLayoutF = (TextInputLayout) findViewById(R.id.inputUserFirstname);
        inputLayoutF.setError("名をアルファベットで入力して下さい。"); // show error
        inputLayoutF.setError(null); // hide error


    }

    @Override
    public void onClick(View v) {
        inputLastname = (EditText) findViewById(R.id.inputLastname);
        inputFirstname = (EditText) findViewById(R.id.inputFirstname);

        if (inputLastname.getText().toString().equals("") || inputFirstname.getText().toString().equals("")) {
//アラートダイアログを出すコード
        }else {
            Intent intent = new Intent(InitialSet1.this, InitialSet2.class);
            startActivity(intent);
        }
    }
}
