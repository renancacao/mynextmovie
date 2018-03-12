package com.rcacao.mynextmovie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;


public class MyMovie implements Serializable {

    private int id;
    private String poster_path;
    private String original_title;
    private String title;
    private String sinopse;
    private Double avaliacao;
    private String dataLancamento;


    MyMovie(JSONObject json) {
        try {

            id = json.getInt("id");
            poster_path = json.getString("poster_path");
            original_title = json.getString("original_title");
            title = json.getString("title");
            sinopse = json.getString("overview");
            avaliacao = json.getDouble("vote_average");
            dataLancamento = json.getString("release_date");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }
}
