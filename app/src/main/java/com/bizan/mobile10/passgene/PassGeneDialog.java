package com.bizan.mobile10.passgene;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.view.View.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PassGeneDialog extends DialogFragment {

    TextView title_pgdf;
    TextView message_pgdf;

/*    *//** ダイアログの共通タグ *//*
    private static final String TAG = "passgene_dialog";*/



    //コンストラクタ
    public PassGeneDialog(){

    }

    /**
     * インスタンス生成とsetArguments
     *
     * @return
     */
    public static PassGeneDialog newInstance(String title, String message,String posi, String nega, int resId) {
        PassGeneDialog instance = new PassGeneDialog();

        // ダイアログに渡すパラメータはBundleにまとめる
        Bundle args = new Bundle();

        args.putString("title", title);             //Title
        args.putString("message", message);         //message
        args.putString("posi", posi);               //posi
        args.putString("nega", nega);               //nega
        args.putInt("resId", resId);   //resId_dialog layout

        instance.setArguments(args);
//        instance.setCancelable(cancelable);
        return instance;
    }


/**
     * activityにアタッチされていなければ使えないリスナー
     * @param activity
     */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implements DialogListener");
        }
    }


    /**
     * Dialog Listener
     */

    private DialogListener mListener = null;

    public interface DialogListener {

        void onPositiveButtonClick(DialogFragment dialog);

        void onNegativeButtonClick(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //値を受け取る
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        int layoutId = getArguments().getInt("resId");        //今後の機能拡張の為に置いときます
        String posi = getArguments().getString("posi");
        String nega = getArguments().getString("nega");

        Dialog dialog = new Dialog(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_pass_gene_dialog, null);

        //ダイアログのタイトルを取得
        title_pgdf = (TextView)view.findViewById(R.id.title_pgdf);
        title_pgdf.setText(title);
        //ダイアログのメッセージを取得
        message_pgdf = (TextView)view.findViewById(R.id.message_pgdf);
        message_pgdf.setText(message);

        //Positiveボタンを実装
        Button pb = (Button) view.findViewById(R.id.positive_button_pgdf);
        pb.setText(posi);
        pb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPositiveButtonClick(PassGeneDialog.this);
            }
        });

        //Negativeボタンを実装
        Button nb = (Button) view.findViewById(R.id.close_button_pgdf);
        nb.setText(nega);
        nb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNegativeButtonClick(PassGeneDialog.this);
            }
        });

        //ダイアログの背景とか消す処理
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);         //タイトル非表示
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);      //フルスクリーン
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));         //背景を透明にする

        //Viewをセットする
        dialog.setContentView(view);

        return dialog;

    }

 /*   @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Dialogをキャンセルした時の処理
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // TODO Dialogを閉じた時の処理
        super.onDismiss(dialog);
    }*/

   /**
     * すでに表示されたダイアログを消去します
     *
     * @param fragmentManager
     *            {@link FragmentManager}
     *//*
    private static void deleteDialogFragment(final FragmentManager fragmentManager) {

        final PassGeneDialog prev = (PassGeneDialog) fragmentManager.findFragmentByTag(TAG);
        // フラグメントが表示されていなければ処理なし
        if (prev == null) {
            return;
        }

        final Dialog dialog = prev.getDialog();
        // ダイアログがなければ処理なし
        if (dialog == null) {
            return;
        }

        // ダイアログが表示されていなければ処理なし
        if (!dialog.isShowing()) {
            return;
        }

        // ダイアログ消去通知と消去
        prev.onDismissExclusiveDialog();
        prev.dismiss();
    }

    *//**
     * ダイアログが排他的消去される際に呼び出されます。
     *//*
    protected void onDismissExclusiveDialog() {
        // 継承先で処理を実装
    }


    public void show(final FragmentManager manager) {
        deleteDialogFragment(manager);
        super.show(manager, TAG);
    }*/

}
