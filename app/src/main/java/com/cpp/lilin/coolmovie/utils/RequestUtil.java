package com.cpp.lilin.coolmovie.utils;

import com.cpp.lilin.coolmovie.constants.MovieApi;

/**
 * Created by lilin on 2016/7/9.
 */
public class RequestUtil {

    private RequestUtil() {
    }

    /**
     * 请求当前流行的电影
     *
     * @return
     */
    public static String getPopularMovies() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MovieApi.SERVICE_URL)
                .append("/3/movie/popular?")
                .append("api_key=")
                .append(MovieApi.API_KEY);
        return stringBuilder.toString();
    }

    /**
     * 请求当前的流行电影
     *
     * @param page 流行电影的页数
     * @return
     */
    public static String getPopularMovies(final int page) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MovieApi.SERVICE_URL)
                .append("/3/movie/popular?")
                .append("api_key=")
                .append(MovieApi.API_KEY)
                .append("&")
                .append("page=" + page);
        return stringBuilder.toString();
    }

    /**
     * 请求当前评分最高的电影
     *
     * @return
     */
    public static String getTopMovies() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MovieApi.SERVICE_URL)
                .append("/3/movie/top_rated?")
                .append("api_key=")
                .append(MovieApi.API_KEY);
        return stringBuilder.toString();
    }

    /**
     * 请求当前评分最高的电影
     *
     * @param page
     * @return
     */
    public static String getTopMovies(final int page) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MovieApi.SERVICE_URL)
                .append("/3/movie/top_rated?")
                .append("api_key=")
                .append(MovieApi.API_KEY)
                .append("&")
                .append("page=" + page);
        return stringBuilder.toString();
    }


    /**
     * 请求一张图片资源
     *
     * @param imageName
     * @return
     */
    public static String getImageUrl(String imageName) {
        String request = MovieApi.SERVICE_IMAGE_URL + imageName;
        return request;
    }

    /**
     * 请求电影的视频
     *
     * @param movieID
     * @return
     */
    public static String getVideos(final String movieID) {
        String request = MovieApi.SERVICE_URL + "/3/movie/" + movieID + "/videos?api_key=" + MovieApi.API_KEY;
        return request;
    }

    /**
     * 请求电影的评论
     *
     * @param movieID
     * @return
     */
    public static String getReviews(final String movieID) {
        String request = MovieApi.SERVICE_URL + "/3/movie/" + movieID + "/reviews?api_key=" + MovieApi.API_KEY;
        return request;
    }
}
