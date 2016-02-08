package com.bizan.mobile10.passgene;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    String nvTITLES[]={"ユーザー情報設定","アプリ設定","バックアップ"};    //NV内のメニュー
//    int nvICONS = {};                                             //NV内のメニューアイコン

    String nvSETTING = "設定画面";                                    //ヘッダービュー内の”設定”の文字
    String useNAME = "";                                             //ユーザーネーム

//    int HEADERVIEW = ;                                             //ヘッダーのビュー

    private Toolbar maToolbar;       //toolbar
    RecyclerView maRecycleView;      //リサイクルビュー
    RecyclerView.Adapter maAdapter;   //リサイクルビューアダプター
    RecyclerView.LayoutManager maLayoutManager;      //リサイクルビューレイアウトマネージャー
    DrawerLayout maDrawer;           //ドロワー

    ActionBarDrawerToggle maDrawerToggle;            //NVを開くためのトグル

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //アクションバーの代わりにツールバーを設定
        maToolbar = (Toolbar) findViewById(R.id.MainActivity_toolbar);
        setSupportActionBar(maToolbar);

        //ツールバーにNVトグルを追加
        maDrawer = (DrawerLayout) findViewById(R.id.MainActivity_DrawerLayout);
        maDrawerToggle = new ActionBarDrawerToggle(this,maDrawer,maToolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
        public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }
            @Override
        public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
            }
        };
        maDrawer.setDrawerListener(maDrawerToggle);
        maDrawerToggle.syncState();
        //ここまでNVトグル設定


    }
}
