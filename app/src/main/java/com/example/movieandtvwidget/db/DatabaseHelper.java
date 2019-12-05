package com.example.movieandtvwidget.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static com.example.movieandtvwidget.db.DatabaseContract.MovieFavoriteColumns.TABLE_NAME;
import static com.example.movieandtvwidget.db.DatabaseContract.TvFavoriteColumns.TABLE_TV;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbfavorite";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FAVORITE = String.format("CREATE TABLE %s"
                    + "(%s INTEGER PRIMARY KEY,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL)",
            TABLE_NAME,
            DatabaseContract.MovieFavoriteColumns._ID,
            DatabaseContract.MovieFavoriteColumns.NAME,
            DatabaseContract.MovieFavoriteColumns.POPULAR,
            DatabaseContract.MovieFavoriteColumns.DESCRIPTION,
            DatabaseContract.MovieFavoriteColumns.PHOTO
    );

    private static final String SQL_CREATE_TABLE_FAVORITETV = String.format("CREATE TABLE %s"
                    + "(%s INTEGER PRIMARY KEY,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL)",
            TABLE_TV,
            DatabaseContract.TvFavoriteColumns._ID,
            DatabaseContract.TvFavoriteColumns.NAME,
            DatabaseContract.TvFavoriteColumns.POPULAR,
            DatabaseContract.TvFavoriteColumns.DESCRIPTION,
            DatabaseContract.TvFavoriteColumns.PHOTO
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
        db.execSQL(SQL_CREATE_TABLE_FAVORITETV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int onlVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TV);
        onCreate(db);
    }


}
