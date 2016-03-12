package com.bizan.mobile10.passgene;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PwConf extends AppCompatActivity
        implements PassGeneDialog.DialogListener {

    DatabaseC dbC;
    Intent intent_pwconf;

    TextView txvValue_address;        //アドレスのエディットテキスト
    TextView txvValue_password;       //passwordのエディットテキスト
    TextView txvValue_account;        //アカウントのエディットテキスト

    Button btnEdit;                  //編集ボタン
    Button btnDelete;                //削除ボタン

    private String service;
    private String title;
    private String userid;
    private String mail;
    private String password;

    private int id_U;
    private int id_S;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_conf);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pwconf_ToolBar);
        setSupportActionBar(toolbar);

        dbC = new DatabaseC(PassList2.getDbHelper());

        //前のページから渡ってきたintentをゲットだぜ！
        intent_pwconf = getIntent();
        id_U = intent_pwconf.getIntExtra("UID", -1);
        id_S = intent_pwconf.getIntExtra("SID", -1);

        //編集ボタン
        btnEdit = (Button) findViewById(R.id.pwconf_hensyubtn);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //P2に飛ばす
                Intent intent = new Intent(PwConf.this, ReRegistPass.class);
                intent.putExtra("SID", String.valueOf(id_S));
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

    @Override
    protected void onResume() {
        super.onResume();

        if (id_U != -1 && id_S == -1) {

//            toast("Error! 前ページのソースコードがおかしくないですか？");          //←最終的に削除
            return;
        } else if (id_U == -1 && id_S != -1) {
//            toast("IDをCatchしました-SID");
            String tmp_S = String.valueOf(id_S);
            Cursor cursor = dbC.readServiceInfo(tmp_S);

//            String[] list = new String[cursor.getCount()];


//                for (int i = 0; i < list.length; i++) {
//                    list[i] = cursor.getString();
            cursor.moveToFirst();
            service = cursor.getString(1);
            userid = cursor.getString(2);
            mail = cursor.getString(3);
            password = cursor.getString(11);


            cursor.close();

//            cursor.moveToFirst();
//            String[] service_array = new String[cursor.getCount()];
//            String[] userid_array = new String[cursor.getCount()];
//            String[] mail_array = new String[cursor.getCount()];
//            String[] password_array = new String[cursor.getCount()];
//
//            for (int i = 0; i < service_array.length; i++) {
//                service_array[i] = cursor.getString(1);
//                userid_array[i] = cursor.getString(2);
//                mail_array[i] = cursor.getString(3);
//                password_array[i] = cursor.getString(11);
//
//                cursor.moveToNext();

        }

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.pwconf_ToolBarLayout);
        toolbarLayout.setTitle(service);
//        getSupportActionBar().setTitle(service);

        txvValue_address = (TextView) findViewById(R.id.pwconf_txvValue_address);
        txvValue_address.setText(mail);
//        String test = txvValue_address.getText().toString();

        txvValue_password = (TextView) findViewById(R.id.pwconf_txvValue_Password);
        txvValue_password.setText(password);
//        String test2 = txvValue_password.getText().toString();

        txvValue_account = (TextView) findViewById(R.id.pwconf_txvValue_account);
        txvValue_account.setText(userid);
//        String test3 = txvValue_account.getText().toString();
    }

    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {

        //DialogFragmentに渡すモノを決めてね
        String title = "この情報を削除しますか？";
        String message = PassList2.getUserName() + " さん、\n" + service + "の情報を削除しますか？";
        String posi = "戻る";
        String nega = "削除";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_pass_gene_dialog;

        FragmentManager fm = getSupportFragmentManager();
        PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "Pw_Conf");
    }

    @Override
    public void onPositiveButtonClick(android.support.v4.app.DialogFragment dialog) {
        // Negativeボタンが押された時の動作
        dialog.dismiss();

    }

    @Override
    public void onNegativeButtonClick(android.support.v4.app.DialogFragment dialog) {
        // Positiveボタンが押された時の動作
        //当該サービスのdeleteフラグをつける
        dbC.deleteServiceInfo(id_S);
        dialog.dismiss();
        PwConf.this.finish();
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
