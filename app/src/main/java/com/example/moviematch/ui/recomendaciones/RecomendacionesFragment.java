package com.example.moviematch.ui.recomendaciones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviematch.ActividadPrincipal;
import com.example.moviematch.R;
import com.example.moviematch.datos.MotorRecomendaciones;
import com.example.moviematch.datos.modelo.Pelicula;
import com.example.moviematch.datos.preferencias.GestorPreferenciasUsuario;
import com.example.moviematch.datos.preferencias.PreferenciasUsuario;
import com.example.moviematch.ui.common.PeliculaAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecomendacionesFragment extends Fragment {

    private static final String ARG_MOOD = "arg_mood";
    private static final String ARG_DURACION = "arg_duracion";
    private static final String ARG_COMPANIA = "arg_compania";

    private PeliculaAdapter peliculaAdapter;
    private ExecutorService executorService;
    private MotorRecomendaciones motorRecomendaciones;
    private GestorPreferenciasUsuario gestorPreferenciasUsuario;

    public static RecomendacionesFragment nuevaInstancia(String mood, Integer duracion, String compania) {
        RecomendacionesFragment fragment = new RecomendacionesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOOD, mood);
        if (duracion != null) {
            args.putInt(ARG_DURACION, duracion);
        }
        args.putString(ARG_COMPANIA, compania);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerMovies);

        peliculaAdapter = new PeliculaAdapter(new PeliculaAdapter.OnPeliculaClickListener() {
            @Override
            public void onPeliculaClick(Pelicula pelicula) {
                ((ActividadPrincipal) requireActivity()).irADetalle(pelicula);
            }

            @Override
            public void onPeliculaLongClick(Pelicula pelicula) {
                Toast.makeText(requireContext(), "Mantén para ver detalles", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(peliculaAdapter);

        executorService = Executors.newSingleThreadExecutor();
        motorRecomendaciones = new MotorRecomendaciones(requireContext());
        gestorPreferenciasUsuario = ((ActividadPrincipal) requireActivity()).obtenerGestorPreferenciasUsuario();

        getParentFragmentManager().setFragmentResultListener("pelicula_descartada", getViewLifecycleOwner(), (requestKey, result) -> {
            String peliculaId = result.getString("pelicula_id");
            if (peliculaId != null) {
                peliculaAdapter.eliminarPorId(peliculaId);
            }
        });

        cargarRecomendaciones();
    }

    private void cargarRecomendaciones() {
        executorService.execute(() -> {
            PreferenciasUsuario preferenciasUsuario = gestorPreferenciasUsuario.obtenerPreferencias();
            String mood = getArguments() != null ? getArguments().getString(ARG_MOOD) : null;
            Integer duracion = getArguments() != null && getArguments().containsKey(ARG_DURACION)
                    ? getArguments().getInt(ARG_DURACION) : null;
            String compania = getArguments() != null ? getArguments().getString(ARG_COMPANIA) : null;

            List<Pelicula> peliculas = motorRecomendaciones.obtenerRecomendaciones(preferenciasUsuario, mood, duracion, compania);

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> mostrarPeliculas(peliculas));
            }
        });
    }

    private void mostrarPeliculas(List<Pelicula> peliculas) {
        peliculaAdapter.actualizarPeliculas(peliculas);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
