package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class PassList2 extends AppCompatActivity {

    private static DatabaseHelper dbH;
    private DatabaseC dbC;
    private PreferenceC pref;
    private final String DB_NAME = "pg.db"; //データベース名
    private final int DB_VERSION = 1;       //データベースのバージョン
    //テーブル名
    private static final String[] DB_TABLE = {"service_info", "user_info"};

    private Toolbar plToolbar;                          //toolbar
    private DrawerLayout plDrawer;                      //ドロワー

    //    private DatabaseC dbC;                              //DatabaseC
    private String[] mServiceId;                        //サービスID
    private String[] mServiceName;                      //サービス名
    private String[] mHint;                             //パスワードヒント
    private String[] mDeleteFlag;                       //削除フラグ


    private static Animation inAnimation;               //インアニメーション
    private static Animation outAnimation;              //アウトアニメーション

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);

        /**
         * DB準備
         */

        String[] dbColTable = {
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " service TEXT UNIQUE NOT NULL," +
                        " user_id TEXT NOT NULL," +
                        " mail_address TEXT NOT NULL," +
                        " char_num INTEGER NOT NULL," +
                        " char_uppercase INTEGER NOT NULL," +
                        " char_lowercase INTEGER NOT NULL," +
                        " char_symbol INTEGER NOT NULL," +
                        " num_of_char INTEGER NOT NULL," +
                        " generated_datetime TEXT NOT NULL," +
                        " updated_datetime TEXT NOT NULL," +
                        " fixed_pass TEXT NOT NULL," +
                        " pass_hint TEXT NOT NULL," +
                        " gene_id1 INTEGER NOT NULL," +
                        " gene_id2 INTEGER NOT NULL," +
                        " gene_id3 INTEGER NOT NULL," +
                        " gene_id4 INTEGER NOT NULL," +
                        " algorithm INTEGER NOT NULL," +
                        " delete_flag INTEGER NOT NULL)",

                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " info_name TEXT UNIQUE NOT NULL," +
                        " value TEXT NOT NULL," +
                        " category INTEGER NOT NULL," +
                        " delete_flag INTEGER NOT NULL," +
                        " useless_flag INTEGER NOT NULL)"
        };

        dbH = new DatabaseHelper(this, DB_NAME, DB_VERSION, DB_TABLE, dbColTable);
        dbC = new DatabaseC(this.dbH);

        pref = new PreferenceC(this);


        /**
         * 初回起動か、あるいはInitialSet1,2を経過しているかのチェック
         */
        if (!pref.readConfig("firstStart", false) || !pref.readConfig("InitialDone", false)) {
            //InitialSet1へ遷移
            Intent intent = new Intent(PassList2.this, InitialSet1.class);
            startActivity(intent);

            //初回起動したので、その旨コンフィグにWriteする
            pref.writeConfig("firstStart", true);
        }

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
                Intent intent = new Intent(PassList2.this, RegistNewPass.class);
                startActivity(intent);
            }
        });

        /**
         * spinner設定
         */
        String[] category = {"登録降順", "登録昇順", "五十音降順", "五十音昇順", "更新日降順", "更新日昇順"};

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this, R.layout.spinner_item2);
        adapterCategory.setDropDownViewResource(R.layout.spinner_item2);

        //アイテムを追加します
        for (int i = 0; i < category.length; i++) {
            adapterCategory.add(category[i]);
        }

        Spinner spnCategory = (Spinner) findViewById(R.id.spnPasslist);
        //アダプターを設定します
        spnCategory.setAdapter(adapterCategory);
        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //選択されたスピナーに対する動作
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();

                if (item.equals("登録降順")) {
                    Cursor cursor = dbC.readPasswordListInfo();
                    boolean cPlace = cursor.moveToFirst();       // 参照先を一番始めに

                    mServiceId = new String[cursor.getCount()];
                    mServiceName = new String[cursor.getCount()];
                    mHint = new String[cursor.getCount()];
                    int i = cursor.getCount() - 1;

                    while (cPlace) {
                        mServiceId[i] = cursor.getString(0);
                        mServiceName[i] = cursor.getString(1);
                        mHint[i] = cursor.getString(2);
                        cPlace = cursor.moveToNext();
                        i--;
                    }
                    cursor.close();     //cursorを閉じる

                    //CardView作成
                    cardView();

                } else if (item.equals("登録昇順")) {
                    Cursor cursor = dbC.readPasswordListInfo();
                    boolean cPlace = cursor.moveToFirst();       // 参照先を一番始めに

                    mServiceId = new String[cursor.getCount()];
                    mServiceName = new String[cursor.getCount()];
                    mHint = new String[cursor.getCount()];
                    int i = 0;

                    while (cPlace) {
                        mServiceId[i] = cursor.getString(0);
                        mServiceName[i] = cursor.getString(1);
                        mHint[i] = cursor.getString(2);
                        cPlace = cursor.moveToNext();
                        i++;
                    }
                    cursor.close();     //cursorを閉じる

                    //CardView作成
                    cardView();

                } else if (item.equals("五十音降順")) {
                    Cursor cursor = dbC.readPasswordListInfo("service");
                    boolean cPlace = cursor.moveToFirst();       // 参照先を一番始めに

                    mServiceId = new String[cursor.getCount()];
                    mServiceName = new String[cursor.getCount()];
                    mHint = new String[cursor.getCount()];
                    int i = cursor.getCount() - 1;

                    while (cPlace) {
                        mServiceId[i] = cursor.getString(0);
                        mServiceName[i] = cursor.getString(1);
                        mHint[i] = cursor.getString(2);
                        cPlace = cursor.moveToNext();
                        i--;
                    }
                    cursor.close();     //cursorを閉じる

                    //CardView作成
                    cardView();

                } else if (item.equals("五十音昇順")) {
                    Cursor cursor = dbC.readPasswordListInfo("service");
                    boolean cPlace = cursor.moveToFirst();       // 参照先を一番始めに

                    mServiceId = new String[cursor.getCount()];
                    mServiceName = new String[cursor.getCount()];
                    mHint = new String[cursor.getCount()];
                    int i = 0;

                    while (cPlace) {
                        mServiceId[i] = cursor.getString(0);
                        mServiceName[i] = cursor.getString(1);
                        mHint[i] = cursor.getString(2);
                        cPlace = cursor.moveToNext();
                        i++;
                    }
                    cursor.close();     //cursorを閉じる

                    //CardView作成
                    cardView();

                } else if (item.equals("更新日降順")) {
                    Cursor cursor = dbC.readPasswordListInfo("update");
                    boolean cPlace = cursor.moveToFirst();       // 参照先を一番始めに

                    mServiceId = new String[cursor.getCount()];
                    mServiceName = new String[cursor.getCount()];
                    mHint = new String[cursor.getCount()];
                    int i = cursor.getCount() - 1;

                    while (cPlace) {
                        mServiceId[i] = cursor.getString(0);
                        mServiceName[i] = cursor.getString(1);
                        mHint[i] = cursor.getString(2);
                        cPlace = cursor.moveToNext();
                        i--;
                    }
                    cursor.close();     //cursorを閉じる

                    //CardView作成
                    cardView();

                } else if (item.equals("更新日昇順")) {
                    Cursor cursor = dbC.readPasswordListInfo("update");
                    boolean cPlace = cursor.moveToFirst();       // 参照先を一番始めに

                    mServiceId = new String[cursor.getCount()];
                    mServiceName = new String[cursor.getCount()];
                    mHint = new String[cursor.getCount()];
                    int i = 0;

                    while (cPlace) {
                        mServiceId[i] = cursor.getString(0);
                        mServiceName[i] = cursor.getString(1);
                        mHint[i] = cursor.getString(2);
                        cPlace = cursor.moveToNext();
                        i++;
                    }
                    cursor.close();     //cursorを閉じる

                    //CardView作成
                    cardView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /**
         * Recycleview
         */
        String nvTITLES[] = {"ユーザー情報設定", "マスターパスワード変更", "アプリ初期化"};      //NV内のメニュー
        int nvICONS[] = {R.drawable.nd_icon_1, R.drawable.nd_icon_2, R.drawable.nd_icon_3};

        //リサイクルビューキャスト
        RecyclerView plRecycleView = (RecyclerView) findViewById(R.id.PassList_RecycleView);
        plRecycleView.setHasFixedSize(true);

        //アダプターセット
        RecyclerViewAdapter plAdapter = new RecyclerViewAdapter(nvTITLES, nvICONS, "設定画面", PassList2.getUserName() + " さん");
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

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    //childPositionから位置を取得してイベント取得
                    switch (recyclerView.getChildPosition(child)) {

                        case 1:
                            //PW確認画面と通してユーザー情報一覧のページに飛ばす
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(PassList2.this, UserInfoList.class);
//                                intent.putExtra("CLASSNAME","com.bizan.mobile10.passgene.UserInfoList");
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
                                    Intent intent = new Intent(PassList2.this, MPchange.class);
                                    intent.putExtra("CLASSNAME", "com.bizan.mobile10.passgene.MPchange");
//                                intent.putExtra("SID","0");
                                    startActivity(intent);
                                }
                            }, 250);
                            plDrawer.closeDrawers();
