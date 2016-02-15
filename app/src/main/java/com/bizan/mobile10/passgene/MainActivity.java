package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn_test_i = (Button)findViewById(R.id.btn_test_i);
        btn_test_i.setOnClickListener(this);

        Button btn_test_u = (Button)findViewById(R.id.btn_test_u);
        btn_test_u.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_test_i) {
            Intent intent = new Intent(MainActivity.this, InitialSet1.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btn_test_u){
            Intent intent = new Intent(MainActivity.this, UserInfoList.class);
            startActivity(intent);
        }
    }
}
