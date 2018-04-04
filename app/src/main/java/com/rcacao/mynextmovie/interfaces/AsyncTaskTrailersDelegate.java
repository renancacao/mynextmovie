package com.rcacao.mynextmovie.interfaces;

import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.models.Trailer;

import java.util.ArrayList;

public interface AsyncTaskTrailersDelegate {
    void processFinishTrailers(ArrayList<Trailer> output);
    void processStartTrailers();
}
