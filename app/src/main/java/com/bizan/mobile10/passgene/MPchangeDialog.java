package com.bizan.mobile10.passgene;

import android.app.Activity;
import android.app.Dialog;
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

/**
 * Created by user on 2016/03/07.
 */
public class MPchangeDialog extends DialogFragment{

    TextView title_mcdl;
    TextView message_mcdl;

    //コンストラクタ
    public MPchangeDialog(){

    }
    /**
     * インスタンス生成とsetArguments
     *
     * @return
     */
    public static MPchangeDialog newInstance(String title, String message,String posi, String nega, int resId) {
        MPchangeDialog instance = new MPchangeDialog();

        // ダイアログに渡すパラメータはBundleにまとめる
        Bundle args = new Bundle();

        args.putString("title", title);             //Title
        args.putString("message", message);         //message
        args.putString("posi", posi);               //posi
        args.putString("nega", nega);               //nega
        args.putInt("resId",resId);   //resId_dialog layout

        instance.setArguments(args);
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
            pListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implements DialogListener");
        }
    }

    private DialogListener pListener = null;

    public interface DialogListener{


        void onPositiveButtonClick(DialogFragment dialog);

        void onNegativeButtonClick(DialogFragment dialog);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //値を受け取る
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        int layoutId = getArguments().getInt("resId");        //今後の機能拡張の為に置いときます
        String posi = getArguments().getString("posi");
        String nega = getArguments().getString("nega");

        Dialog dialog = new Dialog(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view2 = inflater.inflate(R.layout.fragment_mpchange_dialog,null);

        //ダイアログのタイトルを取得
        title_mcdl = (TextView)view2.findViewById(R.id.title_mcdl);
        title_mcdl.setText(title);
        //ダイアログのメッセージを取得
        message_mcdl = (TextView)view2.findViewById(R.id.message_mcdl);
        message_mcdl.setText(message);

        //Positiveボタンを実装
        Button pb = (Button) view2.findViewById(R.id.positive_button_mpchange);
        pb.setText(posi);
        pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pListener.onPositiveButtonClick(MPchangeDialog.this);
            }
        });

        //Negativeボタンを実装
        Button nb = (Button) view2.findViewById(R.id.close_button_mpchange);
        nb.setText(nega);
        nb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pListener.onNegativeButtonClick(MPchangeDialog.this);
            }
        });
        //ダイアログの背景とか消す処理
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);         //タイトル非表示
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);      //フルスクリーン
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));         //背景を透明にする

        //Viewをセットする
        dialog.setContentView(view2);

        return dialog;
    }

}
