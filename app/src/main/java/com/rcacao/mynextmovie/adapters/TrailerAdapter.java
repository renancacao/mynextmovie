package com.rcacao.mynextmovie.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.models.Trailer;
import com.squareup.picasso.Picasso;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {


    final private ListItemClickListener mOnClickListener;
    private Trailer[] trailers;
    private final Context context;


    public TrailerAdapter(Context context, Trailer[] trailers, ListItemClickListener mOnClickListener) {
        this.context = context;
        this.trailers = trailers;
        this.mOnClickListener = mOnClickListener;
    }

    public void setTrailers(Trailer[] trailers) {
        this.trailers = trailers;
    }

    public interface ListItemClickListener {
        void onListItemClickTrailer(int clickedItemIndex);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_trailer, parent,  false);
        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Picasso.get().load(trailers[position].getUrlImage()).into(holder.imgTrailer);
        holder.tTrailer.setText(trailers[position].getTitulo());
    }



    @Override
    public int getItemCount() {
        if  (trailers != null){
            return trailers.length;
        }
        else{
            return 0;
        }
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imgTrailer;
        final TextView tTrailer;

        TrailerViewHolder(View itemView) {
            super(itemView);
            imgTrailer = itemView.findViewById(R.id.imgTrailer);
            tTrailer = itemView.findViewById(R.id.tTrailer);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int cPos = getAdapterPosition();
            mOnClickListener.onListItemClickTrailer(cPos);
        }
    }
}
