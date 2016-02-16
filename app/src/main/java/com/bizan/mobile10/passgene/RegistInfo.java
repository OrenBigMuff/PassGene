package com.bizan.mobile10.passgene;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistInfo extends AppCompatActivity {
    private static int mCategoryNumber;
    private static String mTagName;
    private static String mDataTel;
    private static int mDataBirthday;
    private static String mDataSurname;
    private static String mDataName;
    private static String mDataText;
    private static int mDataNumber;
    private static String mDataAll;

    private InputFilter[] filtersAll = { new AllFilter() };
    private InputFilter[] filtersText = { new TextFilter() };
    private InputFilter[] filtersNumber = { new NumberFilter() };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //情報名の入力
        EditText tag = (EditText) findViewById(R.id.edtRegistInfoTag);
        mTagName = tag.getText().toString();

        //カテゴリーのスピナー
        String[] category = {getString(R.string.regist_info_category1),getString(R.string.regist_info_category2),
                getString(R.string.regist_info_category3),getString(R.string.regist_info_category4),
                getString(R.string.regist_info_category5),getString(R.string.regist_info_category6),
                getString(R.string.regist_info_category7)};

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //アイテムを追加します
        for (int i=0; i<category.length; i++) {
            adapterCategory.add(category[i]);
        }
        Spinner spnRegistInfo = (Spinner) this.findViewById(R.id.spnRegistInfo);
        //アダプターを設定します
        spnRegistInfo.setAdapter(adapterCategory);
        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録
        spnRegistInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                //選択されたアイテムによりカテゴリーナンバーを設定
                String item = (String) spinner.getSelectedItem();
                if (item.equals(getString(R.string.regist_info_category1))) {
                    mCategoryNumber = 0;
                } else if (item.equals(getString(R.string.regist_info_category2))) {
                    mCategoryNumber = 1;
                } else if (item.equals(getString(R.string.regist_info_category3))) {
                    mCategoryNumber = 2;
                } else if (item.equals(getString(R.string.regist_info_category4))) {
                    mCategoryNumber = 3;
                } else if (item.equals(getString(R.string.regist_info_category5))) {
                    mCategoryNumber = 4;
                } else if (item.equals(getString(R.string.regist_info_category6))) {
                    mCategoryNumber = 5;
                } else if (item.equals(getString(R.string.regist_info_category7))) {
                    mCategoryNumber = 6;
                }
                Toast.makeText(RegistInfo.this, String.valueOf(mCategoryNumber), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //電話番号
        EditText tel = (EditText) findViewById(R.id.edtRegistInfoTel);
        tel.setFilters(filtersText);
        mDataTel = tel.getText().toString();

        //生年月日

        //姓
        EditText surname = (EditText) findViewById(R.id.edtRegistInfoSurname);
        surname.setFilters(filtersText);
        mDataSurname = surname.getText().toString();

        //名
        EditText name = (EditText) findViewById(R.id.edtRegistInfoName);
        name.setFilters(filtersText);
        mDataName = name.getText().toString();

        //フリーテキスト
        EditText text = (EditText) findViewById(R.id.edtRegistInfoText);
        text.setFilters(filtersText);
        mDataText = text.getText().toString();

        //フリーナンバー
        EditText number = (EditText) findViewById(R.id.edtRegistInfoNumber);
        number.setFilters(filtersNumber);
        mDataNumber = new Integer(number.getText().toString());

        //フリーテキスト
        EditText all = (EditText) findViewById(R.id.edtRegistInfoAll);
        all.setFilters(filtersAll);
        mDataText = all.getText().toString();

        //カテゴリーによるデータ項目の表示非表示　setVisibility(View.INVISIBLE)
        switch(mCategoryNumber) {
            case 0:

        }

        //登録ボタン
        Button btnElimination = (Button) this.findViewById(R.id.btnRegistRegistration);
        btnElimination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class AllFilter implements InputFilter {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            if( source.toString().matches("^[a-zA-Z0-9]+$") ){
                return source;
            }else{
                return "";
            }
        }
    }

    class TextFilter implements InputFilter {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            if( source.toString().matches("^[a-zA-Z]+$") ){
                return source;
            }else{
                return "";
            }
        }
    }

    class NumberFilter implements InputFilter {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            if( source.toString().matches("^[0-9]+$") ){
                return source;
            }else{
                return "";
            }
        }
    }
}
