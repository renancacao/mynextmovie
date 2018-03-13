package com.rcacao.mynextmovie;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


class Utils {

    //REMOVER -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    private final static String MYAUTH = KEY_AQUI;
    //REMOVER -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=


    private final static String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String KEY_URL = "api_key";

    static final String URL_POSTER = "http://image.tmdb.org/t/p/";
    static final String TAMANHO = "w185/";

    static URL buidingUrlDbMovies(String order){

        Uri builtUri = Uri.parse(BASE_URL + order).buildUpon()
                .appendQueryParameter(KEY_URL,MYAUTH).build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    static String getResponseFromHttpUrl(URL url) throws IOException {
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
