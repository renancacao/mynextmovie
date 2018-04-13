package com.rcacao.mynextmovie.asynctaskloader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rcacao.mynextmovie.models.Review;
import com.rcacao.mynextmovie.utils.NetworkUtils;
import com.rcacao.mynextmovie.utils.ReviewsProcessor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReviewsAsyncTaskLoader extends android.support.v4.content.AsyncTaskLoader<ArrayList<Review>> {

    private final String TAG = getClass().getName();
    private Bundle args;

    public static final String URL_ARG = "url";

    private ArrayList<Review> mReviews;

    public ReviewsAsyncTaskLoader(Context context, Bundle args){
        super(context);
        this.args=args;
    }

    @Override
    protected void onStartLoading() {
        if (mReviews != null) {
            deliverResult(mReviews);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public ArrayList<Review> loadInBackground() {

        if (args==null){
            return null;
        }

        String argUrl = args.getString(URL_ARG);
        if (argUrl == null || argUrl.isEmpty()){
            return null;
        }


        try {
            URL searchUrl = new URL(argUrl);
            String jsonReviews;
            jsonReviews = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            if (jsonReviews != null && !jsonReviews.isEmpty()) {
                return new ReviewsProcessor().getReviews(jsonReviews);
            }
            else
            {
                return null;
            }
        } catch (IOException e) {
            Log.w(TAG,"Erro ao requirir reviews.", e);
           return null;
        }



    }

    @Override
    public void deliverResult(@Nullable ArrayList<Review> mReviews) {
        this.mReviews = mReviews;
        super.deliverResult(mReviews);
    }
}
