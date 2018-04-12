package com.rcacao.mynextmovie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.models.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewActivity extends AppCompatActivity {

    private Review review;

    @BindView(R.id.tAutor) TextView tAutor;
    @BindView(R.id.tReview) TextView tReview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ButterKnife.bind(this);

        Intent i = getIntent();
        if (i.hasExtra(Review.EXTRA_REVIEW)){
            review = i.getParcelableExtra(Review.EXTRA_REVIEW);
        }
        else
        {
            finish();
        }

        tAutor.setText(review.getAutor());
        tReview.setText(review.getReview());

    }
}
