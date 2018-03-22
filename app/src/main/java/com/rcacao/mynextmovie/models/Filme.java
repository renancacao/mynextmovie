package com.rcacao.mynextmovie.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Filme implements Parcelable {

    private int id;
    private String poster;
    private String titulo;
    private String sinopse;
    private Double avaliacao;
    private String lancamento;

    public static final String EXTRA_FILME = "filme";

    public Filme() {

    }

    private Filme(Parcel in) {
        id = in.readInt();
        poster = in.readString();
        titulo = in.readString();
        sinopse = in.readString();
        if (in.readByte() == 0) {
            avaliacao = null;
        } else {
            avaliacao = in.readDouble();
        }
        lancamento = in.readString();
    }

    public static final Creator<Filme> CREATOR = new Creator<Filme>() {
        @Override
        public Filme createFromParcel(Parcel in) {
            return new Filme(in);
        }

        @Override
        public Filme[] newArray(int size) {
            return new Filme[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSinopse() {
        return sinopse;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public String getLancamento() {
        return lancamento;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public void setLancamento(String lancamento) {
        this.lancamento = lancamento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(poster);
        parcel.writeString(titulo);
        parcel.writeString(sinopse);
        if (avaliacao == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(avaliacao);
        }
        parcel.writeString(lancamento);
    }


}
