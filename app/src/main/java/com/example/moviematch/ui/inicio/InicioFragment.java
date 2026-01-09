package com.example.moviematch.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviematch.ActividadPrincipal;
import com.example.moviematch.R;
import com.google.android.material.button.MaterialButton;

public class InicioFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup radioGroupTime = view.findViewById(R.id.radioGroupTime);
        RadioGroup radioGroupMood = view.findViewById(R.id.radioGroupMood);
        RadioGroup radioGroupCompany = view.findViewById(R.id.radioGroupCompany);
        MaterialButton btnBuscar = view.findViewById(R.id.btnBuscarPelicula);
        MaterialButton btnModoGrupo = view.findViewById(R.id.btnModoGrupo);

        btnBuscar.setOnClickListener(v -> navegarARecomendaciones(radioGroupTime, radioGroupMood, radioGroupCompany));
        btnModoGrupo.setOnClickListener(v -> ((ActividadPrincipal) requireActivity()).irAModoGrupo());
    }

    private void navegarARecomendaciones(RadioGroup radioGroupTime, RadioGroup radioGroupMood, RadioGroup radioGroupCompany) {
        Integer duracionMax = obtenerDuracionSeleccionada(radioGroupTime);
        String mood = obtenerTextoSeleccionado(radioGroupMood);
        String compania = obtenerTextoSeleccionado(radioGroupCompany);

        if (mood == null || mood.isEmpty()) {
            Toast.makeText(requireContext(), "Selecciona un mood de juego para recomendar", Toast.LENGTH_SHORT).show();
            return;
        }

        ((ActividadPrincipal) requireActivity()).irARecomendaciones(mood, duracionMax, compania);
    }

    private Integer obtenerDuracionSeleccionada(RadioGroup radioGroupTime) {
        int id = radioGroupTime.getCheckedRadioButtonId();
        if (id == View.NO_ID) {
            return null;
        }
        if (id == R.id.radioTime30) {
            return 30;
        } else if (id == R.id.radioTime60) {
            return 60;
        } else if (id == R.id.radioTime90) {
            return 120;
        } else if (id == R.id.radioTime120) {
            return 240;
        }
        return null;
    }

    private String obtenerTextoSeleccionado(RadioGroup radioGroup) {
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == View.NO_ID) {
            return null;
        }
        RadioButton radioButton = radioGroup.findViewById(id);
        if (radioButton != null) {
            return radioButton.getText().toString();
        }
        return null;
    }
}
