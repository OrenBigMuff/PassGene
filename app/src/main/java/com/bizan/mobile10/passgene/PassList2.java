package com.bizan.mobile10.passgene;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PassList2 extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Toolbar plToolbar;                          //toolbar
    private DrawerLayout plDrawer;                      //ドロワー

    private SearchView mSearchView;                     //サーチビュー

    private String[] mServiceId;                        //サービスID
    private String[] mServiceName;                      //サービス名
    private String[] mHint;                             //パスワードヒント
    private String[] mDeleteFlag;                       //削除フラグ

    private static Animation inAnimation;               //インアニメーション
    private static Animation outAnimetion;              //アウトアニメーション

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);

        /**
         * ツールバーの設定
         */
        //アクションバーの代わりにツールバーを設定
        plToolbar = (Toolbar) findViewById(R.id.PassList_toolbar);
        setSupportActionBar(plToolbar);

        //ツールバーにNVトグルを追加
        plDrawer = (DrawerLayout) findViewById(R.id.PassList_DrawerLayout);
        ActionBarDrawerToggle plDrawerToggle = new ActionBarDrawerToggle(this, plDrawer, plToolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        plDrawer.setDrawerListener(plDrawerToggle);
        plDrawerToggle.syncState();

        //FAB設定
        FloatingActionButton plFab = (FloatingActionButton) findViewById(R.id.passList_fab);
        plFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //RegistNewPassに遷移
                Intent intent = new Intent(PassList2.this, UserConf2.class);
                intent.putExtra("CLASSNAME","com.bizan.mobile10.passgene.RegistNewPass");
                startActivity(intent);
            }
        });


        /**
         * Recycleview
         */
        String nvTITLES[] = {"ユーザー情報設定","アプリ設定","バックアップ"};      //NV内のメニュー
        int nvICONS[] = {android.R.drawable.ic_input_add,android.R.drawable.ic_input_add,android.R.drawable.ic_input_add};

        //リサイクルビューキャスト
        RecyclerView plRecycleView = (RecyclerView) findViewById(R.id.PassList_RecycleView);
        plRecycleView.setHasFixedSize(true);

        //アダプターセット
        RecyclerViewAdapter plAdapter = new RecyclerViewAdapter(nvTITLES, nvICONS, "設定画面", "ユーザーネーム");
        plRecycleView.setAdapter(plAdapter);

        //リサイクルビューにレイアウトマネージャをセット
        LinearLayoutManager plLayoutManager = new LinearLayoutManager(this);
        plRecycleView.setLayoutManager(plLayoutManager);

        final GestureDetector mGestureDetector = new GestureDetector(PassList2.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        //リサイクルビューにタッチイベント登録
        plRecycleView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                //childPositionから位置を取得してイベント取得
                switch (recyclerView.getChildPosition(child)) {

                    case 1:
                        //PW確認画面と通してユーザー情報一覧のページに飛ばす
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(PassList2.this, UserConf2.class);
                                intent.putExtra("CLASSNAME","com.bizan.mobile10.passgene.UserInfoList");
                                startActivity(intent);
                            }
                        }, 250);
                        plDrawer.closeDrawers();
                        break;

                    case 2:
                        //PW確認画面を通してアプリ設定画面に飛ばす
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(PassList2.this, UserConf2.class);
                                intent.putExtra("CLASSNAME","com.bizan.mobile10.passgene.AppSetting");
                                intent.putExtra("SID","0");
                                startActivity(intent);
                            }
                        }, 250);
                        plDrawer.closeDrawers();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(PassList2.this, AppSetting.class);
                                startActivity(intent);
                            }
                        }, 250);
                        plDrawer.closeDrawers();
                        break;

                    case 3:
                        //GoogleDriveページに遷移
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(PassList2.this, SaveLoadDB.class);
                                startActivity(intent);
                            }
                        }, 250);
                        plDrawer.closeDrawers();
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

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * データベースからデータ読み出し
         */
        InitialSet1 initialSet1 = new InitialSet1();
        DatabaseHelper dbHelper = initialSet1.getDbHelper();
        DatabaseC dbC = new DatabaseC(dbHelper);

        Cursor cursor = dbC.readServiceInfoAll();
        boolean cPlace = cursor.moveToFirst();       // 参照先を一番始めに

        mServiceId = new String[cursor.getCount()];
        mServiceName = new String[cursor.getCount()];
        mHint = new String[cursor.getCount()];
        mDeleteFlag = new String[cursor.getCount()];
        int i = 0;

        while (cPlace) {
            mServiceId[i] = cursor.getString(0);
            mServiceName[i] = cursor.getString(1);
            mHint[i] = cursor.getString(12);
            mDeleteFlag[i] = cursor.getString(18);
            cPlace = cursor.moveToNext();
            i++;
        }
        cursor.close();     //cursorを閉じる

        //CardView作成
        cardView();
    }

    /**
     * CardView作成メソッド
     */
    private void cardView() {
        //アニメーション
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.card_in_anim);
        outAnimetion = AnimationUtils.loadAnimation(this, R.anim.card_out_anim);

        //カードビュー設定
        LinearLayout cardLinear = (LinearLayout) findViewById(R.id.cardLinear);
        cardLinear.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        int j = 0;

        for (int i = mServiceId.length - 1; i > -1; i--) {
            if (mDeleteFlag[i].equals("0")) {
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.passlist_card, null);
                CardView cardView = (CardView) linearLayout.findViewById(R.id.plcardView);

                //サービス名
                TextView textView1 = (TextView) linearLayout.findViewById(R.id.plcardTitle);
                textView1.setText(mServiceName[i]);

                //ヒント
                final TextView textView2 = (TextView) linearLayout.findViewById(R.id.plcardHint);
                textView2.setText(mHint[i]);

                cardLinear.addView(linearLayout, j);

                final Button cardButton = (Button) cardView.findViewById(R.id.plcardbutton);

                //Card押下時の動作
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ヒントと編集ボタンをアニメーションさせる
                        if (cardButton.getVisibility() == View.GONE) {
                            cardButton.startAnimation(inAnimation);
                            textView2.startAnimation(inAnimation);
                            cardButton.setVisibility(View.VISIBLE);
                            textView2.setVisibility(View.VISIBLE);
                        } else
                        if (cardButton.getVisibility() == View.VISIBLE) {
                            cardButton.startAnimation(outAnimetion);
                            textView2.startAnimation(outAnimetion);
                            cardButton.setVisibility(View.GONE);
                            textView2.setVisibility(View.GONE);
                        }
                    }
                });

                //ボタン押下時の動作
                cardButton.setTag(i);
                cardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PassList2.this, UserConf2.class);
                        intent.putExtra("CLASSNAME", "com.bizan.mobile10.passgene.PwConf");
                        intent.putExtra("SID", Integer.parseInt(mServiceId[Integer.parseInt(String.valueOf(v.getTag()))]));
                        startActivity(intent);
                    }
                });

                j++;
            }
        }
    }

    /**
     * 検索機能
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plsearch, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.plsearchView);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    searchItem.collapseActionView();
                    mSearchView.setQuery("", false);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        for (int i = 0; i < mServiceId.length; i++) {
            if (mServiceName[i].indexOf(newText)==-1) {
                mDeleteFlag[i] = "1";
            }
        }

        //CardView作成
        cardView();
        return false;
    }

    /**
     * キーボート展開時にテキスト打ち込み０でバックボタンを押すと検索アイコン表示まで戻る
     */
    @Override
    public void onBackPressed() {
        if (mSearchView !=null && !mSearchView.isIconified()) {
            mSearchView.clearFocus();
            mSearchView.onActionViewCollapsed();
            mSearchView.setQuery("", false);
            mSearchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }
}
