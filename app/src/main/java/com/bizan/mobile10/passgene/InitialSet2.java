package com.bizan.mobile10.passgene;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Toast;

public class InitialSet2 extends AppCompatActivity
        implements View.OnClickListener {

    NumberPicker npk1;
    NumberPicker npk2;
    NumberPicker npk3;
    NumberPicker npk4;
    Button btn;
    String fixMaster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_set2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = (Button) findViewById(R.id.btnInitialSet2);
        btn.setOnClickListener(this);

        npk1 = (NumberPicker) findViewById(R.id.npk1);
        npk2 = (NumberPicker) findViewById(R.id.npk2);
        npk3 = (NumberPicker) findViewById(R.id.npk3);
        npk4 = (NumberPicker) findViewById(R.id.npk4);

        npk1.setMinValue(0);
        npk1.setMaxValue(9);
        npk2.setMinValue(0);
        npk2.setMaxValue(9);
        npk3.setMinValue(0);
        npk3.setMaxValue(9);
        npk4.setMinValue(0);
        npk4.setMaxValue(9);

    }

    @Override
    public void onClick(View v) {
        //Num Pickerから数字を取得
        String tmp1 = String.valueOf(npk1.getValue());
        String tmp2 = String.valueOf(npk2.getValue());
        String tmp3 = String.valueOf(npk3.getValue());
        String tmp4 = String.valueOf(npk4.getValue());
        fixMaster = tmp1 + tmp2 + tmp3 + tmp4;

        //Dialogを表示させる
        openPG_Dialog();

    }


    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {

        //DialogFragmentに渡すモノを決めてね
        String title = "マスターパスワード確認";
        String message = InitialSet1.fullname + " さんのマスターパスワードは、\n" + fixMaster + " でよろしいですか？";
        String posi = "登録";
        String nega = "戻る";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_pass_gene_dialog;

        //以下は触らない方がいいかも
        PassGeneDialog dialog = new PassGeneDialog().newInstance();
        //Titleをセット
        dialog.setTitle(title);
        //メッセージをセット
        dialog.setMessage(message);
        //マスターパスワードをセット(このケースのみ使用)
        dialog.setMaster(fixMaster);
        // 自分で定義したレイアウト
        dialog.setContentViewId(resId_dialog);
        //ポジティブボタンテキストをセット
        dialog.setPositiveButtonText(posi);
        //ネガティブボタンテキストをセット
        dialog.setNegativeButtonText(nega);

//        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "TAG_PG");
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




/*
//今はDialogFragmentへのListener設定方法がわからないので コメントアウトしておく
    @Override
    public void onPositiveButtonClick(String tag) {
        if("TAG_PG".equals(tag)) {
            // ok ボタンがおされた
            toast("うぬのマスパスは" + "DBに登録したぞよい！");
            Intent intent = new Intent(InitialSet2.this, InitialSet3.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNegativeButtonClick(String tag) {
        if("TAG_PG".equals(tag)){
            return;
        }
    }*/


/*    // TODO Auto-generated method stub
    new AlertDialog.Builder(InitialSet2.this)
            .setTitle("マスターパスワード確認")
    .setMessage("パスは、" + fixMaster + " でよろしいですか？")
    // 肯定的な意味を持つボタンを設定
    .setPositiveButton("登録", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Positive Buttonが押された時の処理を記述{
            Intent intent = new Intent(InitialSet2.this, InitialSet3.class);
            toast("マスターパスワード" + fixMaster + "を登録しました。");
            startActivity(intent);
            Log.v("Alert", "Positive Button");

        }
    })
            // 否定的な意味を持つボタンを設定
            .setNegativeButton("やりなおす", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            // Negative Buttonが押された時の処理を記述
            toast("もう一度、マスターパスワードを登録してください。");
            Log.v("Alert", "Negative Button");
        }
    })
            .create()
    .show();*/


}

