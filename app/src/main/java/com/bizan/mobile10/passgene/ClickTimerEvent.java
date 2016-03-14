package com.bizan.mobile10.passgene;

/**
 * Created by kei on 2016/03/14.
 */
public class ClickTimerEvent {
    /** クリック連打制御時間(ミリ秒) */
    private static final long CLICK_DELAY = 500;
    /** 前回のクリックイベント実行時間 */
    private static long mOldClickTime;

    public static boolean isClickEvent() {
        // 現在時間を取得する
        long time = System.currentTimeMillis();

        // 一定時間経過していなければクリックイベント実行不可
        if (time - mOldClickTime < CLICK_DELAY) {
            return false;
        }

        // 一定時間経過したらクリックイベント実行可能
        mOldClickTime = time;
        return true;
    }
}
