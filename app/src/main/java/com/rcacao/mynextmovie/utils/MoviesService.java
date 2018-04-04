package com.rcacao.mynextmovie.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.rcacao.mynextmovie.interfaces.AsyncTaskMoviesDelegate;
import com.rcacao.mynextmovie.models.Filme;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MoviesService extends AsyncTask<URL, Void, ArrayList<Filme>> {

    private final String TAG = getClass().getName();

    private AsyncTaskMoviesDelegate delegate;

    public MoviesService(AsyncTaskMoviesDelegate responder){
        this.delegate = responder;
    }

    @Override
    protected  ArrayList<Filme> doInBackground(URL... param) {

        URL searchUrl = param[0];
        String jsonMovies;
        try {
            jsonMovies = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            Log.w(TAG,"Erro ao requirir filmes", e);
            jsonMovies = "";
        }

        if (jsonMovies != null && !jsonMovies.isEmpty()) {
            return new MoviesProcessor().getMovies(jsonMovies);
        }
        else
        {
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<Filme> filmes) {
        super.onPostExecute(filmes);
        if (delegate != null){
            delegate.processFinishMovies(filmes);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (delegate != null){
            delegate.processStart();
        }
    }




}
