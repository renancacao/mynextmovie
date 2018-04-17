package com.rcacao.mynextmovie.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {

    private static final String BASE_URL = "https://api.themoviedb.org/";

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit(){

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofit;
    }

}
