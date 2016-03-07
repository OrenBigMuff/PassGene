package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IForgot extends AppCompatActivity
        implements View.OnClickListener, PassGeneDialog.DialogListener {

    DatabaseC dbC;
    Button btnIForgot1;
    Button btnIForgot2;
    TextView txvIForgot;
    TextInputLayout tilSmsNum;
    EditText edtSmsNum;
    String smsNum;
    String rightPass;           //マスターパスワード from DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iforgot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbC = new DatabaseC(PassList2.getDbHelper());
        rightPass = dbC.readMasterPass();

        txvIForgot = (TextView)findViewById(R.id.txvIForgot);
        txvIForgot.setText(InitialSet1.fullname + " さん、\\nマスターパスワードを忘れましたか？\\n以下に携帯電話の番号を入力すると、\\nマスターパスワード(SMS)を送信することができます。");

        btnIForgot1 = (Button) findViewById(R.id.btnIForgot1);
        btnIForgot1.setOnClickListener(this);
        btnIForgot2 = (Button) findViewById(R.id.btnIForgot2);
        btnIForgot2.setOnClickListener(this);

        edtSmsNum = (EditText) findViewById(R.id.edtSmsNum);

        tilSmsNum = (TextInputLayout) findViewById(R.id.tilSmsNum);
        tilSmsNum.setError("SMSを受信可能な携帯番号(数字のみ)"); // show error
        tilSmsNum.setError(null); // hide error

        edtSmsNum.addTextChangedListener(new PGTextWatcher(tilSmsNum));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnIForgot1) {
            submitForm();
        } else if (v.getId() == R.id.btnIForgot2) {
            //初期化画面へ遷移のコードを記述
//            toast("最終確認じゃ！");
            //Dialogを表示させる
            openPG_Dialog();
        }

    }

    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {

        //DialogFragmentに渡すモノを決めてね
        String title = "アプリ初期化最終確認";
        String message = "初期化をすると、あなたの登録したすべての情報が消去されます。\n本当に初期化しますか？";
        String posi = "戻る";
        String nega = "初期化";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_pass_gene_dialog;

        FragmentManager fm = getSupportFragmentManager();
        PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "fragment_alert2");
    }

    @Override
    public void onPositiveButtonClick(DialogFragment dialog) {
        // Positiveボタンが押された時の動作
        toast(InitialSet1.fullname + " さん、\nおかえりなさい！");
        dialog.dismiss();
    }

    @Override
    public void onNegativeButtonClick(DialogFragment dialog) {
        //堀川さんからもらう予定のInitializeコードを記述する
        toast("ああああぁぁぁぁぁぁ～ 初期化しちゃうぅ～～～");
    }


    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateSmsNum()) {
            return;
        }
        //入力されたSMS番号を取得
        smsNum = edtSmsNum.getText().toString();
        toast(InitialSet1.fullname + "+さんのマスターパスワードを、" + smsNum + "へ送信します。");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Uri smsNumber = Uri.parse("sms:smsNum");       //SMS番号09012345678
        intent.setData(smsNumber);
        intent.putExtra("sms_body", "「パスじぇねくん」です。\nあなたのマスターパスワードは" + this.rightPass + "です。\n再ログイン後は、マスターパスワードの再設定を強くお勧めします。");
        startActivity(Intent.createChooser(intent, "Pick a SMS App"));
    }

    private boolean validateSmsNum() {
        if (edtSmsNum.getText().toString().trim().isEmpty()) {
            tilSmsNum.setError(getString(R.string.err_msg_iforgot));
            requestFocus(edtSmsNum);
            return false;
        }else if(edtSmsNum.getText().toString().length() < 11 ){
            tilSmsNum.setError(getString(R.string.err_msg_iforgot2));
            requestFocus(edtSmsNum);
            return false;
        } else {
            tilSmsNum.setErrorEnabled(false);
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                case R.id.edtSmsNum:
                    validateSmsNum();
                    break;
            }
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

}
