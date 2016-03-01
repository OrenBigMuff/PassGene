package com.bizan.mobile10.passgene;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;

import java.util.Date;
import java.util.logging.Filter;


public class PassList2 extends AppCompatActivity implements SearchView.OnQueryTextListener {

    static String nvTITLES[] = {"ユーザー情報設定", "アプリ設定", "バックアップ"};    //NV内のメニュー
    static int nvICONS[] = {android.R.drawable.ic_input_add, android.R.drawable.ic_input_add, android.R.drawable.ic_input_add};                                             //NV内のメニューアイコン

    static String nvSETTING = "設定画面";                                    //ヘッダービュー内の”設定”の文字
    static String useNAME = "ユーザーネーム";

    String[] mServiceId;      //0
    String[] mServiceInfoName;      //1
    String[] mServicePassHint;      //12
    String[] mServiceDeleteFlag;      //18

    private static String setUserInfoId;
    private static String setUserInfoName;
    private static String setUserInfo;//ユーザーネーム

//    int HEADERICOCVIEW = R.drawable.freeheaderview;                                             //ヘッダーのビュー

    private Toolbar plToolbar;       //toolbar
    RecyclerView plRecycleView;      //リサイクルビュー
    RecyclerView.Adapter plAdapter;   //リサイクルビューアダプター
    RecyclerView.LayoutManager plLayoutManager;      //リサイクルビューレイアウトマネージャー
    DrawerLayout plDrawer;           //ドロワー

    ActionBarDrawerToggle plDrawerToggle;            //NVを開くためのトグル
    FloatingActionButton plFab;                      //FAB

    View containerView;         //カードビューのサービスタイトル

    Animation inAnimation;       //インアニメーション
    Animation outAnimetion;     //アウトアニメーション
    Animation swapAnimation;

    SearchView mSearchView;

    public static final int NOTIFICATION_BROADCAST_REQUEST_CODE = 100;

    private DatabaseC dbC;

    private int SERVICE_INFO = 0;
    private int USER_INFO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);

        //アクションバーの代わりにツールバーを設定
        plToolbar = (Toolbar) findViewById(R.id.PassList_toolbar);
        setSupportActionBar(plToolbar);

        dbC = new DatabaseC(InitialSet1.getDbHelper());
        final Cursor cursor = dbC.readPasswordListInfo();

        /**
         * データベースからデータ読み出し
         */
        int j = 0;
        while (cursor.moveToNext()) {
            mServiceId[j] = cursor.getString(0);
            mServiceInfoName[j] = cursor.getString(1);
            mServicePassHint[j] = cursor.getString(12);
            mServiceDeleteFlag[j] = cursor.getString(18);
            j++;
        }

        //ツールバーにNVトグルを追加
        plDrawer = (DrawerLayout) findViewById(R.id.PassList_DrawerLayout);
        plDrawerToggle = new ActionBarDrawerToggle(this, plDrawer, plToolbar, R.string.openDrawer, R.string.closeDrawer) {

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
        //ここまでNVトグル設定


        //FAB設定
        plFab = (FloatingActionButton) findViewById(R.id.passList_fab);
        plFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //RegistNewPassに遷移
                Intent intent = new Intent(PassList2.this, RegistNewPass.class);
//                intent.putExtra("CLASSNAME","com.bizan.mobile10.passgene.RegistNewPass");
                startActivity(intent);
            }
        });

        /**
         *  ここからリサイクルビュー関連
         */

        //リサイクルビューキャスト
        plRecycleView = (RecyclerView) findViewById(R.id.PassList_RecycleView);
        plRecycleView.setHasFixedSize(true);



        //リサイクルビューにレイアウトマネージャをセット
        plLayoutManager = new LinearLayoutManager(this);
//        mSearchView.setOrientation(LinearLayoutCompat.VERTICAL);
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
                        //PW確認画面を通してユーザー情報一覧のページに飛ばす
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(PassList2.this, UserConf2.class);
                                intent.putExtra("CLASSNAME", "com.bizan.mobile10.passgene.UserInfoList");
                                startActivity(intent);
                            }
                        }, 250);
                        plDrawer.closeDrawers();
                        break;

                    case 2:
                        //PW確認画面を通してアプリ設定画面に飛ばす
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent intent = new Intent(PassList.this, UserConf2.class);
//                                intent.putExtra("CLASSNAME","com.bizan.mobile10.passgene.AppSetting");
//                                intent.putExtra("SID","0");
//                                startActivity(intent);
//                            }
//                        }, 250);
//                        plDrawer.closeDrawers();

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

//        ーーここまでリサイクルビュー関連ーー

        //カードビューのアニメーションキャスト

        //アニメーション
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.card_in_anim);
        outAnimetion = AnimationUtils.loadAnimation(this, R.anim.card_out_anim);

        //カードビュー設定
        LinearLayout cardLinear = (LinearLayout) this.findViewById(R.id.cardLinear);
        cardLinear.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

