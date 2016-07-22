package com.cpp.lilin.coolmovie.detail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cpp.lilin.coolmovie.R;
import com.cpp.lilin.coolmovie.home.MovieModel;
import com.cpp.lilin.coolmovie.utils.RequestUtil;
import com.cpp.lilin.coolmovie.utils.ToastUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lilin on 2016/7/9.
 */
public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private ImageView mMovieBackground;
    private TextView mTvMovieTitle, mTvMovieReleaseData, mTvMovieVote, mTvMovieOverView, mTvNoReview, mTvNoTrailer;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private MovieModel.Result mMovie;
    private RecyclerView mRvVideo;
    private TrailerAdapter mTrailerAdapter;
    private LinearLayout mLinearLayout;


    private CollapsingToolbarLayoutState state;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_detail);

        init();
        initView();
        load();

    }

    private void init() {

        mMovie = (MovieModel.Result) getIntent().getSerializableExtra("movie");

        mImageLoader = ImageLoader.getInstance();
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mMovieBackground = (ImageView) findViewById(R.id.movie_background);
        mTvMovieTitle = (TextView) findViewById(R.id.movie_title);
        mTvMovieReleaseData = (TextView) findViewById(R.id.movie_release_data);
        mTvMovieVote = (TextView) findViewById(R.id.movie_vote);
        mTvMovieOverView = (TextView) findViewById(R.id.movie_overview);
        mTvNoReview = (TextView) findViewById(R.id.tv_no_review);
        mTvNoTrailer = (TextView) findViewById(R.id.tv_no_trailer);

        mRvVideo = (RecyclerView) findViewById(R.id.rv_videos);
        mTrailerAdapter = new TrailerAdapter(new ArrayList<TrailerModel.Result>());
        mRvVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        mRvVideo.setAdapter(mTrailerAdapter);

        mLinearLayout = (LinearLayout) findViewById(R.id.lay_review_content);


        final String title = mMovie.getTitle();
        final String vote = getString(R.string.movie_vote) + mMovie.getVote_average();
        final String releaseData = getString(R.string.movie_release_data) + mMovie.getRelease_date();
        final String overview = mMovie.getOverview();

        mTvMovieTitle.setText(title);
        mTvMovieReleaseData.setText(releaseData);
        mTvMovieVote.setText(vote);
        mTvMovieOverView.setText(overview);

    }

    private void load() {
        //加载背景
        final String fileName = mMovie.getBackdrop_path().substring(1);
        final String imageUrl = RequestUtil.getImageUrl(fileName);
        mImageLoader.displayImage(imageUrl, mMovieBackground, mDisplayImageOptions);
        //请求预告
        StringRequest videoRequest = new StringRequest(StringRequest.Method.GET, RequestUtil.getVideos(mMovie.getId()), new Response
                .Listener<String>() {


            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                TrailerModel trailerModel = gson.fromJson(response, TrailerModel.class);
                if (trailerModel.getResults().size() == 0) {
                    mHandler.obtainMessage(MESSAGE_NO_TRAILER).sendToTarget();
                } else {
                    if (mTrailerAdapter != null) {
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
        StringRequest reviewRequest = new StringRequest(StringRequest.Method.GET, RequestUtil.getReviews(mMovie.getId()), new Response
                .Listener<String>() {


            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ReviewModel reviewModel = gson.fromJson(response, ReviewModel.class);
                mHandler.obtainMessage(MESSAGE_ADD_REVIEW, reviewModel).sendToTarget();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.obtainMessage(MESSAGE_REQUEST_ERROR).sendToTarget();
            }
        });

        Volley.newRequestQueue(this).add(videoRequest);
        Volley.newRequestQueue(this).add(reviewRequest);
    }

    private static final int MESSAGE_REQUEST_ERROR = 1;
    private static final int MESSAGE_ADD_REVIEW = 2;
    private static final int MESSAGE_NO_TRAILER = 3;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REQUEST_ERROR:
                    ToastUtil.show(MovieDetailActivity.this, R.string.error_request);
                    break;
                case MESSAGE_ADD_REVIEW:
                    ReviewModel reviewModel = (ReviewModel) msg.obj;
                    if (reviewModel.getResults().size() == 0) {
                        addEmptyView();
                    } else {
                        for (ReviewModel.Result result : reviewModel.getResults()) {
                            addReview(result);
                        }
                    }
                    break;
                case MESSAGE_NO_TRAILER:
                    mRvVideo.setVisibility(View.GONE);
                    mTvNoTrailer.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private synchronized void addEmptyView() {
        mTvNoReview.setVisibility(View.VISIBLE);
    }

    private synchronized void addReview(ReviewModel.Result result) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_review, null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_reviewer_name);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_review_content);

        tvName.setText(result.getAuthor());
        tvContent.setText(result.getContent());

        mLinearLayout.addView(view);

        View lineView = LayoutInflater.from(this).inflate(R.layout.item_line, null);

        mLinearLayout.addView(lineView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
