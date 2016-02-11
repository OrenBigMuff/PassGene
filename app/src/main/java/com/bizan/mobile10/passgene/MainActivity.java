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

        Button btn_test = (Button)findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_test) {
            Intent intent = new Intent(MainActivity.this, InitialSet3.class);
            startActivity(intent);
        }
    }
}
