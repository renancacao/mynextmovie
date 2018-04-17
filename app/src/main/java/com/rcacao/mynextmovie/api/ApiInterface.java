package com.rcacao.mynextmovie.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("3/movie/{order}")
    Call<ApiJsonObjectFilme> getFilmes(@Path("order") String order, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/videos")
    Call<ApiJsonObjectTrailer> getTrailers(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<ApiJsonObjectReview> getReviews(@Path("id") String id, @Query("api_key") String apiKey);

}
