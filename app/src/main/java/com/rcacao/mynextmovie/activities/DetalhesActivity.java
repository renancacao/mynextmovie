package com.rcacao.mynextmovie.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
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
import com.rcacao.mynextmovie.asynctaskloader.ReviewsAsyncTaskLoader;
import com.rcacao.mynextmovie.asynctaskloader.TrailersAsyncTaskLoader;
import com.rcacao.mynextmovie.data.MovieContract;
import com.rcacao.mynextmovie.data.MovieContract.MovieEntry;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.models.Review;
import com.rcacao.mynextmovie.models.Trailer;
import com.rcacao.mynextmovie.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetalhesActivity extends AppCompatActivity implements
        android.support.v4.app.LoaderManager.LoaderCallbacks,
        TrailerAdapter.ListItemClickListener, ReviewAdapter.ListItemClickListener {


    @BindView(R.id.tTitulo) TextView tTitulo;
    @BindView(R.id.tLancamento) TextView tLancamento;
    @BindView(R.id.tAvaliacao) TextView tAvaliacao;
    @BindView(R.id.tSinopse) TextView tSinopse;
    @BindView(R.id.imgPoster) ImageView imgPoster;
    @BindView(R.id.progressBarTrailers) ProgressBar progressBarTrailers;
    @BindView(R.id.progressBarReviews) ProgressBar progressBarReviews;

    @BindView(R.id.tTrailers) TextView tTrailers;
    @BindView(R.id.tReviews) TextView tReviews;


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

    private static final int TRAILERS_LOADER = 44 ;
    private static final int REVIEWS_LOADER = 55 ;



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

        Picasso.get().load(getPosterURL()).into(imgPoster);
        tTitulo.setText(filme.getTitulo());
        tAvaliacao.setText(String.valueOf(filme.getAvaliacao()));
        tLancamento.setText(filme.getLancamento());
        tSinopse.setText(filme.getSinopse());

        if (carregoTrailers(filme.getId()) && carregoReviews(filme.getId())){
            ajustaTelaErro(false);

        }
        else{
            ajustaTelaErro(true);
        }

        isFavorite = isInFavorits(filme.getId());

    }

    private void ajustaTelaErro(boolean erro){

        if (erro){
            recycleViewTrailers.setVisibility(View.INVISIBLE);
            recyclerViewReviews.setVisibility(View.INVISIBLE);
            tTrailers.setVisibility(View.INVISIBLE);
            tReviews.setVisibility(View.INVISIBLE);
            linearLayoutErro.setVisibility(View.VISIBLE);
        }
        else{
            recycleViewTrailers.setVisibility(View.VISIBLE);
            recyclerViewReviews.setVisibility(View.VISIBLE);
            tTrailers.setVisibility(View.VISIBLE);
            tReviews.setVisibility(View.VISIBLE);
            linearLayoutErro.setVisibility(View.INVISIBLE);
        }

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



    private boolean carregoTrailers(int id){

        if(NetworkUtils.isOnline(this)){
            URL url = NetworkUtils.buidingUrlTrailers(id);
            Bundle queryBundle = new Bundle();
            queryBundle.putString(TrailersAsyncTaskLoader.URL_ARG,url.toString());
            progressBarTrailers.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(TRAILERS_LOADER, queryBundle, this);
            return true;
        }
        else{
            return false;
        }

    }

    private boolean carregoReviews(int id){

        if(NetworkUtils.isOnline(this)){
            URL url = NetworkUtils.buidingUrlReviews(id);
            Bundle queryBundle = new Bundle();
            queryBundle.putString(ReviewsAsyncTaskLoader.URL_ARG,url.toString());
            progressBarReviews.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(REVIEWS_LOADER, queryBundle, this);
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

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {

        if(id == TRAILERS_LOADER)
        {
            return new TrailersAsyncTaskLoader(this,args);
        }
        else //REVIEWS_LOADER
        {
            return new ReviewsAsyncTaskLoader(this,args);
        }
    }


    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {

        if (loader.getId() == TRAILERS_LOADER){
            progressBarTrailers.setVisibility(View.INVISIBLE);

            if(data != null){
                trailers = (ArrayList<Trailer>) data;
                trailerAdapter.setTrailers(trailers);
                trailerAdapter.notifyDataSetChanged();
                ajustaTelaErro(false);

            }else{
                ajustaTelaErro(true);
            }
        }
        else //REVIEWS_LOADER
        {
            progressBarReviews.setVisibility(View.INVISIBLE);

            if(data != null){
                reviews = (ArrayList<Review>) data;
                reviewAdapter.setTrailers(reviews);
                reviewAdapter.notifyDataSetChanged();
                ajustaTelaErro(false);

            }else{
                ajustaTelaErro(true);

            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    @OnClick(R.id.bReconectar) void clickReconectar(){

        carregaFilme();

    }

}
