package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class RegistNewPass extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    PreferenceC pref;
    AutoCompleteTextView rnptxvservice;
    AutoCompleteTextView rnptxvid;
    AutoCompleteTextView rnptxvaddress;

    LinearLayout rnplayout; //パスワードのレイアウト
    TextView rnptxvpass;

    CheckBox rnpchbnum;
    CheckBox rnpchbb;
    CheckBox rnpchbs;
    CheckBox rnpchbk;

    SeekBar rnpskb;

    TextView rnptxvpasslength;
    TextView rpnpasslength;
    Spinner rnpspn;
    Button rnpbtnnext;

    HashMap<String, String> hashMapDB;

    DatabaseC dbC;

    String exID;

    String[] arrayService;
    String[] arrayid;
    String[] arrayadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_new_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //データベース準備
        //dbC = new DatabaseC(MainActivity.getDbHelper(), MainActivity.getDB_TABLE());
        pref = new PreferenceC(this);
        hashMapDB = new HashMap<>();

        //前のページから飛んできたIDをセット
        exID = "";
        Bundle extrasID = getIntent().getExtras();
        if (extrasID != null) {
            exID = extrasID.getString("SID");
        }

        if (exID.length() > 0) {
            //readDB();
        } else {
            pref.writeConfig("id", "0");
        }
        String readid = pref.readConfig("id", "0");

        if (!readid.equals("0")) {
            //DBデータをセットする
            setDB2hash();
        } else {
            //データがなかったらここでぷリファレンスの値を入れちゃう 以下ハッシュマップでとる　ぷリファレンスはページ受け渡し
            hashMapDB.put("id", "");
            hashMapDB.put("service", "");
            hashMapDB.put("mailadd", "");
            hashMapDB.put("pass", "");
            hashMapDB.put("passhint", "");
            if (pref.readConfig("chbn", true)) {
                hashMapDB.put("char_num", "1");
            } else {
                hashMapDB.put("char_num", "0");
            }
            if (pref.readConfig("chbb", true)) {
                hashMapDB.put("char_uppercase", "1");
            } else {
                hashMapDB.put("char_uppercase", "0");
            }
            if (pref.readConfig("chbs", true)) {
                hashMapDB.put("char_lowercase", "1");
            } else {
                hashMapDB.put("char_lowercase", "0");
            }
            if (pref.readConfig("chbk", true)) {
                hashMapDB.put("char_symbol", "1");
            } else {
                hashMapDB.put("char_symbol", "0");
            }
            hashMapDB.put("num_of_char", String.valueOf(pref.readConfig("seekbar", 8)));
        }

        setDB2serviceInfo();

        rnptxvservice = (AutoCompleteTextView) findViewById(R.id.rnptxvservice);
        rnptxvservice.setAdapter(createAdapter(arrayService));
        rnptxvservice.setThreshold(0);
        rnptxvservice.setText(hashMapDB.get("service").toString());

        rnptxvid = (AutoCompleteTextView) findViewById(R.id.rnptxvid);
        rnptxvid.setAdapter(createAdapter(arrayid));
        rnptxvid.setThreshold(0);
        rnptxvid.setText(hashMapDB.get("id").toString());

        rnptxvaddress = (AutoCompleteTextView) findViewById(R.id.rnptxvaddress);
        rnptxvaddress.setAdapter(createAdapter(arrayadd));
        rnptxvaddress.setThreshold(0);
        rnptxvaddress.setText(hashMapDB.get("mailadd").toString());


        rnplayout = (LinearLayout) findViewById(R.id.rnplayout);
        //データがなかったらパスは表示させない
        if (hashMapDB.get("id").toString().equals("")) {
            //パスワードのとこのレイアウト
            rnplayout.setVisibility(View.GONE);
        } else {
            //パスワードのテキストビュー
            rnptxvpass = (TextView) findViewById(R.id.rnptxvpass);
            rnptxvpass.setText(hashMapDB.get("pass").toString());
        }


        //チェックボックスに値をセット
        rnpchbnum = (CheckBox) findViewById(R.id.rnpchbnum);
        if (hashMapDB.get("char_num").toString().equals("0")) {
            rnpchbnum.setChecked(false);
        } else {
            rnpchbnum.setChecked(true);
        }
        rnpchbb = (CheckBox) findViewById(R.id.rnpchbb);
        if (hashMapDB.get("char_uppercase").toString().equals("0")) {
            rnpchbb.setChecked(false);
        } else {
            rnpchbb.setChecked(true);
        }
        rnpchbs = (CheckBox) findViewById(R.id.rnpchbs);
        if (hashMapDB.get("char_lowercase").toString().equals("0")) {
            rnpchbs.setChecked(false);
        } else {
            rnpchbs.setChecked(true);
        }
        rnpchbk = (CheckBox) findViewById(R.id.rnpchbk);
        if (hashMapDB.get("char_symbol").toString().equals("0")) {
            rnpchbk.setChecked(false);
        } else {
            rnpchbk.setChecked(true);
        }

        rnpskb = (SeekBar) findViewById(R.id.rnpskb);
        rnpskb.setMax(12);
        rnpskb.setProgress(Integer.parseInt(hashMapDB.get("num_of_char").toString()) - 4);
        rnpskb.setOnSeekBarChangeListener(this);

        //文字数を表示させる
        rnptxvpasslength = (TextView) findViewById(R.id.rnptxvpasslength);
        StringBuilder stb = new StringBuilder();
        stb.append(getResources().getString(R.string.passlengthbef));
        stb.append(hashMapDB.get("num_of_char"));
        stb.append(getResources().getString(R.string.passlengthaft));
        rnptxvpasslength.setText(stb.toString());

        rnpspn = (Spinner) findViewById(R.id.rnpspn);
        String[] spnstr = {getResources().getString(R.string.month1),
                getResources().getString(R.string.month2),
                getResources().getString(R.string.month3),
                getResources().getString(R.string.month6)};
        rnpspn.setAdapter(createAdapter(spnstr));

        rnpbtnnext = (Button) findViewById(R.id.rnpbtnnext);
        rnpbtnnext.setOnClickListener(this);

        rnpchbb.setOnCheckedChangeListener(this);
        rnpchbs.setOnCheckedChangeListener(this);
        rnpchbk.setOnCheckedChangeListener(this);
        rnpchbnum.setOnCheckedChangeListener(this);

        writePref();
    }

    private void setDB2serviceInfo() {
        Cursor cursor;
        cursor = dbC.readSingleclum("service");
        arrayService = new String[cursor.getCount()];
        arrayService = arrayset(cursor);

        cursor = dbC.readSingleclum("user_id");
        arrayid = new String[cursor.getCount()];
        arrayid = arrayset(cursor);

        cursor = dbC.readSingleclum("mail_address");
        arrayadd = new String[cursor.getCount()];
        arrayadd = arrayset(cursor);
    }

    private String[] arrayset(Cursor cursor) {
        String[] str = new String[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            str[i] = cursor.getString(0);
        }
        return str;
    }

    private void setDB2hash() {
        Cursor cursor = dbC.readServiceInfo(exID);
        cursorLog(cursor);
        hashMapDB.put("id", "1");
        hashMapDB.put("service", "facebook");
        hashMapDB.put("mailadd", "aea@rij.com");
        hashMapDB.put("pass", "afff123456");
        hashMapDB.put("passhint", "aegafaeeeff");
        hashMapDB.put("char_num", "1");
        hashMapDB.put("char_uppercase", "1");
        hashMapDB.put("char_lowercase", "1");
        hashMapDB.put("char_symbol", "1");
        hashMapDB.put("num_of_char", "16");
    }

    private void cursorLog(Cursor cursor) {
        while (cursor.moveToNext()) {
            StringBuilder stb = new StringBuilder();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                stb.append(cursor.getColumnName(i).toString());
                stb.append(":");
                stb.append(cursor.getString(i));
                cursor.getString(i);
            }
            Log.e("log", stb.toString());
        }
    }

    private void writePref() {
        pref.writeConfig("chbb", rnpchbb.isChecked());
        pref.writeConfig("chbs", rnpchbs.isChecked());
        pref.writeConfig("chbk", rnpchbk.isChecked());
        pref.writeConfig("chbn", rnpchbnum.isChecked());
        pref.writeConfig("seekbar", rnpskb.getProgress() + 4);
    }


    //データベースの情報を配列に格納するメソッド
//    private String[] arrayString(){
//
//    }

    private ArrayAdapter createAdapter(String[] dbdata) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        for (String s : dbdata) {
            adapter.add(s);
        }
        return adapter;
    }

    @Override
    public void onClick(View v) {
        if (rnpbtnnext == v) {
            //ネクストボタン　登録やページ遷移
            Intent intent = new Intent(RegistNewPass.this, GenePass.class);
            startActivity(intent);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //チェックボックス
        rnpchbnum.setChecked(true);
        if (buttonView == rnpchbb) {
            rnpchbb.setChecked(isChecked);
        } else if (buttonView == rnpchbs) {
            rnpchbs.setChecked(isChecked);
        } else if (buttonView == rnpchbk) {
            rnpchbk.setChecked(isChecked);
        }
        writePref();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int temp = progress + 4;
        StringBuilder stb = new StringBuilder();
        stb.append(getResources().getString(R.string.passlengthbef));
        stb.append(temp);
        stb.append(getResources().getString(R.string.passlengthaft));
        rnptxvpasslength.setText(stb.toString());
        writePref();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
