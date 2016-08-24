package com.cpp.lilin.coolmovie.home;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cpp.lilin.coolmovie.R;
import com.cpp.lilin.coolmovie.detail.MovieDetailActivity;
import com.cpp.lilin.coolmovie.detail.ReviewModel;
import com.cpp.lilin.coolmovie.detail.TrailerAdapter;
import com.cpp.lilin.coolmovie.detail.TrailerModel;
import com.cpp.lilin.coolmovie.favorite.FavoriteActivity;
import com.cpp.lilin.coolmovie.utils.RequestUtil;
import com.cpp.lilin.coolmovie.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Created by lilin on 2016/8/22.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DetailFragment";

    private ImageView mMovieBackground;
    private TextView mTvMovieTitle, mTvMovieReleaseData, mTvMovieVote, mTvMovieOverView, mTvNoReview, mTvTrailerTitle;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private MovieModel.Result mMovie;
    private RecyclerView mRvVideo;
    private TrailerAdapter mTrailerAdapter;
    private LinearLayout mLayReviewContent;
    private NestedScrollView mNestedScrollView;

    private FloatingActionButton mFabFavorite;


    public static DetailFragment getInstance(MovieModel.Result result) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", result);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("movie", mMovie);
        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        if (savedInstanceState == null) {
            mMovie = (MovieModel.Result) getArguments().getSerializable("movie");
        } else {
            mMovie = (MovieModel.Result) savedInstanceState.getSerializable("movie");
        }
        mImageLoader = ImageLoader.getInstance();
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {

        mNestedScrollView = (NestedScrollView) view.findViewById(R.id.scrollView);

        mFabFavorite = (FloatingActionButton) view.findViewById(R.id.fab_favorite);
        mFabFavorite.setOnClickListener(this);


        mMovieBackground = (ImageView) view.findViewById(R.id.movie_background);
        mTvMovieTitle = (TextView) view.findViewById(R.id.movie_title);
        mTvMovieReleaseData = (TextView) view.findViewById(R.id.movie_release_data);
        mTvMovieVote = (TextView) view.findViewById(R.id.movie_vote);
        mTvMovieOverView = (TextView) view.findViewById(R.id.movie_overview);
        mTvNoReview = (TextView) view.findViewById(R.id.tv_no_review);
        mTvTrailerTitle = (TextView) view.findViewById(R.id.tv_trailer_title);

        mRvVideo = (RecyclerView) view.findViewById(R.id.rv_videos);
        mTrailerAdapter = new TrailerAdapter(new ArrayList<TrailerModel.Result>());
        mRvVideo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        mRvVideo.setAdapter(mTrailerAdapter);

        mLayReviewContent = (LinearLayout) view.findViewById(R.id.lay_review_content);
    }

    public void update(MovieModel.Result result) {
        mMovie = result;
        mNestedScrollView.fullScroll(View.FOCUS_UP);
        showMovie();
    }

    @Override
    public void onResume() {
        super.onResume();
        showMovie();
    }

    private void showMovie() {
        if (mMovie != null) {

            mFabFavorite.setTag(MovieModel.Result.isFavorited(mMovie.getMovieId()));
            changeFavoriteFabStatus((Boolean) mFabFavorite.getTag());

            final String title = mMovie.getTitle();
            final String vote = getString(R.string.movie_vote) + "\t" + mMovie.getVote_average();
            final String releaseData = getString(R.string.movie_release_data) + "\t" + mMovie.getRelease_date();
            final String overview = mMovie.getOverview();

            mTvMovieTitle.setText(title);
            mTvMovieReleaseData.setText(releaseData);
            mTvMovieVote.setText(vote);
            mTvMovieOverView.setText(overview);

            load();
        }
    }

    private void load() {
        //加载背景
        final String fileName = mMovie.getBackdrop_path().substring(1);
        final String imageUrl = RequestUtil.getImageUrl(fileName);
//        mImageLoader.displayImage(imageUrl, mMovieBackground, mDisplayImageOptions);
        mImageLoader.loadImage(imageUrl, mDisplayImageOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mMovieBackground.setImageResource(R.drawable.ic_default);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                int centerX = mMovieBackground.getWidth() / 2;
                int centerY = mMovieBackground.getHeight() / 2;
                int maxRadius = Math.max(mMovieBackground.getWidth(), mMovieBackground.getHeight());

                mMovieBackground.setImageBitmap(loadedImage);
                Animator animator = ViewAnimationUtils.createCircularReveal(mMovieBackground, centerX, centerY, 0, maxRadius);
                animator.setDuration(800);
                animator.start();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        //请求预告
        StringRequest videoRequest = new StringRequest(StringRequest.Method.GET, RequestUtil.getVideos(mMovie.getMovieId()), new Response
                .Listener<String>() {


            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                Gson gson = builder.create();
                TrailerModel trailerModel = gson.fromJson(response, TrailerModel.class);
                if (trailerModel.getResults().size() == 0) {
                    mHandler.obtainMessage(MESSAGE_NO_TRAILER).sendToTarget();
                } else {
                    if (mTrailerAdapter != null) {
                        mHandler.obtainMessage(MESSAGE_ADD_TRAILER).sendToTarget();
                        mTrailerAdapter.clear();
                        mTrailerAdapter.update(trailerModel.getResults());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.obtainMessage(MESSAGE_REQUEST_ERROR).sendToTarget();
            }
        });
        //请求评论
        StringRequest reviewRequest = new StringRequest(StringRequest.Method.GET, RequestUtil.getReviews(mMovie.getMovieId()), new Response
                .Listener<String>() {


            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                Gson gson = builder.create();
                ReviewModel reviewModel = gson.fromJson(response, ReviewModel.class);
                mHandler.obtainMessage(MESSAGE_ADD_REVIEW, reviewModel).sendToTarget();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.obtainMessage(MESSAGE_REQUEST_ERROR).sendToTarget();
            }
        });

        Volley.newRequestQueue(getContext()).add(videoRequest);
        Volley.newRequestQueue(getContext()).add(reviewRequest);
    }

    private synchronized void addEmptyView() {
        mTvNoReview.setText(R.string.detail_no_review);
        mTvNoReview.setVisibility(View.VISIBLE);
    }

    private synchronized void addReview(ReviewModel.Result result) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_review, null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_reviewer_name);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_review_content);

        tvName.setText(result.getAuthor());
        tvContent.setText(result.getContent());

        mLayReviewContent.addView(view);

        View lineView = LayoutInflater.from(getContext()).inflate(R.layout.item_line, null);
        mLayReviewContent.addView(lineView);

    }

    private static final int MESSAGE_REQUEST_ERROR = 1;
    private static final int MESSAGE_ADD_REVIEW = 2;
    private static final int MESSAGE_NO_TRAILER = 3;
    private static final int MESSAGE_ADD_TRAILER = 4;
    private static final int MESSAGE_TOAST = 5;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REQUEST_ERROR:
                    ToastUtil.show(getContext(), R.string.error_request);
                    break;
                case MESSAGE_ADD_REVIEW:
                    mLayReviewContent.removeAllViews();
                    ReviewModel reviewModel = (ReviewModel) msg.obj;
                    if (reviewModel.getResults().size() == 0) {
                        addEmptyView();
                    } else {
                        mTvNoReview.setVisibility(View.GONE);
                        for (ReviewModel.Result result : reviewModel.getResults()) {
                            addReview(result);
                        }
                    }
                    break;
                case MESSAGE_NO_TRAILER:
                    mTvTrailerTitle.setText(R.string.detail_trailer_and_empty);
                    mRvVideo.setVisibility(View.GONE);
                    break;
                case MESSAGE_ADD_TRAILER:
                    mRvVideo.setVisibility(View.VISIBLE);
                    mTvTrailerTitle.setText(R.string.detail_trailer);
                    break;
                case MESSAGE_TOAST:
                    Snackbar.make(mLayReviewContent, msg.arg1, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.message_confirm, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_favorite:
                boolean checked = !(boolean) mFabFavorite.getTag();
                if (checked) {
                    addMovieToFavorites();
                    mHandler.obtainMessage(MESSAGE_TOAST, R.string.favorite_saved, -1).sendToTarget();
                } else {
                    removeMovieFromFavorites();
                    mHandler.obtainMessage(MESSAGE_TOAST, R.string.favorite_remove, -1).sendToTarget();
                }
                mFabFavorite.setTag(checked);
                changeFavoriteFabStatus(checked);
                break;
        }
    }

    private synchronized void addMovieToFavorites() {
        if (!MovieModel.Result.isFavorited(mMovie.getMovieId())) {
            mMovie.save();
        }
    }

    private synchronized void removeMovieFromFavorites() {
        new Delete().from(MovieModel.Result.class).where("movie_id=?", mMovie.getMovieId()).execute();
        if (getActivity() instanceof FavoriteActivity) {
            FavoriteActivity favoriteActivity = (FavoriteActivity) getActivity();
            favoriteActivity.updateFavorite();
        }
    }

    private synchronized void changeFavoriteFabStatus(final boolean checked) {
        if (checked) {
            mFabFavorite.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent));
        } else {
            mFabFavorite.setColorFilter(Color.GRAY);
        }
    }

    @Override
    public void onDestroy() {
        boolean ok = (boolean) mFabFavorite.getTag();
        if (ok) {
            addMovieToFavorites();
        } else {
            removeMovieFromFavorites();
        }
        super.onDestroy();
    }
}
