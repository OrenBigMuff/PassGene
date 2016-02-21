package com.bizan.mobile10.passgene;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class PwConf extends Activity implements View.OnClickListener {
    Button button2;
    Button button3;
    TextView text5;
    TextView text6;
    TextView text7;
    EditText editText;
    EditText editText2;
    EditText editText3;
    String service = "facebook";
    String userid = "syun";
    String mail = "bizan@g.mail";
    String passward = "dfefedfcs";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_conf);

        text5 = (TextView) findViewById(R.id.textView5);
        text6 = (TextView) findViewById(R.id.textView6);
        text7 = (TextView) findViewById(R.id.textView7);

        editText = (EditText) findViewById(R.id.pwconf_address_edt);
        editText.setText(service);
        String test = editText.getText().toString();

        editText2 = (EditText) findViewById(R.id.pwconf_Password_edt);
        editText2.setText(passward);
        String test2 = editText.getText().toString();

        editText3 = (EditText) findViewById(R.id.pwconf_account_edt);
        editText3.setText(mail);
        String test3 = editText.getText().toString();

        button2 = (Button) findViewById(R.id.pwconf_hensyubtn);
        button3 = (Button) findViewById(R.id.pwconf_deletebtn);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    public void onClick(View v) {
        // ボタン1が押された場合
        if (v.getId() == R.id.pwconf_hensyubtn) {
            Intent intent = new Intent(PwConf.this, UserConf.class);
            startActivity(intent);
            // ボタン2が押された場合
        } else if (v.getId() == R.id.pwconf_deletebtn) {
            Intent intent = new Intent(PwConf.this, UserConf.class);
            startActivity(intent);
        }
    }


}


//public class PwConf extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pw_conf);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//}
