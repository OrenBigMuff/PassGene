package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class UserConf2 extends AppCompatActivity
        implements View.OnClickListener, PassGeneDialog.DialogListener {

    NumberPicker npk_uc1;
    NumberPicker npk_uc2;
    NumberPicker npk_uc3;
    NumberPicker npk_uc4;
    Button btn;
    String fixMaster_uc;
    String rightPass;       //DBから引っ張ってきたUserのMP

    //【重要】///////////////////////////////////////////////
    //
    //　投げる元のActivityに、intent.putExtra("CLASSNAME")
    //
    //
    //
    //User認証がクリアだった時に遷移するクラス名をここで設定する
    Intent i = getIntent();
    //final String CLASSNAME = i.getStringExtra("CLASSNAME");
//    final int ID_S = i.getIntExtra("SID", -1);
//    final int ID_U = i.getIntExtra("UID", -1);
    int ID_S = 10;
    int ID_U = -1;
    final String CLASSNAME = "com.bizan.mobile10.passgene.InitialSet3";       //遷移させたい先の完全なClass名（.java不要） ←最終的に削除の行
    final String PKGNAME = "com.bizan.mobile10.passgene";    //ここは変更不要
    //
    //
    //ここまで///////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_conf2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = (Button) findViewById(R.id.btnUserConf);
        btn.setOnClickListener(this);

        npk_uc1 = (NumberPicker) findViewById(R.id.npk_uc1);
        npk_uc2 = (NumberPicker) findViewById(R.id.npk_uc2);
        npk_uc3 = (NumberPicker) findViewById(R.id.npk_uc3);
        npk_uc4 = (NumberPicker) findViewById(R.id.npk_uc4);

        npk_uc1.setMinValue(0);
        npk_uc1.setMaxValue(9);
        npk_uc2.setMinValue(0);
        npk_uc2.setMaxValue(9);
        npk_uc3.setMinValue(0);
        npk_uc3.setMaxValue(9);
        npk_uc4.setMinValue(0);
        npk_uc4.setMaxValue(9);

    }

    @Override
    public void onClick(View v) {
        //Num Pickerから数字を取得
        String tmp_uc1 = String.valueOf(npk_uc1.getValue());
        String tmp_uc2 = String.valueOf(npk_uc2.getValue());
        String tmp_uc3 = String.valueOf(npk_uc3.getValue());
        String tmp_uc4 = String.valueOf(npk_uc4.getValue());
        fixMaster_uc = tmp_uc1 + tmp_uc2 + tmp_uc3 + tmp_uc4;


        //DBからユーザーのMPを引っ張って来る
        rightPass = "0000";


        //入力パスが登録済MPと一致した時は次の画面へ
        if (fixMaster_uc.equals(rightPass)) {
            //次の画面へ遷移するコードを記述
            //前のActivityから渡ってきたIDがUIDかSIDで判別（来ていない方は「-1」なので、、、それをトリガーに）
            if(ID_S == -1 && ID_U != -1) {
                toast("パスワードが一致しました。ID_U");
                Intent intent = new Intent();
                intent.setClassName(PKGNAME, CLASSNAME);
                intent.putExtra("UID", ID_U);
                startActivity(intent);
            }else if(ID_S != -1 && ID_U == -1){
                toast("パスワードが一致しました。ID_S");
                Intent intent = new Intent();
                intent.setClassName(PKGNAME, CLASSNAME);
                intent.putExtra("UID", ID_U);
                startActivity(intent);
            }return;

        } else{
            //Dialogを表示させる
            openPG_Dialog();
        }

    }

    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {

        //DialogFragmentに渡すモノを決めてね
        String title = "パスワードが違います";
        String message = InitialSet1.fullname + " さんのマスターパスワードは、\n「 " + fixMaster_uc + " 」ではありません。";
        String posi = "再入力";
        String nega = "忘れた";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_pass_gene_dialog;

        FragmentManager fm = getSupportFragmentManager();
        PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "fragment_alert2");
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
        toast(InitialSet1.fullname + " さん、\nマスターパスワードを再度入力して下さい。");
        dialog.dismiss();
    }

    @Override
    public void onNegativeButtonClick(android.support.v4.app.DialogFragment dialog) {
        // Negativeボタンが押された時の動作
        //登録済メールアドレスにMPを送信するページに遷移
        Intent intent = new Intent(UserConf2.this, IForgot.class);
        startActivity(intent);
        dialog.dismiss();
    }
}
