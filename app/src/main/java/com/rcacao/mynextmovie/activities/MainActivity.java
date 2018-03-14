package com.rcacao.mynextmovie.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.adapters.MovieAdapter;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.network.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private final String TAG = getClass().getName();

    @BindView(R.id.pgLoading) ProgressBar pgLoading;
    @BindView(R.id.lytErro) LinearLayout lytErro;
    @BindView(R.id.rcMovies) RecyclerView rcMovies;

    private String order = "popular";
    private MovieAdapter adapter;
    private ArrayList<Filme> filmes;

    private MenuItem pop, rat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        rcMovies = findViewById(R.id.rcMovies);

        //GridLayoutManager grid = new GridLayoutManager(this, Utils.getQtdColunas(this));
        //nao gostei de como ficou.

        GridLayoutManager grid = new GridLayoutManager(this, 2);

        rcMovies.setLayoutManager(grid);
        rcMovies.setHasFixedSize(true);

        filmes = new ArrayList<>();

        adapter = new MovieAdapter(this, filmes, this);

        rcMovies.setAdapter(adapter);

        carregoFilmes(order);

    }

    private void carregoFilmes(String order){

        if(isOnline()){
            rcMovies.setVisibility(View.VISIBLE);
            lytErro.setVisibility(View.INVISIBLE);
            URL dbMovieUrl = NetworkUtils.buidingUrlDbMovies(order);
            new MoviesQueryTask().execute(dbMovieUrl);
        }
        else{
            rcMovies.setVisibility(View.INVISIBLE);
            lytErro.setVisibility(View.VISIBLE);
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
                order = "popular";
                carregoFilmes(order);
                pop.setEnabled(false);
                rat.setEnabled(true);
                break;

            case R.id.mnuRat:
                order = "top_rated";
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
        intent.putExtra("filme", filmes.get(clickedItemIndex));

        startActivity(intent);

    }


    class MoviesQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgLoading.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(URL... param) {


            URL searchUrl = param[0];
            String jsonMovies;
            try {
                jsonMovies = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                Log.w(TAG,"Erro ao requerir filmes", e);
                jsonMovies = "";
            }

            return jsonMovies;

        }

        @Override
        protected void onPostExecute(String jsonMovies) {
            super.onPostExecute(jsonMovies);
            pgLoading.setVisibility(View.INVISIBLE);

            if (!jsonMovies.isEmpty() && !jsonMovies.equals("")) {
                filmes = getMovies(jsonMovies);
                adapter.setMovies(filmes);
                adapter.notifyDataSetChanged();
            }
            else
            {
                rcMovies.setVisibility(View.INVISIBLE);
                lytErro.setVisibility(View.VISIBLE);
            }

        }
    }

    private ArrayList<Filme> getMovies(String jsonString) {

        ArrayList<Filme> movies = new ArrayList<>();

        try {
            JSONObject pageJson = new JSONObject(jsonString);
            JSONArray resultsJSON = pageJson.getJSONArray("results");


            JSONObject movieJson;

            for (int i=0; i<resultsJSON.length();i++){
                movieJson = new JSONObject(resultsJSON.getString(i));
                movies.add(new Filme(movieJson));
            }

        } catch (JSONException e) {
           Log.e(TAG,"Erro ao decodificar JSON", e);
        }

        return movies;
    }

    public boolean isOnline() {
        ConnectivityManager cm =(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @OnClick(R.id.bReconectar) void clickReconectar(){

        carregoFilmes(order);

    }

}
