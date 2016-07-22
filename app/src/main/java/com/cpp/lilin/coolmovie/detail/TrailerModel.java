package com.cpp.lilin.coolmovie.detail;

import java.util.List;

/**
 * Created by lilin on 2016/7/22.
 */
public class TrailerModel {

    private String id;

    private List<Result> results;

    public static class Result {
        private String id;
        private String iso_639_1;
        private String iso_3166_1;
        private String key;
        private String name;
        private String site;
        private String size;
        private String type;

        public String getId() {
            return id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getSize() {
            return size;
        }

        public String getSite() {
            return site;
        }

        public String getKey() {
            return key;
        }
    }

    public String getId() {
        return id;
    }

    public List<Result> getResults() {
        return results;
    }
}
