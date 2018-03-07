package com.rcacao.mynextmovie;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Renan on 28/02/2018.
 */

public class MyMovie {

    private int id;
    private String poster_path;
    private String original_title;
    private String title;


    MyMovie(JSONObject json) {
        try {

            id = json.getInt("id");
            poster_path = json.getString("poster_path");
            original_title = json.getString("original_title");
            title = json.getString("title");

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
