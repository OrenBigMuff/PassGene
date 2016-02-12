package com.bizan.mobile10.passgene;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PassGeneDialog extends DialogFragment{

    TextView title_pgdf;
    TextView message_pgdf;
    Button positive_button_pgdf;
    Button close_button_pgdf;

    /**
     * getter/setter用キーの定義
     */
    public static final String KEY_DIALOG_LAYOUT = "layout";
    public static final String KEY_DIALOG_ICON = "icon";
    public static final String KEY_DIALOG_TITLE = "title";
    public static final String KEY_DIALOG_MESSAGE = "message";
    public static final String KEY_DIALOG_MASTER = "master_password";       //このケースのみ使用
    public static final String KEY_DIALOG_LABEL_POSITIVE = "label_positive";
    public static final String KEY_DIALOG_LABEL_NEGATIVE = "label_negative";
    public static final String KEY_DIALOG_LAYOUT_POSITIVE = "layout_positive";
    public static final String KEY_DIALOG_LAYOUT_NEGATIVE = "layout_negative";

    /**
     * getString系のonAttachまでの一時保持用変数
     */
    private static final int DEFAULT_INT_VALUE = 0;
    private int mTitleId = DEFAULT_INT_VALUE;
    private int mMessageId = DEFAULT_INT_VALUE;
    private int mPositiveButtonTextId = DEFAULT_INT_VALUE;
    private int mNegativeButtonTextId = DEFAULT_INT_VALUE;
    private int mPositiveButtonId = DEFAULT_INT_VALUE;
    private int mNegativeButtonId = DEFAULT_INT_VALUE;


    /**
     * インスタンス生成とsetArguments
     *
     * @return
     */
    public static PassGeneDialog newInstance() {
        PassGeneDialog instance = new PassGeneDialog();
        // ダイアログに渡すパラメータはBundleにまとめる
        Bundle arguments = new Bundle();
        instance.setArguments(arguments);
        return instance;
    }

/*
    */
/**
     * activityにアタッチされていなければ使えないgetString系とリスナー
     *
     * @param activity
     *//*

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DialogListener == false) {
            throw new ClassCastException("Activity not implements DialogListener.");
        }
        mListener = (DialogListener) activity;
    }
*/


    /**
     * Dialog Listener
     * ダイアログのボタンにListnerを設定する（今はやり方がわからない）
     */

/*
    private DialogListener mListener = null;



    public interface DialogListener {

        public void onPositiveButtonClick(String tag);

        public void onNegativeButtonClick(String tag);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                if (mListener != null) {
                    mListener.onPositiveButtonClick(getTag());
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                if (mListener != null) {
                    mListener.onNegativeButtonClick(getTag());
                }
                break;
            default:
                break;
        }
    }
*/


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(this.getActivity());

        //値を受け取る
        String title = getTitle();
        String message = getMessage();
        int layoutId = getContentViewId();
        String posi = getPositiveButtonText();
        String nega = getNegativeButtonText();
        final String masterPass = getMaster();            //このケースのみ使用
        int posiId = getPositiveButtonLayout();
        int negaId = getNegativeButtonLayout();

        // dialog customize content view
        if (layoutId != DEFAULT_INT_VALUE) {

            // タイトル非表示
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            // フルスクリーン
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

            dialog.setContentView(layoutId);

            // 背景を透明にする
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        //レイアウトの要素
        title_pgdf = (TextView) dialog.findViewById(R.id.title_pgdf);
        message_pgdf = (TextView) dialog.findViewById(R.id.message_pgdf);
        positive_button_pgdf = (Button) dialog.findViewById(R.id.positive_button_pgdf);
        close_button_pgdf = (Button) dialog.findViewById(R.id.close_button_pgdf);

        //受け取った値をセットする
        title_pgdf.setText(title);
        message_pgdf.setText(message);
        positive_button_pgdf.setText(posi);
        positive_button_pgdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ok ボタンがおされた
                toast(InitialSet1.fullname + " さんのマスターパスワードは" + masterPass + " で登録しました。");
                Intent intent = new Intent(getActivity(), InitialSet3.class);
                startActivity(intent);

            }
        });
        close_button_pgdf.setText(nega);
        close_button_pgdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dismiss();

            }
        });

        return dialog;
    }


    /**
     * ここから鬼のsetter/getterでっせ！
     */
 /*   public String getTag() {
        return getArguments().getTag();
    }*/

    private String getTitle() {
        return getArguments().getString(KEY_DIALOG_TITLE);
    }

    public void setTitle(String title) {
        getArguments().putString(KEY_DIALOG_TITLE, title);
    }

    private String getMessage() {
        return getArguments().getString(KEY_DIALOG_MESSAGE);
    }

    public void setMessage(String text) {
        getArguments().putString(KEY_DIALOG_MESSAGE, text);
    }

    private String getMaster() {
        return getArguments().getString(KEY_DIALOG_MASTER);
    }

    public void setMaster(String text) {
        getArguments().putString(KEY_DIALOG_MASTER, text);
    }

    private int getContentViewId() {
        return getArguments().getInt(KEY_DIALOG_LAYOUT, DEFAULT_INT_VALUE);
    }

    public void setContentViewId(int resId) {
        getArguments().putInt(KEY_DIALOG_LAYOUT, resId);
    }

    private String getPositiveButtonText() {
        return getArguments().getString(KEY_DIALOG_LABEL_POSITIVE);
    }

    public void setPositiveButtonText(String text) {
        getArguments().putString(KEY_DIALOG_LABEL_POSITIVE, text);
    }

    private String getNegativeButtonText() {
        return getArguments().getString(KEY_DIALOG_LABEL_NEGATIVE);
    }

    public void setNegativeButtonText(String text) {
        getArguments().putString(KEY_DIALOG_LABEL_NEGATIVE, text);
    }

    private int getPositiveButtonLayout() {
        return getArguments().getInt(KEY_DIALOG_LAYOUT_POSITIVE, DEFAULT_INT_VALUE);
    }

    private int getNegativeButtonLayout() {
        return getArguments().getInt(KEY_DIALOG_LAYOUT_NEGATIVE, DEFAULT_INT_VALUE);
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
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }


}
