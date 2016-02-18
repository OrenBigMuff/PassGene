package com.bizan.mobile10.passgene;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * @author yyukihhide
 * @version 0.5
 */
/**
 * プリファレンスを設定ファイルとして利用
 * コンテキストを渡してやる。
 * PreferenceC pref = new PreferenceC(this);
 *
 * 呼び出し方
 * キーワードと初期値を渡してやる
 * int number = pref.readConfig("keyword", 0);
 * boolean
 * float
 * long
 * String
 * 第二引数として以上5つの型が利用できる。
 *
 *
 * 書き込み方
 * キーワードと書き込む値を渡してやる
 * pref.writeConfig("keyword", 1);
 * 上記同様、5つの型が利用できる
 *
 */
public class PreferenceC{
    private SharedPreferences pref;

    /**
     * 注意：簡易に使うと"Config"に限定されている。
     * @param context
     */
    public PreferenceC(Context context){
        pref = context.getSharedPreferences("Config", context.MODE_PRIVATE);
    }
    public PreferenceC(Context context , String fileName){
        pref = context.getSharedPreferences(fileName, context.MODE_PRIVATE);
    }
    public PreferenceC(Context context , String fileName, int mode){
        pref = context.getSharedPreferences(fileName, mode);
    }


    /**
     * 読み出し+初期値セット
     * @param key   値とセットとなるキーワード
     * @param defaultValue  値が未登録の場合に初期値として利用される。
     * @return  String,boolean,float,int,longが利用できる
     */
    public String readConfig(String key, String defaultValue){
        return pref.getString(key, defaultValue);
    }
    public boolean readConfig(String key, boolean defaultValue){
        return pref.getBoolean(key, defaultValue);
    }
    public float readConfig(String key, float defaultValue){
        return pref.getFloat(key, defaultValue);
    }
    public int readConfig(String key, int defaultValue){
        return pref.getInt(key, defaultValue);
    }
    public long readConfig(String key, long defaultValue){
        return pref.getLong(key, defaultValue);
    }


    /**
     * 書き込み
     * @param key   値とセットとなるキーワード
     * @param value 書き込む値
     */
    public void writeConfig(String key, String value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public void writeConfig(String key, boolean value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public void writeConfig(String key, float value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }
    public void writeConfig(String key, int value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public void writeConfig(String key, long value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }
}
