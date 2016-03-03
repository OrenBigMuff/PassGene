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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;


import com.google.api.services.drive.model.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class GoogleDriveBackup extends AppCompatActivity
        implements View.OnClickListener/*,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener*/ {
/*
    Activity 起動時に Google アカウントを選択
    「Save」 をタップするとDBのファイルが選択した Google アカウントの Google Drive に保存される (ファイル名は “pass_gene_test”)
    「Load」 をタップすると、Google Drive に保存されているDBデータがGoogleされる
*/

    DatabaseC dbC;
    Button btnGdSave;
    Cursor cursor_s;
    Cursor cursor_u;

    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_AUTHORIZATION = 2;

    static final String DB_TITLE = "pg.db";
    static final String S_TITLE = "pg_s.csv";
    static final String U_TITLE = "pg_u.csv";
    static final String FILE_TITLE = "pg.csv";

    private Drive service = null;
    private GoogleAccountCredential credential = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ここからお試し
     **/
//    private boolean mResolvingError = false;
//    private DriveFile mfile;
//    private static final int DIALOG_ERROR_CODE =100;
//    private static final String DATABASE_NAME = "pg.db";
//    private static final String GOOGLE_DRIVE_FILE_NAME = "pg.db";
//    private static final String TAG = "GoogleDriveBackup";
//
//    DriveId folderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_drive_backup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbC = new DatabaseC(InitialSet1.getDbHelper());
        cursor_s = dbC.readServiceInfoAll();
        cursor_u = dbC.readUserInfo();

        btnGdSave = (Button) findViewById(R.id.btnGdSave);
        btnGdSave.setOnClickListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//        client = new GoogleApiClient.Builder(this).addApi(com.google.android.gms.drive.Drive.API).addScope(com.google.android.gms.drive.Drive.SCOPE_FILE).
//                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        /*if(!mResolvingError){
            client.connect();
        }*/

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

    /*@Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG, "Connection failed");
        if(mResolvingError) { // If already in resolution state, just return.
            return;
        } else if(result.hasResolution()) { // Error can be resolved by starting an intent with user interaction
            mResolvingError = true;
            try {
                result.startResolutionForResult(this, DIALOG_ERROR_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else { // Error cannot be resolved. Display Error Dialog stating the reason if possible.
            ErrorDialogFragment fragment = new ErrorDialogFragment();
            Bundle args = new Bundle();
            args.putInt("error", result.getErrorCode());
            fragment.setArguments(args);
            fragment.show(getFragmentManager(), "errordialog");
        }
    }*/


    //ボタンのクリック処理
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGdSave) {

            //ここにGoogleDriveへのセーブコードを記述
            toast("セーブじゃ！セーブするのじゃ～");
            saveDataToDrive();
/*
            Uri fileUri = Uri.fromFile(new java.io.File(Environment.getDataDirectory().getPath()
                    + "/data/com.bizan.mobile10.passgene/databases/pg.db"));

            java.io.File fileContent = new java.io.File(fileUri.getPath());

            FileContent mediaContent = new FileContent("db", fileContent);
            File body = new com.google.api.services.drive.model.File();
            body.setTitle(fileContent.getName());
            body.setMimeType("db");

            try {
                *//*File file = *//*
                service.files().insert(body, mediaContent).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
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
       /* if(requestCode == DIALOG_ERROR_CODE) {
            mResolvingError = false;
            if(resultCode == RESULT_OK) { // Error was resolved, now connect to the client if not done so.
                if(!client.isConnecting() && !client.isConnected()) {
                    client.connect();
                }
            }
        }*/
    }

    /**
     * ファイルパスの拡張子から MIME Type を取得する
     *
     * @param filePath
     * @return
     */
    public static String getMimeType(String filePath){
        String mimeType = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (extension != null) {
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return mimeType;
    }

    /**
     * セーブの処理を記述
     */
    private void saveDataToDrive() {
        Thread t = new Thread(new Runnable() {
            String fileIdOrNull = null;

            @Override
            public void run() {
                try {
//                    FileList list = service.files().list().execute();
                    /*for (File f : list.getItems()) {
                        if (DB_TITLE.equals(f.getTitle())) {
                            fileIdOrNull = f.getId();
                        }
                    }*/

                    /*Uri fileUri = Uri.fromFile(new java.io.File(Environment.getDataDirectory().getPath()
                            + "/data/com.bizan.mobile10/databases/pg.db"));
*/
//                    String mimeType = getMimeType(fileUri.getPath());



//                    java.io.File fileContent = new java.io.File(fileUri.getPath());

                    java.io.File fileContent_s = createCSV(cursor_s);
                    java.io.File fileContent_u = createCSV(cursor_u);

                    FileContent mediaContent_s = new FileContent("text/csv", fileContent_s);
                    FileContent mediaContent_u = new FileContent("text/csv", fileContent_u);


                    // Fileのメタデータ作成
                    File body_s = new com.google.api.services.drive.model.File();
                    body_s.setTitle(S_TITLE);
                    body_s.setMimeType("text/csv");

                    File body_u = new com.google.api.services.drive.model.File();
                    body_s.setTitle(U_TITLE);
                    body_s.setMimeType("text/csv");

                    if (fileIdOrNull == null) {
                        toast("ここまでおK");
                        Drive.Files.Insert request = service.files().insert(body_s, mediaContent_s);
                        request.setConvert(true);
                        File file_s = request.execute();

//                        File file_s = service.files().insert(body_s, mediaContent_s).execute();
//                        File file_u = service.files().insert(body_s, mediaContent_s).execute();
                        toast("insert!");
                        fileIdOrNull = file_s.getId();
                    } else {
                        service.files().update(fileIdOrNull, body_s, mediaContent_s).execute();
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
     */
 /*   private void loadDataFromDrive() {
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
*/
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
        /*Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GoogleDriveBackup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.bizan.mobile10.passgene/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);*/
        client.disconnect();
    }

    /*@Override
    public void onConnected(Bundle bundle) {
//        Log.v(TAG, "Connected successfully");
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle("PassGene folder").build();
        com.google.android.gms.drive.Drive.DriveApi.getRootFolder(client)
                .createFolder(client, changeSet)
                .setResultCallback(folderCreatedCallback);

    *//* Connection to Google Drive established. Now request for Contents instance,
    which can be used to provide file contents.
       The callback is registered for the same. *//*
//        com.google.android.gms.drive.Drive.DriveApi.newDriveContents(client)
//           .setResultCallback(contentsCallback);
    }

    ResultCallback<DriveFolder.DriveFolderResult> folderCreatedCallback = new ResultCallback<DriveFolder.DriveFolderResult>() {
        @Override
        public void onResult(DriveFolder.DriveFolderResult result) {
            if (!result.getStatus().isSuccess()) {
                toast("Error while trying to create the folder");
                return;
            }
            folderId = result.getDriveFolder().getDriveId();
            toast("Created a folder: " + result.getDriveFolder().getDriveId());
        }
    };*/



    /*final private ResultCallback<DriveApi.DriveContentsResult> contentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.v(TAG, "Error while trying to create new file contents");
                return;
            }

            String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("db");
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(GOOGLE_DRIVE_FILE_NAME) // Google Drive File name
                    .setMimeType(mimeType)
                    .setStarred(true).build();
            // create a file on root folder
            com.google.android.gms.drive.Drive.DriveApi.getRootFolder(client)
                    .createFile(client, changeSet, result.getDriveContents())
                    .setResultCallback(fileCallback);
        }

    };*/

    /*final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {

        @Override
        public void onResult(DriveFolder.DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.v(TAG, "Error while trying to create the file");
                return;
            }
            mfile = result.getDriveFile();
            mfile.open(client, DriveFile.MODE_WRITE_ONLY, null).setResultCallback(contentsOpenedCallback);
        }
    };*/

    /*final private ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult result) {

            if (!result.getStatus().isSuccess()) {
                Log.v(TAG, "Error opening file");
                return;
            }

            try {
                FileInputStream is = new FileInputStream(getDbPath());
                BufferedInputStream in = new BufferedInputStream(is);
                byte[] buffer = new byte[8 * 1024];
                DriveContents content = result.getDriveContents();
                BufferedOutputStream out = new BufferedOutputStream(content.getOutputStream());
                int n = 0;
                while( ( n = in.read(buffer) ) > 0 ) {
                    out.write(buffer, 0, n);
                }

                in.close();
//                commitAndCloseContents is DEPRECATED -->
                *//**mfile.commitAndCloseContents(api, content).setResultCallback(new ResultCallback<Status>() {
    @Override public void onResult(Status result) {
    // Handle the response status
    }
    });**//*
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };*/

   /* private java.io.File getDbPath() {
        return this.getDatabasePath(DATABASE_NAME);
    }*/

    /*@Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "Connection suspended");
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {}

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt("error");
            return GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(), DIALOG_ERROR_CODE);
        }*/

//        public void onDismiss(DialogInterface dialog) {
//            ((ExpectoPatronum) getActivity()).onDialogDismissed();
//        }
}



