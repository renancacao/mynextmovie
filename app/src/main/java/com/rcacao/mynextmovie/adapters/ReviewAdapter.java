package com.rcacao.mynextmovie.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.models.Review;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private ArrayList<Review> reviews;
    private final Context context;

    private final int TAMANHO_MAXIMO = 200;

    public interface ListItemClickListener {
        void onListItemClickReview(int clickedItemIndex);
    }

    public ReviewAdapter(Context context, ArrayList<Review> reviews, ListItemClickListener mOnClickListener) {
        this.context = context;
        this.reviews = reviews;
        this.mOnClickListener = mOnClickListener;
    }

    public void setTrailers(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_review, parent,  false);
        return new ReviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {


        if (reviews.get(position).getReview().length()>TAMANHO_MAXIMO){
            holder.tReview.setText(
                    resumeReview(reviews.get(position).getReview()
                            .replace("\n","").replace("\r", "")));
            holder.tReticencia.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tReview.setText(reviews.get(position).getReview()
                    .replace("\n","").replace("\r", ""));
            holder.tReticencia.setVisibility(View.INVISIBLE);
        }


        holder.tAutor.setText(reviews.get(position).getAutor());
    }



    @Override
    public int getItemCount() {
        if  (reviews != null){
            return reviews.size();
        }
        else{
            return 0;
        }
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tReview;
        final TextView tAutor;
        final TextView tReticencia;

        ReviewViewHolder(View itemView) {
            super(itemView);
            tReview = itemView.findViewById(R.id.tReview);
            tAutor = itemView.findViewById(R.id.tAutor);
            tReticencia = itemView.findViewById(R.id.tReticencia);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int cPos = getAdapterPosition();
            mOnClickListener.onListItemClickReview(cPos);
        }
    }

    private String resumeReview(String text){


        return text.substring(0,TAMANHO_MAXIMO-1);


    }


}
