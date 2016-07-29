package com.cpp.lilin.coolmovie.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpp.lilin.coolmovie.R;
import com.cpp.lilin.coolmovie.utils.RequestUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by lilin on 2016/7/9.
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private Context mContext;
    private List<MovieModel.Result> mMovies;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private LClickListener mLClickListener;
    private RecyclerView mRecyclerView;

    private boolean mAddFooter = true;

    public void update(List<MovieModel.Result> movies) {
        mMovies = movies;
        notifyDataSetChanged();
        notifyItemRemoved(getItemCount());
    }

    public void clear() {
        mMovies.clear();
        notifyDataSetChanged();
    }

    public interface LClickListener {
        void onClick(int position, View v, MovieModel.Result movie);
    }

    public void setClickListener(LClickListener clickListener) {
        mLClickListener = clickListener;
    }

    public MovieAdapter(Context context, List<MovieModel.Result> movies, boolean addFooter) {
        mContext = context;
        mMovies = movies;
        mImageLoader = ImageLoader.getInstance();
        mAddFooter = addFooter;

        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_download)
                .showImageOnFail(R.drawable.ic_error)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
            v.setOnClickListener(this);
            return new ItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(mContext).inflate(R.layout.foot, parent, false);
            return new FootViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final MovieModel.Result movie = mMovies.get(position);
            final String title = movie.getTitle();
            final String language = mContext.getString(R.string.movie_language) + ((TextUtils.equals(movie.getOriginal_language(), "en")) ?
                    "英语" : "中文");
            final String vote = mContext.getString(R.string.movie_vote) + movie.getVote_average();
            final String imageName = movie.getPoster_path().substring(1);
            final String imageUrl = RequestUtil.getImageUrl(imageName);

            itemViewHolder.movieTitle.setText(title);
            itemViewHolder.movieLanguage.setText(language);
            itemViewHolder.movieVote.setText(vote);

            mImageLoader.displayImage(imageUrl, itemViewHolder.moviePoster, mDisplayImageOptions);
        }
    }

    @Override
    public int getItemCount() {
        if (mAddFooter) {
            //添加额外的item-“加载更多”
            return mMovies.size() == 0 ? 0 : mMovies.size() + 1;
        } else {
            //无需添加额外的item
            return mMovies.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mAddFooter) {
            if (position + 1 == getItemCount()) {
                //如果当前position已经是最后一个时，显示“加载更多”的item
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        } else {
            return TYPE_ITEM;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle, movieLanguage, movieVote;
        CardView cardView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.item_iv_poster);
            movieTitle = (TextView) itemView.findViewById(R.id.item_tv_title);
            movieLanguage = (TextView) itemView.findViewById(R.id.item_tv_language);
            movieVote = (TextView) itemView.findViewById(R.id.item_tv_vote);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public void onClick(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        if (mLClickListener != null) {
            mLClickListener.onClick(position, v, mMovies.get(position));
        }
    }
}
