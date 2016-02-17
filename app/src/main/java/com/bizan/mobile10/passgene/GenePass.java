package com.bizan.mobile10.passgene;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GenePass extends AppCompatActivity implements View.OnClickListener {

    private TextView txvSheep;
    private TextView txvPass;
    private TextView txvHint;

    private Button btnInsert;
    private Button btnRegene;

    private PreferenceC pref;
    private boolean chbb;
    private boolean chbs;
    private boolean chbk;
    private boolean chbnum;
    private int seekbarP = 8;
    private PoolString poolString;

    private String passM1;
    private String passM2;
    private String passS;
    private String passN;
    private String passM1E;
    private String passM2E;
    private String passSE;
    private String passNE;
    int totalNum;
    private String partsSign = "@%+^#$:\\/!?.(){-_[~]}'";

    private Button debugbutton;

    private TextView gp_txv_pass;
    private TextView gp_txv_hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gene_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        poolString = new PoolString();

        pref = new PreferenceC(this);
        chbb = pref.readConfig("chbb", true);
        chbs = pref.readConfig("chbs", true);
        chbk = pref.readConfig("chbk", true);
        chbnum = pref.readConfig("chbn", true);
        seekbarP = pref.readConfig("seekbar", 8) - 4;

        gp_txv_pass = (TextView) findViewById(R.id.gp_txv_pass);
        gp_txv_hint = (TextView) findViewById(R.id.gp_txv_hint);

        txvSheep = (TextView) findViewById(R.id.gp_txv_sheep);
        txvPass = (TextView) findViewById(R.id.gp_txv_pass);
        txvHint = (TextView) findViewById(R.id.gp_txv_hint);

        btnInsert = (Button) findViewById(R.id.pg_btn_insert);
        btnInsert.setOnClickListener(this);

        btnRegene = (Button) findViewById(R.id.pg_btn_regene);
        btnRegene.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnInsert) {
            //インサート処理
        } else if (v == btnRegene) {
            //もう一度パスワード作る
            makePass();
        }
    }

    private void setStringD() {
        //とりあえず引っ張る/////////////////////////////////////////////////////////////
        String partsAlb1 = "zxyvw";
        String partsAlb2 = "aiu";
        String partsSign = "@%+^#$:\\/!?.(){-_[~]}'";
        String partsNum = "19870605";
        String partsAlb1E = "姓";
        String partsAlb2E = "名";
        String partsSignE = "記号";
        String partsNumE = "生年月日";

        passM1 = partsAlb1.toLowerCase();
        passM2 = partsAlb2.toLowerCase();
        passS = partsSign;
        passN = partsNum;
        passM1E = partsAlb1E;
        passM2E = partsAlb2E;
        passSE = partsSignE;
        passNE = partsNumE;

        //文字数
        totalNum = seekbarP + 4;
        poolString.setTotalNum(String.valueOf(totalNum));
        Log.e("passN", passN);
        Log.e("passM1", passM1);
        Log.e("passM2", passM2);
        Log.e("passS", passS);
        Log.e("e", totalNum + "");
    }

    private void makePass() {
        setStringD();

        //文字数決定/////////////////////////////////////////////////////////////////////////////////
        //文字数決定/////////////////////////////////////////////////////////////////////////////////
        int numN = 0;
        int numB = 0;
        int numS = 0;
        int numK = 0;
        int i = 0;
        if (chbk) {
            numK++;
            i++;
        }
        for (; i < totalNum; ) {
            if (chbs) {
                numS++;
                i++;
            }
            if (chbb && i < totalNum) {
                numB++;
                i++;
            }
            if (chbnum && i < totalNum) {
                numN++;
                i++;
            }
        }

        Log.e("合計", numK + numB + numN + numS + ":numk::" + numK + ":numB::" + numB + ":numN::" + numN + ":numS::" + numS + "");
        //文字数決定/////////////////////////////////////////////////////////////////////////////////

        //文字の切り出し/////////////////////////////////////////////////////////////////////////////
        //文字の切り出し/////////////////////////////////////////////////////////////////////////////
        //ナンバー下○桁　
        if (chbnum && numN > 0) {
            if (passN.length() >= numN) {
                passN = passN.substring(passN.length() - numN, passN.length());
                //各桁に数字を足したり引いたり
                Log.e("passN", passN);
            } else {
                //文字数が足りてないときの処理
                Log.e("passN", "文字不足");
            }
            passN = plusNumber(passN);
        }
        //文字上○桁
        if (chbb && numB > 0) {
            if (passM1.length() >= numB) {
                passM1 = passM1.substring(0, numB);
                Log.e("passM1", passM1);
            } else {
                //文字数が足りないとき
                Log.e("passM1", "文字不足");
            }
        }
        //文字上○桁
        if (chbs && numS > 0) {
            if (passM2.length() >= numS) {
                passM2 = passM2.substring(0, numS);
                Log.e("passM2", "文字不足");
            }
        }

        if (chbk && numK > 0) {
            int randNum = new java.util.Random().nextInt(partsSign.length());
            passS = passS.substring(randNum, randNum + 1);
            Log.e("passS", passS);
        }
        //文字の切り出し/////////////////////////////////////////////////////////////////////////////


        //ミックスする/////////////////////////////////////////////////////////////////////////
        //ミックスする////////////////////////////////////////////////////////////////////////
        StringBuilder stringB = new StringBuilder();
        stringB.append(passM1);
        stringB.append(passM2);
        String strtemp1 = stringB.toString();

        String strtemp2 = "";
        StringBuilder stringC = new StringBuilder();
        //ミックス
        Log.e("passM1", passM1);//zxyvw
        Log.e("passM2", passM2);//aiu
        if(passM1.length() < passM2.length()){
            for (int k = 0; k < passM1.length(); k++) {
                stringC.append(passM1.substring(k, k + 1));
                stringC.append(passM2.substring(k, k + 1));
            }
            int temp = passM2.length() - passM1.length();
            stringC.append(passM2.substring(temp + 1, passM2.length()));
        }else if(passM1.length() > passM2.length()){
            for (int k = 0; k < passM2.length(); k++) {
                stringC.append(passM1.substring(k, k + 1));
                stringC.append(passM2.substring(k, k + 1));
            }
            int temp = passM1.length() - passM2.length();
            stringC.append(passM1.substring(temp + 1, passM1.length()));
        }else {
            for (int k = 0; k < passM2.length(); k++) {
                stringC.append(passM1.substring(k, k + 1));
                stringC.append(passM2.substring(k, k + 1));
            }
        }

        //ミックスする/////////////////////////////////////////////////////////////////////////


        //大きなセットができる//////////////////////////////////////////////////////////////////
        //大きなセットができる//////////////////////////////////////////////////////////////////
        StringBuilder stb = new StringBuilder();
        if (chbb) {
            poolString.setElem1S(passM1E);
            poolString.setElem1N(String.valueOf(numB));
            stb.append(passM1);
        }
        if (chbs) {
            poolString.setElem2S(passM2E);
            poolString.setElem2N(String.valueOf(numS));
            stb.append(passM2);
        }
        if (chbk) {
            poolString.setSign(passS);
            stb.append(passS);
        }
        if (chbnum) {
            poolString.setnElemS(passNE);
            if (passN.length() > numN) {
                poolString.setnElemN(String.valueOf(numN));
            } else {
                poolString.setnElemN(String.valueOf(passN.length()));
            }
            stb.append(passN);
        }
        //大きなセットができる//////////////////////////////////////////////////////////////////


        //ランダムで大文字をはめ込む
        String haveUpp = randomUpperString(stb.toString());
        int stringLength = passM1.length() + passM2.length();
        int tempRnad = new java.util.Random().nextInt(2);
        for (int j = 0; j < tempRnad; j++) {
            haveUpp = randomUpperString(haveUpp);
        }
        //文字が少ない場合繰り返す
        String minipass = loopMakePass(haveUpp, totalNum);


        if (chbb && !chbs) {
            minipass = minipass.toUpperCase();
        }
        if (!chbb && chbs) {
            minipass = minipass.toLowerCase();
        }

        gp_txv_pass.setText(minipass);
        gp_txv_hint.setText(poolString.makeString());
    }

    /**
     * 一文字ランダムで大文字に変換
     *
     * @param string
     * @return
     */
    private String randomUpperString(String string) {
        if (string.matches(".[0-9@%\\+\\^#\\$:\\\\\\/!\\?\\.\\(\\)\\{\\-_\\[\\]~\\}']*") || string.equals(string.toUpperCase())) {
            return string;
        }
        int randNum = new java.util.Random().nextInt(string.length() - 1);
        String headString = "";
        String uppString = "";
        String buttomString = "";
        if (randNum == 0) {
            headString = "";
            uppString = string.substring(0, 1);
            buttomString = string.substring(1, string.length());
        } else {
            headString = string.substring(0, randNum);
            uppString = string.substring(randNum, randNum + 1);
            buttomString = string.substring(randNum + 1, string.length());
        }

        if (uppString.matches("[a-z]")) {
            if (chbb && chbs) {
                poolString.setUpperN(String.valueOf(randNum + 1));
            }
            uppString = uppString.toUpperCase();
            StringBuilder stb = new StringBuilder();
            stb.append(headString);
            stb.append(uppString);
            stb.append(buttomString);
            return stb.toString();
        } else {
            String str = randomUpperString(string);
            return str;
        }
    }

    private String plusNumber(String numString) {
        int plus = 1;
        String str = "";
        int num = Integer.parseInt(numString);
        for (int i = 0; i < numString.length(); i++) {
            if (numString.substring(numString.length() - 1 - i, numString.length() - i).equals("9")) {
                num = (int) (num - ((Math.pow(10, i)) * 9));
            } else {
                num = (int) (num + ((Math.pow(10, i)) * plus));
            }
        }
        str = String.valueOf(num);
        poolString.setnElemP(String.valueOf(plus));
        return str;
    }

    private String loopMakePass(String baseStr, int totalNum) {
        StringBuilder stb = new StringBuilder();
        stb.append(baseStr);
        if (stb.toString().length() < totalNum) {
            stb.append(stb.toString().substring(0, totalNum - stb.toString().length()));
            poolString.setLoop(true);
            loopMakePass(stb.toString(), totalNum);
        } else {
            return stb.toString();
        }
        return stb.toString();
    }

}
