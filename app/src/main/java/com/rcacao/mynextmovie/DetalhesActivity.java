package com.rcacao.mynextmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetalhesActivity extends AppCompatActivity {


    private TextView tTitulo, tLancamento, tAvaliacao, tSinopse;
    private ImageView imgPoster;

    private Filme filme = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);


        Intent i = getIntent();
        if (i.hasExtra("filme")){
            filme = (Filme) i.getSerializableExtra("filme");
        }
        else
        {
            finish();
        }


        tTitulo = findViewById(R.id.tTitulo);
        tLancamento = findViewById(R.id.tLancamento);
        tAvaliacao = findViewById(R.id.tAvaliacao);
        tSinopse = findViewById(R.id.tSinopse);

        imgPoster = findViewById(R.id.imgPoster);


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

        return Utils.URL_POSTER + Utils.TAMANHO + filme.getPoster();
    }


}
