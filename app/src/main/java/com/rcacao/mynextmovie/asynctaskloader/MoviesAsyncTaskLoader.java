package com.rcacao.mynextmovie.asynctaskloader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rcacao.mynextmovie.data.MovieContract;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.utils.MoviesProcessor;
import com.rcacao.mynextmovie.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MoviesAsyncTaskLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<Filme>> {


    private final String TAG = getClass().getName();
    private Bundle args;

    public static final String URL_ARG = "url";
    public static final String GET_FAVS = "getfavs" ;

    private ArrayList<Filme> mFilmes;

    public MoviesAsyncTaskLoader(Context context, Bundle args){
        super(context);
        this.args=args;
    }

    @Override
    protected void onStartLoading() {

        if (mFilmes != null) {
            deliverResult(mFilmes);
        } else {
            forceLoad();
        }

    }

    @Override
    public ArrayList<Filme> loadInBackground() {

        if (args==null){
            return null;
        }

        //verifico se devo carregar do SQLite ou da api
        boolean getFavs = args.getBoolean(GET_FAVS);
        if (getFavs){
            return loadFromDataBase();
        }
        else
        {
            return loadFromAPI();
        }

    }

    private ArrayList<Filme> loadFromAPI(){

        String argUrl = args.getString(URL_ARG);
        if (argUrl == null || argUrl.isEmpty()){
            return null;
        }

        try {

            URL searchUrl = new URL(argUrl);
            String jsonMovies;
            jsonMovies = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            if (jsonMovies != null && !jsonMovies.isEmpty()) {
                return new MoviesProcessor().getMovies(jsonMovies);
            }
            else
            {
                return null;
            }

        } catch (IOException e) {
            Log.w(TAG,"Erro ao requirir filmes", e);
            return null;
        }
    }

    private ArrayList<Filme> loadFromDataBase(){

        Cursor resultCursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null,  null);
        if (resultCursor==null){
            return null;
        }

        ArrayList<Filme> filmes = new ArrayList<>();

        while (resultCursor.moveToNext()){

           Filme f = new Filme();
           f.setId(resultCursor.getInt(0));
           f.setPoster(resultCursor.getString(1));
           f.setTitulo(resultCursor.getString(2));
           f.setSinopse(resultCursor.getString(3) );
           f.setAvaliacao(resultCursor.getDouble(4));
           f.setLancamento(resultCursor.getString(5));

           filmes.add(f);

       }

       resultCursor.close();

       return filmes;

    }

    @Override
    public void deliverResult(@Nullable ArrayList<Filme> mFilmes) {
        this.mFilmes = mFilmes;
        super.deliverResult(mFilmes);
    }


}
