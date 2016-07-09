package com.cpp.lilin.coolmovie.detail;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpp.lilin.coolmovie.R;
import com.cpp.lilin.coolmovie.home.MovieModel;
import com.cpp.lilin.coolmovie.utils.RequestUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lilin on 2016/7/9.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private ImageView mMoviePoster, mMovieBackground;
    private TextView mTvMovieTitle, mTvMovieReleaseData, mTvMovieVote, mTvMovieOverView;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mDisplayImageOptions;
    private MovieModel.Result mMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
//        getWindow()
//                .getDecorView()
//                .setSystemUiVisibility(
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_detail);

        init();
        initView();
        loadImage();

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

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(" ");

        mMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        mMovieBackground = (ImageView) findViewById(R.id.movie_background);

        mTvMovieTitle = (TextView) findViewById(R.id.movie_title);
        mTvMovieReleaseData = (TextView) findViewById(R.id.movie_release_data);
        mTvMovieVote = (TextView) findViewById(R.id.movie_vote);
        mTvMovieOverView = (TextView) findViewById(R.id.movie_overview);

        final String title = getString(R.string.movie_title) + mMovie.getTitle();
        final String vote = getString(R.string.movie_vote) + mMovie.getVote_average();
        final String releaseData = getString(R.string.movie_release_data) + mMovie.getRelease_date();
        final String overview = getString(R.string.movie_overview) + "\n" + mMovie.getOverview();

        mTvMovieTitle.setText(title);
        mTvMovieReleaseData.setText(releaseData);
        mTvMovieVote.setText(vote);
        mTvMovieOverView.setText(overview);

    }

    private void loadImage() {
        //加载背景
        final String fileName = mMovie.getBackdrop_path().substring(1);
        final String imageUrl = RequestUtil.getImageUrl(fileName);
        mImageLoader.displayImage(imageUrl, mMovieBackground, mDisplayImageOptions);

        //加载海报
        final String posterFileName = mMovie.getPoster_path().substring(1);
        final String posterImageUrl = RequestUtil.getImageUrl(posterFileName);
        mImageLoader.displayImage(posterImageUrl, mMoviePoster, mDisplayImageOptions);
    }

}
