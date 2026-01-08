package com.example.moviematch.ui.bienvenida;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviematch.ActividadPrincipal;
import com.example.moviematch.R;
import com.example.moviematch.datos.preferencias.GestorPreferenciasUsuario;
import com.google.android.material.button.MaterialButton;

public class BienvenidaFragment extends Fragment {

    private GestorPreferenciasUsuario gestorPreferenciasUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gestorPreferenciasUsuario = ((ActividadPrincipal) requireActivity()).obtenerGestorPreferenciasUsuario();

        MaterialButton btnEmpezar = view.findViewById(R.id.btnEmpezar);
        btnEmpezar.setOnClickListener(v -> navegarDesdeBienvenida());
    }

    private void navegarDesdeBienvenida() {
        if (gestorPreferenciasUsuario == null) {
            return;
        }

        if (gestorPreferenciasUsuario.onboardingCompletado()) {
            ((ActividadPrincipal) requireActivity()).irAInicio();
        } else {
            ((ActividadPrincipal) requireActivity()).irAOnboarding();
        }
    }
}
