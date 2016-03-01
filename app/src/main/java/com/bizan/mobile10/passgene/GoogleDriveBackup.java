package com.bizan.mobile10.passgene;
/**
 * imai
 */

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;


import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

public class GoogleDriveBackup extends AppCompatActivity
        implements View.OnClickListener {
/*

    Activity 起動時に Google アカウントを選択
    「Save」 をタップするとDBのファイルが選択した Google アカウントの Google Drive に保存される (ファイル名は “pass_gene_test”)
    「Load」 をタップすると、Google Drive に保存されているDBデータがGoogleされる

*/

    DatabaseC dbC;
    Button btnGdSave;
    Button btnGdLoad;

    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_AUTHORIZATION = 2;

    static final String DB_TITLE = "pg.db";
    static final String FILE_TITLE = "pg.csv";

    private Drive service = null;
    private GoogleAccountCredential credential = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnGdSave = (Button) findViewById(R.id.btnGdSave);
        btnGdSave.setOnClickListener(this);
        btnGdLoad = (Button) findViewById(R.id.btnGdLoad);
        btnGdLoad.setOnClickListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (service == null) {
            credential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(DriveScopes.DRIVE));
            startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GoogleDriveBackup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.bizan.mobile10.passgene/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    //ボタンのクリック処理
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGdSave) {

            //ここにGoogleDriveへのセーブコードを記述
            toast("セーブじゃ！セーブするのじゃ～");
            saveDataToDrive();

        } else if (v.getId() == R.id.btnGdLoad) {

            //ここにGoogleDriveからのロードコードを記述
            toast("ロードじゃ！ロードせんか～い\nせんのか～い！");
            loadDataFromDrive();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    credential.setSelectedAccountName(accountName);
                    service = getDriveService(credential);
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    saveDataToDrive();
                } else {
                    startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
                }
                break;
        }
    }

    /**
     * セーブの処理を記述
     *
     *
     *
     */
    private void saveDataToDrive() {
        dbC = new DatabaseC(InitialSet1.getDbHelper());
        Cursor cursor = dbC.readUserInfoAll();
//        Cursor cursor2 = dbC.readServiceInfoAll();

        final java.io.File ff_u = createCSV(cursor);
//        final java.io.File ff_s = createCSV(cursor2);

        //DBからexportしたcsvファイルを定義する
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String fileIdOrNull = null;
                try {
                    toast("ここまでおK");
                    FileList list = service.files().list().execute();
                    for (File f : list.getItems()) {
                        if (DB_TITLE.equals(f.getTitle())) {
                            fileIdOrNull = f.getId();
                        }
                    }

                    File body = new File();
                    body.setTitle(FILE_TITLE);                  //fileContent.getName();
                    body.setMimeType("text/plain");             //csvで保存
                    final FileContent content = new FileContent("text/plain", ff_u);

//                    ByteArrayContent content = new ByteArrayContent("text/plain", inputText.getBytes(Charset.forName("UTF-8")));

                    if (fileIdOrNull == null) {
                        service.files().insert(body, content).execute();
                        toast("insert!");
                    } else {
                        service.files().update(fileIdOrNull, body, content).execute();
                        toast("update!");
                    }
                    //TODO失敗時の処理
                } catch (UserRecoverableAuthIOException e) {
                    startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
                    toast("こりゃひどい");
                } catch (IOException e) {
                    toast("error occur...");
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    /**
     * ロードの処理を記述
     *
     *
     *
     */
    private void loadDataFromDrive() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 指定のタイトルのファイルの ID を取得
                    String fileIdOrNull = null;
                    FileList list = service.files().list().execute();
                    for (File f : list.getItems()) {
                        if (FILE_TITLE.equals(f.getTitle())) {
                            fileIdOrNull = f.getId();
                        }
                    }

                    InputStream is = null;
                    if (fileIdOrNull != null) {
                        File f = service.files().get(fileIdOrNull).execute();
                        is = downloadFile(service, f);
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    try {
                        StringBuffer sb = new StringBuffer();
                        sb.append(br.readLine());

                        final String text = sb.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                ((EditText)findViewById(R.id.editText)).setText(text);
                            }
                        });
                    } finally {
                        if (br != null) br.close();
                    }
                    // TODO 失敗時の処理?
                } catch (UserRecoverableAuthIOException e) {
                    startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
                } catch (IOException e) {
                    toast("error occur...");
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private static InputStream downloadFile(Drive service, File file) {
        if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
            try {
                HttpResponse resp =
                        service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
                return resp.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    private Drive getDriveService(GoogleAccountCredential credential) {
        return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
                .build();
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
     * DBからCSVを書出そうのコーナー
     */
    public java.io.File createCSV(Cursor cursor) {
        boolean var = true;
        final String filename;
        java.io.File folder = new java.io.File("/data/data/com.bizan.mobile10.passgene/databases");
        if (!folder.exists())
            var = folder.mkdir();
        filename = folder.toString() + "/" + "pg.csv";

        FileWriter fw = null;
        try {
            fw = new FileWriter(filename);
            if (cursor != null) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    for (int j = 0; j < cursor.getColumnNames().length; j++) {
                        fw.append(cursor.getString(j) + ";");
                    }
                    fw.append("\n");

                    cursor.moveToNext();
                }
                cursor.close();
            }
            fw.close();
        } catch (Exception e) {
        }
        java.io.File fixedFile = new java.io.File(filename);
        return fixedFile;
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GoogleDriveBackup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.bizan.mobile10.passgene/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

