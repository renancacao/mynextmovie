package com.rcacao.mynextmovie.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.interfaces.AsyncTaskTrailersDelegate;
import com.rcacao.mynextmovie.models.Trailer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class TrailersService extends AsyncTask<URL, Void, ArrayList<Trailer>> {

    private final String TAG = getClass().getName();

    private AsyncTaskTrailersDelegate delegate = null;
    private Context context = null;


    public TrailersService(AsyncTaskTrailersDelegate responder, Context context){
        this.delegate = responder;
        this.context = context;
    }

    @Override
    protected  ArrayList<Trailer> doInBackground(URL... param) {

        URL searchUrl = param[0];
        String jsonTrailers;
        try {
            jsonTrailers = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            Log.w(TAG,context.getString(R.string.erro_requirir_trailers), e);
            jsonTrailers = "";
        }

        if (jsonTrailers != null && !jsonTrailers.isEmpty()) {
            return new TrailersProcessor(context).getTrailers(jsonTrailers);
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
            delegate.processFinish(trailers);
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
