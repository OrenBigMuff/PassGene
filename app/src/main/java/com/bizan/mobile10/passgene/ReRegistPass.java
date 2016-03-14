package com.bizan.mobile10.passgene;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import java.util.HashMap;

public class ReRegistPass extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private PreferenceC pref;
    private TextInputLayout rrpTilServicename;
    private AutoCompleteTextView rrptxvservice;
    private AutoCompleteTextView rrptxvid;
    private AutoCompleteTextView rrptxvaddress;

//    private LinearLayout rrplayout1; //パスワードのレイアウト
//    private TextView rrptxvpass;

    private CheckBox rrpchbnum;
    private CheckBox rrpchbb;
    private CheckBox rrpchbs;
    private CheckBox rrpchbk;

    private SeekBar rrpskb;

    private TextView rrptxvpass;
    private TextView rrptxvpasslength;
    private TextView rpnpasslength;
    private Spinner rrpspn;
    private Button rrpbtnNext;
    private Button rrpbtnRegist;

    private HashMap<String, String> hashMapDB;

    private DatabaseC dbC;

    private String exID;

    private String[] arrayService;
    private String[] arrayid;
    private String[] arrayadd;
    private String[] mServiceName;

    private boolean reRegist = false;


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
        setContentView(R.layout.activity_re_regist_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbC = new DatabaseC(PassList2.getDbHelper());

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

        /**
         * DBからService名の読み込み
         */
        Cursor cursor = dbC.readServiceInfoAll();
        boolean cPlace = cursor.moveToFirst();

        mServiceName = new String[cursor.getCount()];
        int i = 0;

        while (cPlace) {
            mServiceName[i] = cursor.getString(1);
            cPlace = cursor.moveToNext();
            i++;
        }
        cursor.close();

        //前のページから飛んできたSIDをセット

//        Intent intent_rrp = getIntent();
        Bundle extrasID = getIntent().getExtras();
        if (extrasID != null) {
            exID = extrasID.getString("SID");
        } else {
            exID = "";
        }
//        if(intent_rrp != null){
//            exID = intent_rrp.getStringExtra("SID");
//        }
///////////////////////////////一時対比
        if (exID.length() > 0) {

            //readDB();
//            dbC = new DatabaseC(PassList2.getDbHelper());
            pref.writeConfig("id", "1");
/*
            Cursor cursor = dbC.readServiceInfo(exID);
            cursor.moveToFirst();
            service = cursor.getString(1);
            userid = cursor.getString(2);
            mail = cursor.getString(3);
            password = cursor.getString(11);
*/

        } else {
            pref.writeConfig("id", "0");
        }

        String readId = pref.readConfig("id", "0");

        if (!readId.equals("0")) {

            //DBデータをセットする
            setDB2hash();
            //「新規」ではなく「編集」の時は次のページでパスを表示する為にコンフィグを渡す
            pref.writeConfig("PassKeep", true);

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


        rrptxvservice = (AutoCompleteTextView) findViewById(R.id.rrptxvservice);
        if (arrayService != null) {
            rrptxvservice.setAdapter(createAdapter(arrayService));
        } else {

        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.service));
//        rrptxvservice.setHint(stringBuilder.toString());
        rrptxvservice.setThreshold(0);
        rrptxvservice.setText(hashMapDB.get("service").toString());

        rrptxvid = (AutoCompleteTextView) findViewById(R.id.rrptxvid);
        if (arrayid != null) {
            rrptxvid.setAdapter(createAdapter(arrayid));
        } else {


        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.id));
//        rrptxvid.setHint(stringBuilder.toString());
        rrptxvid.setThreshold(0);
        rrptxvid.setText(hashMapDB.get("user_id").toString());

        rrptxvaddress = (AutoCompleteTextView) findViewById(R.id.rrptxvaddress);
        if (arrayadd != null) {
            rrptxvaddress.setAdapter(createAdapter(arrayadd));
        } else {

        }stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.address));
        //stringBuilder.append(getResources().getString(R.string.mei));
