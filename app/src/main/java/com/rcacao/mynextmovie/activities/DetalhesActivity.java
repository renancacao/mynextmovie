package com.rcacao.mynextmovie.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.adapters.ReviewAdapter;
import com.rcacao.mynextmovie.adapters.TrailerAdapter;
import com.rcacao.mynextmovie.data.MovieContract;
import com.rcacao.mynextmovie.data.MovieContract.MovieEntry;
import com.rcacao.mynextmovie.interfaces.AsyncTaskReviewsDelegate;
import com.rcacao.mynextmovie.interfaces.AsyncTaskTrailersDelegate;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.models.Review;
import com.rcacao.mynextmovie.models.Trailer;
import com.rcacao.mynextmovie.utils.NetworkUtils;
import com.rcacao.mynextmovie.utils.ReviewsService;
import com.rcacao.mynextmovie.utils.TrailersService;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetalhesActivity extends AppCompatActivity implements AsyncTaskTrailersDelegate, AsyncTaskReviewsDelegate,
        TrailerAdapter.ListItemClickListener, ReviewAdapter.ListItemClickListener {


    @BindView(R.id.tTitulo) TextView tTitulo;
    @BindView(R.id.tLancamento) TextView tLancamento;
    @BindView(R.id.tAvaliacao) TextView tAvaliacao;
    @BindView(R.id.tSinopse) TextView tSinopse;
    @BindView(R.id.imgPoster) ImageView imgPoster;
    @BindView(R.id.progressBarTrailers) ProgressBar progressBarTrailers;
    @BindView(R.id.progressBarReviews) ProgressBar progressBarReviews;

    @BindView(R.id.recyclerViewTrailers) RecyclerView recycleViewTrailers;
    @BindView(R.id.recyclerViewReviews) RecyclerView recyclerViewReviews;

    @BindView(R.id.linearLayoutErro) LinearLayout linearLayoutErro;

    private MenuItem mnuFav;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;

    private Filme filme = null;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        ButterKnife.bind(this);


        Intent i = getIntent();
        if (i.hasExtra(Filme.EXTRA_FILME)){
            filme = i.getParcelableExtra(Filme.EXTRA_FILME);
        }
        else
        {
            finish();
        }


        LinearLayoutManager layoutManagerTrailers = new LinearLayoutManager(this);
        layoutManagerTrailers.setOrientation(LinearLayoutManager.HORIZONTAL);


        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this);
        layoutManagerReviews.setOrientation(LinearLayoutManager.HORIZONTAL);

        //trailers ///////////////////////////
        trailers = new ArrayList<>();

        trailerAdapter = new TrailerAdapter(this, trailers, this);
        recycleViewTrailers.setLayoutManager(layoutManagerTrailers);
        recycleViewTrailers.setHasFixedSize(true);
        recycleViewTrailers.setFocusable(false);
        recycleViewTrailers.setAdapter(trailerAdapter);

        //reviews ////////////////////////////
        reviews = new ArrayList<>();

        reviewAdapter = new ReviewAdapter(this, reviews,this);
        recyclerViewReviews.setLayoutManager(layoutManagerReviews);
        recyclerViewReviews.setHasFixedSize(false);
        recyclerViewReviews.setFocusable(false);
        recyclerViewReviews.setAdapter(reviewAdapter);

        carregaFilme();
    }

    private void carregaFilme() {

        Picasso.with(this).load(getPosterURL()).into(imgPoster);
        tTitulo.setText(filme.getTitulo());
        tAvaliacao.setText(String.valueOf(filme.getAvaliacao()));
        tLancamento.setText(filme.getLancamento());
        tSinopse.setText(filme.getSinopse());

        if (carregoTrailers(filme.getId()) && carregoReviews(filme.getId())){
            recycleViewTrailers.setVisibility(View.VISIBLE);
            recyclerViewReviews.setVisibility(View.VISIBLE);
            linearLayoutErro.setVisibility(View.INVISIBLE);
        }
        else{
            recycleViewTrailers.setVisibility(View.INVISIBLE);
            recyclerViewReviews.setVisibility(View.INVISIBLE);
            linearLayoutErro.setVisibility(View.VISIBLE);
        }

        isFavorite = isInFavorits(filme.getId());

    }


    private boolean isInFavorits(int id){

        Cursor resultCursor = getContentResolver().query(MovieEntry.CONTENT_URI, null, MovieEntry._ID + "=?", new String[]{String.valueOf(id)}, null);
        if (resultCursor != null && resultCursor.getCount() == 1) {
            resultCursor.close();
            return true;
        }

        return false;
    }


    private String getPosterURL() {

        return NetworkUtils.URL_POSTER + NetworkUtils.TAMANHO + filme.getPoster();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detalhes_menu, menu);
        mnuFav = menu.findItem(R.id.mnuFav);
        adjustImgFav();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.mnuFav){
            if (!isFavorite){
                addToFavorits();
            }
            else
            {
                removeFromFavorits();
            }

            adjustImgFav();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinishTrailers(ArrayList<Trailer> outTrailers) {

        progressBarTrailers.setVisibility(View.INVISIBLE);

        if(outTrailers != null){
            trailers = outTrailers;
            trailerAdapter.setTrailers(trailers);
            trailerAdapter.notifyDataSetChanged();
        }else{
            recycleViewTrailers.setVisibility(View.INVISIBLE);
            linearLayoutErro.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void processFinishReviews(ArrayList<Review> outReviews) {

        progressBarReviews.setVisibility(View.INVISIBLE);

        if(outReviews != null){
            reviews = outReviews;
            reviewAdapter.setTrailers(reviews);
            reviewAdapter.notifyDataSetChanged();
        }else{
            recyclerViewReviews.setVisibility(View.INVISIBLE);
            linearLayoutErro.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void processStartTrailers() {

        progressBarTrailers.setVisibility(View.VISIBLE);

    }

    @Override
    public void processStartReviews() {

        progressBarReviews.setVisibility(View.VISIBLE);

    }

    private boolean carregoTrailers(int id){

        if(NetworkUtils.isOnline(this)){
            URL url = NetworkUtils.buidingUrlTrailers(id);
            new TrailersService(this).execute(url);
            return true;
        }
        else{
            return false;
        }

    }

    private boolean carregoReviews(int id){

        if(NetworkUtils.isOnline(this)){
            URL url = NetworkUtils.buidingUrlReviews(id);
            new ReviewsService(this).execute(url);
            return true;
        }
        else{
            return false;
        }

    }


    private void addToFavorits(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry._ID,filme.getId());
        contentValues.put(MovieEntry.COLUMN_TITULO,filme.getTitulo());
        contentValues.put(MovieEntry.COLUMN_POSTER,filme.getPoster());
        contentValues.put(MovieEntry.COLUMN_SINOPSE ,filme.getSinopse());
        contentValues.put(MovieEntry.COLUMN_AVALIACAO,filme.getAvaliacao());
        contentValues.put(MovieEntry.COLUMN_LANCAMENTO ,filme.getLancamento());
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        if (uri != null){
            isFavorite=true;
            Toast.makeText(this, getString(R.string.add_favoritos),Toast.LENGTH_LONG).show();
        }

    }

    private void removeFromFavorits(){

        Uri uriDelete = MovieContract.MovieEntry.CONTENT_URI;
        uriDelete = uriDelete.buildUpon().appendPath(String.valueOf(filme.getId())).build();

        int result = getContentResolver().delete(uriDelete, null, null);
        if (result == 1){
            isFavorite=false;
            Toast.makeText(this,getString(R.string.remove_favoritos),Toast.LENGTH_LONG).show();

        }

    }

    private void adjustImgFav(){

        if (mnuFav == null) {return;}

        if (isFavorite){
            mnuFav.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_favorite_white_24dp));
        }
        else{
            mnuFav.setIcon(ContextCompat.getDrawable(this,R.drawable.ic_favorite_border_white_24dp));

        }
    }


    @Override
    public void onListItemClickReview(int clickedItemIndex) {
        Intent intent = new Intent(this,ReviewActivity.class);
        intent.putExtra(Review.EXTRA_REVIEW, reviews.get(clickedItemIndex));

        startActivity(intent);
    }

    @Override
    public void onListItemClickTrailer(int clickedItemIndex) {

        String video_path =  trailers.get(clickedItemIndex).getLink();
        Uri uri = Uri.parse(video_path);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
}
