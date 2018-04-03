package com.rcacao.mynextmovie.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Trailer  {

    private String id;
    private String key;
    private String titulo;


    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrlImage(){
        return "https://i.ytimg.com/vi/"+ key +"/hqdefault.jpg";
    }

    public String getLink(){ return "http://www.youtube.com/watch?v="+key;}

    public void setId(String id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
