package com.example.moviematch.ui.modogrupo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moviematch.R;
import com.google.android.material.button.MaterialButton;

public class ModoGrupoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialButton btnCrearSala = view.findViewById(R.id.btnCreateRoom);
        MaterialButton btnUnirse = view.findViewById(R.id.btnJoinRoom);

        btnCrearSala.setOnClickListener(v -> Toast.makeText(requireContext(), "Funcionalidad en construcción", Toast.LENGTH_SHORT).show());
        btnUnirse.setOnClickListener(v -> Toast.makeText(requireContext(), "Funcionalidad en construcción", Toast.LENGTH_SHORT).show());
    }
}
