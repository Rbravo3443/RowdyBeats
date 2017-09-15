package com.rowdybeats.rowdybeats.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rowdybeats.rowdybeats.R;
import java.util.ArrayList;

/**
 * Created by tefnick on 8/9/17.
 */

public class NewsListAdapter extends RecyclerView.Adapter {
    private ArrayList list;
    private LayoutInflater layoutInflater;
    private Context mContext;

    /* Constructor */
    public NewsListAdapter(){
        this.list = list;
        layoutInflater = LayoutInflater.from(mContext);
        mContext = mContext;
    }

    public int getCount() {
        return list.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
