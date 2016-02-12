package com.bizan.mobile10.passgene;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InitialSet1 extends AppCompatActivity
        implements View.OnClickListener {

    Button btn;
    private TextInputLayout inputLayoutL;
    private TextInputLayout inputLayoutF;
    private EditText inputLastname;
    private EditText inputFirstname;
    static String name ;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_set1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = (Button) findViewById(R.id.btnInitialSet1);
        btn.setOnClickListener(this);

        inputLastname = (EditText)findViewById(R.id.inputLastname);
        inputFirstname = (EditText)findViewById(R.id.inputFirstname);

        inputLayoutL = (TextInputLayout) findViewById(R.id.inputUserLastname);
        inputLayoutL.setError("姓をアルファベットで入力して下さい。"); // show error
        inputLayoutL.setError(null); // hide error

        inputLayoutF = (TextInputLayout) findViewById(R.id.inputUserFirstname);
        inputLayoutF.setError("名をアルファベットで入力して下さい。"); // show error
        inputLayoutF.setError(null); // hide error

        inputLastname.addTextChangedListener(new PGTextWatcher(inputLayoutL));
        inputFirstname.addTextChangedListener(new PGTextWatcher(inputLayoutF));


    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateLastname()) {
            return;
        }

        if (!validateFirstname()) {
            return;
        }
        String tmpLast = inputLastname.getText().toString();
        String tmpFirst = inputFirstname.getText().toString();
        //ユーザーの名前を変数nameに・・・
        name = tmpLast + " " + tmpFirst;

        toast("はじめまして" + tmpLast + " " + tmpFirst +"さん、\n次はマスターパスワードを決めてください。");
        Intent intent = new Intent(InitialSet1.this, InitialSet2.class);
        startActivity(intent);
    }

    private boolean validateLastname() {
        if (inputLastname.getText().toString().trim().isEmpty()) {
            inputLayoutL.setError(getString(R.string.err_msg_lastname));
            requestFocus(inputLastname);
            return false;
        } else {
            inputLayoutL.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateFirstname() {
        if (inputFirstname.getText().toString().trim().isEmpty()) {
            inputLayoutF.setError(getString(R.string.err_msg_firstname));
            requestFocus(inputFirstname);
            return false;
        } else {
            inputLayoutF.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class PGTextWatcher implements TextWatcher {

        private View view;

        private PGTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.inputUserLastname:
                    validateLastname();
                    break;
                case R.id.inputUserFirstname:
                    validateFirstname();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {

        //ここにDBへの登録コードを記述

        submitForm();



    }

    /**
     * あったら便利！トーストメソッドだよ
     *
     * @param text
     */
    private void toast(String text) {
        if (text == null) {
            text = "";
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
