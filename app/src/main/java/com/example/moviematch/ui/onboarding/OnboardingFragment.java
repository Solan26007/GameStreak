package com.example.moviematch.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviematch.ActividadPrincipal;
import com.example.moviematch.R;
import com.example.moviematch.datos.preferencias.GestorPreferenciasUsuario;
import com.example.moviematch.datos.preferencias.PreferenciasUsuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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

        ChipGroup chipGroupGeneros = view.findViewById(R.id.chipGroupGenres);
        ChipGroup chipGroupPlataformas = view.findViewById(R.id.chipGroupPlatforms);
        ChipGroup chipGroupEvitar = view.findViewById(R.id.chipGroupAvoid);
        MaterialButton btnContinuar = view.findViewById(R.id.btnFinishOnboarding);
        MaterialButton btnSaltar = view.findViewById(R.id.btnSkipOnboarding);

        btnContinuar.setOnClickListener(v -> guardarPreferencias(chipGroupGeneros, chipGroupPlataformas, chipGroupEvitar));
        btnSaltar.setOnClickListener(v -> {
            gestorPreferenciasUsuario.marcarOnboardingCompletado(true);
            ((ActividadPrincipal) requireActivity()).irAInicio();
        });
    }

    private void guardarPreferencias(ChipGroup generos, ChipGroup plataformas, ChipGroup evitar) {
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

    private List<String> obtenerTextosSeleccionados(ChipGroup chipGroup) {
        List<String> seleccionados = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            View hijo = chipGroup.getChildAt(i);
            if (hijo instanceof Chip) {
                Chip chip = (Chip) hijo;
                if (chip.isChecked()) {
                    seleccionados.add(chip.getText().toString());
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
