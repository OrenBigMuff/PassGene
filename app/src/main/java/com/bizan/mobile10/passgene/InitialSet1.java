package com.bizan.mobile10.passgene;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;

public class InitialSet1 extends AppCompatActivity
        implements View.OnClickListener {

    Button btn;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_set1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = (Button) findViewById(R.id.btnInitialSet1);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(InitialSet1.this, InitialSet2.class);
        startActivity(intent);

    }
}
