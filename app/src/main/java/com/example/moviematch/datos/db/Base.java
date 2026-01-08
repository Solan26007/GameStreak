package com.example.moviematch.datos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Base de datos SQLite (estilo "Base" como el ejemplo que compartiste).
 *
 * Nota:
 * - Aquí se crean las tablas necesarias para MoviesMatch.
 * - Se deja un constructor con la firma clásica (context, name, factory, version)
 *   para que puedas usarla exactamente como en el ejemplo.
 */
public class Base extends SQLiteOpenHelper {

    public static final String NOMBRE_BD = "pelis_app.db";
    public static final int VERSION_BD = 1;

    // Tablas
    public static final String TABLA_WATCHLIST = "watchlist";
    public static final String TABLA_DESCARTADAS = "descartadas";

    public Base(@Nullable Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    /**
     * Constructor con la misma firma que tu ejemplo.
     * Úsalo si quieres pasar el nombre de la BD manualmente.
     */
    public Base(@Nullable Context context,
                @Nullable String name,
                @Nullable SQLiteDatabase.CursorFactory factory,
                int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Watchlist (Ver después)
        db.execSQL("CREATE TABLE " + TABLA_WATCHLIST + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pelicula_id TEXT UNIQUE NOT NULL, " +
                "titulo TEXT, " +
                "anio INTEGER, " +
                "duracion_min INTEGER, " +
                "generos TEXT, " +
                "plataformas TEXT, " +
                "mood TEXT, " +
                "sinopsis TEXT, " +
                "poster_url TEXT, " +
                "fecha_guardado INTEGER" +
                ");");

        // Descartadas (No me interesa)
        db.execSQL("CREATE TABLE " + TABLA_DESCARTADAS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pelicula_id TEXT UNIQUE NOT NULL, " +
                "fecha_descartado INTEGER" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Para este proyecto, lo más simple es recrear.
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_WATCHLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_DESCARTADAS);
        onCreate(db);
    }
}
