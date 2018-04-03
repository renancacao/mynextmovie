package com.rcacao.mynextmovie.interfaces;

import com.rcacao.mynextmovie.models.Filme;
import com.rcacao.mynextmovie.models.Trailer;

import java.util.ArrayList;

public interface AsyncTaskTrailersDelegate {
    void processFinish(ArrayList<Trailer> output);
    void processStart();
}
