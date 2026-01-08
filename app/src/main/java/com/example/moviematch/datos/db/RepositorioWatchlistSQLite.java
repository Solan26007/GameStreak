package com.example.moviematch.datos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.moviematch.datos.modelo.Pelicula;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioWatchlistSQLite {

    private final AyudanteBaseDatosSQLite ayudanteBaseDatosSQLite;
    private final Gson gson = new Gson();
    private final Type tipoListaString = new TypeToken<List<String>>() {
    }.getType();

    public RepositorioWatchlistSQLite(Context context) {
        ayudanteBaseDatosSQLite = new AyudanteBaseDatosSQLite(context.getApplicationContext());
    }

    public boolean guardarPelicula(Pelicula pelicula) {
        SQLiteDatabase db = ayudanteBaseDatosSQLite.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("pelicula_id", pelicula.getId());
        valores.put("titulo", pelicula.getTitulo());
        valores.put("anio", pelicula.getAnio());
        valores.put("duracion_min", pelicula.getDuracionMin());
        valores.put("generos", gson.toJson(pelicula.getGeneros()));
        valores.put("plataformas", gson.toJson(pelicula.getPlataformas()));
        valores.put("mood", pelicula.getMood());
        valores.put("sinopsis", pelicula.getSinopsis());
        valores.put("poster_url", pelicula.getPosterUrl());
        valores.put("fecha_guardado", System.currentTimeMillis());

        try {
            long resultado = db.insertWithOnConflict(
                    AyudanteBaseDatosSQLite.TABLA_WATCHLIST,
                    null,
                    valores,
                    SQLiteDatabase.CONFLICT_IGNORE
            );
            return resultado != -1;
        } catch (SQLiteException e) {
            return false;
        }
    }

    public boolean eliminarPeliculaPorId(String peliculaId) {
        SQLiteDatabase db = ayudanteBaseDatosSQLite.getWritableDatabase();
        int filas = db.delete(
                AyudanteBaseDatosSQLite.TABLA_WATCHLIST,
                "pelicula_id = ?",
                new String[]{peliculaId}
        );
        return filas > 0;
    }

    public boolean existeEnWatchlist(String peliculaId) {
        SQLiteDatabase db = ayudanteBaseDatosSQLite.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT pelicula_id FROM " + AyudanteBaseDatosSQLite.TABLA_WATCHLIST + " WHERE pelicula_id = ?",
                    new String[]{peliculaId}
            );
            return cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<Pelicula> obtenerWatchlist() {
        SQLiteDatabase db = ayudanteBaseDatosSQLite.getReadableDatabase();
        Cursor cursor = null;
        List<Pelicula> peliculas = new ArrayList<>();
        try {
            cursor = db.rawQuery(
                    "SELECT pelicula_id, titulo, anio, duracion_min, generos, plataformas, mood, sinopsis, poster_url FROM " +
                            AyudanteBaseDatosSQLite.TABLA_WATCHLIST + " ORDER BY fecha_guardado DESC",
                    null
            );
            while (cursor.moveToNext()) {
                Pelicula pelicula = new Pelicula();
                asignarValoresDesdeCursor(cursor, pelicula);
                peliculas.add(pelicula);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return peliculas;
    }

    private void asignarValoresDesdeCursor(Cursor cursor, Pelicula pelicula) {
        pelicula.setId(cursor.getString(0));
        pelicula.setTitulo(cursor.getString(1));
        pelicula.setAnio(cursor.getInt(2));
        pelicula.setDuracionMin(cursor.getInt(3));
        String generosJson = cursor.getString(4);
        String plataformasJson = cursor.getString(5);
        pelicula.setGeneros(convertirALista(generosJson));
        pelicula.setPlataformas(convertirALista(plataformasJson));
        pelicula.setMood(cursor.getString(6));
        pelicula.setSinopsis(cursor.getString(7));
        pelicula.setPosterUrl(cursor.getString(8));
    }

    private List<String> convertirALista(String json) {
        if (json == null) {
            return Collections.emptyList();
        }
        try {
            List<String> lista = gson.fromJson(json, tipoListaString);
            return lista != null ? lista : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
