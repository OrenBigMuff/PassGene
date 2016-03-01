package com.bizan.mobile10.passgene;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SaveLoadDB extends AppCompatActivity
        implements View.OnClickListener {

    DatabaseC dbC;
    private Button btnSdSave;
    private Button btnSdLoad;
    File sd;
    File data;
    String sd_stt;

    Boolean bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_load_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbC = new DatabaseC(InitialSet1.getDbHelper());

        btnSdSave = (Button) findViewById(R.id.btnSdSave);
        btnSdSave.setOnClickListener(this);
        btnSdLoad = (Button) findViewById(R.id.btnSdLoad);
        btnSdLoad.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSdSave) {

            //ここにSDへのセーブコードを記述
            toast("セーブじゃ！セーブするのじゃ～");
            exportDB();

        } else if (v.getId() == R.id.btnSdLoad) {

            //ここにGoogleDriveからのロードコードを記述
            toast("ロードじゃ！ロードせんか～い\nせんのか～い！");
//            loadDataFromDrive();
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

    /**
     *
     */
    private void exportDB() {
        sd = Environment.getExternalStorageDirectory();
        data = Environment.getDataDirectory();
        sd_stt = Environment.getExternalStorageState();

        FileChannel source = null;
        FileChannel destination = null;

        String currentDBPath = "/data/" + "com.bizan.mobile10" + "/databases/" + "pg.db";
        String sdPath = sd.getPath();
        String backupDBPath = sd.getPath() + "/passgene";

        bool = sd_stt.equals(Environment.MEDIA_MOUNTED);
        if(bool == false) {  //書込み状態でマウントされていない。
            toast("SDメモリが書込み状態でマウントされていません。");
            return;         //ディレクトリ作成失敗
        }

        File f = new File(backupDBPath);
        bool = f.exists();
        if(!bool){
            f.mkdir();
            if(!f.mkdir()){
                toast("フォルダの作成に失敗しました。");
                return;
            }
        }

        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
