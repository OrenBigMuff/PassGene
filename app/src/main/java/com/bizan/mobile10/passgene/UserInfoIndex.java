package com.bizan.mobile10.passgene;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

public class UserInfoIndex extends AppCompatActivity {

    private static String mUseService = "･Twitter\n･LINE\n･Facebook";
    private static String mUserInfo = "携帯電話番号";
    UserInfoList userInfoList = new UserInfoList();
    private int mUserInfoId = userInfoList.getUserInfoId();
    private String mUserInfoName = userInfoList.getUserInfoName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_index);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) this.findViewById(R.id.tblIndexToolbarLayout);
        toolbarLayout.setTitle(mUserInfoName);

        TextView serviceName = (TextView) this.findViewById(R.id.txvIndexServiceName);
        serviceName.setText(mUseService);

        TextView userInfoName = (TextView) this.findViewById(R.id.txvIndexUserInfo);
        userInfoName.setText(mUserInfo);

        Button btnElimination = (Button) this.findViewById(R.id.btnIndexElimination);

        //削除ボタンの背景色決定
        if (mUseService.equals("")) {
            btnElimination.setBackgroundResource(R.drawable.pass_red_button);
        }

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
}
