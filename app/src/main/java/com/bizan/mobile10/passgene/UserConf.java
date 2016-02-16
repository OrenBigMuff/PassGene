package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class UserConf extends Activity implements OnClickListener {
    NumberPicker numPicker;
    Button button1;
    TextView textView1;
    NumberPicker numPicker2;
    TextView textView2;
    NumberPicker numPicker3;
    TextView textView3;
    NumberPicker numPicker4;
    TextView textView4;
    String textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_conf);

        findViews();
        initViews();
        findViews2();
        initViews2();
        findViews3();
        initViews3();
        findViews4();
        initViews4();

        final String rightPass = "0001";

        final String textView5 = textView1.getText().toString() +
                textView2.getText().toString() +
                textView3.getText().toString() +
                textView4.getText().toString();


        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String password = textView5;
                if (password.equals(rightPass)) {
                    // ページ遷移2
                    Intent intent = new Intent(UserConf.this, InitialSet1.class);
                    startActivity(intent);
                    Log.d("", "OK");
                } else {
                    // エラー表示
                    Log.d("", "Error");
                }
            }
        });

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


    }

    private void setSupportActionBar(Toolbar toolbar) {

    }


    private void findViews() {
        numPicker = (NumberPicker) findViewById(R.id.numPicker);
        textView1 = (TextView) findViewById(R.id.textView1);
    }

    private void initViews() {
        numPicker.setMaxValue(9);
        numPicker.setMinValue(0);

    }

    private void findViews2() {
        numPicker2 = (NumberPicker) findViewById(R.id.numPicker2);
        textView2 = (TextView) findViewById(R.id.textView2);
    }

    private void initViews2() {
        numPicker2.setMaxValue(9);
        numPicker2.setMinValue(0);

    }


    private void findViews3() {
        numPicker3 = (NumberPicker) findViewById(R.id.numPicker3);
        textView3 = (TextView) findViewById(R.id.textView3);
    }

    private void initViews3() {
        numPicker3.setMaxValue(9);
        numPicker3.setMinValue(0);

    }

    private void findViews4() {
        numPicker4 = (NumberPicker) findViewById(R.id.numPicker4);
        textView4 = (TextView) findViewById(R.id.textView4);
    }

    private void initViews4() {
        numPicker4.setMaxValue(9);
        numPicker4.setMinValue(0);

    }

    @Override
    public void onClick(View v) {

    }
}

//    @Override
//    //押された数字が正しければ次に進むのを書く
//    public void onClick(View v) {
//        textView1.setText(numPicker.getValue() + "");
//        textView2.setText(numPicker2.getValue() + "");
//        textView3.setText(numPicker3.getValue() + "");
//        textView4.setText(numPicker4.getValue() + "");
//        setContentView(R.layout.activity_pw_conf);
//    }







//public class UserConf extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_conf);
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
