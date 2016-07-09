package com.cpp.lilin.coolmovie.home;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lilin on 2016/7/9.
 */
public class MovieModel {

    private String page;
    private List<Result> results;
    private String total_results;
    private String total_pages;


    public static class Result implements Serializable{
        private String poster_path;
        private String adult;
        private String overview;
        private String release_date;
        private String[] genre_ids;
        private String id;
        private String original_title;
        private String original_language;
        private String title;
        private String backdrop_path;
        private String popularity;
        private String vote_count;
        private String video;
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

        public String getId() {
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
