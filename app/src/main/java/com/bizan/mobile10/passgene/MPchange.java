package com.bizan.mobile10.passgene;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.Toolbar;

/**
 * Created by user on 2016/03/07.
 */
public class MPchange extends AppCompatActivity implements PassGeneDialog.DialogListener, View.OnClickListener {

    public static DatabaseC dbC;
    PreferenceC pref;
    NumberPicker npk1;
    NumberPicker npk2;
    NumberPicker npk3;
    NumberPicker npk4;
    Button btn;
    String newpass;
    String masterpass;

    public static Fflag mflag;

    CollapsingToolbarLayout tollbartitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpchange);

        tollbartitle = (CollapsingToolbarLayout) findViewById(R.id.mp_toolbar_title);
        tollbartitle.setTitle("マスターパスワード変更");

        dbC = new DatabaseC(PassList2.getDbHelper());

        masterpass = String.valueOf(dbC.readMasterPass());

        mflag = new Fflag();

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
        if (!ClickTimerEvent.isClickEvent()) return;
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
        if (masterpass.equals(newpass)) {
            String title = "パスワードが同じです";
            String message = "マスターパスワードが、「 " + newpass + " 」" + "から\n変更がありませんがよろしいですか？";
            String posi = "登録";
            String nega = "戻る";
            //ダイアログのレイアウトResId
            int resId_dialog = R.layout.fragment_pass_gene_dialog;

            FragmentManager fm = getSupportFragmentManager();
            PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
            alertDialog.show(fm, "fragment_alert");
        } else {

            String title = "マスターパスワード変更";
            String message = "マスターパスワードを" + "「" + masterpass + "」" + "から\n" +
                    "「" + newpass + "」" + "に変更します。\n" + "よろしいですか？";
            String posi = "変更";
            String nega = "戻る";
            //ダイアログのレイアウトResId
            int resId_dialog = R.layout.fragment_pass_gene_dialog;

            FragmentManager fm = getSupportFragmentManager();
            PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
            alertDialog.show(fm, "fragment_alert");
        }
    }

    @Override
    public void onPositiveButtonClick(DialogFragment dialog) {
        if (!ClickTimerEvent.isClickEvent()) return;
        Toast.makeText(this, "マスターパスワードが" + newpass + "に変更されさました。", Toast.LENGTH_SHORT).show();
        dbC.updateMasterPass(newpass);
        dialog.dismiss();
        MPchange.mflag.setFnishflg(true);
        MPchange.this.finish();
    }

    @Override
    public void onNegativeButtonClick(DialogFragment dialog) {

        dialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mflag.getFinishFlg() == true) {
            finish();
        }
    }

}
