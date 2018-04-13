package com.rcacao.mynextmovie.utils;

import android.util.Log;
import com.rcacao.mynextmovie.models.Review;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewsProcessor {

    public ArrayList<Review> getReviews(String jsonString) {

        ArrayList<Review> reviews = new ArrayList<>();

        try {

            JSONObject pageJson = new JSONObject(jsonString);
            JSONArray resultsJSON = pageJson.getJSONArray("results");

            JSONObject reviewJson;

            for (int i=0; i<resultsJSON.length();i++){
                reviewJson = new JSONObject(resultsJSON.getString(i));
                Review nReview = leJSON(reviewJson);
                if (nReview != null) {
                    reviews.add(nReview);
                }
            }

        } catch (JSONException e) {
            Log.e("ReviewProcessor","Erro ao ler JSON", e);

        }

        return reviews;
    }

    private static Review leJSON(JSONObject json){

        Review review = new Review();
        try {

            String id = json.getString("id");
            String autor = json.getString("author");
            String content = json.getString("content");

            review.setId(id);
            review.setReview(content);
            review.setAutor(autor);

            return review;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
