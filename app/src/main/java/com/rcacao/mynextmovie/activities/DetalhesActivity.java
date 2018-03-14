package com.rcacao.mynextmovie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.rcacao.mynextmovie.R;
import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetalhesActivity extends AppCompatActivity {


    @BindView(R.id.tTitulo) TextView tTitulo;
    @BindView(R.id.tLancamento) TextView tLancamento;
    @BindView(R.id.tAvaliacao) TextView tAvaliacao;
    @BindView(R.id.tSinopse) TextView tSinopse;
    @BindView(R.id.imgPoster) ImageView imgPoster;

    private Filme filme = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        ButterKnife.bind(this);


        Intent i = getIntent();
        if (i.hasExtra("filme")){
            filme = i.getParcelableExtra("filme");
        }
        else
        {
            finish();
        }

           carregaFilme();
    }

    private void carregaFilme() {

        Picasso.with(this).load(getPoster()).into(imgPoster);
        tTitulo.setText(filme.getTitulo());
        tAvaliacao.setText(String.valueOf(filme.getAvaliacao()));
        tLancamento.setText(filme.getLancamento());
        tSinopse.setText(filme.getSinopse());

    }

    private String getPoster() {

        return NetworkUtils.URL_POSTER + NetworkUtils.TAMANHO + filme.getPoster();
    }


}
