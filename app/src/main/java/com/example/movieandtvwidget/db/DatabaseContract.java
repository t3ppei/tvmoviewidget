package com.example.movieandtvwidget.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static final class MovieFavoriteColumns implements BaseColumns {
        public static final String TABLE_NAME = "tablefavorite";
        public static final String NAME = "name";
        public static final String POPULAR = "popular";
        public static final String DESCRIPTION = "description";
        public static final String PHOTO = "photo";
    }

    public static final class TvFavoriteColumns implements BaseColumns {
        public static final String TABLE_TV = "tablefavoritetv";
        public static final String NAME = "name";
        public static final String POPULAR = "popular";
        public static final String DESCRIPTION = "description";
        public static final String PHOTO = "photo";
    }

}
