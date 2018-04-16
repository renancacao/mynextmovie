package com.rcacao.mynextmovie.asynctaskloader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rcacao.mynextmovie.BuildConfig;
import com.rcacao.mynextmovie.api.ApiInterface;
import com.rcacao.mynextmovie.api.ApiJsonObjectFilme;
import com.rcacao.mynextmovie.api.ApiRetrofit;
import com.rcacao.mynextmovie.data.MovieContract;
import com.rcacao.mynextmovie.models.Filme;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MoviesAsyncTaskLoader extends android.support.v4.content.AsyncTaskLoader<Filme[]> {


    private Bundle args;

    public static final String ORDER = "order";
    public static final String GET_FAVS = "getfavs" ;

    private Filme[] mFilmes;

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
    public Filme[] loadInBackground() {

        if (args==null){
            return null;
        }

        //verifico se devo carregar do SQLite ou da api
        boolean getFavs = args.getBoolean(GET_FAVS);
        if (getFavs){
            return loadFromDataBase();
        }
        else
        {
            return loadFromAPI();
        }

    }

    private Filme[] loadFromAPI(){

        String order = args.getString(ORDER);
        if (order == null || order.isEmpty()){
            order = "popular";
        }

        Retrofit retrofit = ApiRetrofit.getRetrofit();
        ApiInterface ApiService = retrofit.create(ApiInterface.class);

        Call<ApiJsonObjectFilme> call = ApiService.getFilmes(order, BuildConfig.MYAUTH);

        try {
            Response<ApiJsonObjectFilme> response = call.execute();
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

    private Filme[] loadFromDataBase(){

        Cursor resultCursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null,  null);
        if (resultCursor==null){
            return null;
        }

        Filme[] filmes = new Filme[resultCursor.getCount()];
        int cont = 0;

        while (resultCursor.moveToNext()){

           Filme f = new Filme();
           f.setId(resultCursor.getInt(0));
           f.setPoster(resultCursor.getString(1));
           f.setTitulo(resultCursor.getString(2));
           f.setSinopse(resultCursor.getString(3) );
           f.setAvaliacao(resultCursor.getDouble(4));
           f.setLancamento(resultCursor.getString(5));

           filmes[cont] = f;
           cont++;

       }

       resultCursor.close();

       return filmes;

    }

    @Override
    public void deliverResult(@Nullable Filme[] mFilmes) {
        this.mFilmes = mFilmes;
        super.deliverResult(mFilmes);
    }


}
