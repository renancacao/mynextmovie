package com.rcacao.mynextmovie.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.adapters.TrailerAdapter;
import com.rcacao.mynextmovie.interfaces.AsyncTaskTrailersDelegate;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.models.Trailer;
import com.rcacao.mynextmovie.utils.NetworkUtils;
import com.rcacao.mynextmovie.utils.TrailersService;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetalhesActivity extends AppCompatActivity implements AsyncTaskTrailersDelegate, TrailerAdapter.ListItemClickListener {


    @BindView(R.id.tTitulo) TextView tTitulo;
    @BindView(R.id.tLancamento) TextView tLancamento;
    @BindView(R.id.tAvaliacao) TextView tAvaliacao;
    @BindView(R.id.tSinopse) TextView tSinopse;
    @BindView(R.id.imgPoster) ImageView imgPoster;
    @BindView(R.id.progressBarTrailers) ProgressBar progressBarTrailers;
    @BindView(R.id.recyclerViewTrailers) RecyclerView recycleViewTrailers;
    @BindView(R.id.linearLayoutErro) LinearLayout linearLayoutErro;

    private TrailerAdapter adapter;
    private ArrayList<Trailer> trailers;


    private Filme filme = null;

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


        trailers = new ArrayList<>();

        adapter = new TrailerAdapter(this, trailers, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleViewTrailers.setLayoutManager(layoutManager);
        recycleViewTrailers.setHasFixedSize(true);

        recycleViewTrailers.setAdapter(adapter);

           carregaFilme();
    }

    private void carregaFilme() {

        Picasso.with(this).load(getPoster()).into(imgPoster);
        tTitulo.setText(filme.getTitulo());
        tAvaliacao.setText(String.valueOf(filme.getAvaliacao()));
        tLancamento.setText(filme.getLancamento());
        tSinopse.setText(filme.getSinopse());

        carregoTrailers(filme.getId());


    }

    private String getPoster() {

        return NetworkUtils.URL_POSTER + NetworkUtils.TAMANHO + filme.getPoster();
    }


    @Override
    public void processFinish(ArrayList<Trailer> outTrailers) {

        progressBarTrailers.setVisibility(View.INVISIBLE);

        if(outTrailers != null){
            trailers = outTrailers;
            adapter.setTrailers(trailers);
            adapter.notifyDataSetChanged();
        }else{
            recycleViewTrailers.setVisibility(View.INVISIBLE);
            linearLayoutErro.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void processStart() {

        progressBarTrailers.setVisibility(View.VISIBLE);

    }

    private void carregoTrailers(int id){

        if(NetworkUtils.isOnline(this)){
            recycleViewTrailers.setVisibility(View.VISIBLE);
            linearLayoutErro.setVisibility(View.INVISIBLE);
            URL trailersUrl = NetworkUtils.buidingUrlTrailers(id);
            new TrailersService(this,this).execute(trailersUrl);
        }
        else{
            recycleViewTrailers.setVisibility(View.INVISIBLE);
            linearLayoutErro.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        String video_path =  trailers.get(clickedItemIndex).getLink();
        Uri uri = Uri.parse(video_path);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
}
