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
    private NumberPicker npk1;
    private NumberPicker npk2;
    private NumberPicker npk3;
    private NumberPicker npk4;
    private Button btn;
    private String lastname_ini2;
    private String firstname_ini2;
    private String birthday_ini2;
    private String fullname_ini2;
    private String fixMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_set2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        dbC = new DatabaseC(PassList2.getDbHelper());

        btn = (Button) findViewById(R.id.btnInitialSet2);
        btn.setOnClickListener(this);

        Intent intent_ini2 = getIntent();
        lastname_ini2 = intent_ini2.getStringExtra("Lastname");
        firstname_ini2 = intent_ini2.getStringExtra("Firstname");
        birthday_ini2 = intent_ini2.getStringExtra("Birthday");
        fullname_ini2 = lastname_ini2 + " " + firstname_ini2;


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
    public void onResume(){
        super.onResume();
    }


    @Override
    public void onClick(View v) {
//        btn.setClickable(false);
//        if (InitialSet2A == false){
//            InitialSet2A = true;
        if (!ClickTimerEvent.isClickEvent()) return;
            //Num Pickerから数字を取得
            String tmp1 = String.valueOf(npk1.getValue());
            String tmp2 = String.valueOf(npk2.getValue());
            String tmp3 = String.valueOf(npk3.getValue());
            String tmp4 = String.valueOf(npk4.getValue());
            fixMaster = tmp1 + tmp2 + tmp3 + tmp4;

            //Dialogを表示させる
            openPG_Dialog();
//        }else
//        return;
    }



    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {

//        btn.setClickable(true);
        //DialogFragmentに渡すモノを決めてね
        String title = "マスターパスワード確認";
        String message =lastname_ini2 + " " + firstname_ini2 + " さんの\nマスターパスワードは、\n「 " + fixMaster + " 」でよろしいですか？";
        String posi = "登録";
        String nega = "戻る";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_pass_gene_dialog;

        FragmentManager fm = getSupportFragmentManager();
        PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "fragment_alert");
//        InitialSet2A = false;
        btn.setClickable(true);

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
        if (!ClickTimerEvent.isClickEvent()) return;
            toast(fullname_ini2 + " さんのマスターパスワードは \n「" + this.fixMaster + " 」で登録します。");
            Intent intent = new Intent(InitialSet2.this, InitialSet3.class);
            intent.putExtra("LastName", lastname_ini2);
            intent.putExtra("FirstName", firstname_ini2);
            intent.putExtra("BirthDay", birthday_ini2);
            intent.putExtra("MasterPW", fixMaster);
            startActivity(intent);

            //InitialSet2を通過したのでコンフィグにWriteする
            pref.writeConfig("p0_2", true);
            //次画面から戻ってきた時の為に一旦、ダイアログを閉じる
            dialog.dismiss();
            InitialSet2.this.finish();

    }

    @Override
    public void onNegativeButtonClick(android.support.v4.app.DialogFragment dialog) {
        // Negativeボタンが押された時の動作
        //ダイアログを閉じる
        dialog.dismiss();
    }


}

