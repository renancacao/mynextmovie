package com.rcacao.mynextmovie.utils;

import android.os.AsyncTask;
import android.util.Log;
import com.rcacao.mynextmovie.interfaces.AsyncTaskReviewsDelegate;
import com.rcacao.mynextmovie.models.Review;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ReviewsService extends AsyncTask<URL, Void, ArrayList<Review>> {

    private final String TAG = getClass().getName();

    private AsyncTaskReviewsDelegate delegate;

    public ReviewsService(AsyncTaskReviewsDelegate responder){
        this.delegate = responder;
    }

    @Override
    protected  ArrayList<Review> doInBackground(URL... param) {

        URL searchUrl = param[0];
        String jsonReviews;
        try {
            jsonReviews = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            Log.w(TAG,"Erro ao requirir reviews.", e);
            jsonReviews = "";
        }

        if (jsonReviews != null && !jsonReviews.isEmpty()) {
            return new ReviewsProcessor().getReviews(jsonReviews);
        }
        else
        {
            return null;
        }

    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        super.onPostExecute(reviews);
        if (delegate != null){
            delegate.processFinishReviews(reviews);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (delegate != null){
            delegate.processStartReviews();
        }
    }




}
