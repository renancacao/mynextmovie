package com.rcacao.mynextmovie.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.rcacao.mynextmovie.utils.MovieService;
import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.adapters.MovieAdapter;
import com.rcacao.mynextmovie.interfaces.AsyncTaskDelegate;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener, AsyncTaskDelegate {

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.linearLayoutErro) LinearLayout linearLayoutErro;
    @BindView(R.id.recycleMovies) RecyclerView recycleMovies;

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

        recycleMovies = findViewById(R.id.recycleMovies);

        //GridLayoutManager grid = new GridLayoutManager(this, Utils.getQtdColunas(this));
        //nao gostei de como ficou.

        GridLayoutManager grid = new GridLayoutManager(this, 2);

        recycleMovies.setLayoutManager(grid);
        recycleMovies.setHasFixedSize(true);

        filmes = new ArrayList<>();

        adapter = new MovieAdapter(this, filmes, this);

        recycleMovies.setAdapter(adapter);

        carregoFilmes(order);

    }

    private void carregoFilmes(String order){

        if(NetworkUtils.isOnline(this)){
            recycleMovies.setVisibility(View.VISIBLE);
            linearLayoutErro.setVisibility(View.INVISIBLE);
            URL dbMovieUrl = NetworkUtils.buidingUrlDbMovies(order);
            new MovieService(this,this).execute(dbMovieUrl);
        }
        else{
            recycleMovies.setVisibility(View.INVISIBLE);
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
            recycleMovies.setVisibility(View.INVISIBLE);
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

}
