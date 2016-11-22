package com.cpp.lilin.coolmovie.home;

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



    public static class Result  implements Serializable {


//        @Column
        private String poster_path;

//        @Column
        private String adult;

//        @Column
        private String overview;

//        @Column
        private String release_date;

        private String[] genre_ids;

//        @PrimaryKey
        private String id;

//        @Column
        private String original_title;

//        @Column
        private String original_language;

//        @Column
        private String title;

//        @Column
        private String backdrop_path;

//        @Column
        private String popularity;

//        @Column
        private String vote_count;

//        @Column
        private String video;

//        @Column
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

        public Result setPoster_path(String poster_path) {
            this.poster_path = poster_path;
            return this;
        }

        public Result setVote_average(String vote_average) {
            this.vote_average = vote_average;
            return this;
        }

        public Result setVideo(String video) {
            this.video = video;
            return this;
        }

        public Result setVote_count(String vote_count) {
            this.vote_count = vote_count;
            return this;
        }

        public Result setPopularity(String popularity) {
            this.popularity = popularity;
            return this;
        }

        public Result setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
            return this;
        }

        public Result setTitle(String title) {
            this.title = title;
            return this;
        }

        public Result setOriginal_language(String original_language) {
            this.original_language = original_language;
            return this;
        }

        public Result setOriginal_title(String original_title) {
            this.original_title = original_title;
            return this;
        }

        public Result setMovieId(String id) {
            this.id = id;
            return this;
        }

        public Result setGenre_ids(String[] genre_ids) {
            this.genre_ids = genre_ids;
            return this;
        }

        public Result setRelease_date(String release_date) {
            this.release_date = release_date;
            return this;
        }

        public Result setOverview(String overview) {
            this.overview = overview;
            return this;
        }

        public Result setAdult(String adult) {
            this.adult = adult;
            return this;
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
