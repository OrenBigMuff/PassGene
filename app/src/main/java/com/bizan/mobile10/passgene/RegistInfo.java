package com.bizan.mobile10.passgene;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class RegistInfo extends AppCompatActivity implements PassGeneDialog.DialogListener {
    private DatabaseC dbC;
    private String mCategoryNumber;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String mRegistTag;
    private String mRegistData;

    private EditText mTag;
    private EditText mTel;
    private DatePicker dpkBirthday;
    private EditText mSurname;
    private EditText mName;
    private EditText mText;
    private EditText mNumber;
    private EditText mAll;
    private TextInputLayout mTilTag;
    private TextInputLayout mTilTel;
    private TextInputLayout mTilSurname;
    private TextInputLayout mTilName;
    private TextInputLayout mTilText;
    private TextInputLayout mTilNumber;
    private TextInputLayout mTilAll;
    private String[] mUserInfoName;

    private String mErrormessage1 = "入力情報のタイトルは必須項目です。";
    private String mErrormessage2 = "電話番号の入力は必須です。";
    private String mErrormessage3 = "姓(Last name)の入力は必須です。";
    private String mErrormessage4 = "名(First name)の入力は必須です。";
    private String mErrormessage5 = "情報の入力は必須です。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbC = new DatabaseC(PassList2.getDbHelper());

        /**
         * DBからTagの読み込み
         */
        Cursor cursor = dbC.readUserInfoAll();
        boolean cPlace = cursor.moveToFirst();

        mUserInfoName = new String[cursor.getCount()];
        int i = 0;

        while (cPlace) {
            mUserInfoName[i] = cursor.getString(1);
            cPlace = cursor.moveToNext();
            i++;
        }
        cursor.close();

        /**
         * 各情報の入力
         */
        //情報名
        mTag = (EditText) findViewById(R.id.edtRegistInfoTag);
        mTilTag = (TextInputLayout) findViewById(R.id.tilRegistInfoTag);
        mTilTag.setError(mErrormessage1); // show error
        mTilTag.setError(null); // hide error
        mTag.addTextChangedListener(new PGTextWatcher(mTilTag));
        //EnterKey押下時にソフトキーボードを非表示にする。
        mTag.setOnKeyListener(new View.OnKeyListener() {
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

        //電話番号
        mTel = (EditText) findViewById(R.id.edtRegistInfoTel);
        mTilTel = (TextInputLayout) findViewById(R.id.tilRegistInfoTel);
        mTilTel.setError(mErrormessage2); // show error
        mTilTel.setError(null); // hide error
        mTel.addTextChangedListener(new PGTextWatcher(mTilTel));

        //生年月日
        dpkBirthday = (DatePicker) findViewById(R.id.dpkRegistInfoBirthday);
        Calendar calendar = Calendar.getInstance();
        // 初期値を設定する
        dpkBirthday.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // APIレベル11以降の「日」のカレンダーを表示しない 初期値true
        dpkBirthday.setCalendarViewShown(false);

        //姓
        mSurname = (EditText) findViewById(R.id.edtRegistInfoSurname);
        mTilSurname = (TextInputLayout) findViewById(R.id.tilRegistInfoSurname);
        mTilSurname.setError(mErrormessage3); // show error
        mTilSurname.setError(null); // hide error
        mSurname.addTextChangedListener(new PGTextWatcher(mTilSurname));

        //名
        mName = (EditText) findViewById(R.id.edtRegistInfoName);
        mTilName = (TextInputLayout) findViewById(R.id.tilRegistInfoName);
        mTilName.setError(mErrormessage4); // show error
        mTilName.setError(null); // hide error
        mName.addTextChangedListener(new PGTextWatcher(mTilName));

        //フリーテキスト
        mText = (EditText) findViewById(R.id.edtRegistInfoText);
        mTilText = (TextInputLayout) findViewById(R.id.tilRegistInfoText);
        mTilText.setError(mErrormessage5); // show error
        mTilText.setError(null); // hide error
        mText.addTextChangedListener(new PGTextWatcher(mTilText));

        //フリーナンバー
        mNumber = (EditText) findViewById(R.id.edtRegistInfoNumber);
        mTilNumber = (TextInputLayout) findViewById(R.id.tilRegistInfoNumber);
        mTilNumber.setError(mErrormessage5); // show error
        mTilNumber.setError(null); // hide error
        mNumber.addTextChangedListener(new PGTextWatcher(mTilNumber));

        //オールフリー
        mAll = (EditText) findViewById(R.id.edtRegistInfoAll);
        mTilAll = (TextInputLayout) findViewById(R.id.tilRegistInfoAll);
        mTilAll.setError(mErrormessage5); // show error
        mTilAll.setError(null); // hide error
        mAll.addTextChangedListener(new PGTextWatcher(mTilAll));


        /**
         * カテゴリーのスピナー
         */
        String[] category = {"電話番号","生年月日","姓","名","フリーテキスト","フリーナンバー","フリー"};

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this, R.layout.spinner_item);
        adapterCategory.setDropDownViewResource(R.layout.spinner_item);

        //アイテムを追加します
        for (i=0; i<category.length; i++) {
            adapterCategory.add(category[i]);
        }

        Spinner spnCategory = (Spinner) findViewById(R.id.spnRegistInfo);
        //アダプターを設定します
        spnCategory.setAdapter(adapterCategory);
        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //選択されたスピナーに対する動作
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                if (item.equals("電話番号")) {
                    mCategoryNumber = "0";
                    mTilTel.setVisibility(View.VISIBLE);
                    dpkBirthday.setVisibility(View.GONE);
                    mTilSurname.setVisibility(View.GONE);
                    mTilName.setVisibility(View.GONE);
                    mTilText.setVisibility(View.GONE);
                    mTilNumber.setVisibility(View.GONE);
                    mTilAll.setVisibility(View.GONE);
                } else if (item.equals("生年月日")) {
                    mCategoryNumber = "1";
                    mTilTel.setVisibility(View.GONE);
                    dpkBirthday.setVisibility(View.VISIBLE);
                    mTilSurname.setVisibility(View.GONE);
                    mTilName.setVisibility(View.GONE);
                    mTilText.setVisibility(View.GONE);
                    mTilNumber.setVisibility(View.GONE);
                    mTilAll.setVisibility(View.GONE);
                } else if (item.equals("姓")) {
                    mCategoryNumber = "2";
                    mTilTel.setVisibility(View.GONE);
                    dpkBirthday.setVisibility(View.GONE);
                    mTilSurname.setVisibility(View.VISIBLE);
                    mTilName.setVisibility(View.GONE);
                    mTilText.setVisibility(View.GONE);
                    mTilNumber.setVisibility(View.GONE);
                    mTilAll.setVisibility(View.GONE);
                } else if (item.equals("名")) {
                    mCategoryNumber = "3";
                    mTilTel.setVisibility(View.GONE);
                    dpkBirthday.setVisibility(View.GONE);
                    mTilSurname.setVisibility(View.GONE);
                    mTilName.setVisibility(View.VISIBLE);
                    mTilText.setVisibility(View.GONE);
                    mTilNumber.setVisibility(View.GONE);
                    mTilAll.setVisibility(View.GONE);
                } else if (item.equals("フリーテキスト")) {
                    mCategoryNumber = "4";
                    mTilTel.setVisibility(View.GONE);
                    dpkBirthday.setVisibility(View.GONE);
                    mTilSurname.setVisibility(View.GONE);
                    mTilName.setVisibility(View.GONE);
                    mTilText.setVisibility(View.VISIBLE);
                    mTilNumber.setVisibility(View.GONE);
                    mTilAll.setVisibility(View.GONE);
                } else if (item.equals("フリーナンバー")) {
                    mCategoryNumber = "5";
                    mTilTel.setVisibility(View.GONE);
                    dpkBirthday.setVisibility(View.GONE);
                    mTilSurname.setVisibility(View.GONE);
                    mTilName.setVisibility(View.GONE);
                    mTilText.setVisibility(View.GONE);
                    mTilNumber.setVisibility(View.VISIBLE);
                    mTilAll.setVisibility(View.GONE);
                } else if (item.equals("フリー")) {
                    mCategoryNumber = "6";
                    mTilTel.setVisibility(View.GONE);
                    dpkBirthday.setVisibility(View.GONE);
                    mTilSurname.setVisibility(View.GONE);
                    mTilName.setVisibility(View.GONE);
                    mTilText.setVisibility(View.GONE);
                    mTilNumber.setVisibility(View.GONE);
                    mTilAll.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * 登録ボタン
         */
        Button btnElimination = (Button) findViewById(R.id.btnRegistRegistration);
        btnElimination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickTimerEvent.isClickEvent()) return;
                if (!tag()) {
                    return;
                }
                mRegistTag = mTag.getText().toString();
                for (int i =0; i < mUserInfoName.length; i++) {
                    if (mRegistTag.equals(mUserInfoName[i])) {
                        Toast.makeText(RegistInfo.this, "過去に同じユーザー情報名で登録されています｡", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                switch(mCategoryNumber) {
                    case "0":
                        if (!tel()) {
                            return;
                        }
                        mRegistData = mTel.getText().toString();
                        break;
                    case "1":
                        // 年を取得
                        mYear = dpkBirthday.getYear();
                        mYear = mYear*10000;
                        // 月を取得 0～11
                        mMonth = dpkBirthday.getMonth();
                        mMonth = (mMonth+1)*100;
                        // 日を取得
                        mDay = dpkBirthday.getDayOfMonth();
                        mRegistData = String.valueOf(mYear+mMonth+mDay);
                        break;
                    case "2":
                        if (!surname()) {
                            return;
                        }
                        mRegistData = mSurname.getText().toString();
                    case "3":
                        if (!name()) {
                            return;
                        }
                        mRegistData = mName.getText().toString();
                        break;
                    case "4":
                        if (!text()) {
                            return;
                        }
                        mRegistData = mText.getText().toString();
                        break;
                    case "5":
                        if (!number()) {
                            return;
                        }
                        mRegistData = mNumber.getText().toString();
                        break;
                    case "6":
                        if (!all()) {
                            return;
                        }
                        mRegistData = mAll.getText().toString();
                        break;
                }
                openPG_Dialog();
            }
        });
    }

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
                case R.id.edtRegistInfoTag:
                    tag();
                    break;
                case R.id.edtRegistInfoTel:
                    tel();
                    break;
                case R.id.edtRegistInfoSurname:
                    surname();
                    break;
                case R.id.edtRegistInfoName:
                    name();
                    break;
                case R.id.edtRegistInfoText:
                    text();
                    break;
                case R.id.edtRegistInfoNumber:
                    number();
                    break;
                case R.id.edtRegistInfoAll:
                    all();
                    break;
            }
        }
    }

    private boolean tag() {
        if (mTag.getText().toString().trim().isEmpty()) {
            mTilTag.setError(mErrormessage1);
            requestFocus(mTag);
            return false;
        } else {
            mTilTag.setErrorEnabled(false);
        }

        return true;
    }

    private boolean tel() {
        if (mTel.getText().toString().trim().isEmpty()) {
            mTilTel.setError(mErrormessage2);
            requestFocus(mTel);
            return false;
        } else {
            mTilTel.setErrorEnabled(false);
        }

        return true;
    }

    private boolean surname() {
        if (mSurname.getText().toString().trim().isEmpty()) {
            mTilSurname.setError(mErrormessage3);
            requestFocus(mSurname);
            return false;
        } else {
            mTilSurname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean name() {
        if (mName.getText().toString().trim().isEmpty()) {
            mTilName.setError(mErrormessage4);
            requestFocus(mName);
            return false;
        } else {
            mTilName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean text() {
        if (mText.getText().toString().trim().isEmpty()) {
            mTilText.setError(mErrormessage5);
            requestFocus(mText);
            return false;
        } else {
            mTilText.setErrorEnabled(false);
        }

        return true;
    }

    private boolean number() {
        if (mNumber.getText().toString().trim().isEmpty()) {
            mTilNumber.setError(mErrormessage5);
            requestFocus(mNumber);
            return false;
        } else {
            mTilNumber.setErrorEnabled(false);
        }

        return true;
    }

    private boolean all() {
        if (mAll.getText().toString().trim().isEmpty()) {
            mTilAll.setError(mErrormessage5);
            requestFocus(mAll);
            return false;
        } else {
            mTilAll.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {

        //DialogFragmentに渡すモノを決めてね
        String title = "登録確認";
        String message = mRegistTag + "を" + mRegistData + "で登録します｡";;
        String posi = "登録";
        String nega = "戻る";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_pass_gene_dialog;

        FragmentManager fm = getSupportFragmentManager();
        PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "fragment_alert");
    }

    /**
     * Positiveボタンが押された時の動作
     * @param dialog
     */
    @Override
    public void onPositiveButtonClick(android.support.v4.app.DialogFragment dialog) {
        if (!ClickTimerEvent.isClickEvent()) return;
        String[] value = {mRegistTag, mRegistData, mCategoryNumber};
        dbC.insertUserInfo(value);
        this.finish();
        dialog.dismiss();
    }

    /**
     * Negativeボタンが押された時の動作
     * @param dialog
     */
    @Override
    public void onNegativeButtonClick(android.support.v4.app.DialogFragment dialog) {
        dialog.dismiss();
    }
}
