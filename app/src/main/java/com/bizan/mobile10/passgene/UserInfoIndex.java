package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

public class UserInfoIndex extends AppCompatActivity implements PassGeneDialog.DialogListener {

    private static String mUseService = "";     //"･Twitter\n･LINE\n･Facebook"
    private static String mUserInfo = "携帯電話番号";
    UserInfoList userInfoList = new UserInfoList();
    private String mUserInfoId = userInfoList.getUserInfoId();
    private String mUserInfoName = userInfoList.getUserInfoName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
