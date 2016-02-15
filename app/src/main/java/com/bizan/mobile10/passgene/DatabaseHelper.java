package com.bizan.mobile10.passgene;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private String db_name;
    private String[] db_table;
    private int db_version;

    /**
     * テーブルを削除
     * "drop table if exists " + db_table
     */
    private String[] dbColTable;
    private String query;

    /**
     *
     * @param context
     * @param db_name
     * @param db_version
     * @param dbColTable
     */
    //コンストラクタ
    public DatabaseHelper(Context context, String db_name, int db_version,String[] db_table, String[] dbColTable){
        super(context, db_name, null, db_version);
        this.db_name = db_name;
        this.db_version = db_version;

        this.dbColTable = new String[dbColTable.length];
        this.db_table = new String[db_table.length];

        for(int i = 0; i < dbColTable.length; i++){
            this.db_table[i] = db_table[i];
            this.dbColTable[i] = dbColTable[i];
        }
    }


    //データベースの生成
    @Override
    public void onCreate(SQLiteDatabase db) {
        for(int i = 0; i < dbColTable.length; i++){
            db.execSQL("create table if not exists " +
                    db_table[i] + dbColTable[i]);
        }
    }

    //データベースのアップグレード
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
/*        db.execSQL("drop table if exists " + db_table);
        Log.e("onUpgrade:::", "onUpgrade");*/
        db.execSQL(query);
        onCreate(db);
    }


}
