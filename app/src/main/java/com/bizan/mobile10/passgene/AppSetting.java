package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

/**
 * Created by user on 2016/02/15.
 */
public class AppSetting extends AppCompatActivity implements View.OnClickListener{

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

    Button btn1;                     //password編集画面に遷移するボタン
    Button btn2;                     //アプリ初期化に遷移するボタン

    Spinner asSpiner;                //スピナー
    Switch asSwict;                  //スウィッチ

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appsetting);

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
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent intent = new Intent(AppSetting.this, Sample.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }, 260);
//                        asDrawer.closeDrawers();
                        break;

                    case 2:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(AppSetting.this, AppSetting.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 250);
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

        //ボタンキャスト
        btn1 = (Button) findViewById(R.id.asPSbtn);
        btn1.setOnClickListener(this);

        btn2 = (Button) findViewById(R.id.AppSetting_btn);
        btn2.setOnClickListener(this);

        //Adapter作成
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Adapterにアイテム追加
        adapter.add("2ヵ月に1回");
        adapter.add("3ヵ月に1回");
        adapter.add("半年に1回");

        asSpiner = (Spinner) findViewById(R.id.asBNspn);
        asSpiner.setAdapter(adapter);

        //Spinerのアイテム取得
        asSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();

                //選択されたアイテムの処理
                if (item.equals("2ヵ月に1回")){

                }else if (item.equals("3ヵ月に1回")){

                }else if (item.equals("半年に1回")){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //swichの実装
        asSwict = (Switch) findViewById(R.id.asBNswt);
        asSwict.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true){
                    //ONにした場合
                    asSpiner.setEnabled(true);

                }else if (isChecked == false){
                    //OFFにした場合
                    asSpiner.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.asPSbtn:
                Intent intent1 = new Intent(AppSetting.this, InitialSet2.class);
                startActivity(intent1);

                break;

            case R.id.AppSetting_btn:
                Intent intent2 = new Intent(AppSetting.this, AppInit.class);
                startActivity(intent2);

                break;
        }
    }
}
