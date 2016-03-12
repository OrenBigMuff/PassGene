package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InitialSet3 extends AppCompatActivity
        implements View.OnClickListener {

    private DatabaseC dbC;
    private PreferenceC pref;

    private TextView txvDoInput;
    private Button btn0_3Posi;
    private Button btn0_3Nega;
    private String lastname_ini3;
    private String firstname_ini3;
    private String fullname_ini3;
    private String birthday_ini3;
    private String fixMasterPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_set3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = new PreferenceC(this);

        dbC = new DatabaseC(PassList2.getDbHelper());

        Intent intent_ini3 = getIntent();
        lastname_ini3 = intent_ini3.getStringExtra("LastName");
        firstname_ini3 = intent_ini3.getStringExtra("FirstName");
        birthday_ini3 = intent_ini3.getStringExtra("BirthDay");
        fixMasterPass = intent_ini3.getStringExtra("MasterPW");
        fullname_ini3 = lastname_ini3 + " " + firstname_ini3;

        /**
         *ここにDBへの登録を記述
         */
        //InitialSet1の分
        String[] value_sei ={"ユーザーの姓", lastname_ini3, "2"};
        dbC.insertUserInfo(value_sei);
        String[] value_mei ={"ユーザーの名", firstname_ini3, "3"};
        dbC.insertUserInfo(value_mei);
        String[] value_birth ={"ユーザーの生年月日", birthday_ini3, "1"};
        dbC.insertUserInfo(value_birth);
        //InitialSet2の分
        dbC.insertMasterPass(fixMasterPass);

        txvDoInput = (TextView)findViewById(R.id.txvDoInput);
        txvDoInput.setText(fullname_ini3 + " さん、このアプリはあなたの入力情報からパスワードを生成します。\nユーザー情報を追加しなければ、\n簡易的なPWしかご提案できません。");
        btn0_3Posi = (Button) findViewById(R.id.btnInitialSet3Posi);
        btn0_3Posi.setOnClickListener(this);
        btn0_3Nega = (Button) findViewById(R.id.btnInitialSet3Nega);
        btn0_3Nega.setOnClickListener(this);


        //InitialSet1とInitialSet2を通過した事をコンフィグに記録する
        if(pref.readConfig("p0_1", true) && pref.readConfig("p0_2", true)){
            pref.writeConfig("InitialDone", true);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnInitialSet3Posi){

            Intent intent1 = new Intent(InitialSet3.this, RegistInfo.class);
//            toast("ユーザー情報登録画面へ");
            startActivity(intent1);

            InitialSet3.this.finish();

        }else if(v.getId() == R.id.btnInitialSet3Nega){

//            Intent intent2 = new Intent(InitialSet3.this, PassList2.class);
//            toast("サービス名一覧画面へ");
//            startActivity(intent2);

            InitialSet3.this.finish();
        }

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


    /**
     * 端末のBack Keyを無効にするメソッド
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    // ダイアログ表示など特定の処理を行いたい場合はここに記述
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
//                    Intent intent = new Intent(InitialSet3.this, InitialSet2.class);
//                    startActivity(intent);
                    toast("この操作はできません。");
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
