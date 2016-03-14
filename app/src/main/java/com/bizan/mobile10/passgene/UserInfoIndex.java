package com.bizan.mobile10.passgene;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoIndex extends AppCompatActivity implements PassGeneDialog.DialogListener {
    private DatabaseC dbC;
    private String mUserInfoId;
    private String mUserInfoName;
    private String mUserInfo;
    private String mUseService;

    private String[] mServiceId1;
    private String[] mServiceId2;
    private String[] mServiceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * データベースからデータ読み出し
         */
        DatabaseHelper dbHelper = PassList2.getDbHelper();
        dbC = new DatabaseC(dbHelper);

        UserInfoList userInfoList = new UserInfoList();
        mUserInfoId = userInfoList.getUserInfoId();
        mUserInfoName = userInfoList.getUserInfoName();
        mUserInfo = userInfoList.getUserInfo();

        Cursor cursor1 = dbC.readGeneUseid(mUserInfoId);
        Cursor cursor2 = dbC.readServiceInfoAll();

        boolean cPlace1 = cursor1.moveToFirst();       // 参照先を一番始めに
        boolean cPlace2 = cursor2.moveToFirst();       // 参照先を一番始めに

        mServiceId1 = new String[cursor1.getCount()];
        mServiceId2 = new String[cursor2.getCount()];
        mServiceName = new String[cursor2.getCount()];

        if (cursor1.getCount()==0) {
            mUseService = "";
        } else {
            //情報IDから使用中のサービスIDを取得
            int i = 0;
            while (cPlace1) {
                mServiceId1[i] = cursor1.getString(0);
                cPlace1 = cursor1.moveToNext();
                i++;
            }

            //全サービスIDとサービス名を取得
            int j = 0;
            while (cPlace2) {
                mServiceId2[j] = cursor2.getString(0);
                mServiceName[j] = cursor2.getString(1);
                cPlace2 = cursor2.moveToNext();
                j++;
            }

            mUseService = "";
            for (i = 0; i < mServiceId1.length; i++) {
                for (j = 0; j < mServiceId2.length; j++) {
                    if (mServiceId2[j].equals(mServiceId1[i])) {
                        mUseService = mUseService + "･ " + mServiceName[j] + "\n";
                    }
                }
            }
        }
        cursor1.close();     //cursorを閉じる
        cursor2.close();     //cursorを閉じる

        /**
         * 各TextViewに内容を代入
         */
        //ツールバーにユーザー情報名を表示
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.tblIndexToolbarLayout);
        toolbarLayout.setTitle(mUserInfoName);

        //使用中のサービス
        TextView serviceName = (TextView) findViewById(R.id.txvIndexServiceName);
        serviceName.setText(mUseService);

        //ユーザー情報
        TextView userInfoName = (TextView) findViewById(R.id.txvIndexUserInfo);
        userInfoName.setText(mUserInfo);

        /**
         * 削除ボタン
         */
        Button btnElimination1 = (Button) findViewById(R.id.btnIndexElimination1);
        Button btnElimination2 = (Button) findViewById(R.id.btnIndexElimination2);

        //削除ボタンの背景色決定
        if (mUseService.equals("")) {
            btnElimination1.setVisibility(View.GONE);
            btnElimination2.setVisibility(View.VISIBLE);
        } else if (!mUseService.equals("")) {
            btnElimination1.setVisibility(View.VISIBLE);
            btnElimination2.setVisibility(View.GONE);
        }
        if (mUserInfoId.equals("1")||mUserInfoId.equals("2")||mUserInfoId.equals("3")) {
            btnElimination1.setVisibility(View.VISIBLE);
            btnElimination2.setVisibility(View.GONE);
        }

        //削除ボタン押下時の動作
        btnElimination1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInfoId.equals("1")||mUserInfoId.equals("2")||mUserInfoId.equals("3")) {
                    Toast.makeText(UserInfoIndex.this, "この情報は基本情報であるため削除できません｡", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UserInfoIndex.this, "この情報を使用中のサービスがあるため削除できません｡", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnElimination2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickTimerEvent.isClickEvent()) return;
                openPG_Dialog();
            }
        });
    }

    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {

        //DialogFragmentに渡すモノを決めてね
        String title = "削除確認";
        String message = "情報を削除します。\nよろしいですか?";
        String posi = "戻る";
        String nega = "削除";
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
        //「戻る」ボタンを押下した時
        dialog.dismiss();
    }

    /**
     * Negativeボタンが押された時の動作
     * @param dialog
     */
    @Override
    public void onNegativeButtonClick(android.support.v4.app.DialogFragment dialog) {
        //「削除」ボタンを押下した時
        if (!ClickTimerEvent.isClickEvent()) return;
        dbC.deleteUserInfoDeleteFlag(Integer.parseInt(mUserInfoId));
        this.finish();
        dialog.dismiss();
    }
}
