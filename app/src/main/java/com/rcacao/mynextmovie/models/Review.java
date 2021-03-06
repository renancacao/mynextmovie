package com.rcacao.mynextmovie.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable{

    public static final String EXTRA_REVIEW = "review";

    @SerializedName("content")
    private String review;

    @SerializedName("author")
    private String autor;

    private String id;

    private Review(Parcel in) {
        review = in.readString();
        autor = in.readString();
        id = in.readString();
    }


    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getReview() {
        return review;
    }

    public String getAutor() {
        return autor;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(review);
        parcel.writeString(autor);
        parcel.writeString(id);

    }
}
