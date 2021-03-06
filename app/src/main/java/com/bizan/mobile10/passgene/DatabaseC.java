package com.bizan.mobile10.passgene;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author yyukihhide
 * @version 0.5
 */

public class DatabaseC {
    //テーブル名
    private static String[] dbTable = {"service_info", "user_info"};
    private static SQLiteDatabase db;

    private int SERVICE_INFO = 0;
    private int USER_INFO = 1;

    /**
     * コンストラクタ
     *
     * @param dbHelper
     */
    public DatabaseC(DatabaseHelper dbHelper) {
        this.db = dbHelper.getWritableDatabase();
    }

    public void closeDB() {
        db.close();
    }

    /**
     * *マスターパスワード登録
     * 注意：フラッグを例外的に”2”に設定している
     *
     * @param value
     * @return
     */
    public long insertMasterPass(String value) {
        long result;
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("info_name", "masterpassword");
            contentValues.put("value", value);
            contentValues.put("category", "5");
            contentValues.put("delete_flag", "2");
            contentValues.put("useless_flag", "2");
            result = db.insert(dbTable[USER_INFO], "", contentValues);
        } catch (Exception e) {
            result = -1;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        return result;
    }


    /**
     * *サービス情報の登録
     *
     * @param value
     * @return
     */
    public long insertServiceInfo(String[] value) {
        long result;
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("service", value[0]);
            contentValues.put("user_id", value[1]);
            contentValues.put("mail_address", value[2]);
            contentValues.put("char_num", value[3]);
            contentValues.put("char_uppercase", value[4]);
            contentValues.put("char_lowercase", value[5]);
            contentValues.put("char_symbol", value[6]);
            contentValues.put("num_of_char", value[7]);
            contentValues.put("generated_datetime", getDate());
            contentValues.put("updated_datetime", getDate());
            contentValues.put("fixed_pass", value[8]);
            contentValues.put("pass_hint", value[9]);
            contentValues.put("gene_id1", value[10]);
            contentValues.put("gene_id2", value[11]);
            contentValues.put("gene_id3", value[12]);
            contentValues.put("gene_id4", value[13]);
            contentValues.put("algorithm", value[14]);
            contentValues.put("delete_flag", "0");
            result = db.insert(dbTable[SERVICE_INFO], "", contentValues);
        } catch (Exception e) {
            result = -1;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        return result;
    }

    /**
     * *サービス情報の更新
     *
     * @param id
     * @param value
     * @return
     */
    public boolean updateServiceInfo(int id, String[] value) {
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("service", value[0]);
            contentValues.put("user_id", value[1]);
            contentValues.put("mail_address", value[2]);
            contentValues.put("char_num", value[3]);
            contentValues.put("char_uppercase", value[4]);
            contentValues.put("char_lowercase", value[5]);
            contentValues.put("char_symbol", value[6]);
            contentValues.put("num_of_char", value[7]);
            //contentValues.put("generated_datetime", getDate());
            contentValues.put("updated_datetime", getDate());
            contentValues.put("fixed_pass", value[8]);
            contentValues.put("pass_hint", value[9]);
            contentValues.put("gene_id1", value[10]);
            contentValues.put("gene_id2", value[11]);
            contentValues.put("gene_id3", value[12]);
            contentValues.put("gene_id4", value[13]);
            contentValues.put("algorithm", value[14]);
            //contentValues.put("delete_flag", value[15]);
            db.update(dbTable[SERVICE_INFO], contentValues, "_id=?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (Exception e) {
            Log.e("updateServiceInfo", "err");
            return false;
        }
    }

