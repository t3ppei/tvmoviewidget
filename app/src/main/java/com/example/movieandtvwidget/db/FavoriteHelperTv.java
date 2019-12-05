package com.example.movieandtvwidget.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.movieandtvwidget.db.DatabaseContract.TvFavoriteColumns.TABLE_TV;

public class FavoriteHelperTv {
    private static final String DATABASE_TABLE = TABLE_TV;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelperTv INSTANCE;

    private static SQLiteDatabase database;

    private FavoriteHelperTv(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelperTv getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelperTv(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }


    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }
    

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

}
