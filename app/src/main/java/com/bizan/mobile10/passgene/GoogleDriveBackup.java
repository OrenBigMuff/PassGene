package com.bizan.mobile10.passgene;
/**
 * imai
 */

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.services.drive.Drive;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Arrays;

public class GoogleDriveBackup extends AppCompatActivity
        implements View.OnClickListener {

    private DatabaseC dbC;
    private Button btnGdSave;
    //    private static final String TAG = "drive-quickstart";
    private static final int REQUEST_ACCOUNT_PICKER = 1;
    private static final int SELECT_DB = 3;
    private static final int REQUEST_AUTHORIZATION = 2;

    private static Drive service;
    private GoogleAccountCredential credential;
    private static Uri selectedDBUri;

    private String selectedDBPath;
    private ImageView img;
    private TextView tv;
    Intent databaseIntent;
    private static String DB_PATH = null;
    private static final String DATABASE_NAME = "pg.db";

    /**
     * お試し
     *//*
    private static final String TAG = "GoogleDriveBackup";
    private GoogleApiClient api;
    private boolean mResolvingError = false;
    private DriveFile mfile;
    private static final int DIALOG_ERROR_CODE = 100;

    private static final String GOOGLE_DRIVE_FILE_NAME = "pg.db";


*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbC = new DatabaseC(PassList2.getDbHelper());
        btnGdSave = (Button) findViewById(R.id.btnGdSave);
        btnGdSave.setOnClickListener(this);
        DB_PATH = Environment.getDataDirectory().getPath() + "/data/" + "com.bizan.mobile10.passgene" + "/databases/pg.db";

        /*credential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(new String[]{DriveScopes.DRIVE_FILE}));
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);*/

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        service = getDriveService(credential);
                        saveFileToDrive();
                    }
                }
                break;

            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    saveFileToDrive();
                } else {
                    startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
                }

            case SELECT_DB:
                if (resultCode == Activity.RESULT_OK) {

                    selectedDBUri = data.getData();
                    selectedDBPath = getPath(selectedDBUri);
                    // tv = (TextView) findViewById(R.id.text1);
                    //tv.setText("File Path: " + selectedImagePath);
                    toast("DB Path : " + selectedDBPath);
                    //img.setImageURI(selectedImageUri);
                    startDBIntent();
                }
        }
    }

    private Drive getDriveService(GoogleAccountCredential credential) {
        return new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
                .build();
    }

    public void startDBIntent(){

        credential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(new String[]{DriveScopes.DRIVE_FILE}));
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);

    }

    private void saveFileToDrive() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // File's binary content
                    java.io.File fileContent = new java.io.File(selectedDBUri.getPath());
                    FileContent mediaContent = new FileContent(getMimeType(DB_PATH), fileContent);

                    // File's metadata.
                    File body = new File();
                    body.setTitle(DATABASE_NAME);
                    body.setMimeType(getMimeType(DB_PATH));

                    File file = service.files().insert(body, mediaContent).execute();
                    if(file != null) {

                        toast("uploaded: ");

                    }

                }catch (UserRecoverableAuthIOException e) {
                    startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
                }   catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedDBUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //ボタンのクリック処理
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGdSave) {

            databaseIntent = new Intent();
            databaseIntent.setType(getMimeType(DB_PATH));
            databaseIntent.setAction(Intent.ACTION_GET_CONTENT);
            databaseIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedDBUri);
            startActivityForResult(databaseIntent, SELECT_DB);

//            saveFileToDrive();

        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
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

}



