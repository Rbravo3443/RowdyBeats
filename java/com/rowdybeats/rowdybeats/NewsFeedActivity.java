package com.rowdybeats.rowdybeats;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class NewsFeedActivity extends Activity {
    private RecyclerView rView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view);

        rView = (RecyclerView) findViewById(R.id.my_recycler_view);
        rView.setHasFixedSize(true);
        rView.setVisibility(View.GONE);

        //use linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rView.setLayoutManager(mLayoutManager);

//        //specify an adapter
//        mAdapter = new MyAdapter(mDataSet);
//        rView.setAdapter(mAdapter);

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView mTextview;
            public ViewHolder(TextView v){
                super(v);
                mTextview = v;
            }
        }

        /* constructor */
        public MyAdapter(String[] data) {
            mDataset = data;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_view, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            holder.mTextview.setText(mDataset[position]);
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
