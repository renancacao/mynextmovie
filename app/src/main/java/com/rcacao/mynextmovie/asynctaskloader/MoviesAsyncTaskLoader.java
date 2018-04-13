package com.rcacao.mynextmovie.asynctaskloader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

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

    @Override
    public void deliverResult(@Nullable ArrayList<Filme> mFilmes) {
        this.mFilmes = mFilmes;
        super.deliverResult(mFilmes);
    }


}
