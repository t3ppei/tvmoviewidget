package com.example.movieandtvwidget.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.movieandtvwidget";
    private static final String SCHEME = "content";

    public static final class MovieFavoriteColumns implements BaseColumns {
        public static final String TABLE_NAME = "tablefavorite";
        public static final String NAME = "name";
        public static final String POPULAR = "popular";
        public static final String DESCRIPTION = "description";
        public static final String PHOTO = "photo";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static final class TvFavoriteColumns implements BaseColumns {
        public static final String TABLE_TV = "tablefavoritetv";
        public static final String NAME = "name";
        public static final String POPULAR = "popular";
        public static final String DESCRIPTION = "description";
        public static final String PHOTO = "photo";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_TV)
                .build();
    }

}
