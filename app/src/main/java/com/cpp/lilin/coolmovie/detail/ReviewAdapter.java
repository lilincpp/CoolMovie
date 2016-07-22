package com.cpp.lilin.coolmovie.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cpp.lilin.coolmovie.R;

import java.util.List;

/**
 * Created by lilin on 2016/7/22.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> implements View.OnClickListener {

    private List<ReviewModel.Result> mResults;
    private RecyclerView mRecyclerView;

    public ReviewAdapter(List<ReviewModel.Result> results) {
        this.mResults = results;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        holder.tvReviewer.setText(mResults.get(position).getAuthor());
        holder.tvReviewContent.setText(mResults.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public synchronized void clear() {
        mResults.clear();
        notifyDataSetChanged();
    }

    public synchronized void update(List<ReviewModel.Result> results) {
        mResults = results;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewer, tvReviewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvReviewer = (TextView) itemView.findViewById(R.id.tv_reviewer_name);
            tvReviewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
