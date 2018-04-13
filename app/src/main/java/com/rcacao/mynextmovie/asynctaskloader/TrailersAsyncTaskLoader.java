package com.rcacao.mynextmovie.asynctaskloader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rcacao.mynextmovie.models.Trailer;
import com.rcacao.mynextmovie.utils.NetworkUtils;
import com.rcacao.mynextmovie.utils.TrailersProcessor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class TrailersAsyncTaskLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<Trailer>> {

    private final String TAG = getClass().getName();
    private Bundle args;

    public static final String URL_ARG = "url";

    private ArrayList<Trailer> mTrailer;

    public TrailersAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        this.args=args;
    }

    @Override
    protected void onStartLoading() {
        if (mTrailer != null) {
            deliverResult(mTrailer);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public ArrayList<Trailer> loadInBackground() {

        if (args==null){
            return null;
        }

        String argUrl = args.getString(URL_ARG);
        if (argUrl == null || argUrl.isEmpty()){
            return null;
        }

        try {
            URL searchUrl = new URL(argUrl);
            String jsonTrailers;
            jsonTrailers = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            if (jsonTrailers != null && !jsonTrailers.isEmpty()) {
                return new TrailersProcessor().getTrailers(jsonTrailers);
            }
            else
            {
                return null;
            }
        } catch (IOException e) {
            Log.w( TAG, "Erro ao requirir trailers.", e);
            return null;
        }



    }

    @Override
    public void deliverResult(@Nullable ArrayList<Trailer> mTrailer) {
        this.mTrailer = mTrailer;
        super.deliverResult(mTrailer);
    }
}
