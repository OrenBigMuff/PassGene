package com.bizan.mobile10.passgene;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;


public class PwConf extends Activity {

    EditText addressEdt;        //アドレスのエディットテキスト
    EditText passwordEdt;       //passwordのエディットテキスト
    EditText accountEdt;        //アカウントのエディットテキスト

    Button hensyuBtn;           //編集ボタン
    Button deleteBtn;           //削除ボタン

    String service = "facebook";
    String userid = "syun";
    String mail = "bizan@g.mail";
    String passward = "dfefedfcs";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_conf);

        addressEdt = (EditText) findViewById(R.id.pwconf_address_edt);
        addressEdt.setText(service);
        String test = addressEdt.getText().toString();

        passwordEdt = (EditText) findViewById(R.id.pwconf_Password_edt);
        passwordEdt.setText(passward);
        String test2 = passwordEdt.getText().toString();

        accountEdt = (EditText) findViewById(R.id.pwconf_account_edt);
        accountEdt.setText(mail);
        String test3 = accountEdt.getText().toString();

        //編集ボタン
        hensyuBtn = (Button) findViewById(R.id.pwconf_hensyubtn);
        hensyuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //P4PW最終確認画面に飛ばす
//                Intent intent = new Intent(PwConf.this,FixPass.class);
//                startActivity(intent);

            }
        });

        //削除ボタン
        deleteBtn = (Button) findViewById(R.id.pwconf_deletebtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
