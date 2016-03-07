package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.Toolbar;

/**
 * Created by user on 2016/02/16.
 */
public class AppInit extends AppCompatActivity implements View.OnClickListener, NumberPicker.OnValueChangeListener,
DeleteDialog.DialogListener{

/*    private final String DB_NAME = "pg.db"; //データベース名
    private final int DB_VERSION = 1;       //データベースのバージョン
    private static final String[] DB_TABLE = {"service_info", "user_info"};
    private static DatabaseHelper dbHelper; //DBヘルパー
    public static DatabaseHelper getDbHelper() {
        return dbHelper;
    }*/
    private DatabaseC dbC;


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

    String num1 = "";
    String num2 = "";
    String num3 = "";
    String num4 = "";

    String PassNum;
    String DeletePassNumber;

    String a1;
    String a2;
    String a3;
    String a4;

    String PassA;

    String masterPass;      //マスターパス

    CollapsingToolbarLayout collapsingToolbar;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.AppSetting_toolbar);

        dbC = new DatabaseC(PassList2.getDbHelper());
        //マスターパス呼び出し
        dbC.readMasterPass();
        masterPass = dbC.readMasterPass();


        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.AppInit_toolbar_layout);
        collapsingToolbar.setTitle("アプリ初期化画面");

        btnSyokika = (Button) findViewById(R.id.appinitbtn);
        btnSyokika.setOnClickListener(this);
        btnSyokika.setEnabled(false);

        //ボタンにかぶせるView
        appinitView = findViewById(R.id.AppInitView);

        //ナンバーピッカー
        AppInit_npk1 = (NumberPicker) findViewById(R.id.AppInit_npk1);
        AppInit_npk1.setOnValueChangedListener(this);
        AppInit_npk2 = (NumberPicker) findViewById(R.id.AppInit_npk2);
        AppInit_npk2.setOnValueChangedListener(this);
        AppInit_npk3 = (NumberPicker) findViewById(R.id.AppInit_npk3);
        AppInit_npk3.setOnValueChangedListener(this);
        AppInit_npk4 = (NumberPicker) findViewById(R.id.AppInit_npk4);
        AppInit_npk4.setOnValueChangedListener(this);

        AppInit_npk1.setMinValue(0);
        AppInit_npk1.setMaxValue(9);
        num1 = String.valueOf(0);
        AppInit_npk2.setMinValue(0);
        AppInit_npk2.setMaxValue(9);
        num2 = String.valueOf(0);
        AppInit_npk3.setMinValue(0);
        AppInit_npk3.setMaxValue(9);
        num3 = String.valueOf(0);
        AppInit_npk4.setMinValue(0);
        AppInit_npk4.setMaxValue(9);
        num4 = String.valueOf(0);

        //numberpickerの初期値設定
        PassNum = num1 + num2 + num3 + num4;


        a1 = String.valueOf(1);
        a2 = String.valueOf(0);
        a3 = String.valueOf(0);
        a4 = String.valueOf(0);

        PassA = a1 + a2 + a3 + a4;

//        AppInit_npk1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    AppInit_npk1.requestFocus();
//                }else if (event.getAction() == MotionEvent.ACTION_UP){
//                    AppInit_npk1.clearFocus();
//                }
//                return false;
//            }
//        });
//        AppInit_npk2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    AppInit_npk2.requestFocus();
//                }else if (event.getAction() == MotionEvent.ACTION_UP){
//                    AppInit_npk2.clearFocus();
//                }
//                return false;
//            }
//        });
//        AppInit_npk3.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    AppInit_npk3.requestFocus();
//                }else if (event.getAction() == MotionEvent.ACTION_UP){
//                    AppInit_npk3.clearFocus();
//                }
//                return false;
//            }
//        });
//        AppInit_npk4.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN){
//                    AppInit_npk4.requestFocus();
//                }else if (event.getAction() == MotionEvent.ACTION_UP){
//                    AppInit_npk4.clearFocus();
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.appinitbtn:
                openDeleteDialog();
                break;
        }

//        tmp1 = String.valueOf(AppInit_npk1.getValue());
//        tmp2 = String.valueOf(AppInit_npk2.getValue());
//        tmp3 = String.valueOf(AppInit_npk3.getValue());
//        tmp4 = String.valueOf(AppInit_npk4.getValue());
//
//        DeletePassNumber = tmp1 + tmp2 + tmp3 + tmp4;

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


        if (picker == AppInit_npk1){
            num1 = String.valueOf(newVal);
        }else if (picker == AppInit_npk2){
            num2 = String.valueOf(newVal);
        }else if (picker == AppInit_npk3){
            num3 = String.valueOf(newVal);
        }else if (picker == AppInit_npk4){
            num4 = String.valueOf(newVal);
        }
        PassNum = num1 + num2 + num3 + num4;

        Log.e("Passnum", PassNum + ":" + PassA);

        if (PassNum.equals(masterPass)){
            appinitView.setVisibility(View.GONE);
            btnSyokika.setEnabled(true);
        }else {
            appinitView.setVisibility(View.VISIBLE);
            btnSyokika.setEnabled(false);
        }
    }

    private void openDeleteDialog(){
        //DialogFragmentに渡すモノを決めてね
        String title = "アプリ初期化";
        String message = "アプリを初期化します\n本当によろしいですか？";
        String posi = "初期化";
        String nega = "戻る";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.delete_dialog;

        FragmentManager fm = getSupportFragmentManager();
        DeleteDialog alertDialog = DeleteDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onPositiveButtonClick(DialogFragment dialog) {
        //positiveぼたん
        //テーブル初期化
        dbC.reset();
        Toast.makeText(this,"アプリが初期化されました",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AppInit.this,InitialSet1.class);
        startActivity(intent);
    }

    @Override
    public void onNegativeButtonClick(DialogFragment dialog) {
        //negativeぼたん
        Toast.makeText(this,"°˖✧◝(⁰▿⁰)◜✧˖°",Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
