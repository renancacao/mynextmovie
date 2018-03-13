package com.rcacao.mynextmovie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


class Filme implements Serializable {

    private int id;
    private String poster;
    private String titulo;
    private String sinopse;
    private Double avaliacao;
    private String lancamento;

    Filme(JSONObject json) {
        try {

            id = json.getInt("id");
            poster = json.getString("poster_path");
            titulo = json.getString("original_title");
            sinopse = json.getString("overview");
            avaliacao = json.getDouble("vote_average");
            lancamento = json.getString("release_date");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    String getPoster() {
        return poster;
    }

    String getTitulo() {
        return titulo;
    }

    String getSinopse() {
        return sinopse;
    }

    Double getAvaliacao() {
        return avaliacao;
    }

    String getLancamento() {
        return lancamento;
    }
}
