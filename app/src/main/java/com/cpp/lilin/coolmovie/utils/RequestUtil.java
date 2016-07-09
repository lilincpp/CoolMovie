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
     * 请求一张图片资源
     *
     * @param imageName
     * @return
     */
    public static String getImageUrl(String imageName) {
        String request = MovieApi.SERVICE_IMAGE_URL + imageName;
        return request;
    }
}
