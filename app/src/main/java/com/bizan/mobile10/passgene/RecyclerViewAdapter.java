package com.bizan.mobile10.passgene;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 2016/02/09.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;       //ヘッダータイプ
    private static final int TYPE_ITEM = 1;

    private String mNVTitles[];
    private int mIcons[];

    private String mNVStting;
    private String mUSEName;

    public RecyclerViewAdapter(String nvTITLES[], int nvICONS[], String nvSETTING, String useNAME) {
        mNVTitles = nvTITLES;
        mIcons = nvICONS;
        mNVStting = nvSETTING;
        mUSEName = useNAME;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid;

        TextView titles;
        ImageView icons;
        TextView setting;
        TextView usename;

        public ViewHolder(View itemView,int ViewType){
            super(itemView);
            if (ViewType == TYPE_ITEM){
                titles = (TextView) itemView.findViewById(R.id.plrowText);
                icons = (ImageView) itemView.findViewById(R.id.plrowIcon);
                Holderid = 1;
            }else {
                setting = (TextView) itemView.findViewById(R.id.plNVSetting);
                usename = (TextView) itemView.findViewById(R.id.plUseName);
                Holderid = 0;
            }
        }

    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.passlist_item_row,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);

            return vhItem;
        }else if (viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.passlist_header,parent,false);
            ViewHolder vhHedaer = new ViewHolder(v,viewType);
            
            return vhHedaer;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == 1){
            holder.titles.setText(mNVTitles[position - 1]);
            holder.icons.setImageResource(mIcons[position - 1]);
        }else {
            holder.setting.setText(mNVStting);
            holder.usename.setText(mUSEName);
        }
    }

    @Override
    public int getItemCount() {
        return mNVTitles.length+1;
    }

    @Override
    public int getItemViewType(int position){
        if (isPositionHeader(position)){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


}
