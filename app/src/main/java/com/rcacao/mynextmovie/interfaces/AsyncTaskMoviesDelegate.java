package com.rcacao.mynextmovie.interfaces;

import com.rcacao.mynextmovie.models.Filme;
import java.util.ArrayList;

public interface AsyncTaskMoviesDelegate {
    void processFinishMovies(ArrayList<Filme> output);
    void processStart();
}
