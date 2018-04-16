package com.rcacao.mynextmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.rcacao.mynextmovie.data.MovieContract.MovieEntry;

public class DbHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mynextmovie.db";
    private static final int VERSION = 1;

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE "  + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID                + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITULO + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_SINOPSE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_AVALIACAO + " NUMERIC NOT NULL, " +
                MovieEntry.COLUMN_LANCAMENTO    + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
