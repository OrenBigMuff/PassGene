package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserInfoList extends AppCompatActivity {
    private DatabaseC dbC;
    private String[] mUserInfoId;
    private String[] mUserInfoName;
    private String[] mUserInfo;
    private String[] mDeleteFlag;
    private String[] mUselessFlag;

    private static String setUserInfoId;
    private static String setUserInfoName;
    private static String setUserInfo;

    //押下されたユーザー情報カードのIDを他Activityで使用するためのメソッド
    public static String getUserInfoId() {
        return setUserInfoId;
    }
    //押下されたユーザー情報カードの情報名を他Activityで使用するためのメソッド
    public static String getUserInfoName() {
        return setUserInfoName;
    }
    //押下されたユーザー情報カードの情報を他Activityで使用するためのメソッド
    public static String getUserInfo() {
        return setUserInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * データベースからデータ読み出し
         */
        Cursor cursor = dbC.readUserInfoAll();
        int j = 0;
        while (cursor.moveToNext()) {
            mUserInfoId[j] = cursor.getString(0);
            mUserInfoName[j] = cursor.getString(1);
            mUserInfo[j] = cursor.getString(2);
            mDeleteFlag[j] = cursor.getString(4);
            mUselessFlag[j] = cursor.getString(5);
            j++;
        }

        /**
         * FABボタンの動作
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabUserInfoList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoList.this, RegistInfo.class);
                startActivity(intent);
            }
        });

        /**
         * CardViewを表示させる
         */
        LinearLayout cardLinear = (LinearLayout) findViewById(R.id.lilUserInfoList);
        cardLinear.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = mUserInfoId.length; i > 0; i--) {

            if (mDeleteFlag[i].equals("0")) {
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.card_user_info_list, null);

                CardView cardView = (CardView) linearLayout.findViewById(R.id.cdvUserInfoList);

                //ユーザー情報名
                TextView txvUserInfo = (TextView) linearLayout.findViewById(R.id.txvCardUserInfo);
                txvUserInfo.setText(mUserInfoName[i]);

                //カード押下時の動作
                cardView.setTag(i);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInfoList.this, UserConf2.class);
                        intent.putExtra("CLASSNAME","com.bizan.mobile10.passgene.UserInfoIndex");
                        startActivity(intent);

                        setUserInfoId = mUserInfoId[Integer.parseInt(String.valueOf(v.getTag()))];
                        setUserInfoName = mUserInfoName[Integer.parseInt(String.valueOf(v.getTag()))];
                        setUserInfo = mUserInfo[Integer.parseInt(String.valueOf(v.getTag()))];

                    }
                });

                cardLinear.addView(linearLayout, i);

                //CheckBoxの動作
                final CheckBox checkBox = (CheckBox) cardView.findViewById(R.id.chbGenerateFlag);
                final int userInfoId = Integer.parseInt(mUserInfoId[i]);

                if (mUselessFlag[i].equals("0")) {
                    checkBox.setChecked(true);
                }

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBox.isChecked() == true) {
                            // チェックされた状態の時の処理を記述
                            dbC.changeUserInfoUselessFlag(userInfoId, "0");
                        } else {
                            // チェックされていない状態の時の処理を記述
                            dbC.changeUserInfoUselessFlag(userInfoId, "1");
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * CardViewを表示させる
         */
        LinearLayout cardLinear = (LinearLayout) findViewById(R.id.lilUserInfoList);
        cardLinear.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = mUserInfoId.length; i > 0; i--) {

            if (mDeleteFlag[i].equals("0")) {
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.card_user_info_list, null);

                CardView cardView = (CardView) linearLayout.findViewById(R.id.cdvUserInfoList);

                //ユーザー情報名
                TextView txvUserInfo = (TextView) linearLayout.findViewById(R.id.txvCardUserInfo);
                txvUserInfo.setText(mUserInfoName[i]);

                //カード押下時の動作
                cardView.setTag(i);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserInfoList.this, UserInfoIndex.class);
                        startActivity(intent);

                        setUserInfoId = mUserInfoId[Integer.parseInt(String.valueOf(v.getTag()))];
                        setUserInfoName = mUserInfoName[Integer.parseInt(String.valueOf(v.getTag()))];
                        setUserInfo = mUserInfo[Integer.parseInt(String.valueOf(v.getTag()))];

                    }
                });

                cardLinear.addView(linearLayout, i);

                //CheckBoxの動作
                final CheckBox checkBox = (CheckBox) cardView.findViewById(R.id.chbGenerateFlag);
                final int userInfoId = Integer.parseInt(mUserInfoId[i]);

                if (mUselessFlag[i].equals("0")) {
                    checkBox.setChecked(true);
                }

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBox.isChecked() == true) {
                            // チェックされた状態の時の処理を記述
                            dbC.changeUserInfoUselessFlag(userInfoId, "0");
                        } else {
                            // チェックされていない状態の時の処理を記述
                            dbC.changeUserInfoUselessFlag(userInfoId, "1");
                        }
                    }
                });
            }
        }
    }
}
