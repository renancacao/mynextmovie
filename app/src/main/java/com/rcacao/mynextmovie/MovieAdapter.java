package com.rcacao.mynextmovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {


    final private ListItemClickListener mOnClickListener;
    private ArrayList<MyMovie> movies;
    private Context context;

    public MovieAdapter(Context context, ArrayList<MyMovie> movies, ListItemClickListener mOnClickListener) {
        this.context = context;
        this.movies = movies;
        this.mOnClickListener = mOnClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public PosterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_poster, parent,  false);
        return new PosterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PosterViewHolder holder, int position) {
        Picasso.with(context).load(movies.get(position).getPoster()).into(holder.imgPoster);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgPoster;

        PosterViewHolder(View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int cPos = getAdapterPosition();
            mOnClickListener.onListItemClick(cPos);
        }
    }
}
