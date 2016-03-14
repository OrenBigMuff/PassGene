package com.bizan.mobile10.passgene;
/**
 * imai
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InitialSet1 extends AppCompatActivity
        implements View.OnClickListener {

    /*private static DatabaseHelper dbH;
    private DatabaseC dbC;*/
    private PreferenceC pref;
    private Button btn;

/*    private final String DB_NAME = "pg.db"; //データベース名
    private final int DB_VERSION = 1;       //データベースのバージョン
    //テーブル名
    private static final String[] DB_TABLE = {"service_info", "user_info"};*/

    private TextInputLayout inputLayoutL;
    private TextInputLayout inputLayoutF;

    private EditText inputLastname;
    private EditText inputFirstname;
    private DatePicker dpkBirthDay;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat dateFormatter2;
    private String dispBirth;
    private String registBirth;
    private String lastname;
    private String firstname;
    private String fullname;
    private int mYear;
    private int mMonth;
    private int mDay;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_set1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*String[] dbColTable = {
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

        dbH = new DatabaseHelper(this, DB_NAME, DB_VERSION, DB_TABLE, dbColTable);
        dbC = new DatabaseC(this.dbH);*/

        pref = new PreferenceC(this);

        dpkBirthDay = (DatePicker) findViewById(R.id.dpkBirthDay);
        dpkBirthDay.setCalendarViewShown(false);
        dpkBirthDay.updateDate(1996, 2, 16);  //Mは　+1で表示

        dateFormatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.US);
        dateFormatter2 = new SimpleDateFormat("yyyyMMdd", Locale.US);

        btn = (Button) findViewById(R.id.btnInitialSet1);
        btn.setOnClickListener(this);

        inputLastname = (EditText) findViewById(R.id.inputLastname);
        inputFirstname = (EditText) findViewById(R.id.inputFirstname);

        //EnterKey押下時にソフトキーボードを非表示にする。
        inputFirstname.setOnKeyListener(new View.OnKeyListener() {
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

        //EditTextの外枠（TIL）にErrorヒントを表示させる
        inputLayoutL = (TextInputLayout) findViewById(R.id.inputUserLastname);

        inputLayoutL.setError("姓をアルファベットで入力して下さい。"); // show error
        inputLayoutL.setError(null); // hide error

        inputLayoutF = (TextInputLayout) findViewById(R.id.inputUserFirstname);

        inputLayoutF.setError("名をアルファベットで入力して下さい。"); // show error
        inputLayoutF.setError(null); // hide error

        inputLastname.addTextChangedListener(new PGTextWatcher(inputLayoutL));
        inputFirstname.addTextChangedListener(new PGTextWatcher(inputLayoutF));

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateLastname()) {
            return;
        }
        if (!validateFirstname()) {
            return;
        }

        lastname = inputLastname.getText().toString();
        firstname = inputFirstname.getText().toString();
        //ユーザーの名前を変数nameに・・・
        fullname = lastname + " " + firstname;

        mYear = dpkBirthDay.getYear();
        mMonth = dpkBirthDay.getMonth();
        mDay = dpkBirthDay.getDayOfMonth();

        dispBirth = String.valueOf(mYear) + "年" + String.valueOf(mMonth + 1) + "月" + String.valueOf(mDay) + "日";

        if (mMonth >= 9) {
            if (mDay >= 9) {
                registBirth = String.valueOf(mYear) + String.valueOf(mMonth + 1) + String.valueOf(mDay);
            } else {
                registBirth = String.valueOf(mYear) + String.valueOf(mMonth + 1) + "0" + String.valueOf(mDay);
            }
        } else {
            if (mDay >= 9) {
                registBirth = String.valueOf(mYear) + "0" + String.valueOf(mMonth + 1) + String.valueOf(mDay);
            }
            registBirth = String.valueOf(mYear) + "0" + String.valueOf(mMonth + 1) + "0" + String.valueOf(mDay);
        }


    }

    /**
     * Errorメッセージのくだり
     *
     * @return
     */
    private boolean validateLastname() {
        if (inputLastname.getText().toString().trim().isEmpty()) {
            inputLayoutL.setError(getString(R.string.err_msg_lastname));
            requestFocus(inputLastname);
            return false;
        } else {
            inputLayoutL.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateFirstname() {
        if (inputFirstname.getText().toString().trim().isEmpty()) {
            inputLayoutF.setError(getString(R.string.err_msg_firstname));
            requestFocus(inputFirstname);
            return false;
        } else {
            inputLayoutF.setErrorEnabled(false);
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
                case R.id.inputUserLastname:
                    validateLastname();
                    break;
                case R.id.inputUserFirstname:
                    validateFirstname();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!ClickTimerEvent.isClickEvent()) return;

            if (v.getId() == R.id.btnInitialSet1) {
//                btn.setClickable(false);

//            if(inputLastname.getText().toString().equals("") || inputFirstname.getText().toString().equals("")){
//
//            }else {
                if (!validateLastname()) {
                    return;
                }
                if (!validateFirstname()) {
                    return;
                }
                submitForm();

                toast("はじめまして" + this.fullname + "さん、\n生年月日は、" + this.dispBirth + "で登録します。" + "\n次はマスターパスワードを決めてください。");
                //InitialSet1を通過したのでコンフィグにWriteする
                pref.writeConfig("p0_1", true);

                Intent intent = new Intent(InitialSet1.this, InitialSet2.class);
                intent.putExtra("Lastname", lastname);
                intent.putExtra("Firstname", firstname);
                intent.putExtra("Birthday", registBirth);
                startActivity(intent);
                InitialSet1.this.finish();

            }

    }

    /*public static String getDB_TABLE_S() {
        return DB_TABLE[0];
    }

    public static String getDB_TABLE_U() {
        return DB_TABLE[1];
    }

    //DBヘルパーゲットだぜ なや～つ DBに関係する
    public static DatabaseHelper getDbHelper() {
        return dbH;
    }*/

    /**
     * あったら便利！トーストメソッドだよ
     *
     * @param text
     */
    private void toast(String text) {
        if (text == null) {
            text = "";
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
