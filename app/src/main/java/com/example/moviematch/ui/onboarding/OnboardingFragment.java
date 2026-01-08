package com.example.moviematch.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviematch.ActividadPrincipal;
import com.example.moviematch.R;
import com.example.moviematch.datos.preferencias.GestorPreferenciasUsuario;
import com.example.moviematch.datos.preferencias.PreferenciasUsuario;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnboardingFragment extends Fragment {

    private ExecutorService executorService;
    private GestorPreferenciasUsuario gestorPreferenciasUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        executorService = Executors.newSingleThreadExecutor();
        gestorPreferenciasUsuario = ((ActividadPrincipal) requireActivity()).obtenerGestorPreferenciasUsuario();

        LinearLayout layoutGeneros = view.findViewById(R.id.layoutGenres);
        LinearLayout layoutPlataformas = view.findViewById(R.id.layoutPlatforms);
        LinearLayout layoutEvitar = view.findViewById(R.id.layoutAvoid);
        MaterialButton btnContinuar = view.findViewById(R.id.btnFinishOnboarding);
        MaterialButton btnSaltar = view.findViewById(R.id.btnSkipOnboarding);

        btnContinuar.setOnClickListener(v -> guardarPreferencias(layoutGeneros, layoutPlataformas, layoutEvitar));
        btnSaltar.setOnClickListener(v -> {
            gestorPreferenciasUsuario.marcarOnboardingCompletado(true);
            ((ActividadPrincipal) requireActivity()).irAInicio();
        });
    }

    private void guardarPreferencias(LinearLayout generos, LinearLayout plataformas, LinearLayout evitar) {
        executorService.execute(() -> {
            PreferenciasUsuario preferenciasUsuario = new PreferenciasUsuario();
            preferenciasUsuario.setGeneros(obtenerTextosSeleccionados(generos));
            preferenciasUsuario.setPlataformas(obtenerTextosSeleccionados(plataformas));
            preferenciasUsuario.setEvitar(obtenerTextosSeleccionados(evitar));

            gestorPreferenciasUsuario.guardarPreferencias(preferenciasUsuario);
            gestorPreferenciasUsuario.marcarOnboardingCompletado(true);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Preferencias guardadas", Toast.LENGTH_SHORT).show();
                    ((ActividadPrincipal) requireActivity()).irAInicio();
                });
            }
        });
    }

    private List<String> obtenerTextosSeleccionados(LinearLayout contenedor) {
        List<String> seleccionados = new ArrayList<>();
        for (int i = 0; i < contenedor.getChildCount(); i++) {
            View hijo = contenedor.getChildAt(i);
            if (hijo instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) hijo;
                if (checkBox.isChecked()) {
                    seleccionados.add(checkBox.getText().toString());
                }
            }
        }
        return seleccionados;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
