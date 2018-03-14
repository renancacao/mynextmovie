package com.rcacao.mynextmovie.network;

import android.net.Uri;

import com.rcacao.mynextmovie.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {

    private final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String KEY_URL = "api_key";

    public static final String URL_POSTER = "http://image.tmdb.org/t/p/";
    public static final String TAMANHO = "w185/";


    public static URL buidingUrlDbMovies(String order){

        Uri builtUri = Uri.parse(BASE_URL + order).buildUpon()
                .appendQueryParameter(KEY_URL, BuildConfig.MYAUTH).build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }



}
