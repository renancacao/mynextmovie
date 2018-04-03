package com.rcacao.mynextmovie.utils;

import android.content.Context;
import android.util.Log;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class TrailersProcessor {

    private Context context = null;
    TrailersProcessor(Context context) {
        this.context = context;
    }

    ArrayList<Trailer> getTrailers(String jsonString) {

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
            Log.e(context.getString(R.string.tag_trailerprocessor),context.getString(R.string.erro_ler_json), e);

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
