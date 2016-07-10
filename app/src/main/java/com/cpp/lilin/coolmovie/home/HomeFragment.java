package com.cpp.lilin.coolmovie.home;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cpp.lilin.coolmovie.R;
import com.cpp.lilin.coolmovie.detail.MovieDetailActivity;
import com.cpp.lilin.coolmovie.utils.RequestUtil;
import com.cpp.lilin.coolmovie.utils.SortUtil;
import com.cpp.lilin.coolmovie.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilin on 2016/7/9.
 */
public class HomeFragment extends Fragment implements MovieAdapter.LClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    public static HomeFragment getIntance() {
        return new HomeFragment();
    }

    private RecyclerView mRv;
    private MovieAdapter mMovieAdapter;
    private List<MovieModel.Result> mMovies;

    public enum SORT_METHOD {
        POPULAR, VOTE
    }

    private static final int MESSAGE_NOTIFY_DATA_SET_CHANGE = 0;
    private static final int MESSAGE_TOAST = 1;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NOTIFY_DATA_SET_CHANGE:
                    Log.e(TAG, "MESSAGE_NOTIFY_DATA_SET_CHANGE");
                    mMovieAdapter.notifyDataSetChanged();
                    break;
                case MESSAGE_TOAST:
                    Log.e(TAG, "MESSAGE_TOAST");
                    ToastUtil.show(getActivity(), msg.arg1);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMovies = new ArrayList<>();
        mMovieAdapter = new MovieAdapter(getActivity(), mMovies);
        mMovieAdapter.setClickListener(this);
        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(8);
        mRv = (RecyclerView) view.findViewById(R.id.rv_movies);
        mRv.addItemDecoration(spaceItemDecoration);
        mRv.setLayoutManager(new GridLayoutManager(mRv.getContext(), 2));
        mRv.setAdapter(mMovieAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        requestPopularMovies();
    }

    public void refresh() {
        mMovieAdapter.clear();
        requestPopularMovies();
    }

    public void sort(SORT_METHOD sort) {
        SortUtil.sort(mMovies, sort);
        mMovieAdapter.update(mMovies);
    }

    public synchronized void requestPopularMovies() {
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, RequestUtil.getPopularMovies(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                Gson gson = new Gson();
                MovieModel movieModel = gson.fromJson(response, MovieModel.class);
                mMovies = movieModel.getResults();
                mMovieAdapter.update(mMovies);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHandler.obtainMessage(MESSAGE_TOAST, R.string.error_request, -1, null).sendToTarget();
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onClick(int position, View v, MovieModel.Result movie) {
        ImageView imageView = (ImageView) v.findViewById(R.id.item_iv_poster);
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, "share_poster").toBundle());
    }
}
