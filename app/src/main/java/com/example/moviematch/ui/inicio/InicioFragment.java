package com.example.moviematch.ui.inicio;

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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class InicioFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChipGroup chipGroupTime = view.findViewById(R.id.chipGroupTime);
        ChipGroup chipGroupMood = view.findViewById(R.id.chipGroupMood);
        ChipGroup chipGroupCompany = view.findViewById(R.id.chipGroupCompany);
        MaterialButton btnBuscar = view.findViewById(R.id.btnBuscarPelicula);
        MaterialButton btnModoGrupo = view.findViewById(R.id.btnModoGrupo);

        btnBuscar.setOnClickListener(v -> navegarARecomendaciones(chipGroupTime, chipGroupMood, chipGroupCompany));
        btnModoGrupo.setOnClickListener(v -> ((ActividadPrincipal) requireActivity()).irAModoGrupo());
    }

    private void navegarARecomendaciones(ChipGroup chipGroupTime, ChipGroup chipGroupMood, ChipGroup chipGroupCompany) {
        Integer duracionMax = obtenerDuracionSeleccionada(chipGroupTime);
        String mood = obtenerTextoChip(chipGroupMood);
        String compania = obtenerTextoChip(chipGroupCompany);

        if (mood == null || mood.isEmpty()) {
            Toast.makeText(requireContext(), "Selecciona un mood para recomendar", Toast.LENGTH_SHORT).show();
            return;
        }

        ((ActividadPrincipal) requireActivity()).irARecomendaciones(mood, duracionMax, compania);
    }

    private Integer obtenerDuracionSeleccionada(ChipGroup chipGroupTime) {
        int id = chipGroupTime.getCheckedChipId();
        if (id == View.NO_ID) {
            return null;
        }
        if (id == R.id.chipTime30) {
            return 45;
        } else if (id == R.id.chipTime60) {
            return 70;
        } else if (id == R.id.chipTime90) {
            return 100;
        } else if (id == R.id.chipTime120) {
            return 140;
        }
        return null;
    }

    private String obtenerTextoChip(ChipGroup chipGroup) {
        int id = chipGroup.getCheckedChipId();
        if (id == View.NO_ID) {
            return null;
        }
        Chip chip = chipGroup.findViewById(id);
        if (chip != null) {
            return chip.getText().toString();
        }
        return null;
    }
}
