package com.example.moviematch.datos.preferencias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PreferenciasUsuario implements Serializable {

    private List<String> generos = new ArrayList<>();
    private List<String> plataformas = new ArrayList<>();
    private List<String> evitar = new ArrayList<>();

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

    public List<String> getEvitar() {
        return evitar;
    }

    public void setEvitar(List<String> evitar) {
        this.evitar = evitar;
    }
}
