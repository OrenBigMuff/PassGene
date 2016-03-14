package com.bizan.mobile10.passgene;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IForgot extends AppCompatActivity
        implements View.OnClickListener, PassGeneDialog.DialogListener {

    private DatabaseC dbC;
    private PreferenceC pref;
    private Button btnIForgot1;
    private Button btnIForgot2;
    private TextView txvIForgot;
    private TextInputLayout tilSmsNum;
    private EditText edtSmsNum;
    private String smsNum;
    private String rightPass;           //マスターパスワード from DB

    PendingIntent sentIntent;
    SMSSentBroadcastReceiver sentBroadcastReceiver;
    final String ACTION_SENT = "com.bizan.mobile10.passgene.ACTION_SENT";

//    SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iforgot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = new PreferenceC(this);
        dbC = new DatabaseC(PassList2.getDbHelper());
        rightPass = dbC.readMasterPass();

        sentIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SENT), 0);

//        smsManager = SmsManager.getDefault();

        txvIForgot = (TextView) findViewById(R.id.txvIForgot);
        txvIForgot.setText(PassList2.getUserName() + " さん、\nマスターパスワードを忘れましたか？\n以下に携帯電話の番号を入力すると、\nマスターパスワードを送信(SMS)することができます。");

        btnIForgot1 = (Button) findViewById(R.id.btnIForgot1);
        btnIForgot1.setOnClickListener(this);
//        btnIForgot2 = (Button) findViewById(R.id.btnIForgot2);
//        btnIForgot2.setOnClickListener(this);

        edtSmsNum = (EditText) findViewById(R.id.edtSmsNum);

        tilSmsNum = (TextInputLayout) findViewById(R.id.tilSmsNum);
        tilSmsNum.setError("SMSを受信可能な携帯番号(数字のみ)"); // show error
        tilSmsNum.setError(null); // hide error

        edtSmsNum.addTextChangedListener(new PGTextWatcher(tilSmsNum));

        //EnterKey押下時にソフトキーボードを非表示にする。
        edtSmsNum.setOnKeyListener(new View.OnKeyListener() {
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIForgot1:
//                submitForm();
                if (!validateSmsNum()) {
                    return;
                }
                openPG_Dialog();

                break;
        }
//        if (v.getId() == R.id.btnIForgot1) {
//            submitForm();
//        } else if (v.getId() == R.id.btnIForgot2) {
//            //初期化画面へ遷移のコードを記述
////            toast("最終確認じゃ！");
//            //Dialogを表示させる
//            openPG_Dialog();
//        }

    }

    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {
        smsNum = edtSmsNum.getText().toString();
        //DialogFragmentに渡すモノを決めてね
        String title = "SMS送信の最終確認";
        String message = PassList2.getUserName() + "さんのマスターパスワードを、" + smsNum + "へ送信します。" + "SMSが送信可能な端末でのみご利用いただけます。また、ご利用状況によって通信料が発生する場合があります。\n本当に送信しますか？";
        String posi = "送信する";
        String nega = "しない";
        //ダイアログのレイアウトResId
        int resId_dialog = R.layout.fragment_pass_gene_dialog;

        FragmentManager fm = getSupportFragmentManager();
        PassGeneDialog alertDialog = PassGeneDialog.newInstance(title, message, posi, nega, resId_dialog);
        alertDialog.show(fm, "fragment_iForgot");
    }

    @Override
    public void onPositiveButtonClick(DialogFragment dialog) {
        // Positiveボタンが押された時の動作
        sendSMS();
//        toast("ご指定の番号にマスターパスワードを送信しました。");
        dialog.dismiss();
    }

    @Override
    public void onNegativeButtonClick(DialogFragment dialog) {

        dialog.dismiss();
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
        toast(PassList2.getUserName() + "さんのマスターパスワードを、" + smsNum + "へ送信します。");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        Uri smsNumber = Uri.parse("sms:" + smsNum);       //SMS番号09012345678
        intent.setData(smsNumber);
        intent.putExtra("sms_body", "「パスじぇねくん」です。\nあなたのマスターパスワードは" + this.rightPass + "です。\nアプリ起動後は、マスターパスワードの再設定を強くお勧めします。");
        startActivity(Intent.createChooser(intent, "Pick a SMS App"));

    }

    private void sendSMS() {

        //入力されたSMS番号を取得
        smsNum = edtSmsNum.getText().toString();
        toast(PassList2.getUserName() + "さんのマスターパスワードを、" + smsNum + "へ送信します。");
        SmsManager smsMgr = SmsManager.getDefault();
        String smsBody = "「パスじぇねくん」です。\nあなたのマスターパスワードは" + this.rightPass + "です。\nアプリ起動後は、マスターパスワードの再設定を強くお勧めします。";
        smsMgr.sendTextMessage(
                smsNum,
                null,
                smsBody,
                sentIntent,
                null);
    }

    private boolean validateSmsNum() {
        if (edtSmsNum.getText().toString().trim().isEmpty()) {
            tilSmsNum.setError(getString(R.string.err_msg_iforgot));
            requestFocus(txvIForgot);
            return false;
        } else if (edtSmsNum.getText().toString().length() < 11) {
            tilSmsNum.setError(getString(R.string.err_msg_iforgot2));
            requestFocus(txvIForgot);
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

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(sentBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sentBroadcastReceiver = new SMSSentBroadcastReceiver();
        registerReceiver(sentBroadcastReceiver, new IntentFilter(ACTION_SENT));
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

    private class SMSSentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMSの送信が完了しました。", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getBaseContext(), "SMSの送信がキャンセルされました。", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "Error：Generic Failure", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getBaseContext(), "お客様の端末ではSMSは\nご利用いただけません。", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), "Error：NULL PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "お客様の通信状況をご確認ください。", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
