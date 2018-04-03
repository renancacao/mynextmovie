package com.rcacao.mynextmovie.interfaces;

import com.rcacao.mynextmovie.models.Filme;
import java.util.ArrayList;

public interface AsyncTaskMoviesDelegate {
    void processFinish(ArrayList<Filme> output);
    void processStart();
}
