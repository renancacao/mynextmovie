package com.rcacao.mynextmovie.interfaces;

import com.rcacao.mynextmovie.models.Review;

import java.util.ArrayList;

public interface AsyncTaskReviewsDelegate {
    void processFinishReviews(ArrayList<Review> output);
    void processStartReviews();
}
