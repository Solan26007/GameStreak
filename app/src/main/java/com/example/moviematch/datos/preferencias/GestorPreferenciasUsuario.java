package com.example.moviematch.datos.preferencias;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class GestorPreferenciasUsuario {

    private static final String PREFS_NAME = "preferencias_usuario";
    private static final String KEY_ONBOARDING = "onboarding_completado";
    private static final String KEY_PREFERENCIAS = "preferencias";

    private final SharedPreferences sharedPreferences;
    private final Gson gson = new Gson();

    public GestorPreferenciasUsuario(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void guardarPreferencias(PreferenciasUsuario preferenciasUsuario) {
        String json = gson.toJson(preferenciasUsuario);
        sharedPreferences.edit().putString(KEY_PREFERENCIAS, json).apply();
    }

    public PreferenciasUsuario obtenerPreferencias() {
        String json = sharedPreferences.getString(KEY_PREFERENCIAS, null);
        if (json == null) {
            return new PreferenciasUsuario();
        }
        try {
            return gson.fromJson(json, PreferenciasUsuario.class);
        } catch (Exception e) {
            return new PreferenciasUsuario();
        }
    }

    public void marcarOnboardingCompletado(boolean completado) {
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING, completado).apply();
    }

    public boolean onboardingCompletado() {
        return sharedPreferences.getBoolean(KEY_ONBOARDING, false);
    }
}
