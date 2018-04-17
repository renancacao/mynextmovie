package com.rcacao.mynextmovie.models;

import com.google.gson.annotations.SerializedName;

public class Trailer  {


    private String key;

    @SerializedName("name")
    private String titulo;


    public String getTitulo() {
        return titulo;
    }

    public String getUrlImage(){
        return "https://i.ytimg.com/vi/"+ key +"/hqdefault.jpg";
    }

    public String getLink(){ return "http://www.youtube.com/watch?v="+key;}

}
