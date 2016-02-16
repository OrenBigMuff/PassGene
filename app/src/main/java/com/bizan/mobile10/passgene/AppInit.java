package com.bizan.mobile10.passgene;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toolbar;

/**
 * Created by user on 2016/02/16.
 */
public class AppInit extends AppCompatActivity implements View.OnClickListener {

    Button btnSyokika;      //初期化ボタン
    View appinitView;       //ボタンにかぶせるView

//    ナンバーピッカー
    NumberPicker AppInit_npk1;
    NumberPicker AppInit_npk2;
    NumberPicker AppInit_npk3;
    NumberPicker AppInit_npk4;

    String tmp1;
    String tmp2;
    String tmp3;
    String tmp4;

    String DeletePassNumber;

    int aaa;

    CollapsingToolbarLayout collapsingToolbar;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.AppSetting_toolbar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.AppInit_toolbar_layout);
        collapsingToolbar.setTitle("Initialization");

        btnSyokika = (Button) findViewById(R.id.appinitbtn);
        btnSyokika.setOnClickListener(this);
        btnSyokika.setEnabled(false);

        //ボタンにかぶせるView
        appinitView = findViewById(R.id.AppInitView);
        appinitView.setVisibility(View.VISIBLE);

        //ナンバーピッカー
        AppInit_npk1 = (NumberPicker) findViewById(R.id.AppInit_npk1);
        AppInit_npk2 = (NumberPicker) findViewById(R.id.AppInit_npk2);
        AppInit_npk3 = (NumberPicker) findViewById(R.id.AppInit_npk3);
        AppInit_npk4 = (NumberPicker) findViewById(R.id.AppInit_npk4);

        AppInit_npk1.setMinValue(0);
        AppInit_npk1.setMaxValue(9);
        AppInit_npk2.setMinValue(0);
        AppInit_npk2.setMaxValue(9);
        AppInit_npk3.setMinValue(0);
        AppInit_npk3.setMaxValue(9);
        AppInit_npk4.setMinValue(0);
        AppInit_npk4.setMaxValue(9);

        aaa = 1234;
    }

    @Override
    public void onClick(View v) {

        tmp1 = String.valueOf(AppInit_npk1.getValue());
        tmp2 = String.valueOf(AppInit_npk2.getValue());
        tmp3 = String.valueOf(AppInit_npk3.getValue());
        tmp4 = String.valueOf(AppInit_npk4.getValue());

        DeletePassNumber = tmp1 + tmp2 + tmp3 + tmp4;

//        if (aaa = DeletePassNumber){
//
//        }
    }
}
