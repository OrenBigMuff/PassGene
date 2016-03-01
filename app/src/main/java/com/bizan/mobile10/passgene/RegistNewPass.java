package com.bizan.mobile10.passgene;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class RegistNewPass extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private PreferenceC pref;
    private AutoCompleteTextView rnptxvservice;
    private AutoCompleteTextView rnptxvid;
    private AutoCompleteTextView rnptxvaddress;

    private LinearLayout rnplayout; //パスワードのレイアウト
    private TextView rnptxvpass;

    private CheckBox rnpchbnum;
    private CheckBox rnpchbb;
    private CheckBox rnpchbs;
    private CheckBox rnpchbk;

    private SeekBar rnpskb;

    private TextView rnptxvpasslength;
    private TextView rpnpasslength;
    private Spinner rnpspn;
    private Button rnpbtnnext;

    private HashMap<String, String> hashMapDB;

    private DatabaseC dbC;

    private String exID;

    private String[] arrayService;
    private String[] arrayid;
    private String[] arrayadd;



    /**
     * debug
     *****************************
    private final String DB_NAME = "pg.db"; //データベース名
    private final int DB_VERSION = 1;       //データベースのバージョン
    //テーブル名
    private static final String[] DB_TABLE = {"service_info", "user_info"};
    private static DatabaseHelper dbHelper; //DBヘルパー

    /*******************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_new_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbC = new DatabaseC(InitialSet1.getDbHelper());

/**debug*****************************
        String[] dbColTable = {
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " service TEXT UNIQUE NOT NULL," +
                        " user_id TEXT NOT NULL," +
                        " mail_address TEXT NOT NULL," +
                        " char_num INTEGER NOT NULL," +
                        " char_uppercase INTEGER NOT NULL," +
                        " char_lowercase INTEGER NOT NULL," +
                        " char_symbol INTEGER NOT NULL," +
                        " num_of_char INTEGER NOT NULL," +
                        " generated_datetime TEXT NOT NULL," +
                        " updated_datetime TEXT NOT NULL," +
                        " fixed_pass TEXT NOT NULL," +
                        " pass_hint TEXT NOT NULL," +
                        " gene_id1 INTEGER NOT NULL," +
                        " gene_id2 INTEGER NOT NULL," +
                        " gene_id3 INTEGER NOT NULL," +
                        " gene_id4 INTEGER NOT NULL," +
                        " algorithm INTEGER NOT NULL," +
                        " delete_flag INTEGER NOT NULL)",

                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " info_name TEXT UNIQUE NOT NULL," +
                        " value TEXT NOT NULL," +
                        " category INTEGER NOT NULL," +
                        " delete_flag INTEGER NOT NULL," +
                        " useless_flag INTEGER NOT NULL)"
        };
        dbHelper = new DatabaseHelper(this, DB_NAME, DB_VERSION, DB_TABLE, dbColTable);
/*******************************
        //データベース準備
        dbC = new DatabaseC(dbHelper);
//        Cursor cursor = dbC.readServiceInfoAll();
//        cursorLog(cursor);
//        cursor.close();
/**debug*****************************/
        //cursorLog(dbC.readServiceInfoAll());
        //cursorLog(dbC.readUserInfoAll());
//        String[] str2 = new String[]{"姓", "jouge", "2"};
//        dbC.insertUserInfo(str2);
//        str2 = new String[]{"名", "sayuu", "3"};
//        dbC.insertUserInfo(str2);
//
//        dbC.insertMasterPass(1234);
//
//        str2 = new String[]{"生年月日", "19900801", "1"};
//        dbC.insertUserInfo(str2);
/*******************************/

        pref = new PreferenceC(this);
        hashMapDB = new HashMap<>();

        dbC = new DatabaseC(InitialSet1.getDbHelper());

        //前のページから飛んできたIDをセット
        exID = "";
        Bundle extrasID = getIntent().getExtras();
        if (extrasID != null) {
            exID = extrasID.getString("SID");
        }
///////////////////////////////一時対比
        if (exID.length() > 0) {
            //readDB();
        } else {
            pref.writeConfig("id", "0");
        }
        String readId = pref.readConfig("id", "0");

        if (!readId.equals("0")) {
            //DBデータをセットする
            setDB2hash();
        } else {
            //データがなかったらここでプリファレンスの値を入れちゃう 以下ハッシュマップでとる　プリファレンスはページ受け渡し
            hashMapDB.put("id", "0");
            hashMapDB.put("user_id", "");
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
            hashMapDB.put("spinner", "0");
        }

        setDB2userInfo();
//        Cursor cursor1 = dbC.readUserInfoAll();
//        //cursorLog(cursor1);
//        cursor.close();

        rnptxvservice = (AutoCompleteTextView) findViewById(R.id.rnptxvservice);
        if (arrayService != null) {
            rnptxvservice.setAdapter(createAdapter(arrayService));
        } else {

        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.service));
//        rnptxvservice.setHint(stringBuilder.toString());
        rnptxvservice.setThreshold(0);
        rnptxvservice.setText(hashMapDB.get("service").toString());

        rnptxvid = (AutoCompleteTextView) findViewById(R.id.rnptxvid);
        if (arrayid != null) {
            rnptxvid.setAdapter(createAdapter(arrayid));
        } else {


        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.id));
