package com.bizan.mobile10.passgene;

import android.annotation.TargetApi;
import android.os.Build;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SaveLoadDB extends AppCompatActivity
        implements View.OnClickListener {

    DatabaseC dbC;
    private Button btnSdSave;

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

        dbC = new DatabaseC(PassList2.getDbHelper());

        btnSdSave = (Button) findViewById(R.id.btnSdSave);
        btnSdSave.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSdSave) {

            if(getMount_sd() != null){

//                data = Environment.getDataDirectory();
                FileChannel source = null;
                FileChannel destination = null;
                String currentDBPath = "/data/" + "com.bizan.mobile10" + "/databases/" + "pg.db";
                String backupDirectory =  getMount_sd() + "/passgene";
                String backupDBPath =  getMount_sd() + "/passgene/bg.db";
                // SDカードに保存する処理
                File file = new File(backupDirectory);
                bool = file.exists();
                if(!bool){
                    file.mkdir();
                    if(!file.mkdir()){
                        toast("フォルダの作成に失敗しました。");
                        return;
                    }
                }
                File currentDB = new File(currentDBPath);
                File backupDB = new File(backupDBPath);
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

            }else{
                // SDカードのマウント先が見つからない場合の処理
                toast("SDメモリが書込み状態でマウントされていません。");
            }
            //ここにSDへのセーブコードを記述
            toast("セーブじゃ！セーブするのじゃ～");
//            exportDB();

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

    /**
     * SDカードのマウント先をゲットするメソッド
     * @return
     */

    @TargetApi(9)
    private String getMount_sd() {
        List<String> mountList = new ArrayList<String>();
        String mount_sdcard = null;

        Scanner scanner = null;
        try {
            // システム設定ファイルにアクセス
            File vold_fstab = new File("/system/etc/vold.fstab");
            scanner = new Scanner(new FileInputStream(vold_fstab));
            // 一行ずつ読み込む
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // dev_mountまたはfuse_mountで始まる行の
                if (line.startsWith("dev_mount") || line.startsWith("fuse_mount")) {
                    // 半角スペースではなくタブで区切られている機種もあるらしいので修正して
                    // 半角スペース区切り３つめ（path）を取得
                    String path = line.replaceAll("\t", " ").split(" ")[2];
                    // 取得したpathを重複しないようにリストに登録
                    if (!mountList.contains(path)){
                        mountList.add(path);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        // Environment.isExternalStorageRemovable()はGINGERBREAD以降しか使えない
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            // getExternalStorageDirectory()が罠であれば、そのpathをリストから除外
            if (!Environment.isExternalStorageRemovable()) {   // 注1
                mountList.remove(Environment.getExternalStorageDirectory().getPath());
            }
        }

        // マウントされていないpathは除外
        for (int i = 0; i < mountList.size(); i++) {
            if (!isMounted(mountList.get(i))){
                mountList.remove(i--);
            }
        }

        // 除外されずに残ったものがSDカードのマウント先
        if(mountList.size() > 0){
            mount_sdcard = mountList.get(0);
        }

        // マウント先をreturn（全て除外された場合はnullをreturn）
        return mount_sdcard;
    }

    // 引数に渡したpathがマウントされているかどうかチェックするメソッド
    public boolean isMounted(String path) {
        boolean isMounted = false;

        Scanner scanner = null;
        try {
            // マウントポイントを取得する
            File mounts = new File("/proc/mounts");   // 注2
            scanner = new Scanner(new FileInputStream(mounts));
            // マウントポイントに該当するパスがあるかチェックする
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().contains(path)) {
                    // 該当するパスがあればマウントされているってこと
                    isMounted = true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        // マウント状態をreturn
        return isMounted;
    }
}
