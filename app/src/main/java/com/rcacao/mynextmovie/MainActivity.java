package com.rcacao.mynextmovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener{

    private final String TAG = getClass().getName();

    private RecyclerView rcMovies;
    private ProgressBar pgLoading;

    private String order = "popular";
    private MovieAdapter adapter;
    private ArrayList<MyMovie> filmes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcMovies = findViewById(R.id.rcMovies);
        pgLoading = findViewById(R.id.pgLoading);

        GridLayoutManager grid = new GridLayoutManager(this,2);
        rcMovies.setLayoutManager(grid);
        rcMovies.setHasFixedSize(true);

        filmes = new ArrayList<>();

        adapter = new MovieAdapter(this, filmes, this);

        rcMovies.setAdapter(adapter);

        carregoFilmes(order);

    }

    private void carregoFilmes(String order){

        URL dbMovieUrl = Utils.buidingUrlDbMovies(order);
        new MoviesQueryTask().execute(dbMovieUrl);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.mnuPop:
                order = "popular";
                carregoFilmes(order);
                break;

            case R.id.mnuRat:
                order = "top_rated";
                carregoFilmes(order);
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
            String jsonMovies =  "";
            try {
                jsonMovies = Utils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                Log.e(TAG,"Erro ao requerir filmes", e);
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

        }
    }

    private ArrayList<MyMovie> getMovies(String jsonString) {

        ArrayList<MyMovie> movies = new ArrayList<>();

        try {
            JSONObject pageJson = new JSONObject(jsonString);
            JSONArray resultsJSON = pageJson.getJSONArray("results");


            JSONObject movieJson;

            for (int i=0; i<resultsJSON.length();i++){
                movieJson = new JSONObject(resultsJSON.getString(i));
                movies.add(new MyMovie(movieJson));
            }

        } catch (JSONException e) {
           Log.e(TAG,"Erro ao decodificar JSON", e);
        }

        return movies;
    }


}
