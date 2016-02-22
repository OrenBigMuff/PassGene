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
 * Created by user on 2016/02/22.
 */
public class DeleteDialog extends DialogFragment {

    TextView title_dltdl;       //タイトル
    TextView message_dltdl;     //メッセージ

    //コンストラクタ
    public DeleteDialog() {

    }

    /**
     * インスタンスの生成
     *
     */
    public static DeleteDialog newInstance(String title, String message,String posi, String nega, int resId){
        DeleteDialog instance = new DeleteDialog();

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            tListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implements DialogListener");
        }
    }
    private DialogListener tListener = null;

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

        View view1 = inflater.inflate(R.layout.delete_dialog,null);

        //ダイアログのタイトルを取得
        title_dltdl = (TextView)view1.findViewById(R.id.title_dltdl);
        title_dltdl.setText(title);
        //ダイアログのメッセージを取得
        message_dltdl = (TextView)view1.findViewById(R.id.message_dltdl);
        message_dltdl.setText(message);

        //Positiveボタンを実装
        Button pb = (Button) view1.findViewById(R.id.positive_button_dltdl);
        pb.setText(posi);
        pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tListener.onPositiveButtonClick(DeleteDialog.this);
            }
        });

        //Negativeボタンを実装
        Button nb = (Button) view1.findViewById(R.id.close_button_dltdl);
        nb.setText(nega);
        nb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tListener.onNegativeButtonClick(DeleteDialog.this);
            }
        });
        //ダイアログの背景とか消す処理
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);         //タイトル非表示
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);      //フルスクリーン
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));         //背景を透明にする

        //Viewをセットする
        dialog.setContentView(view1);

        return dialog;
    }
}
