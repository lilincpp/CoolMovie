package com.cpp.lilin.coolmovie.favorite;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cpp.lilin.coolmovie.BaseActivity;
import com.cpp.lilin.coolmovie.R;
import com.cpp.lilin.coolmovie.home.DetailFragment;
import com.cpp.lilin.coolmovie.home.HomeFragment;
import com.cpp.lilin.coolmovie.home.MovieModel;

/**
 * Created by lilin on 2016/7/28.
 */
public class FavoriteActivity extends BaseActivity implements View.OnClickListener {

    public boolean mIsTablet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.favorite_title);
        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(this);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (homeFragment == null) {
            homeFragment = HomeFragment.getIntance();
            Bundle bundle = new Bundle();
            bundle.putBoolean(HomeFragment.TYPE_KEY, HomeFragment.TYPE_FAVORITE);
            homeFragment.setArguments(bundle);
        }

        if (!homeFragment.isAdded()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, homeFragment).commit();
        }

        mIsTablet = (findViewById(R.id.container_detail) != null);
    }

    public void addDetailFragment(MovieModel.Result result) {
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.container_detail);

        if (detailFragment == null) {
            detailFragment = DetailFragment.getInstance(result);
        } else {
            detailFragment.update(result);
        }

        if (!detailFragment.isAdded()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_detail, detailFragment);
            fragmentTransaction.commit();
        }

    }

    public void updateFavorite() {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (homeFragment != null) {
            homeFragment.requestFavoriteMovies();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_refresh).setVisible(false);
        menu.findItem(R.id.menu_loved).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        switch (item.getItemId()) {
            case R.id.menu_sort_popular:
                if (homeFragment != null) {
                    homeFragment.sort(HomeFragment.SORT_METHOD.POPULAR);
                }
                break;
            case R.id.menu_sort_vote:
                homeFragment.sort(HomeFragment.SORT_METHOD.VOTE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final long DOUBLE_CLICK_TIME = 240;
    private final Handler mHandler = new Handler();
    private int mClickCount = 0;
    private final Runnable mCancelClick = new Runnable() {
        @Override
        public void run() {
            mClickCount = 0;
        }
    };

    @Override
    public void onClick(View v) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        switch (v.getId()) {
            case R.id.toolbar:
                mClickCount++;
                if (mClickCount == 2 && homeFragment != null && homeFragment.isVisible()) {
                    homeFragment.moveToTop();
                }
                mHandler.postDelayed(mCancelClick, DOUBLE_CLICK_TIME);
                break;
        }
    }
}
