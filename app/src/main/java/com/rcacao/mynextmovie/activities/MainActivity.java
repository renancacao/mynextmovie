package com.rcacao.mynextmovie.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.adapters.MovieAdapter;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.asynctaskloader.MoviesAsyncTaskLoader;
import com.rcacao.mynextmovie.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener,  SharedPreferences.OnSharedPreferenceChangeListener,
        android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Filme>>{

    private static final int MOVIES_LOADER = 33 ;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.linearLayoutErro) LinearLayout linearLayoutErro;
    @BindView(R.id.recycleViewMovies) RecyclerView recycleViewMovies;

    private GridLayoutManager grid;

    private final String ORDER_POPULAR = "popular";
    private final String ORDER_TOP_RATED = "top_rated";
    private final String ORDER_FAVORITOS = "fav";

    private String order = "";

    private MovieAdapter adapter;
    private ArrayList<Filme> filmes;


    private MenuItem menuPop, menuRat, menuFav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        order = ORDER_POPULAR;

        recycleViewMovies = findViewById(R.id.recycleViewMovies);

        //GridLayoutManager grid = new GridLayoutManager(this, Utils.getQtdColunas(this));
        //nao gostei de como ficou.

        grid = new GridLayoutManager(this, 2);

        setupSharedPreferences();

        recycleViewMovies.setLayoutManager(grid);
        recycleViewMovies.setHasFixedSize(true);

        filmes = new ArrayList<>();

        adapter = new MovieAdapter(this, filmes, this);

        recycleViewMovies.setAdapter(adapter);

        carregoFilmes(order);

    }

    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            grid.setSpanCount(Integer.parseInt(sharedPreferences.getString(
                    getString(R.string.pref_key_num_col),getString(R.string.pref_value_num_col))));

        }catch(Exception ignored) {}

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void carregoFilmes(String order){

        boolean isonline = NetworkUtils.isOnline(this);
        Bundle queryBundle = new Bundle();

        if (order.equals(ORDER_POPULAR) || order.matches(ORDER_TOP_RATED)){
            if(isonline){
                URL dbMovieUrl = NetworkUtils.buidingUrlDbMovies(order);
                recycleViewMovies.setVisibility(View.VISIBLE);
                linearLayoutErro.setVisibility(View.INVISIBLE);
                queryBundle.putString(MoviesAsyncTaskLoader.URL_ARG, dbMovieUrl.toString());
                progressBar.setVisibility(View.VISIBLE);
                getSupportLoaderManager().restartLoader(MOVIES_LOADER, queryBundle, this);
            }
            else{
                recycleViewMovies.setVisibility(View.INVISIBLE);
                linearLayoutErro.setVisibility(View.VISIBLE);
            }
        }
        else if (order.equals(ORDER_FAVORITOS)){
            recycleViewMovies.setVisibility(View.VISIBLE);
            linearLayoutErro.setVisibility(View.INVISIBLE);
            queryBundle.putBoolean(MoviesAsyncTaskLoader.GET_FAVS, true);
            getSupportLoaderManager().restartLoader(MOVIES_LOADER, queryBundle, this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuPop = menu.findItem(R.id.mnuPop);
        menuRat = menu.findItem(R.id.mnuRat);
        menuFav = menu.findItem(R.id.mnuFav);

        menuPop.setEnabled(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.mnuPop:
                order = ORDER_POPULAR;
                carregoFilmes(order);
                menuPop.setEnabled(false);
                menuRat.setEnabled(true);
                menuFav.setEnabled(true);
                break;

            case R.id.mnuRat:
                order = ORDER_TOP_RATED;
                carregoFilmes(order);
                menuPop.setEnabled(true);
                menuRat.setEnabled(false);
                menuFav.setEnabled(true);
                break;

            case R.id.mnuFav:
                order = ORDER_FAVORITOS;
                carregoFilmes(order);
                menuPop.setEnabled(true);
                menuRat.setEnabled(true);
                menuFav.setEnabled(false);
                break;

            case R.id.mnuPreferencias:
                Intent intent = new Intent(this,PreferencesActivity.class);
                startActivity(intent);

            default:
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        //Toast.makeText(this,String.valueOf(clickedItemIndex),Toast.LENGTH_LONG).show();

        Intent intent= new Intent(this, DetalhesActivity.class);
        intent.putExtra(Filme.EXTRA_FILME, filmes.get(clickedItemIndex));

        startActivity(intent);

    }



    @OnClick(R.id.bReconectar) void clickReconectar(){

        carregoFilmes(order);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_key_num_col))){
            grid.setSpanCount(Integer.parseInt(sharedPreferences.getString(
                    getString(R.string.pref_key_num_col),getString(R.string.pref_value_num_col))));
        }
    }


    @NonNull
    @Override
    public android.support.v4.content.Loader<ArrayList<Filme>> onCreateLoader(int id, @Nullable Bundle args) {
        return new MoviesAsyncTaskLoader(this, args);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<ArrayList<Filme>> loader, ArrayList<Filme> outFilmes) {

        progressBar.setVisibility(View.INVISIBLE);

        if(outFilmes != null){
            filmes = outFilmes;
            adapter.setMovies(filmes);
            adapter.notifyDataSetChanged();
        }else{
            recycleViewMovies.setVisibility(View.INVISIBLE);
            linearLayoutErro.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<ArrayList<Filme>> loader) {

    }


}