    /**
     * *ユーザー情報の登録
     *
     * @param value
     * @return
     */
    public long insertUserInfo(String[] value) {
        long result;
        try {
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put("info_name", value[0]);
            contentValues.put("value", value[1]);
            contentValues.put("category", value[2]);
            contentValues.put("delete_flag", "0");
            contentValues.put("useless_flag", "0");
            result = db.insert(dbTable[USER_INFO], "", contentValues);
        } catch (Exception e) {
            result = -1;
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        return result;
    }


    /**
     * *マスターパス
     *
     * @return int マスターパスの呼び出し
     */
    public String readMasterPass() {
        String masterpass = null;
        try {
            String[] sqlD = {"masterpassword"};
            String sql = "SELECT value FROM user_info WHERE info_name=?";
            Cursor cursor = db.rawQuery(sql, sqlD);
            cursor.moveToNext();
            masterpass = cursor.getString(0);

        } catch (Exception e) {
            Log.e("readMasterPass", "err");
        }
        return masterpass;
    }

    /**
     * *マスターパスワードの変更
     *
     * @param value
     * @return
     */
    public boolean updateMasterPass(String value) {
        try {
            db.beginTransaction();
            ContentValues val = new ContentValues();
            val.put("value", value);
            db.update(dbTable[USER_INFO], val, "info_name=?",
                    new String[]{"masterpassword"});
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (Exception e) {
            Log.e("updateMasterPass", "err");
            return false;
        }
    }

    /**
     * *ユーザー情報の消去 使わない時にtrue
     *
     * @param id
     * @return
     */
    public boolean deleteUserInfoDeleteFlag(int id) {
        try {
            db.beginTransaction();
            ContentValues val = new ContentValues();
            val.put("delete_flag", "1");
            db.update(dbTable[USER_INFO], val, "_id=?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (Exception e) {
            Log.e("delUserInfoDelFlag", "err");
            return false;
        }
    }

    /**
     * *ユーザー情報をパスワード生成に利用するかどうか
     *
     * @param id
     * @param uselessFlag 使わない時にtrue
     * @return
     */
    public boolean changeUserInfoUselessFlag(int id, String uselessFlag) {
        if (!uselessFlag.equals("0") && !uselessFlag.equals("1")) {
            return false;
        }
        try {
            db.beginTransaction();
            ContentValues val = new ContentValues();
            val.put("useless_flag", uselessFlag);
            db.update(dbTable[USER_INFO], val, "_id=?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (Exception e) {
            Log.e("chagUserInfoUselessFlag", "err");
            return false;
        }
    }

    /**
     * *サービス毎削除したときにTrue
     *
     * @param id
     * @return
     */
    public boolean deleteServiceInfo(int id) {
        try {
            db.beginTransaction();
            ContentValues val = new ContentValues();
            val.put("delete_flag", "1");
            db.update(dbTable[SERVICE_INFO], val, "_id=?",
                    new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (Exception e) {
            Log.e("deleteServiceInfo", "err");
            return false;
        }
    }


    /**
     * PasswordListInfoページで使用
     *
     *
     * @return
     */
    public Cursor readPasswordListInfo() {
        Cursor cursor = null;
        try {
            String[] sqlD = {"0"};
            String sql = "SELECT _id,service,pass_hint,generated_datetime, updated_datetime" +
                    " FROM service_info WHERE delete_flag=? ORDER BY updated_datetime DESC";
            cursor = db.rawQuery(sql, sqlD);
            return cursor;
        } catch (Exception e) {
            Log.e("readPasswordListInfo", "err");
        }
        return cursor;
    }

    public Cursor readPasswordListInfo(String str) {
        Cursor cursor = null;
        String sql = "";
        try {
            String[] sqlD = {"0"};
            if (str.equals("update")) {
                sql = "SELECT _id,service,pass_hint,generated_datetime, updated_datetime" +
                        " FROM service_info WHERE delete_flag=? ORDER BY updated_datetime DESC";
            } else if (str.equals("service")) {
                sql = "SELECT _id,service,pass_hint,generated_datetime, updated_datetime" +
                        " FROM service_info WHERE delete_flag=? ORDER BY service";
            }

            cursor = db.rawQuery(sql, sqlD);
            return cursor;
        } catch (Exception e) {
            Log.e("readPasswordListInfo", "err");
        }
        return cursor;
    }

    public Cursor readSingleclum(int col){
        Cursor cursor = null;
        String sql = "";
        String[] sqlD = {"0"};
        switch(col){
            case 1:
                sql = "SELECT DISTINCT service FROM service_info WHERE delete_flag=?";
                break;
            case 2:
                sql = "SELECT DISTINCT user_id FROM service_info WHERE delete_flag=?";
                break;
            case 3:
                sql = "SELECT DISTINCT mail_address FROM service_info WHERE delete_flag=?";
                break;
        }
        try{
            cursor = db.rawQuery(sql, sqlD);
        }catch (Exception e){
            Log.e("readSingleclum", e.getMessage());
        }
        return cursor;
    }


    /**
     * *service_infoの全情報
     *
     * @return
     */
    public Cursor readServiceInfoAll() {
        Cursor cursor = db.query(dbTable[SERVICE_INFO], new String[]{
                        "_id", "service",
                        "user_id", "mail_address", "char_num", "char_uppercase", "char_lowercase",
                        "char_symbol", "num_of_char", "generated_datetime", "updated_datetime",
                        "fixed_pass", "pass_hint", "gene_id1", "gene_id2", "gene_id3", "gene_id4",
                        "algorithm", "delete_flag"},
                null, null, null, null, null);
        return cursor;
    }

    /**
     * *useer_infoの全情報
     *
     * @return
     */
    public Cursor readUserInfoAll() {
        Cursor cursor = db.query(dbTable[USER_INFO], new String[]{
                        "_id", "info_name",
                        "value", "category", "delete_flag", "useless_flag"},
                null, null, null, null, null);
        return cursor;
    }

    public Cursor readServiceInfo(String id) {
        String[] sqlD = {id};
        Cursor cursor;
        String sql = "SELECT * FROM service_info WHERE delete_flag=0 AND _id=?";
        cursor = db.rawQuery(sql, sqlD);
        return cursor;
    }

    public Cursor readUserSingleInfo(String id) {
        String[] sqlD = {id};
        Cursor cursor;
        String sql = "SELECT * FROM user_info WHERE delete_flag=0 AND _id=?";
        cursor = db.rawQuery(sql, sqlD);
        return cursor;
    }

    /**
     * 一件のユーザー情報を返す
     *
     * @return
     */
    public Cursor readUserInfo() {
        String[] sqlD = {"1"};
        Cursor cursor;
        String sql = "SELECT * FROM user_info WHERE useless_flag=0 AND delete_flag=0 ORDER BY RANDOM() LIMIT ?";
        cursor = db.rawQuery(sql, sqlD);
        return cursor;
    }

    /**
     * カテゴリー別
     * 一件のユーザー情報を返す
     *
     * @param category 0:電話
     *                 1:生年月日
     *                 2:姓
     *                 3:名
     *                 4:フリーテキスト
     *                 5:フリーナンバー
     *                 6:オールフリー
     * @return
     */
    public Cursor readUserInfo(String category) {
        String[] sqlD = {category, "1"};
        Cursor cursor;
        String sql = "SELECT * FROM user_info WHERE useless_flag=0 AND delete_flag=0 AND category=? ORDER BY RANDOM() LIMIT ?";
        cursor = db.rawQuery(sql, sqlD);
        return cursor;
    }

    /**
     * 数字だけ
     * @return
     */
    public Cursor readUserInfoCategryNumber() {
        String[] sqlD = {"0", "1", "5", "1"};
        Cursor cursor;
        String sql = "SELECT * FROM user_info WHERE useless_flag=0 AND delete_flag=0 AND (category=? OR category=? OR category=?) ORDER BY RANDOM() LIMIT ?";
        cursor = db.rawQuery(sql, sqlD);
        return cursor;
    }

    /**
     * 文字だけ
     * @return
     */
    public Cursor readUserInfoCategryString() {
        String[] sqlD = {"2", "3", "4", "1"};
        Cursor cursor;
        String sql = "SELECT * FROM user_info WHERE useless_flag=0 AND delete_flag=0 AND (category=? OR category=? OR category=?) ORDER BY RANDOM() LIMIT ?";
        cursor = db.rawQuery(sql, sqlD);
        return cursor;
    }


    /**
     * 試作品です。　user_info の ID 渡すと　service_info の　IDを含むカーソルが返ってくる
     *
     * @param id
     * @return
     */
    public Cursor readGeneUseid(String id) {
        String[] sqlD = new String[4];
        for (int i = 0; i < sqlD.length; i++) {
            sqlD[i] = id;
        }
        Cursor cursor = null;
        try {
            //String[] sqlD = {"master"};
            String sql = "SELECT _id FROM service_info WHERE gene_id1=? OR gene_id2=? OR gene_id3=? OR gene_id4=?";
            cursor = db.rawQuery(sql, sqlD);
            return cursor;
        } catch (Exception e) {
            Log.e("readMasterPass", "err");
        }
        return cursor;
    }

    public Cursor readGeneUseid(int id) {
        String[] sqlD = new String[4];
        for (int i = 0; i < sqlD.length; i++) {
            sqlD[i] = String.valueOf(id);
        }
        Cursor cursor = null;
        try {
            //String[] sqlD = {"master"};
            String sql = "SELECT _id FROM service_info WHERE gene_id1=? OR gene_id2=? OR gene_id3=? OR gene_id4=?";
            cursor = db.rawQuery(sql, sqlD);
            return cursor;
        } catch (Exception e) {
            Log.e("readMasterPass", "err");
        }
        return cursor;
    }


    /**
     * @return boolean 登録あり：true、0件:false
     */
    public boolean isCheckServiceInfo() {
        try {
            Cursor cursor = db.query(dbTable[SERVICE_INFO], new String[]{"_id"},
                    null, null, null, null, null);
            if (cursor.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e("isCheckServiceInfo", "err");
            return false;
        }
    }

    public boolean isCheckUserInfo() {
        try {
            Cursor cursor = db.query(dbTable[USER_INFO], new String[]{"_id"},
                    null, null, null, null, null);
            if (cursor.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e("isCheckUserInfo", "err");
            return false;
        }
    }

    /**
     * テーブルの削除
     *
     * @return 成功：true失敗：false
     */
    public boolean reset() {
        try {

            db.execSQL("drop table if exists service_info");
            db.execSQL("drop table if exists user_info");
            return true;
        } catch (Exception e) {
            Log.e("reset", "err");
            return false;
        }
    }

    /**
     * 本日の日付
     *
     * @return
     */
    private String getDate() {
        String today = "";
        Calendar cale = Calendar.getInstance();
        today = longToStringYDM(cale.getTimeInMillis());
        return today;
    }

    /**
     * 日付のフォーマット
     *
     * @param longNum
     * @return
     */
    private String longToStringYDM(long longNum) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("y/M/d");
        str = sdf.format(longNum);
        return str;
    }

    /**
     * DBのパス取得
     *
     */
    public String getPath() {
        String tmp = db.getPath();
        return tmp;
    }

}