//        rnptxvid.setHint(stringBuilder.toString());
        rnptxvid.setThreshold(0);
        rnptxvid.setText(hashMapDB.get("user_id").toString());

        rnptxvaddress = (AutoCompleteTextView) findViewById(R.id.rnptxvaddress);
        if (arrayadd != null) {
            rnptxvaddress.setAdapter(createAdapter(arrayadd));
        } else {

        }stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.address));
        //stringBuilder.append(getResources().getString(R.string.mei));
//        rnptxvaddress.setHint(stringBuilder.toString());
        rnptxvaddress.setThreshold(0);
        rnptxvaddress.setText(hashMapDB.get("mailadd").toString());

        /*****************************************
         * 今井追加 ソフトキーボード非表示にするや～つ
         *****************************************/
        //EnterKey押下時にソフトキーボードを非表示にする。
        rnptxvaddress.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // EnterKeyが押されたか判定
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imputMethodManager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });
        /**ここまで****************************************************/


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

/*        rnpspn = (Spinner) findViewById(R.id.rnpspn);
        String[] spnstr = {getResources().getString(R.string.month1),
                getResources().getString(R.string.month3),
                getResources().getString(R.string.month6)};
        rnpspn.setAdapter(createAdapter(spnstr));
        rnpspn.setSelection(Integer.parseInt(hashMapDB.get("spinner")));*/

        rnpbtnnext = (Button) findViewById(R.id.rnpbtnnext);
        rnpbtnnext.setOnClickListener(this);

        rnpchbb.setOnCheckedChangeListener(this);
        rnpchbs.setOnCheckedChangeListener(this);
        rnpchbk.setOnCheckedChangeListener(this);
        rnpchbnum.setOnCheckedChangeListener(this);

        writePref();
    }

    private void setDB2userInfo() {
        Cursor cursor;
        cursor = dbC.readSingleclum(1);

        if (cursor.getCount() != 0) {
            arrayService = new String[cursor.getCount()];
            arrayService = arrayset(cursor);
        } else {
            arrayService = null;
            Log.e("arrayService", "err");
        }

        cursor = dbC.readSingleclum(2);
        if (cursor.getCount() != 0) {
            arrayid = new String[cursor.getCount()];
            arrayid = arrayset(cursor);
        } else {
            Log.e("arrayid", "err");
        }

        cursor = dbC.readSingleclum(3);
        if (cursor.getCount() != 0) {
            arrayadd = new String[cursor.getCount()];
            arrayadd = arrayset(cursor);
        } else {
            Log.e("arrayadd", "err");
        }
        cursor.close();
    }

    private String[] arrayset(Cursor cursor) {
        String[] str = new String[cursor.getCount()];
        cursor.moveToFirst();
        Log.e("cursor", cursor.getString(0).toString());
        for (int i = 0; i < cursor.getCount(); i++) {
            str[i] = cursor.getString(0).toString();
            cursor.moveToNext();
        }
        //cursor.close();
        return str;
    }

    private void setDB2hash() {exID= String.valueOf(1);
        Cursor cursor = dbC.readServiceInfo(exID);
        cursor.moveToFirst();
        String[] spindata = cursor.getString(10).split(",");

        hashMapDB.put("id", String.valueOf(cursor.getInt(0)));
        hashMapDB.put("service", cursor.getString(1));
        hashMapDB.put("user_id", cursor.getString(2));
        hashMapDB.put("mailadd", cursor.getString(3));
        hashMapDB.put("char_num", cursor.getString(4));
        hashMapDB.put("char_uppercase", cursor.getString(5));
        hashMapDB.put("char_lowercase", cursor.getString(6));
        hashMapDB.put("char_symbol", cursor.getString(7));
        hashMapDB.put("num_of_char", cursor.getString(8));
        hashMapDB.put("pass", cursor.getString(11));
        hashMapDB.put("passhint", cursor.getString(12));
        hashMapDB.put("spinner", spindata[1]);

        //これはここに書かないといけない
        pref.writeConfig("id", "1");

        cursor.close();
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
            //サービス名がかぶっている、新規（idが0）の場合
            if (checkServiceName(rnptxvservice.getText().toString()) && pref.readConfig("id", "0").equals("0")) {
                Toast.makeText(this, getText(R.string.errServiceInfo), Toast.LENGTH_SHORT).show();
                return;
            }
            createSendData();
            //ネクストボタン　登録やページ遷移
            Intent intent = new Intent(RegistNewPass.this, GenePass.class);
            startActivity(intent);
        }
    }

    private boolean checkServiceName(String string) {
        if (arrayService != null) {
            for (String s : arrayService) {
                if (s.equals(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void createSendData() {
        writePref();
        pref.writeConfig("id", hashMapDB.get("id").toString());
        pref.writeConfig("user_id", rnptxvid.getText().toString());
        pref.writeConfig("service", rnptxvservice.getText().toString());
        pref.writeConfig("mailadd", rnptxvaddress.getText().toString());
        pref.writeConfig("passhint", hashMapDB.get("passhint").toString());
        pref.writeConfig("spinner", hashMapDB.get("spinner").toString());
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
