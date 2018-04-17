package com.rcacao.mynextmovie.asynctaskloader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rcacao.mynextmovie.BuildConfig;
import com.rcacao.mynextmovie.api.ApiInterface;
import com.rcacao.mynextmovie.api.ApiJsonObjectTrailer;
import com.rcacao.mynextmovie.api.ApiRetrofit;
import com.rcacao.mynextmovie.models.Trailer;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TrailersAsyncTaskLoader extends android.support.v4.content.AsyncTaskLoader<Trailer[]> {

    private final Bundle args;

    public static final String ID = "id";

    private Trailer[] mTrailer;

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
    public Trailer[] loadInBackground() {


        if (args==null){
            return null;
        }

        String id = args.getString(ID);
        if (id==null || id.isEmpty()){
            return null;
        }

        Retrofit retrofit = ApiRetrofit.getRetrofit();
        ApiInterface ApiService = retrofit.create(ApiInterface.class);

        Call<ApiJsonObjectTrailer> call = ApiService.getTrailers(id, BuildConfig.MYAUTH);

        try {
            Response<ApiJsonObjectTrailer> response = call.execute();
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
    public void deliverResult(@Nullable Trailer[] mTrailer) {
        this.mTrailer = mTrailer;
        super.deliverResult(mTrailer);
    }
}
