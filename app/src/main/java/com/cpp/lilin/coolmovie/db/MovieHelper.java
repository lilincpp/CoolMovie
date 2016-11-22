package com.cpp.lilin.coolmovie.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.cpp.lilin.coolmovie.home.MovieModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilin on 2016/11/22.
 */

public class MovieHelper {

    private MovieHelper() {

    }

    public static boolean isFavorited(Context context, String movieId) {
        boolean ok = false;

        List<MovieModel.Result> results = getAll(context);

        for (MovieModel.Result result : results) {
            if (TextUtils.equals(result.getMovieId(), movieId)) {
                ok = true;
                break;
            }
        }
        return ok;
    }

    public static List<MovieModel.Result> getAll(Context context) {
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getInstance(context).getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseHelper.TABLE_MOVIE_NAME;

        List<MovieModel.Result> results = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                MovieModel.Result result = new MovieModel.Result();
                result
                        .setPoster_path(cursor.getString(cursor.getColumnIndex("poster_path")))
                        .setAdult(cursor.getString(cursor.getColumnIndex("adult")))
                        .setOverview(cursor.getString(cursor.getColumnIndex("overview")))
                        .setRelease_date(cursor.getString(cursor.getColumnIndex("release_date")))
                        .setMovieId(cursor.getString(cursor.getColumnIndex("movie_id")))
                        .setOriginal_title(cursor.getString(cursor.getColumnIndex("original_title")))
                        .setOriginal_language(cursor.getString(cursor.getColumnIndex("original_language")))
                        .setTitle(cursor.getString(cursor.getColumnIndex("title")))
                        .setBackdrop_path(cursor.getString(cursor.getColumnIndex("backdrop_path")))
                        .setPopularity(cursor.getString(cursor.getColumnIndex("popularity")))
                        .setVote_count(cursor.getString(cursor.getColumnIndex("vote_count")))
                        .setVideo(cursor.getString(cursor.getColumnIndex("video")))
                        .setVote_average(cursor.getString(cursor.getColumnIndex("vote_average")));
                results.add(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return results;
    }

    public static void add(Context context, MovieModel.Result movie) {
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getInstance(context).getWritableDatabase();
        String sql = "INSERT INTO " + DatabaseHelper.TABLE_MOVIE_NAME
                + " (poster_path,adult,overview,release_date,movie_id,original_title,original_language,title,backdrop_path,popularity,vote_count,video,vote_average)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        sqLiteDatabase.execSQL(sql,
                new Object[]{
                        movie.getPoster_path(),
                        movie.getAdult(),
                        movie.getOverview(),
                        movie.getRelease_date(),
                        movie.getMovieId(),
                        movie.getOriginal_title(),
                        movie.getOriginal_language(),
                        movie.getTitle(),
                        movie.getBackdrop_path(),
                        movie.getPopularity(),
                        movie.getVote_count(),
                        movie.getVideo(),
                        movie.getVote_average()});
        sqLiteDatabase.close();
    }

    public static void delete(Context context, MovieModel.Result movie) {
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getInstance(context).getWritableDatabase();
        String sql = "DELETE FROM " + DatabaseHelper.TABLE_MOVIE_NAME + " WHERE movie_id=?";
        sqLiteDatabase.execSQL(sql, new Object[]{movie.getMovieId()});
        sqLiteDatabase.close();
    }
}
