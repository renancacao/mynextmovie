package com.rcacao.mynextmovie.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.interfaces.AsyncTaskDelegate;
import com.rcacao.mynextmovie.models.Filme;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieService extends AsyncTask<URL, Void, ArrayList<Filme>> {

    private final String TAG = getClass().getName();

    private AsyncTaskDelegate delegate = null;
    private Context context = null;


    public MovieService(AsyncTaskDelegate responder,Context context){
        this.delegate = responder;
        this.context = context;
    }

    @Override
    protected  ArrayList<Filme> doInBackground(URL... param) {

        URL searchUrl = param[0];
        String jsonMovies;
        try {
            jsonMovies = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            Log.w(TAG,context.getString(R.string.log_erro_requirir_filmes), e);
            jsonMovies = "";
        }

        if (jsonMovies != null && !jsonMovies.isEmpty()) {
            return new MovieProcessor(context).getMovies(jsonMovies);
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
            delegate.processFinish(filmes);
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
