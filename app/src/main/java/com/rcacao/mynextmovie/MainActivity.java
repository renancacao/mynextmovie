package com.rcacao.mynextmovie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener{

    private RecyclerView rcMovies;
    private ProgressBar pgLoading;

    private String order = "popular";
    private MovieAdapter adapter;
    private ArrayList<MyMovie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcMovies = findViewById(R.id.rcMovies);
        pgLoading = findViewById(R.id.pgLoading);

        GridLayoutManager grid = new GridLayoutManager(this,2);
        rcMovies.setLayoutManager(grid);
        rcMovies.setHasFixedSize(true);

        movies= new ArrayList<>();

        adapter = new MovieAdapter(this, movies, this);


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
            case R.id.mnuRat:
                order = "top_rated";
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(this,String.valueOf(clickedItemIndex),Toast.LENGTH_LONG).show();
    }


    class MoviesQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgLoading.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(URL... urls) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pgLoading.setVisibility(View.INVISIBLE);
        }
    }


}
