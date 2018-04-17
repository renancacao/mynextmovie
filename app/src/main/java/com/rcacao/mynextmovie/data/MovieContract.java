package com.rcacao.mynextmovie.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.rcacao.mynextmovie";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

       public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();


        // table and column names
        public static final String TABLE_NAME = "movies";

         //automatic _ID
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_SINOPSE = "sinopese";
        public static final String COLUMN_AVALIACAO = "avaliacao";
        public static final String COLUMN_LANCAMENTO = "lancamento";


       }

}
