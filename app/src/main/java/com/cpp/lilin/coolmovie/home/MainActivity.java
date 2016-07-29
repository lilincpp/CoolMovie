package com.cpp.lilin.coolmovie.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cpp.lilin.coolmovie.BaseActivity;
import com.cpp.lilin.coolmovie.R;
import com.cpp.lilin.coolmovie.favorite.FavoriteActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private enum FRAGMENTS {
        HOME_FRAGMENT
    }

    private FRAGMENTS mCurrentFragment = FRAGMENTS.HOME_FRAGMENT;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.home_title);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        mToolbar.setOnClickListener(this);


        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (homeFragment == null) {
            homeFragment = HomeFragment.getIntance();
        }

        if (!homeFragment.isAdded()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, homeFragment);
            fragmentTransaction.commit();
            mCurrentFragment = FRAGMENTS.HOME_FRAGMENT;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mCurrentFragment) {
            case HOME_FRAGMENT:
                menu.findItem(R.id.menu_refresh).setVisible(true);
                menu.findItem(R.id.menu_sort_popular).setVisible(true);
                menu.findItem(R.id.menu_sort_vote).setVisible(true);
                break;
            default:
                menu.findItem(R.id.menu_refresh).setVisible(false);
                menu.findItem(R.id.menu_sort_popular).setVisible(false);
                menu.findItem(R.id.menu_sort_vote).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                if (homeFragment != null) {
                    homeFragment.refresh();
                }
                return true;
            case R.id.menu_sort_popular:
                if (homeFragment != null) {
                    homeFragment.sort(HomeFragment.SORT_METHOD.POPULAR);
                }
                return true;
            case R.id.menu_sort_vote:
                if (homeFragment != null) {
                    homeFragment.sort(HomeFragment.SORT_METHOD.VOTE);
                }
                break;
            case R.id.menu_loved:
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
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
