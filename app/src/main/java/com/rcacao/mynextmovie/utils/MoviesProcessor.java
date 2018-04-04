package com.rcacao.mynextmovie.utils;

import android.util.Log;
import com.rcacao.mynextmovie.models.Filme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class MoviesProcessor {

    ArrayList<Filme> getMovies(String jsonString) {

        ArrayList<Filme> movies = new ArrayList<>();

        try {
            JSONObject pageJson = new JSONObject(jsonString);
            JSONArray resultsJSON = pageJson.getJSONArray("results");


            JSONObject movieJson;

            for (int i=0; i<resultsJSON.length();i++){
                movieJson = new JSONObject(resultsJSON.getString(i));
                Filme nFilme = leJSON(movieJson);
                if (nFilme != null) {
                    movies.add(nFilme);
                }
            }

        } catch (JSONException e) {
            Log.e("MoviesProcessor","Erro ao ler JSON", e);

        }

        return movies;
    }

    private static Filme leJSON(JSONObject json){

        Filme filme = new Filme();
        try {

            int id = json.getInt("id");
            String poster = json.getString("poster_path");
            String titulo = json.getString("original_title");
            String sinopse = json.getString("overview");
            Double avaliacao = json.getDouble("vote_average");
            String lancamento = json.getString("release_date");

            filme.setId(id);
            filme.setPoster(poster);
            filme.setTitulo(titulo);
            filme.setSinopse(sinopse);
            filme.setAvaliacao(avaliacao);
            filme.setLancamento(lancamento);

            return filme;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