//        while (cursor.moveToNext()) {
//            int j = 0;
//                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.passlist_card, null);
//                CardView cardView = (CardView) linearLayout.findViewById(R.id.plcardView);
//
//                //サービス名
//                final TextView serviceTxv = (TextView) linearLayout.findViewById(R.id.plcardTitle);
//                serviceTxv.setText(cursor.getString(1));
//
//                //確認ボタン
//                final Button cardButton = (Button) cardView.findViewById(R.id.plcardbutton);
//
//                //ヒント
//                final TextView hintTxv = (TextView) cardView.findViewById(R.id.plcardHint);
//                hintTxv.setText(cursor.getString(2));
//
//                cardLinear.addView(linearLayout, j);
//
//                containerView.setTag(j);
//                containerView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(PassList.this, String.valueOf(v.getTag()) + "番目のCardViewがクリックされました", Toast.LENGTH_SHORT).show();
//
//                        //ヒントと編集ボタンをアニメーションさせる
//                        if (cardButton.getVisibility() == View.GONE) {
//                            cardButton.startAnimation(inAnimation);
//                            hintTxv.startAnimation(inAnimation);
//                            cardButton.setVisibility(View.VISIBLE);
//                            hintTxv.setVisibility(View.VISIBLE);
//                        } else if (cardButton.getVisibility() == View.VISIBLE) {
//                            cardButton.startAnimation(outAnimetion);
//                            hintTxv.startAnimation(outAnimetion);
//                            cardButton.setVisibility(View.GONE);
//                            hintTxv.setVisibility(View.GONE);
//                        }
//                    }
//                });
//
//            //PW確認画面を通して確認画面に遷移させる
//            cardButton.setTag(cursor.getString(0));
//            cardButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //遷移するときデータを渡す
//                    Intent intent = new Intent(PassList.this, UserConf2.class);
//                    intent.putExtra("CLASSNAME", "com.bizan.mobile10.passgene.PwConf");
//                    intent.putExtra("SID", v.getTag().toString());
//                }
//            });
//            j++;
//        }


        if (mServiceId.length > 0) {
            for (int i = 0; mServiceId.length > i; i++) {
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.passlist_card, null);
                final CardView cardView = (CardView) linearLayout.findViewById(R.id.plcardView);

                TextView textBox = (TextView) linearLayout.findViewById(R.id.plcardTitle);
                textBox.setText(mServiceInfoName[i]);
                cardLinear.addView(linearLayout, i);

                final LinearLayout mLineaLayout = (LinearLayout) cardView.findViewById(R.id.plcontainer);
                final Button cardButton = (Button) cardView.findViewById(R.id.plcardbutton);

                final TextView cardHint = (TextView) cardView.findViewById(R.id.plcardHint);
                cardHint.setText(mServicePassHint[i]);

                mLineaLayout.setTag(i);
                final int finalI = i;
                mLineaLayout.setOnClickListener(new View.OnClickListener() {
                    //cardViewクリック時のアクション
                    @Override
                    public void onClick(View v) {
                        toast(mServiceInfoName[finalI] + "のカードが選択されました。");

                        //ヒントと編集ボタンの表示・非表示をアニメーションさせる
                        if (cardButton.getVisibility() == View.GONE) {
                            cardButton.startAnimation(inAnimation);
                            cardHint.startAnimation(inAnimation);
                            cardButton.setVisibility(View.VISIBLE);
                            cardHint.setVisibility(View.VISIBLE);
                        } else if (cardButton.getVisibility() == View.VISIBLE) {
                            cardButton.startAnimation(outAnimetion);
                            cardHint.startAnimation(outAnimetion);
                            cardButton.setVisibility(View.GONE);
                            cardHint.setVisibility(View.GONE);
                        }
                    }
                });
                cardButton.setTag(i);
                cardButton.setOnClickListener(new View.OnClickListener() {
                    //確認ボタン押下時のアクション
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PassList2.this, "(∩´∀｀)∩ﾜｰｲ" + String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        //アダプターセット
        plAdapter = new RecyclerViewAdapter(nvTITLES, nvICONS, nvSETTING, useNAME);
        plRecycleView.setAdapter(plAdapter);

//        //リニアレイアウトの枠色を変更
//        LinearLayout strokeLinear = (LinearLayout) findViewById(R.id.strokeLinear);
//
//        //青枠追加
//        Drawable strokeBLUE = getResources().getDrawable(R.drawable.flame_style_pg);
//        containerView.setBackgroundDrawable(strokeBLUE);

        //Notification関連
    }

    //検索機能
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
        return false;
    }

    //キーボート展開時にテキスト打ち込み０でバックボタンを押すと検索アイコン表示まで戻る
    @Override
    public void onBackPressed() {
        if (mSearchView != null && !mSearchView.isIconified()) {
            mSearchView.clearFocus();
            mSearchView.onActionViewCollapsed();
            mSearchView.setQuery("", false);
            mSearchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * あったら便利！トーストメソッドだよ
     *
     * @param text
     */
    private void toast(String text) {
        if (text == null) {
            text = "";
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

//    /**
//     * ID、サービス名、ヒント、リジェネデータタイム、アップデートタイムを取得して各種配列に入れる
//     *
//     * @return
//     */
//    public Cursor readPassListInfo(){
//        Cursor cursor = null;
//        int i = 0;
//        while (cursor.moveToNext()){
//            id[i] = Integer.parseInt(cursor.getString(0));
//            service[i] = cursor.getString(1);
//            pass_hint[i] = cursor.getString(2);
//            generated_datetime[i] = Integer.parseInt(cursor.getString(3));
//            updated_datetime[i] = Integer.parseInt(cursor.getString(4));
//            i++;
//
//        }
//        cursor.close();
//        return cursor;
//    }


}
