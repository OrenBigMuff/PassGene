package com.bizan.mobile10.passgene;

/**
 * imai
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class InitialSet2 extends AppCompatActivity
        implements View.OnClickListener, PassGeneDialog.DialogListener {

    public static DatabaseC dbC;
    private PreferenceC pref;
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

        pref = new PreferenceC(this);

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
        String message = InitialSet1.fullname + " さんの\nマスターパスワードは、\n「 " + fixMaster + " 」でよろしいですか？";
        String posi = "登録";
        String nega = "戻る";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_pass_gene_dialog;

        FragmentManager fm = getSupportFragmentManager();
        PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "fragment_alert");
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

    @Override
    public void onPositiveButtonClick(android.support.v4.app.DialogFragment dialog) {
        // Positiveボタンが押された時の動作
        //DBに登録
        dbC = new DatabaseC(InitialSet1.getDbHelper());
        dbC.insertMasterPass(fixMaster);
        toast(InitialSet1.fullname + " さんのマスターパスワードは 「" + fixMaster + " 」で登録しました。");
        Intent intent = new Intent(InitialSet2.this, InitialSet3.class);
        startActivity(intent);
        //preference に渡す(初回時以外表示させないフラグ)
        pref.writeConfig("p0_1", true);
        pref.writeConfig("p0_2", true);
        //次画面から戻ってきた時の為に一旦、ダイアログを閉じる
        dialog.dismiss();
    }

    @Override
    public void onNegativeButtonClick(android.support.v4.app.DialogFragment dialog) {
        // Negativeボタンが押された時の動作
        //ダイアログを閉じる
        dialog.dismiss();
    }
}

