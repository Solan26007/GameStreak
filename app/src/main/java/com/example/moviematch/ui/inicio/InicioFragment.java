package com.example.moviematch.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviematch.ActividadPrincipal;
import com.example.moviematch.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class InicioFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup radioGroupMood = view.findViewById(R.id.radioGroupMood);
        RadioGroup radioGroupCompany = view.findViewById(R.id.radioGroupCompany);
        TextInputEditText inputStorageGb = view.findViewById(R.id.inputStorageGb);
        MaterialButton btnBuscar = view.findViewById(R.id.btnBuscarPelicula);

        btnBuscar.setOnClickListener(v -> navegarARecomendaciones(inputStorageGb, radioGroupMood, radioGroupCompany));
    }

    private void navegarARecomendaciones(TextInputEditText inputStorageGb, RadioGroup radioGroupMood, RadioGroup radioGroupCompany) {
        Integer espacioDisponibleGb = obtenerEspacioDisponible(inputStorageGb);
        String mood = obtenerTextoSeleccionado(radioGroupMood);
        String compania = obtenerTextoSeleccionado(radioGroupCompany);

        if (mood == null || mood.isEmpty()) {
            Toast.makeText(requireContext(), "Selecciona un mood de juego para recomendar", Toast.LENGTH_SHORT).show();
            return;
        }

        ((ActividadPrincipal) requireActivity()).irARecomendaciones(mood, espacioDisponibleGb, compania);
    }

    private Integer obtenerEspacioDisponible(TextInputEditText inputStorageGb) {
        if (inputStorageGb == null || inputStorageGb.getText() == null) {
            return null;
        }
        String texto = inputStorageGb.getText().toString().trim();
        if (texto.isEmpty()) {
            return null;
        }
        try {
            int valor = Integer.parseInt(texto);
            if (valor < 0) {
                Toast.makeText(requireContext(), "Ingresa un valor válido de GB libres", Toast.LENGTH_SHORT).show();
                return null;
            }
            return valor;
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Ingresa un valor válido de GB libres", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private String obtenerTextoSeleccionado(RadioGroup radioGroup) {
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == View.NO_ID) {
            return null;
        }
        android.widget.RadioButton radioButton = radioGroup.findViewById(id);
        if (radioButton != null) {
            return radioButton.getText().toString();
        }
        return null;
    }
}
