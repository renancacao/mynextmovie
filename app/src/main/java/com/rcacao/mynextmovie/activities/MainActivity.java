package com.rcacao.mynextmovie.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.rcacao.mynextmovie.interfaces.AsyncTaskMoviesDelegate;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.utils.MovieService;
import com.rcacao.mynextmovie.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener, AsyncTaskMoviesDelegate, SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.linearLayoutErro) LinearLayout linearLayoutErro;
    @BindView(R.id.recycleViewMovies) RecyclerView recycleViewMovies;

    private GridLayoutManager grid;

    private String order = "";
    private MovieAdapter adapter;
    private ArrayList<Filme> filmes;


    private MenuItem pop, rat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        order = getString(R.string.order_popular);

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

        if(NetworkUtils.isOnline(this)){
            recycleViewMovies.setVisibility(View.VISIBLE);
            linearLayoutErro.setVisibility(View.INVISIBLE);
            URL dbMovieUrl = NetworkUtils.buidingUrlDbMovies(order);
            new MovieService(this,this).execute(dbMovieUrl);
        }
        else{
            recycleViewMovies.setVisibility(View.INVISIBLE);
            linearLayoutErro.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        pop = menu.findItem(R.id.mnuPop);
        rat = menu.findItem(R.id.mnuRat);
        pop.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.mnuPop:
                order = getString(R.string.order_popular);
                carregoFilmes(order);
                pop.setEnabled(false);
                rat.setEnabled(true);
                break;

            case R.id.mnuRat:
                order = getString(R.string.order_top_rated);
                carregoFilmes(order);
                pop.setEnabled(true);
                rat.setEnabled(false);
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

    @Override
    public void processFinish(ArrayList<Filme> outfilmes) {

        progressBar.setVisibility(View.INVISIBLE);

        if(outfilmes != null){
            filmes = outfilmes;
            adapter.setMovies(filmes);
            adapter.notifyDataSetChanged();
        }else{
            recycleViewMovies.setVisibility(View.INVISIBLE);
            linearLayoutErro.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void processStart() {
        progressBar.setVisibility(View.VISIBLE);
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
}
