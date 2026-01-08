package com.example.moviematch.datos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.HashSet;
import java.util.Set;

public class RepositorioDescartadasSQLite {

    private final AyudanteBaseDatosSQLite ayudanteBaseDatosSQLite;

    public RepositorioDescartadasSQLite(Context context) {
        ayudanteBaseDatosSQLite = new AyudanteBaseDatosSQLite(context.getApplicationContext());
    }

    public boolean descartarPelicula(String peliculaId) {
        SQLiteDatabase db = ayudanteBaseDatosSQLite.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("pelicula_id", peliculaId);
        valores.put("fecha_descartado", System.currentTimeMillis());
        try {
            long resultado = db.insertWithOnConflict(
                    AyudanteBaseDatosSQLite.TABLA_DESCARTADAS,
                    null,
                    valores,
                    SQLiteDatabase.CONFLICT_IGNORE
            );
            return resultado != -1;
        } catch (SQLiteException e) {
            return false;
        }
    }

    public boolean estaDescartada(String peliculaId) {
        SQLiteDatabase db = ayudanteBaseDatosSQLite.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT pelicula_id FROM " + AyudanteBaseDatosSQLite.TABLA_DESCARTADAS + " WHERE pelicula_id = ?",
                    new String[]{peliculaId}
            );
            return cursor.moveToFirst();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Set<String> obtenerIdsDescartadas() {
        SQLiteDatabase db = ayudanteBaseDatosSQLite.getReadableDatabase();
        Cursor cursor = null;
        Set<String> ids = new HashSet<>();
        try {
            cursor = db.rawQuery(
                    "SELECT pelicula_id FROM " + AyudanteBaseDatosSQLite.TABLA_DESCARTADAS,
                    null
            );
            while (cursor.moveToNext()) {
                ids.add(cursor.getString(0));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ids;
    }
}
