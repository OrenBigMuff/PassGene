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
import android.widget.Toast;

public class InitialSet3 extends AppCompatActivity
        implements View.OnClickListener {

    private PreferenceC pref;

    Button btn0_3Posi;
    Button btn0_3Nega;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_set3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = new PreferenceC(this);

        btn0_3Posi = (Button) findViewById(R.id.btnInitialSet3Posi);
        btn0_3Posi.setOnClickListener(this);
        btn0_3Nega = (Button) findViewById(R.id.btnInitialSet3Nega);
        btn0_3Nega.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnInitialSet3Posi){

            Intent intent1 = new Intent(InitialSet3.this, RegistInfo.class);
            toast("ユーザー情報登録画面に遷移");
            startActivity(intent1);
            pref.writeConfig("p0_3", true);

        }else if(v.getId() == R.id.btnInitialSet3Nega){

            Intent intent2 = new Intent(InitialSet3.this, PassList2.class);
            toast("サービス名一覧画面に遷移");
            startActivity(intent2);
            pref.writeConfig("p0_3", true);
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
//                    toast("この操作はできません。");
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
