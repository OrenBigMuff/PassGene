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

    private String passKpGp;

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

    private String fixed_pass = "";
    private String passHint;

    private DatabaseC dbC;

    private int userInfoId1;
    private String partsAlb1E;
    private String partsAlb1;

    private int userInfoId2;
    private String partsAlb2E;
    private String partsAlb2;

    private int userInfoId3;
    private String partsNum;
    private String partsNumE;

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
        setContentView(R.layout.activity_gene_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
/*******************************/
//        dbC = new DatabaseC(dbHelper);

        dbC = new DatabaseC(PassList2.getDbHelper());

        poolString = new PoolString();

        pref = new PreferenceC(this);

        passKpGp = pref.readConfig("pass","");
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

        /*//もし新規の場合はmakePass()に流れる
        if(!pref.readConfig("PassKeep", false)){
            makePass();
        }else{
        //もし編集から流れてきた場合は以前のパスを一旦表示する。
            String tmp_service = pref.readConfig("service", "");
            String tmp_passhint = pref.readConfig("passhint", "");
            txvSheep.setText(tmp_service + " の\n 現在のパスワード");
            gp_txv_pass.setText(passKpGp);
            gp_txv_hint.setText(tmp_passhint);
        }*/


    }

    @Override
    public void onClick(View v) {
        if (v == btnInsert) {
            //「登録」ボタン　⇒インサート処理
            if (pref.readConfig("id", "0").equals("0")) {
                datainsert();
            } else {
                dataupdate();
            }

            Intent intent = new Intent(GenePass.this, PassList2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (v == btnRegene) {
            //「ReGene」ボタン　⇒もう一度パスワード作る
            poolString.init();
            makePass();
        }
    }

    //todo 初期値文字を変える
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
        dataset[8] = pref.readConfig("fixed_pass", fixed_pass);
        dataset[9] = pref.readConfig("pass_hint", passHint);
        dataset[10] = String.valueOf(userInfoId1);
        dataset[11] = String.valueOf(userInfoId2);
        dataset[12] = String.valueOf(userInfoId3);
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

    private void datainsert() {
        String[] dataset = new String[16];
        dataset[0] = pref.readConfig("service", "service名");
        dataset[1] = pref.readConfig("user_id", "user_id名");
        dataset[2] = pref.readConfig("mailadd", "service名");
        dataset[3] = pref.readConfig("chbn", true) ? "1" : "0";
        dataset[4] = pref.readConfig("chbb", true) ? "1" : "0";
        dataset[5] = pref.readConfig("chbs", true) ? "1" : "0";
        dataset[6] = pref.readConfig("chbk", true) ? "1" : "0";
        dataset[7] = String.valueOf(pref.readConfig("seekbar", 8));
        dataset[8] = pref.readConfig("fixed_pass", fixed_pass);
        dataset[9] = pref.readConfig("pass_hint", passHint);
        dataset[10] = String.valueOf(userInfoId1);
        dataset[11] = String.valueOf(userInfoId2);
        dataset[12] = String.valueOf(userInfoId3);
        dataset[13] = "0";
        dataset[14] = "1";
        dataset[15] = pref.readConfig("spinner", "0");
        dbC.insertServiceInfo(dataset);
    }

    private String[] tempstr = new String[3];

    private String[] randuserinfo() {
        String[] str = new String[3];
        Cursor cursor = dbC.readUserInfo();
        cursor.moveToFirst();
        if (cursor.getInt(3) == 2 || cursor.getInt(3) == 3 || cursor.getInt(3) == 4) {
            str[0] = String.valueOf(cursor.getInt(0));
            str[1] = cursor.getString(1);
            str[2] = cursor.getString(2);
            tempstr = str;
            return str;
        } else {
            randuserinfo();
        }
        return tempstr;
    }

    private String[] randuserinfoNum() {
        String[] str = new String[3];
        Cursor cursor = dbC.readUserInfo();
        cursor.moveToFirst();
        if (cursor.getInt(3) == 0 || cursor.getInt(3) == 1 || cursor.getInt(3) == 5) {
            str[0] = String.valueOf(cursor.getInt(0));
            str[1] = cursor.getString(1);
            str[2] = cursor.getString(2);
            tempstr = str;
            return str;
        } else {
            randuserinfoNum();
        }
        return tempstr;
    }

    private void setStringD() {
        //とりあえず引っ張る/////////////////////////////////////////////////////////////
        String[] temp;
        temp = randuserinfo();
        userInfoId1 = Integer.parseInt(temp[0]);
        partsAlb1E = temp[1];
        partsAlb1 = temp[2];
        temp = randuserinfo();
        userInfoId2 = Integer.parseInt(temp[0]);
        partsAlb2E = temp[1];
        partsAlb2 = temp[2];
        String partsSign = "@%+^#$:\\/!?.(){-_[~]}'";
        String partsSignE = getResources().getString(R.string.passk);
        temp = randuserinfoNum();
        userInfoId3 = Integer.parseInt(temp[0]);
        partsNumE = temp[1];
        partsNum = temp[2];

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
                numB = passM1.length();
                Log.e("passM1", "文字不足");
            }
        }
        //文字上○桁
        if (chbs && numS > 0) {
            if (passM2.length() >= numS) {
                passM2 = passM2.substring(0, numS);

            } else {
                numS = passM2.length();
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
        if (passM1.length() < passM2.length()) {
            for (int k = 0; k < passM1.length(); k++) {
                stringC.append(passM1.substring(k, k + 1));
                stringC.append(passM2.substring(k, k + 1));
            }
            int temp = passM2.length() - passM1.length();
            stringC.append(passM2.substring(temp + 1, passM2.length()));
        } else if (passM1.length() > passM2.length()) {
            for (int k = 0; k < passM2.length(); k++) {
                stringC.append(passM1.substring(k, k + 1));
                stringC.append(passM2.substring(k, k + 1));
            }
            int temp = passM1.length() - passM2.length();
            stringC.append(passM1.substring(temp + 1, passM1.length()));
        } else {
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

        passHint = poolString.makeString();
        fixed_pass = minipass;
        txvSheep.setText(setComment());

    }

    private String setComment() {
        StringBuilder stb = new StringBuilder();
        stb.append(pref.readConfig("service", ""));
        stb.append(getText(R.string.pg_comment));
        return stb.toString();
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

    String returnstring = "";

    private String loopMakePass(String baseStr, int totalNum) {
        StringBuilder stb = new StringBuilder();

        stb.append(baseStr);
        if (stb.toString().length() < totalNum) {
            try {
                stb.append(stb.toString().substring(0, totalNum - stb.toString().length()));
            } catch (Exception e) {
                stb.append(stb.toString().substring(0, stb.toString().length()));
            }
            poolString.setLoop(true);
            loopMakePass(stb.toString(), totalNum);
        } else {
            returnstring = stb.toString();
            return stb.toString();
        }
        return returnstring;
    }

    @Override
    public void onPause() {
        super.onPause();
        resetSendData();
    }

    private void resetSendData() {
        //writePref();
        pref.writeConfig("id", "0");
        pref.writeConfig("user_id", "");
        pref.writeConfig("service", "");
        pref.writeConfig("mailadd", "");
        pref.writeConfig("passhint", "");
        pref.writeConfig("passhint", "");
    }
}
