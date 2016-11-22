package com.cpp.lilin.coolmovie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lilin on 2016/11/22.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "LCoolMovieDatabase.db";
    private static int DATABASE_VERSION = 1;

    public static String TABLE_MOVIE_NAME = "table_movie";

    private static String TABLE_CREATE = "CREATE TABLE " + TABLE_MOVIE_NAME + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "poster_path TEXT,"
            + "adult TEXT,"
            + "overview TEXT,"
            + "release_date TEXT,"
            + "movie_id TEXT,"
            + "original_title TEXT,"
            + "original_language TEXT,"
            + "title TEXT,"
            + "backdrop_path TEXT,"
            + "popularity TEXT,"
            + "vote_count TEXT,"
            + "video TEXT,"
            + "vote_average TEXT"
            + ")";

    private static volatile DatabaseHelper mDatabaseHelper = null;

    public synchronized static DatabaseHelper getInstance(Context context) {
        DatabaseHelper databaseHelper = mDatabaseHelper;
        if (databaseHelper == null) {
            synchronized (DatabaseHelper.class) {
                databaseHelper = mDatabaseHelper;
                if (databaseHelper == null) {
                    databaseHelper = new DatabaseHelper(context);
                    mDatabaseHelper = databaseHelper;
                }
            }
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
