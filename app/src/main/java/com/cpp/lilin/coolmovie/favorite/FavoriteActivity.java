package com.cpp.lilin.coolmovie.favorite;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cpp.lilin.coolmovie.BaseActivity;
import com.cpp.lilin.coolmovie.R;
import com.cpp.lilin.coolmovie.home.HomeFragment;

/**
 * Created by lilin on 2016/7/28.
 */
public class FavoriteActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.favorite_title);
        setSupportActionBar(toolbar);

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
}
