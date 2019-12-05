package com.example.movieandtvwidget.db;

import android.database.Cursor;

import com.example.movieandtvwidget.favorite.FavoriteItem;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<FavoriteItem> mapCursorToArrayList(Cursor favCursor) {
        ArrayList<FavoriteItem> favList = new ArrayList<>();

        while (favCursor.moveToNext()) {
            int id = favCursor.getInt(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns._ID));
            String judul = favCursor.getString(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.NAME));
            String popular = favCursor.getString(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.POPULAR));
            String deskripsi = favCursor.getString(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.DESCRIPTION));
            String foto = favCursor.getString(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.PHOTO));
            favList.add(new FavoriteItem(id, judul, popular, deskripsi, foto));
        }
        return favList;
    }

    public static FavoriteItem mapCursorToObject(Cursor favCursor) {
        favCursor.moveToFirst();

        int id = favCursor.getInt(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns._ID));
        String judul = favCursor.getString(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.NAME));
        String popular = favCursor.getString(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.POPULAR));
        String deskripsi = favCursor.getString(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.DESCRIPTION));
        String foto = favCursor.getString(favCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavoriteColumns.PHOTO));

        return new FavoriteItem(id, judul, popular, deskripsi, foto);
    }

}