/*
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(PassList2.this, AppSetting.class);
                                startActivity(intent);
                            }
                        }, 250);
                        plDrawer.closeDrawers();*/
                            break;

                        case 3:
                            //GoogleDriveページに遷移
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(PassList2.this, AppInit.class);
                                    startActivity(intent);
                                }
                            }, 250);
                            plDrawer.closeDrawers();
                            break;
                    }
                    return true;
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
         * 登録降順でカード作成
         */
//        Cursor cursor = dbC.readPasswordListInfo();
//        InitialSet1 initialSet1 = new InitialSet1();
//        DatabaseHelper dbHelper = initialSet1.getDbHelper();
//        DatabaseC dbC = new DatabaseC(InitialSet1.getDbHelper());
        Cursor cursor = dbC.readPasswordListInfo();

        boolean cPlace = cursor.moveToFirst();       // 参照先を一番始めに

        mServiceId = new String[cursor.getCount()];
        mServiceName = new String[cursor.getCount()];
        mHint = new String[cursor.getCount()];
        int i = cursor.getCount() - 1;

        while (cPlace) {
            mServiceId[i] = cursor.getString(0);
            mServiceName[i] = cursor.getString(1);
            mHint[i] = cursor.getString(2);
            cPlace = cursor.moveToNext();
            i--;
        }
        cursor.close();     //cursorを閉じる

        //CardView作成
        cardView();
    }

    public static String getDB_TABLE_S() {
        return DB_TABLE[0];
    }

    public static String getDB_TABLE_U() {
        return DB_TABLE[1];
    }

    //DBヘルパーゲットだぜ なや～つ DBに関係する
    public static DatabaseHelper getDbHelper() {
        return dbH;
    }


    /**
     * Userの姓名をReturnするメソッド
     */
    public static String getUserName() {
        DatabaseC dbC = new DatabaseC(PassList2.getDbHelper());
        Cursor cursor = dbC.readUserInfoAll();
        if(cursor.getCount() == 0){
            return "Username";
        }
        cursor.moveToFirst();
        String[] list = new String[cursor.getCount()];

        for (int i = 0; i < list.length; i++) {
            cursor.moveToNext();
            list[i] = cursor.getString(2);
        }

        cursor.close();

        //ユーザーの姓
        String lastName = list[0];
        //ユーザーの名
        String firstName = list[1];
        //フルネーム
        String fullName = lastName + " " + firstName;

        return fullName;
    }


    /**
     * CardView作成メソッド
     */

    private void cardView() {
        //アニメーション
        inAnimation = AnimationUtils.loadAnimation(this, R.anim.card_in_anim);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.card_out_anim);

        //カードビュー設定
        LinearLayout cardLinear = (LinearLayout) findViewById(R.id.cardLinear);
        cardLinear.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < mServiceId.length; i++) {
            LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.passlist_card, null);
            CardView cardView = (CardView) linearLayout.findViewById(R.id.plcardView);

            //サービス名
            TextView textView1 = (TextView) linearLayout.findViewById(R.id.plcardTitle);
            textView1.setText(mServiceName[i]);

            //ヒント
            final TextView textView2 = (TextView) linearLayout.findViewById(R.id.plcardHint);
            textView2.setText(mHint[i]);

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
                    } else if (cardButton.getVisibility() == View.VISIBLE) {
                        cardButton.startAnimation(outAnimation);
                        textView2.startAnimation(outAnimation);
                        cardButton.setVisibility(View.GONE);
                        textView2.setVisibility(View.GONE);
                    }
                }
            });

            cardLinear.addView(linearLayout, i);

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
        }



        /*int j = 0;

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
                        } else if (cardButton.getVisibility() == View.VISIBLE) {
                            cardButton.startAnimation(outAnimation);
                            textView2.startAnimation(outAnimation);
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
        }*/


    }
}
