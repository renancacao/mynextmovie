package com.rcacao.mynextmovie.utils;

import android.os.AsyncTask;
import android.util.Log;
import com.rcacao.mynextmovie.interfaces.AsyncTaskTrailersDelegate;
import com.rcacao.mynextmovie.models.Trailer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class TrailersService extends AsyncTask<URL, Void, ArrayList<Trailer>> {

    private final String TAG = getClass().getName();

    private AsyncTaskTrailersDelegate delegate;


    public TrailersService(AsyncTaskTrailersDelegate responder){
        this.delegate = responder;
    }

    @Override
    protected  ArrayList<Trailer> doInBackground(URL... param) {

        URL searchUrl = param[0];
        String jsonTrailers;
        try {
            jsonTrailers = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            Log.w( TAG, "Erro ao requirir trailers.", e);
            jsonTrailers = "";
        }

        if (jsonTrailers != null && !jsonTrailers.isEmpty()) {
            return new TrailersProcessor().getTrailers(jsonTrailers);
        }
        else
        {
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        super.onPostExecute(trailers);
        if (delegate != null){
            delegate.processFinishTrailers(trailers);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (delegate != null){
            delegate.processStartTrailers();
        }
    }




}
