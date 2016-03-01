package com.bizan.mobile10.passgene;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PwConf extends AppCompatActivity
implements PassGeneDialog.DialogListener{

    DatabaseC dbC;
    Intent intent_pwconf;

    TextView txvValue_address;        //アドレスのエディットテキスト
    TextView txvValue_password;       //passwordのエディットテキスト
    TextView txvValue_account;        //アカウントのエディットテキスト

    Button btnEdit;                  //編集ボタン
    Button btnDelete;                //削除ボタン

    String service = "facebook";
    String title = "テストやで～";
    String userid = "syun";
    String mail = "bizan@g.mail";
    String passward = "dfefedfcs";

    private int id_U;
    private int id_S;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_conf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pwconf_ToolBar);
        setSupportActionBar(toolbar);


        dbC = new DatabaseC(InitialSet1.getDbHelper());

        //前のページから渡ってきたintentをゲットだぜ！
        intent_pwconf = getIntent();
        id_U = intent_pwconf.getIntExtra("UID", -1);
        id_S = intent_pwconf.getIntExtra("SID", -1);

        if(id_U != -1 && id_S == -1) {

            toast("Error! コードがおかしくないですか？");

        }else if(id_U == -1 && id_S != -1){
            toast("IDをCatchしました-SID");
            String tmp_S = String.valueOf(id_S);
            Cursor cursor = dbC.readServiceInfo(tmp_S);
            service = cursor.getString(1);
            userid = cursor.getString(2);
            mail = cursor.getString(3);
            passward = cursor.getString(11);
        }

        getSupportActionBar().setTitle(service);

        txvValue_address = (TextView) findViewById(R.id.pwconf_txvValue_address);
        txvValue_address.setText(service);
//        String test = txvValue_address.getText().toString();

        txvValue_password = (TextView) findViewById(R.id.pwconf_txvValue_Password);
        txvValue_password.setText(passward);
//        String test2 = txvValue_password.getText().toString();

        txvValue_account = (TextView) findViewById(R.id.pwconf_txvValue_account);
        txvValue_account.setText(mail);
//        String test3 = txvValue_account.getText().toString();

        //編集ボタン
        btnEdit = (Button) findViewById(R.id.pwconf_hensyubtn);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //P2に飛ばす
                Intent intent = new Intent(PwConf.this, RegistNewPass.class);
                intent.putExtra("SID",String.valueOf(id_S));
                startActivity(intent);
            }
        });

        //削除ボタン
        btnDelete = (Button) findViewById(R.id.pwconf_deletebtn);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialogを表示させる？ or 直接Delete
                openPG_Dialog();
            }
        });
    }

    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {

        //DialogFragmentに渡すモノを決めてね
        String title = "この情報を削除しますか？";
        String message = InitialSet1.fullname + " さん、\n" + service + "の情報を削除しますか？";
        String posi = "削除";
        String nega = "戻る";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_pass_gene_dialog;

        FragmentManager fm = getSupportFragmentManager();
        PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "Pw_Conf");
    }

    @Override
    public void onPositiveButtonClick(android.support.v4.app.DialogFragment dialog) {
        // Positiveボタンが押された時の動作
        //当該サービスのdeleteフラグをつける
        dbC.deleteServiceInfo(id_S);
        dialog.dismiss();
    }

    @Override
    public void onNegativeButtonClick(android.support.v4.app.DialogFragment dialog) {
        // Negativeボタンが押された時の動作
        dialog.dismiss();
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

}
