package com.bizan.mobile10.passgene;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toolbar;

/**
 * Created by user on 2016/03/07.
 */
public class MPchange extends AppCompatActivity implements MPchangeDialog.DialogListener, View.OnClickListener {

    public static DatabaseC dbC;
    private PreferenceC pref;
    private NumberPicker npk1;
    private NumberPicker npk2;
    private NumberPicker npk3;
    private NumberPicker npk4;
    private Button btn;
    private String newpass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpchange);

        dbC = new DatabaseC(PassList2.getDbHelper());

        btn = (Button) findViewById(R.id.btnMPchange);
        btn.setOnClickListener(this);

        npk1 = (NumberPicker) findViewById(R.id.mpcnpk1);
        npk2 = (NumberPicker) findViewById(R.id.mpcnpk2);
        npk3 = (NumberPicker) findViewById(R.id.mpcnpk3);
        npk4 = (NumberPicker) findViewById(R.id.mpcnpk4);

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
        newpass = tmp1 + tmp2 + tmp3 + tmp4;

        //Dialogを表示させる
        openPG_Dialog();
    }

    private void openPG_Dialog() {
        //DialogFragmentに渡すモノを決めてね
        String title = "マスターパスワード変更";
        String message ="マスターパスワードを" + newpass + "に変更します。\nよろしいですか？";
        String posi = "変更";
        String nega = "戻る";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_mpchange_dialog;

        FragmentManager fm = getSupportFragmentManager();
        MPchangeDialog alertDialog = MPchangeDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onPositiveButtonClick(DialogFragment dialog) {

    }

    @Override
    public void onNegativeButtonClick(DialogFragment dialog) {

    }

}
