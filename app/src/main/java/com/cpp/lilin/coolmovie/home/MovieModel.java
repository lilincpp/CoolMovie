package com.cpp.lilin.coolmovie.home;

import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lilin on 2016/7/9.
 */
public class MovieModel {

    private String page;
    private List<Result> results;
    private String total_results;
    private String total_pages;


    @Table(name = "MovieModel_Result")
    public static class Result extends Model implements Serializable {

        /**
         * 查询所有收藏的电影
         *
         * @return
         */
        public static List<MovieModel.Result> getAll() {
            return new Select().from(MovieModel.Result.class).execute();
        }

        /**
         * 查询某电影是否收藏
         *
         * @param movieId
         * @return
         */
        public static boolean isFavorited(String movieId) {
            boolean ok = false;

            List<MovieModel.Result> results = getAll();

            for (MovieModel.Result result : results) {
                if (TextUtils.equals(result.getMovieId(), movieId)) {
                    ok = true;
                    break;
                }
            }
            return ok;
        }


        @Column(name = "poster_path")
        private String poster_path;

        @Column(name = "adult")
        private String adult;

        @Column(name = "overview")
        private String overview;

        @Column(name = "release_date")
        private String release_date;

        private String[] genre_ids;

        @Column(name = "movie_id")
        private String id;

        @Column(name = "original_title")
        private String original_title;

        @Column(name = "original_language")
        private String original_language;

        @Column(name = "title")
        private String title;

        @Column(name = "backdrop_path")
        private String backdrop_path;

        @Column(name = "popularity")
        private String popularity;

        @Column(name = "vote_count")
        private String vote_count;

        @Column(name = "video")
        private String video;

        @Column(name = "vote_average")
        private String vote_average;

        public String getPoster_path() {
            return poster_path;
        }

        public String getAdult() {
            return adult;
        }

        public String getRelease_date() {
            return release_date;
        }

        public String getPopularity() {
            return popularity;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public String[] getGenre_ids() {
            return genre_ids;
        }

        public String getMovieId() {
            return id;
        }

        public String getOverview() {
            return overview;
        }

        public String getTitle() {
            return title;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public String getVote_count() {
            return vote_count;
        }

        public String getVideo() {
            return video;
        }

        public String getVote_average() {
            return vote_average;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "poster_path='" + poster_path + '\'' +
                    ", adult='" + adult + '\'' +
                    ", overview='" + overview + '\'' +
                    ", release_date='" + release_date + '\'' +
                    ", genre_ids=" + Arrays.toString(genre_ids) +
                    ", id='" + id + '\'' +
                    ", original_title='" + original_title + '\'' +
                    ", original_language='" + original_language + '\'' +
                    ", title='" + title + '\'' +
                    ", backdrop_path='" + backdrop_path + '\'' +
                    ", popularity='" + popularity + '\'' +
                    ", vote_count='" + vote_count + '\'' +
                    ", video='" + video + '\'' +
                    ", vote_average='" + vote_average + '\'' +
                    '}';
        }
    }

    public String getPage() {
        return page;
    }

    public List<Result> getResults() {
        return results;
    }

    public String getTotal_results() {
        return total_results;
    }

    public String getTotal_pages() {
        return total_pages;
    }
}
