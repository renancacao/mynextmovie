package com.rcacao.mynextmovie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetalhesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        MyMovie filme = null;

        Intent i = getIntent();
        if (i.hasExtra("filme")){
            filme = (MyMovie) i.getSerializableExtra("filme");
        }
        else
        {
            finish();
        }

        carregaFilme();
    }

    private void carregaFilme() {

    }


}
