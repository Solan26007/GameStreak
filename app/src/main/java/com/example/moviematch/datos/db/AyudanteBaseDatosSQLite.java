package com.example.moviematch.datos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AyudanteBaseDatosSQLite extends SQLiteOpenHelper {

    public static final String NOMBRE_BD = "pelis_app.db";
    private static final int VERSION = 1;

    public static final String TABLA_WATCHLIST = "watchlist";
    public static final String TABLA_DESCARTADAS = "descartadas";

    public AyudanteBaseDatosSQLite(Context context) {
        super(context, NOMBRE_BD, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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

        db.execSQL("CREATE TABLE " + TABLA_DESCARTADAS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pelicula_id TEXT UNIQUE NOT NULL, " +
                "fecha_descartado INTEGER" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_WATCHLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_DESCARTADAS);
        onCreate(db);
    }
}
