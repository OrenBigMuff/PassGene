package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by user on 2016/02/15.
 */
public class AppSetting extends AppCompatActivity {

    String nvTITLES[]={"ユーザー情報設定","アプリ設定","バックアップ"};    //NV内のメニュー
    int nvICONS[] = {android.R.drawable.ic_input_add,android.R.drawable.ic_input_add,android.R.drawable.ic_input_add};                                             //NV内のメニューアイコン

    String nvSETTING = "設定画面";                                    //ヘッダービュー内の”設定”の文字
    String useNAME = "ユーザーネーム";                                             //ユーザーネーム


    private Toolbar asToolbar;       //toolbar
    RecyclerView asRecycleView;      //リサイクルビュー
    RecyclerView.Adapter asAdapter;   //リサイクルビューアダプター
    RecyclerView.LayoutManager asLayoutManager;      //リサイクルビューレイアウトマネージャー
    DrawerLayout asDrawer;           //ドロワー
    CollapsingToolbarLayout asCollapsingToolbarLayout;  //ツールバーレイアウト

    ActionBarDrawerToggle asDrawerToggle;            //NVを開くためのトグル

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appsetting);

        //タイトル変更
        asCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.AppSetting_toolbar_layout);
        asCollapsingToolbarLayout.setTitle("App Setting");

        //アクションバーの代わりにツールバーを設定
        asToolbar = (Toolbar) findViewById(R.id.AppSetting_toolbar);
        setSupportActionBar(asToolbar);

        //ツールバーにNVトグルを追加
        asDrawer = (DrawerLayout) findViewById(R.id.AppSetting_DrawerLayout);
        asDrawerToggle = new ActionBarDrawerToggle(this, asDrawer, asToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        asDrawer.setDrawerListener(asDrawerToggle);
        asDrawerToggle.syncState();
        //ここまでNVトグル設定

        //リサイクルビューキャスト
        asRecycleView = (RecyclerView) findViewById(R.id.AppSetting_RecycleView);
        asRecycleView.setHasFixedSize(true);

        //アダプターセット
        asAdapter = new RecyclerViewAdapter(nvTITLES, nvICONS, nvSETTING, useNAME);
        asRecycleView.setAdapter(asAdapter);

        //リサイクルビューにレイアウトマネージャをセット
        asLayoutManager = new LinearLayoutManager(this);
        asRecycleView.setLayoutManager(asLayoutManager);

        final GestureDetector mGestureDetector = new GestureDetector(AppSetting.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        //リサイクルビューにタッチイベント登録
        asRecycleView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                //childPositionから位置を取得してイベント取得
                switch (recyclerView.getChildPosition(child)) {

                    case 1:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(AppSetting.this, Sample.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 260);
                        asDrawer.closeDrawers();
                        break;

                    case 2:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(AppSetting.this, AppSetting.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 260);
                        asDrawer.closeDrawers();
                        break;

                    case 3:
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }

        });

    }
}
