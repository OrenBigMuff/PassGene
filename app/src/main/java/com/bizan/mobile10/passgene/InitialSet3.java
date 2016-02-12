package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class InitialSet3 extends AppCompatActivity
        implements View.OnClickListener {

    Button btn0_3Posi;
    Button btn0_3Nega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_set3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn0_3Posi = (Button) findViewById(R.id.btnInitialSet3Posi);
        btn0_3Posi.setOnClickListener(this);
        btn0_3Nega = (Button) findViewById(R.id.btnInitialSet3Nega);
        btn0_3Nega.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnInitialSet3Posi){

            Intent intent1 = new Intent(InitialSet3.this, MainActivity.class);
            startActivity(intent1);

        }else if(v.getId() == R.id.btnInitialSet3Nega){

            Intent intent2 = new Intent(InitialSet3.this, MainActivity.class);
            startActivity(intent2);

        }

    }
}