//        rrptxvaddress.setHint(stringBuilder.toString());
        rrptxvaddress.setThreshold(0);
        rrptxvaddress.setText(hashMapDB.get("mailadd").toString());

        /*****************************************
         * 今井追加 入力後ソフトキーボードを非表示にするや～つ
         *****************************************/
        //EnterKey押下時にソフトキーボードを非表示にする。
        rrptxvaddress.setOnKeyListener(new View.OnKeyListener() {
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
        /**今井追加ここまで****************************************************/


        /*rrplayout1 = (LinearLayout) findViewById(R.id.rrplayout1);
        //データがなかったらパスは表示させない
        if (hashMapDB.get("id").toString().equals("")) {
            //パスワードのとこのレイアウト
            rrplayout1.setVisibility(View.GONE);
        } else {
            //パスワードのテキストビュー
            rrptxvpass = (TextView) findViewById(R.id.rrptxvpass);
            rrptxvpass.setText(hashMapDB.get("pass").toString());
        }*/

        //パスワードのテキストビュー
        rrptxvpass = (TextView) findViewById(R.id.rrptxvpass);
        rrptxvpass.setText(hashMapDB.get("pass").toString());

        //チェックボックスに値をセット
        rrpchbnum = (CheckBox) findViewById(R.id.rrpchbnum);
        if (hashMapDB.get("char_num").toString().equals("0")) {
            rrpchbnum.setChecked(false);
        } else {
            rrpchbnum.setChecked(true);
        }
        rrpchbb = (CheckBox) findViewById(R.id.rrpchbb);
        if (hashMapDB.get("char_uppercase").toString().equals("0")) {
            rrpchbb.setChecked(false);
        } else {
            rrpchbb.setChecked(true);
        }
        rrpchbs = (CheckBox) findViewById(R.id.rrpchbs);
        if (hashMapDB.get("char_lowercase").toString().equals("0")) {
            rrpchbs.setChecked(false);
        } else {
            rrpchbs.setChecked(true);
        }
        rrpchbk = (CheckBox) findViewById(R.id.rrpchbk);
        if (hashMapDB.get("char_symbol").toString().equals("0")) {
            rrpchbk.setChecked(false);
        } else {
            rrpchbk.setChecked(true);
        }

        rrpskb = (SeekBar) findViewById(R.id.rrpskb);
        rrpskb.setMax(12);
        rrpskb.setProgress(Integer.parseInt(hashMapDB.get("num_of_char").toString()) - 4);
        rrpskb.setOnSeekBarChangeListener(this);

        //文字数を表示させる
        rrptxvpasslength = (TextView) findViewById(R.id.rrptxvpasslength);
        StringBuilder stb = new StringBuilder();
        stb.append(getResources().getString(R.string.passlengthbef));
        stb.append(hashMapDB.get("num_of_char"));
        stb.append(getResources().getString(R.string.passlengthaft));
        rrptxvpasslength.setText(stb.toString());

/*        rrpspn = (Spinner) findViewById(R.id.rrpspn);
        String[] spnstr = {getResources().getString(R.string.month1),
                getResources().getString(R.string.month3),
                getResources().getString(R.string.month6)};
        rrpspn.setAdapter(createAdapter(spnstr));
        rrpspn.setSelection(Integer.parseInt(hashMapDB.get("spinner")));*/

        rrpbtnNext = (Button) findViewById(R.id.rrpbtnNext);
        rrpbtnNext.setOnClickListener(this);
        rrpbtnRegist = (Button) findViewById(R.id.rrpbtnRegist);
        rrpbtnRegist.setOnClickListener(this);

        //EditTextの外枠（TIL）にErrorヒントを表示させる
        rrpTilServicename = (TextInputLayout) findViewById(R.id.rrpTilServicename);
        rrpTilServicename.setError("サービス名は入力必須項目です。"); // show error
        rrpTilServicename.setError(null); // hide error

        rrptxvservice = (AutoCompleteTextView) findViewById(R.id.rrptxvservice);

        rrptxvservice.addTextChangedListener(new PGTextWatcher(rrpTilServicename));

        rrpchbb.setOnCheckedChangeListener(this);
        rrpchbs.setOnCheckedChangeListener(this);
        rrpchbk.setOnCheckedChangeListener(this);
        rrpchbnum.setOnCheckedChangeListener(this);

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
            toast("arrayService:error");
        }

        cursor = dbC.readSingleclum(2);
        if (cursor.getCount() != 0) {
            arrayid = new String[cursor.getCount()];
            arrayid = arrayset(cursor);
        } else {
            toast("arrayid:error");
        }

        cursor = dbC.readSingleclum(3);
        if (cursor.getCount() != 0) {
            arrayadd = new String[cursor.getCount()];
            arrayadd = arrayset(cursor);
        } else {
            toast("arrayadd:error");
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

    private void setDB2hash() {
//        exID= String.valueOf(1);
        Cursor cursor = dbC.readServiceInfo(exID);
        cursor.moveToFirst();
//        String[] spindata = cursor.getString(10).split(",");

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
        hashMapDB.put("gene_id1", cursor.getString(13));
        hashMapDB.put("gene_id2", cursor.getString(14));
        hashMapDB.put("gene_id3", cursor.getString(15));
//        hashMapDB.put("spinner", spindata[1]);

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
        pref.writeConfig("chbb", rrpchbb.isChecked());
        pref.writeConfig("chbs", rrpchbs.isChecked());
        pref.writeConfig("chbk", rrpchbk.isChecked());
        pref.writeConfig("chbn", rrpchbnum.isChecked());
        pref.writeConfig("seekbar", rrpskb.getProgress() + 4);
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
        if (!validateServiceName()) {
            toast("サービス名は入力必須項目です。");
            return;
        }
        if (!ClickTimerEvent.isClickEvent()) return;
        switch(v.getId()) {
            case R.id.rrpbtnNext:
                if (!reRegist) {
                    //パスワードを非表示
                    rrptxvpass.setVisibility(View.GONE);

                    //パスワード生成設定を表示
                    LinearLayout rrplayout2 = (LinearLayout) findViewById(R.id.rrplayout2);
                    LinearLayout rrplayout3 = (LinearLayout) findViewById(R.id.rrplayout3);
                    rrplayout2.setVisibility(View.VISIBLE);
                    rrplayout3.setVisibility(View.VISIBLE);

                    rrpbtnNext.setText(getString(R.string.next));

                    reRegist = true;
                } else {
                    //サービス名がかぶっている、新規（idが0）の場合
                    for (int i =0; i < mServiceName.length; i++) {
                        if (rrptxvservice.getText().toString().equals(mServiceName[i])) {
                            Toast.makeText(ReRegistPass.this, "過去に同じサービス名で登録されています｡", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    createSendData();
                    //ネクストボタン　登録やページ遷移
                    Intent intent = new Intent(ReRegistPass.this, GenePass.class);
                    startActivity(intent);
                    this.finish();
                }
            break;

            case R.id.rrpbtnRegist:
                createSendData();
                dataupdate();
                this.finish();
            break;
        }

        /*if (rrpbtnNext == v) {
            //サービス名がかぶっている、新規（idが0）の場合
            if (checkServiceName(rrptxvservice.getText().toString()) &&
                    pref.readConfig("id", "0").equals("0")) {
                Toast.makeText(this, getText(R.string.errServiceInfo), Toast.LENGTH_SHORT).show();
                return;
            }
            createSendData();
            //ネクストボタン　登録やページ遷移
            Intent intent = new Intent(ReRegistPass.this, GenePass.class);
            startActivity(intent);
        }*/
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
        pref.writeConfig("user_id", rrptxvid.getText().toString());
        pref.writeConfig("service", rrptxvservice.getText().toString());
        pref.writeConfig("mailadd", rrptxvaddress.getText().toString());
        pref.writeConfig("pass", hashMapDB.get("pass").toString());
        pref.writeConfig("passhint", hashMapDB.get("passhint").toString());
//        pref.writeConfig("spinner", hashMapDB.get("spinner").toString());
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //チェックボックス
        rrpchbnum.setChecked(true);
        if (buttonView == rrpchbb) {
            rrpchbb.setChecked(isChecked);
        } else if (buttonView == rrpchbs) {
            rrpchbs.setChecked(isChecked);
        } else if (buttonView == rrpchbk) {
            rrpchbk.setChecked(isChecked);
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
        rrptxvpasslength.setText(stb.toString());
        writePref();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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

    private void dataupdate() {
        String[] dataset = new String[16];
        dataset[0] = pref.readConfig("service", "service名");
        dataset[1] = pref.readConfig("user_id", "user_id名");
        dataset[2] = pref.readConfig("mailadd", "mailadd");
        dataset[3] = pref.readConfig("chbn", true) ? "1" : "0";
        dataset[4] = pref.readConfig("chbb", true) ? "1" : "0";
        dataset[5] = pref.readConfig("chbs", true) ? "1" : "0";
        dataset[6] = pref.readConfig("chbk", true) ? "1" : "0";
        dataset[7] = String.valueOf(pref.readConfig("seekbar", 8));
        dataset[8] = pref.readConfig("pass", hashMapDB.get("pass").toString());
        dataset[9] = pref.readConfig("passhint", hashMapDB.get("passhint").toString());
        dataset[10] = hashMapDB.get("gene_id1").toString();
        dataset[11] = hashMapDB.get("gene_id2").toString();
        dataset[12] = hashMapDB.get("gene_id3").toString();
        dataset[13] = "0";
        dataset[14] = "1";
        dataset[15] = pref.readConfig("spinner", "0");
        for (String s : dataset) {
            Log.e("aaaaaaaaa", s + "");
            Log.e("bbbbbbbb", "" + pref.readConfig("id", "0"));

        }

        dbC.updateServiceInfo(Integer.parseInt(pref.readConfig("id", "999")),
                dataset);
    }

    /**
     * Errorメッセージのくだり
     *
     * @return
     */
    private boolean validateServiceName() {
        if (rrptxvservice.getText().toString().trim().isEmpty()) {
            rrpTilServicename.setError(getString(R.string.err_msg_servicename));
            requestFocus(rrptxvservice);
            return false;
        } else {
            rrpTilServicename.setErrorEnabled(false);
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * TextWatcherのコーナー
     */
    private class PGTextWatcher implements TextWatcher {

        private View view;

        private PGTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.rnptxvservice:
                    validateServiceName();
                    break;
            }
        }
    }
}
