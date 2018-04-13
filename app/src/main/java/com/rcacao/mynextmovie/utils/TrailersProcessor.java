package com.rcacao.mynextmovie.utils;

import android.util.Log;

import com.rcacao.mynextmovie.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrailersProcessor {

       public ArrayList<Trailer> getTrailers(String jsonString) {

        ArrayList<Trailer> trailers = new ArrayList<>();

        try {
            JSONObject pageJson = new JSONObject(jsonString);
            JSONArray resultsJSON = pageJson.getJSONArray("results");


            JSONObject trailerJson;

            for (int i=0; i<resultsJSON.length();i++){
                trailerJson = new JSONObject(resultsJSON.getString(i));
                Trailer trailer = leJSON(trailerJson);
                if (trailer != null) {
                    trailers.add(trailer);
                }
            }

        } catch (JSONException e) {
            Log.e("TrailersProcessor","Erro ao ler JSON.", e);

        }

        return trailers;
    }

    private static Trailer leJSON(JSONObject json){

        Trailer trailer = new Trailer();

        try {

            String id = json.getString("id");
            String key = json.getString("key");
            String titulo = json.getString("name");

            trailer.setId(id);
            trailer.setKey(key);
            trailer.setTitulo(titulo);

            return trailer;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
