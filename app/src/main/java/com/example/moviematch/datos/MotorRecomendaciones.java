package com.example.moviematch.datos;

import android.content.Context;

import com.example.moviematch.datos.db.RepositorioDescartadasSQLite;
import com.example.moviematch.datos.modelo.Pelicula;
import com.example.moviematch.datos.preferencias.PreferenciasUsuario;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MotorRecomendaciones {

    private final Context context;
    private final RepositorioDescartadasSQLite repositorioDescartadasSQLite;
    private final Gson gson = new Gson();
    private final Type tipoListaPeliculas = new TypeToken<List<Pelicula>>() {
    }.getType();

    public MotorRecomendaciones(Context context) {
        this.context = context.getApplicationContext();
        this.repositorioDescartadasSQLite = new RepositorioDescartadasSQLite(context);
    }

    public List<Pelicula> obtenerRecomendaciones(PreferenciasUsuario preferenciasUsuario, String mood, Integer espacioDisponibleGb, String compania) {
        List<Pelicula> peliculas = cargarPeliculasDesdeAssets();
        Set<String> idsDescartadas = repositorioDescartadasSQLite.obtenerIdsDescartadas();
        List<Pelicula> filtradas = new ArrayList<>();

        for (Pelicula pelicula : peliculas) {
            if (idsDescartadas.contains(pelicula.getId())) {
                continue;
            }
            if (contieneCriterioAEvitar(pelicula, preferenciasUsuario.getEvitar())) {
                continue;
            }

            filtradas.add(pelicula);
        }

        filtradas.sort(Comparator.comparingInt(p -> -calcularPuntaje(p, preferenciasUsuario, mood, espacioDisponibleGb, compania)));
        if (filtradas.size() > 10) {
            return new ArrayList<>(filtradas.subList(0, 10));
        }
        return filtradas;
    }

    private int calcularPuntaje(Pelicula pelicula, PreferenciasUsuario preferenciasUsuario, String mood, Integer espacioDisponibleGb, String compania) {
        int puntaje = 0;

        if (mood != null && !mood.isEmpty() && pelicula.getMood() != null && mood.equalsIgnoreCase(pelicula.getMood())) {
            puntaje += 5;
        }

        if (pelicula.getGeneros() != null) {
            for (String genero : pelicula.getGeneros()) {
                if (preferenciasUsuario.getGeneros().contains(genero)) {
                    puntaje += 2;
                }
            }
        }

        if (pelicula.getPlataformas() != null) {
            for (String plataforma : pelicula.getPlataformas()) {
                if (preferenciasUsuario.getPlataformas().contains(plataforma)) {
                    puntaje += 1;
                }
            }
        }

        if (espacioDisponibleGb != null) {
            if (pelicula.getDuracionMin() <= espacioDisponibleGb) {
                puntaje += 2;
            } else {
                puntaje -= 1;
            }
        }

        if (compania != null && !compania.isEmpty()) {
            puntaje += 1;
        }

        return puntaje;
    }

    private boolean contieneCriterioAEvitar(Pelicula pelicula, List<String> evitar) {
        if (evitar == null || evitar.isEmpty()) {
            return false;
        }
        String sinopsis = pelicula.getSinopsis() != null ? pelicula.getSinopsis().toLowerCase(Locale.ROOT) : "";
        for (String criterio : evitar) {
            String criterioLower = criterio.toLowerCase(Locale.ROOT);
            if (sinopsis.contains(criterioLower)) {
                return true;
            }
            if (criterioLower.contains("micro")) {
                if (sinopsis.contains("microtransaccion")) {
                    return true;
                }
            }
            if (criterioLower.contains("grindeo") || criterioLower.contains("grind")) {
                if (sinopsis.contains("grindeo") || sinopsis.contains("farmeo")) {
                    return true;
                }
            }
            if (criterioLower.contains("pesado") && pelicula.getDuracionMin() > 90) {
                return true;
            }
            if (criterioLower.contains("dificultad") && (sinopsis.contains("difícil") || sinopsis.contains("desafiante"))) {
                return true;
            }
        }
        return false;
    }

    private List<Pelicula> cargarPeliculasDesdeAssets() {
        try (InputStream is = context.getAssets().open("peliculas.json");
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            List<Pelicula> peliculas = gson.fromJson(reader, tipoListaPeliculas);
            return peliculas != null ? peliculas : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
