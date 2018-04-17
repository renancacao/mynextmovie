package com.rcacao.mynextmovie.asynctaskloader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rcacao.mynextmovie.BuildConfig;
import com.rcacao.mynextmovie.api.ApiInterface;
import com.rcacao.mynextmovie.api.ApiJsonObjectReview;
import com.rcacao.mynextmovie.api.ApiRetrofit;
import com.rcacao.mynextmovie.models.Review;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewsAsyncTaskLoader extends android.support.v4.content.AsyncTaskLoader<Review[]> {

    public static final String ID = "id" ;
    private final Bundle args;

    private Review[] mReviews;

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
    public Review[] loadInBackground() {

        if (args==null){
            return null;
        }

        String id = args.getString(ID);
        if (id==null || id.isEmpty()){
            return null;
        }

        Retrofit retrofit = ApiRetrofit.getRetrofit();
        ApiInterface ApiService = retrofit.create(ApiInterface.class);

        Call<ApiJsonObjectReview> call = ApiService.getReviews(id, BuildConfig.MYAUTH);

        try {
            Response<ApiJsonObjectReview> response = call.execute();
            if (response != null){
                if (response.body() != null) {
                    return response.body().getResults();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;



    }

    @Override
    public void deliverResult(@Nullable Review[] mReviews) {
        this.mReviews = mReviews;
        super.deliverResult(mReviews);
    }
}
