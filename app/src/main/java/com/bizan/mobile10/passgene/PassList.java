package com.bizan.mobile10.passgene;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class PassList extends AppCompatActivity {

    String nvTITLES[]={"ユーザー情報設定","アプリ設定","バックアップ"};    //NV内のメニュー
//    int nvICONS = {};                                             //NV内のメニューアイコン

    String nvSETTING = "設定画面";                                    //ヘッダービュー内の”設定”の文字
    String useNAME = "ユーザーネーム";                                             //ユーザーネーム

//    int HEADERVIEW = ;                                             //ヘッダーのビュー

    private Toolbar plToolbar;       //toolbar
    RecyclerView plRecycleView;      //リサイクルビュー
    RecyclerView.Adapter plAdapter;   //リサイクルビューアダプター
    RecyclerView.LayoutManager plLayoutManager;      //リサイクルビューレイアウトマネージャー
    DrawerLayout plDrawer;           //ドロワー

    ActionBarDrawerToggle plDrawerToggle;            //NVを開くためのトグル

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);

        //アクションバーの代わりにツールバーを設定
        plToolbar = (Toolbar) findViewById(R.id.PassList_toolbar);
        setSupportActionBar(plToolbar);

        //ツールバーにNVトグルを追加
        plDrawer = (DrawerLayout) findViewById(R.id.MainActivity_DrawerLayout);
        plDrawerToggle = new ActionBarDrawerToggle(this,plDrawer,plToolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
        public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }
            @Override
        public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }
        };
        plDrawer.setDrawerListener(plDrawerToggle);
        plDrawerToggle.syncState();
        //ここまでNVトグル設定


    }
}
