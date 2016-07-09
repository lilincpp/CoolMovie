package com.cpp.lilin.coolmovie.utils;

import com.cpp.lilin.coolmovie.home.HomeFragment;
import com.cpp.lilin.coolmovie.home.MovieModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lilin on 2016/7/9.
 */
public class SortUtil {

    static class PopularSort implements Comparator<MovieModel.Result> {
        @Override
        public int compare(MovieModel.Result lhs, MovieModel.Result rhs) {
            final float l = Float.valueOf(lhs.getPopularity());
            final float r = Float.valueOf(rhs.getPopularity());
            if (l > r) {
                return -1;
            } else if (l == r) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    static class VoteSort implements Comparator<MovieModel.Result> {
        @Override
        public int compare(MovieModel.Result lhs, MovieModel.Result rhs) {
            final float l = Float.valueOf(lhs.getVote_average());
            final float r = Float.valueOf(rhs.getVote_average());
            if (l > r) {
                return -1;
            } else if (l == r) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public static void sort(List<MovieModel.Result> movies, HomeFragment.SORT_METHOD sort_method) {
        switch (sort_method) {
            case POPULAR:
                Comparator comparatorPopular = new PopularSort();
                Collections.sort(movies, comparatorPopular);
                break;
            case VOTE:
                Comparator comparatorVote = new VoteSort();
                Collections.sort(movies, comparatorVote);
                break;
        }
    }
}
