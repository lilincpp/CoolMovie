package com.cpp.lilin.coolmovie.detail;

import java.util.List;

/**
 * Created by lilin on 2016/7/22.
 */
public class ReviewModel {

    private String id;
    private String page;
    private List<Result> results;
    private String total_pages;
    private String total_results;

    public static class Result {
        private String id;
        private String author;
        private String content;
        private String url;

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }
    }

    public String getId() {
        return id;
    }

    public String getPage() {
        return page;
    }

    public List<Result> getResults() {
        return results;
    }
}
