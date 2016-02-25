package com.bizan.mobile10.passgene;

<<<<<<< HEAD
import android.content.Intent;
import android.database.Cursor;
=======
>>>>>>> origin/Hori1
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

<<<<<<< HEAD
public class UserInfoIndex extends AppCompatActivity implements PassGeneDialog.DialogListener {
    private DatabaseC dbC;
=======
public class UserInfoIndex extends AppCompatActivity {

    private static String mUseService = "･Twitter\n･LINE\n･Facebook";
>>>>>>> origin/Hori1
    UserInfoList userInfoList = new UserInfoList();
    private String mUserInfoId = userInfoList.getUserInfoId();
    private String mUserInfoName = userInfoList.getUserInfoName();
    private String mUserInfo = userInfoList.getUserInfo();
<<<<<<< HEAD
    private String mUseService;

    private String[] mServiceId1;
    private String[] mServiceId2;
    private String[] mServiceName;

=======
>>>>>>> origin/Hori1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

<<<<<<< HEAD
        /**
         * データベースからデータ読み出し
         */
        Cursor cursor1 = dbC.readGeneUseid(mUserInfoId);
        Cursor cursor2 = dbC.readServiceInfoAll()
                ;
        if (cursor1.getCount()==0) {
            mUseService = "";
        } else {
            //情報IDから使用中のサービスIDを取得
            int i = 0;
            while (cursor1.moveToNext()) {
                mServiceId1[i] = cursor1.getString(0);
                i++;
            }

            //全サービスIDとサービス名を取得
            int j = 0;
            while (cursor2.moveToNext()) {
                mServiceId2[j] = cursor1.getString(0);
                mServiceName[j] = cursor1.getString(1);
                j++;
            }

            for (i = 0; i < mServiceId1.length; i++) {
                for (j = 0; j < mServiceId2.length; j++) {
                    if (mServiceId2[j].equals(mServiceId1[i])) {
                        mUseService = mUseService + "･ " + mServiceName[j] + "\n";
                    }
                }
            }
        }

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
=======
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) this.findViewById(R.id.tblIndexToolbarLayout);
        toolbarLayout.setTitle(mUserInfoName);

        TextView serviceName = (TextView) this.findViewById(R.id.txvIndexServiceName);
        serviceName.setText(mUseService);

        TextView userInfoName = (TextView) this.findViewById(R.id.txvIndexUserInfo);
        userInfoName.setText(mUserInfo);

        MaterialRippleLayout indexRipple = (MaterialRippleLayout) findViewById(R.id.ripIndexRipple);
        indexRipple.setRippleColor(R.color.passGeneCardBackgroundGray);

        Button btnElimination = (Button) this.findViewById(R.id.btnIndexElimination);
>>>>>>> origin/Hori1

        //削除ボタンの背景色決定
        if (mUseService.equals("")) {
            btnElimination1.setVisibility(View.GONE);
            btnElimination2.setVisibility(View.VISIBLE);
        } else if (!mUseService.equals("")) {
            btnElimination1.setVisibility(View.VISIBLE);
            btnElimination2.setVisibility(View.GONE);
        }

<<<<<<< HEAD
        //削除ボタン押下時の動作
        btnElimination1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserInfoIndex.this, getString(R.string.dialog_message2), Toast.LENGTH_LONG).show();
            }
        });
        btnElimination2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPG_Dialog();
            }
        });
    }

    /**
     * DialogFragmentにゴニョゴニョするメソッド
     */
    private void openPG_Dialog() {

        //DialogFragmentに渡すモノを決めてね
        String title = getString(R.string.dialog_title1);
        String message = getString(R.string.dialog_message1);
        String posi = getString(R.string.button2);
        String nega = getString(R.string.button5);
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
        dbC.deleteUserInfoDeleteFlag(Integer.parseInt(mUserInfoId));
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
=======
        btnElimination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUseService.equals("")) {
                    Toast.makeText(UserInfoIndex.this, "削除するけどいいですか?", Toast.LENGTH_SHORT).show();
                } else if (!mUseService.equals("")) {
                    Toast.makeText(UserInfoIndex.this, "使用中のサービスがあるため削除できません｡", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
>>>>>>> origin/Hori1
}
