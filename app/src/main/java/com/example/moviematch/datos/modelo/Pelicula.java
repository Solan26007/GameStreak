package com.example.moviematch.datos.modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Pelicula implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("titulo")
    private String titulo;
    @SerializedName("anio")
    private int anio;
    @SerializedName("duracionMin")
    private int duracionMin;
    @SerializedName("generos")
    private List<String> generos;
    @SerializedName("plataformas")
    private List<String> plataformas;
    @SerializedName("mood")
    private String mood;
    @SerializedName("sinopsis")
    private String sinopsis;
    @SerializedName("posterUrl")
    private String posterUrl;

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    public List<String> getPlataformas() {
        return plataformas;
    }

    public void setPlataformas(List<String> plataformas) {
        this.plataformas = plataformas;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setId(String id) {
        this.id = id;
    }
}
