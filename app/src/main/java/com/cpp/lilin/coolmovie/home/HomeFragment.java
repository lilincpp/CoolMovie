package com.cpp.lilin.coolmovie.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cpp.lilin.coolmovie.R;
import com.cpp.lilin.coolmovie.detail.MovieDetailActivity;
import com.cpp.lilin.coolmovie.favorite.FavoriteActivity;
import com.cpp.lilin.coolmovie.utils.RequestUtil;
import com.cpp.lilin.coolmovie.utils.SortUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilin on 2016/7/9.
 */
public class HomeFragment extends Fragment implements MovieAdapter.LClickListener {

    public static final String TYPE_KEY = "type_key";
    public static final String SORT_KEY = "sort_key";
    /**
     * 默认页面-请求网络数据及自动加载
     */
    public static final boolean TYPE_DEFAULT = true;
    /**
     * 收藏页面-请求数据库数据
     */
    public static final boolean TYPE_FAVORITE = false;

    private static final String TAG = HomeFragment.class.getSimpleName();

    public static HomeFragment getIntance() {
        return new HomeFragment();
    }

    private RecyclerView mRv;
    private MovieAdapter mMovieAdapter;
    private List<MovieModel.Result> mMovies;
    private ProgressBar mLoading;
    /**
     * 流行电影当前页数
     */
    private int mCurrentPage = 1;
    /**
     * 流行电影API最大的页数
     */
    private int mMaxPage = 1;
    /**
     * 是否在加载更多
     */
    private boolean mIsLoading = false;

    private boolean mType = TYPE_DEFAULT;

    private SORT_METHOD mSort = SORT_METHOD.POPULAR;

    public enum SORT_METHOD implements Serializable {
        POPULAR, VOTE
    }

    private static final int MESSAGE_LOADING_GONE = 0;
    private static final int MESSAGE_TOAST = 1;
    private static final int MESSAGE_LOAD_MORE_SUCCESS = 2;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_LOADING_GONE:
                    mLoading.setVisibility(View.GONE);
                    break;
                case MESSAGE_TOAST:
                    Snackbar.make(mRv, msg.arg1, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.message_confirm, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();
                    break;
                case MESSAGE_LOAD_MORE_SUCCESS:
                    mMovieAdapter.update(mMovies);
                    mCurrentPage++;
                    mIsLoading = false;
                    break;
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TYPE_KEY, mType);
        outState.putSerializable(SORT_KEY, mSort);
        super.onSaveInstanceState(outState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && savedInstanceState == null) {
            mType = getArguments().getBoolean(TYPE_KEY);
        }

        if (savedInstanceState != null) {
            mType = savedInstanceState.getBoolean(TYPE_KEY);
            mSort = (SORT_METHOD) savedInstanceState.getSerializable(SORT_KEY);
        }

        mLoading = (ProgressBar) view.findViewById(R.id.loading);
        mMovies = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(getActivity(), mMovies, mType);
        mMovieAdapter.setClickListener(this);
        mRv = (RecyclerView) view.findViewById(R.id.rv_movies);
        mGridLayoutManager = new GridLayoutManager(mRv.getContext(), 2);
        mRv.setLayoutManager(mGridLayoutManager);
        mRv.setAdapter(mMovieAdapter);

        mRv.addOnScrollListener(mOnScrollListener);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mType) {
            if (mMovies.size() == 0) {
                requestMovies();
            }
        } else {
            requestFavoriteMovies();
        }
    }

    private GridLayoutManager mGridLayoutManager;


    /**
     * 当用户滑到最后时，自动加载后一页内容
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastitem = mGridLayoutManager.findLastVisibleItemPosition();
            if (dy > 0 && lastitem == mMovieAdapter.getItemCount() - 1 && !mIsLoading && mType) {
                mIsLoading = true;
                requestMovies(mCurrentPage + 1);
            }
        }
    };

    /**
     * 刷新页面，将会导致数据清空
     */
    public void refresh() {
        if (mType) {
            mMovieAdapter.clear();
            requestMovies();
        }
    }

    public void sort(SORT_METHOD sort) {
        if (mSort != sort) {
            mSort = sort;
            mMovies.clear();
            mMovieAdapter.update(mMovies);
            requestMovies();
        }
    }

    public void moveToTop() {
        if (mRv != null) {
            mRv.scrollToPosition(0);
        }
    }

    public synchronized void requestMovies() {
        String request;
        if (mSort == SORT_METHOD.POPULAR) {
            request = RequestUtil.getPopularMovies();
        } else {
            request = RequestUtil.getTopMovies();
        }

        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, request, new Response
                .Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                Gson gson = builder.create();
                MovieModel movieModel = gson.fromJson(response, MovieModel.class);
                mMaxPage = Integer.decode(movieModel.getTotal_pages());
                mMovies = movieModel.getResults();
                mMovieAdapter.update(mMovies);
                mHandler.obtainMessage(MESSAGE_LOADING_GONE).sendToTarget();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.obtainMessage(MESSAGE_TOAST, R.string.error_request, -1, null).sendToTarget();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    /**
     * 请求流行电影列表
     *
     * @param page 页数
     */
    public synchronized void requestMovies(final int page) {
        Log.e(TAG, "requestPopularMovies: page->" + page);
        if (page > mMaxPage) {
            return;
        }

        String request;

        if (mSort == SORT_METHOD.POPULAR) {
            request = RequestUtil.getPopularMovies(page);
        } else {
            request = RequestUtil.getTopMovies(page);
        }

        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, request, new Response
                .Listener<String>() {
            @Override
            public void onResponse(final String response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GsonBuilder builder = new GsonBuilder();
                        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
                        Gson gson = builder.create();
                        MovieModel movieModel = gson.fromJson(response, MovieModel.class);
                        mMovies.addAll(movieModel.getResults());
                        mHandler.obtainMessage(MESSAGE_LOAD_MORE_SUCCESS).sendToTarget();
                    }
                }).start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.obtainMessage(MESSAGE_TOAST, R.string.error_request, -1).sendToTarget();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    /**
     * 获取收藏的电影
     */
    public synchronized void requestFavoriteMovies() {
        mMovieAdapter.clear();
        List<MovieModel.Result> results = MovieModel.Result.getAll();
        mMovies = results;
        mMovieAdapter.update(mMovies);
        mHandler.obtainMessage(MESSAGE_LOADING_GONE).sendToTarget();
    }

    @Override
    public void onClick(int position, View v, MovieModel.Result movie) {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity.mIsTablet) {
                mainActivity.addDetailFragment(movie);
            } else {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        } else if (getActivity() instanceof FavoriteActivity) {

            FavoriteActivity favoriteActivity = (FavoriteActivity) getActivity();

            if (favoriteActivity.mIsTablet) {
                favoriteActivity.addDetailFragment(movie);
            } else {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        }
    }
}
