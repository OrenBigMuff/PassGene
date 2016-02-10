package com.bizan.mobile10.passgene;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PassList extends AppCompatActivity {

    String nvTITLES[]={"ユーザー情報設定","アプリ設定","バックアップ"};    //NV内のメニュー
    int nvICONS[] = {android.R.drawable.ic_input_add,android.R.drawable.ic_input_add,android.R.drawable.ic_input_add};                                             //NV内のメニューアイコン

    String nvSETTING = "設定画面";                                    //ヘッダービュー内の”設定”の文字
    String useNAME = "ユーザーネーム";                                             //ユーザーネーム

//    int HEADERICOCVIEW = R.drawable.freeheaderview;                                             //ヘッダーのビュー

    private Toolbar plToolbar;       //toolbar
    RecyclerView plRecycleView;      //リサイクルビュー
    RecyclerView.Adapter plAdapter;   //リサイクルビューアダプター
    RecyclerView.LayoutManager plLayoutManager;      //リサイクルビューレイアウトマネージャー
    DrawerLayout plDrawer;           //ドロワー

    ActionBarDrawerToggle plDrawerToggle;            //NVを開くためのトグル
    FloatingActionButton plFab;                      //ふぁぶ

    LinearLayout plCardLinear ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_list);

        //アクションバーの代わりにツールバーを設定
        plToolbar = (Toolbar) findViewById(R.id.PassList_toolbar);
        setSupportActionBar(plToolbar);

        //ツールバーにNVトグルを追加
        plDrawer = (DrawerLayout) findViewById(R.id.PassList_DrawerLayout);
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

        //FAB設定
        plFab = (FloatingActionButton) findViewById(R.id.PassList_fab);
        plFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "(∩´∀｀)∩ﾜｰｲ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //リサイクルビューキャスト
        plRecycleView = (RecyclerView) findViewById(R.id.PassList_RecycleView);
        plRecycleView.setHasFixedSize(true);

        //アダプターセット
        plAdapter = new RecyclerViewAdapter(nvTITLES,nvICONS,nvSETTING,useNAME);
        plRecycleView.setAdapter(plAdapter);

        //リサイクルビューにレイアウトマネージャをセット
        plLayoutManager = new LinearLayoutManager(this);
        plRecycleView.setLayoutManager(plLayoutManager);

        final GestureDetector mGestureDetector = new GestureDetector(PassList.this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e){
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
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Intent intent = new Intent(PassList.this, Sample.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }, 260);
//                        plDrawer.closeDrawers();
                        break;

                    case 2:
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

//        //カードビュー設定
//        LinearLayout cardLinear = (LinearLayout)this.findViewById(R.id.cardLinear);
//        cardLinear.removeAllViews();
//        for (int i = 0; i<10 ; i++){
//            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//            LinearLayout  linearLayout  = (LinearLayout) inflater.inflate(R.layout.samplecard,null);
//            CardView cardView = (CardView) linearLayout.findViewById(R.id.plcardView);
//            TextView textBox = (TextView) linearLayout.findViewById(R.id.plcardText);
//            textBox.setText("CardView" + i);
//            cardView.setTag(i);
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(PassList.this, String.valueOf(v.getTag()) + "番目のCardViewがクリックされました", Toast.LENGTH_SHORT).show();
//                }
//            });
//        cardLinear.addView(linearLayout,i);
//        }
//
    }

}
