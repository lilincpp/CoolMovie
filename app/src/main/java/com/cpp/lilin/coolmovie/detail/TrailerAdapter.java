package com.cpp.lilin.coolmovie.detail;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpp.lilin.coolmovie.R;

import java.util.List;

/**
 * Created by lilin on 2016/7/22.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<TrailerModel.Result> mResults;

    public TrailerAdapter(List<TrailerModel.Result> results) {
        this.mResults = results;
    }

    public synchronized void update(List<TrailerModel.Result> results) {
        this.mResults = results;
        notifyDataSetChanged();
    }

    public synchronized void clear() {
        this.mResults.clear();
        notifyDataSetChanged();
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        holder.tvTailerName.setText(mResults.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTailerName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
        }
    }

    private static final String TAG = "TrailerAdapter";

    @Override
    public void onClick(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + mResults.get(position).getId()));
        mRecyclerView.getContext().startActivity(intent);
    }
}
