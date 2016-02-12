package com.bizan.mobile10.passgene;

import android.content.Intent;
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
    private int[] mUserInfoId = {0, 1, 2, 3, 4, 5, 6, 7};
    private String[] mUserInfoName = {"姓", "名", "生年月日", "携帯電話番号", "自宅電話番号", "ペットの名前", "出身校名", "車のナンバー"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FABボタンの動作
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistInfo.class);
                startActivity(intent);
            }
        });

        //CardViewを表示させる
        LinearLayout cardLinear = (LinearLayout) this.findViewById(R.id.lilUserInfoList);
        cardLinear.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < mUserInfoName.length; i++) {

            LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.card_user_info_list, null);

            CardView cardView = (CardView) linearLayout.findViewById(R.id.cdvUserInfoList);

            //ユーザー情報名
            TextView txvInfoUserInfo = (TextView) linearLayout.findViewById(R.id.txvInfoUserInfo);
            txvInfoUserInfo.setText(mUserInfoName[i]);

            cardView.setTag(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegistInfo.class);
                    intent.putExtra("UserInfoId", mUserInfoId[Integer.parseInt(String.valueOf(v.getTag()))]);
                    startActivity(intent);
                }
            });

            cardLinear.addView(linearLayout, i);

            //CheckBoxの動作
            final CheckBox checkBox = (CheckBox) cardView.findViewById(R.id.chbGenerateFlag);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked() == true) {
                        // チェックされた状態の時の処理を記述
                    } else {
                        // チェックされていない状態の時の処理を記述
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //CardViewを表示させる
        LinearLayout cardLinear = (LinearLayout) this.findViewById(R.id.lilUserInfoList);
        cardLinear.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < mUserInfoName.length; i++) {

            LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.card_user_info_list, null);

            CardView cardView = (CardView) linearLayout.findViewById(R.id.cdvUserInfoList);

            //ユーザー情報名
            TextView txvInfoUserInfo = (TextView) linearLayout.findViewById(R.id.txvInfoUserInfo);
            txvInfoUserInfo.setText(mUserInfoName[i]);

            cardView.setTag(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegistInfo.class);
                    intent.putExtra("UserInfoId", mUserInfoId[Integer.parseInt(String.valueOf(v.getTag()))]);
                    startActivity(intent);
                }
            });

            cardLinear.addView(linearLayout, i);

            //CheckBoxの動作
            final CheckBox checkBox = (CheckBox) cardView.findViewById(R.id.chbGenerateFlag);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked() == true) {
                        // チェックされた状態の時の処理を記述
                    } else {
                        // チェックされていない状態の時の処理を記述
                    }
                }
            });
        }
    }
}
