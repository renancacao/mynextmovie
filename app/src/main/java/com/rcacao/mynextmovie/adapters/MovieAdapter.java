package com.rcacao.mynextmovie.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {


    final private ListItemClickListener mOnClickListener;
    private ArrayList<Filme> movies;
    private final Context context;



    public void setMovies(ArrayList<Filme> movies) {
        this.movies = movies;
    }

    public MovieAdapter(Context context, ArrayList<Filme> movies, ListItemClickListener mOnClickListener) {
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
        Picasso.get().load(getPoster(position)).into(holder.imgPoster);

    }

    private String getPoster(int position) {

        return NetworkUtils.URL_POSTER + NetworkUtils.TAMANHO + movies.get(position).getPoster();
    }

    @Override
    public int getItemCount() {
        if  (movies != null){
            return movies.size();
        }
        else{
            return 0;
        }
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imgPoster;

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
