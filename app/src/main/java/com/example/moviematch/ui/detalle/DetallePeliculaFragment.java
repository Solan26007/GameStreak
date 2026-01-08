package com.example.moviematch.ui.detalle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.moviematch.R;
import com.example.moviematch.datos.db.RepositorioDescartadasSQLite;
import com.example.moviematch.datos.db.RepositorioWatchlistSQLite;
import com.example.moviematch.datos.modelo.Pelicula;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetallePeliculaFragment extends Fragment {

    private static final String ARG_PELICULA = "arg_pelicula";

    private Pelicula pelicula;
    private ExecutorService executorService;
    private RepositorioWatchlistSQLite repositorioWatchlistSQLite;
    private RepositorioDescartadasSQLite repositorioDescartadasSQLite;

    public static DetallePeliculaFragment nuevaInstancia(Pelicula pelicula) {
        DetallePeliculaFragment fragment = new DetallePeliculaFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PELICULA, pelicula);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        executorService = Executors.newSingleThreadExecutor();
        repositorioWatchlistSQLite = new RepositorioWatchlistSQLite(requireContext());
        repositorioDescartadasSQLite = new RepositorioDescartadasSQLite(requireContext());

        pelicula = (Pelicula) (getArguments() != null ? getArguments().getSerializable(ARG_PELICULA) : null);
        if (pelicula == null) {
            Toast.makeText(requireContext(), "No se pudo cargar la película", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageView imgPoster = view.findViewById(R.id.imgPosterBig);
        TextView txtTitulo = view.findViewById(R.id.txtMovieTitleDetail);
        TextView txtMeta = view.findViewById(R.id.txtMovieMetaDetail);
        TextView txtPlataformas = view.findViewById(R.id.txtMoviePlatformsDetail);
        TextView txtSinopsis = view.findViewById(R.id.txtMovieSynopsis);
        ChipGroup chipGroupTags = view.findViewById(R.id.chipGroupTags);
        MaterialButton btnWatchlist = view.findViewById(R.id.btnAddToWatchlist);
        MaterialButton btnDislike = view.findViewById(R.id.btnDislike);

        txtTitulo.setText(pelicula.getTitulo());
        String generosTexto = pelicula.getGeneros() != null ? String.join(", ", pelicula.getGeneros()) : "";
        String meta = String.format(Locale.getDefault(), "%d · %dm · %s", pelicula.getAnio(), pelicula.getDuracionMin(), generosTexto);
        txtMeta.setText(meta);
        String plataformasTexto = pelicula.getPlataformas() != null ? String.join(", ", pelicula.getPlataformas()) : "";
        txtPlataformas.setText(String.format(Locale.getDefault(), "Disponible en: %s", plataformasTexto));
        txtSinopsis.setText(pelicula.getSinopsis());

        chipGroupTags.removeAllViews();
        if (pelicula.getGeneros() != null) {
            for (String genero : pelicula.getGeneros()) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_mini, chipGroupTags, false);
                chip.setText(genero);
                chipGroupTags.addView(chip);
            }
        }

        Glide.with(this)
                .load(pelicula.getPosterUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(imgPoster);

        btnWatchlist.setOnClickListener(v -> guardarEnWatchlist());
        btnDislike.setOnClickListener(v -> marcarComoNoInteresa());
    }

    private void guardarEnWatchlist() {
        executorService.execute(() -> {
            boolean guardado = repositorioWatchlistSQLite.guardarPelicula(pelicula);
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    String mensaje = guardado ? "Guardada en Ver después" : "Ya estaba en Ver después";
                    Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void marcarComoNoInteresa() {
        executorService.execute(() -> {
            boolean descartada = repositorioDescartadasSQLite.descartarPelicula(pelicula.getId());
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    if (descartada) {
                        Bundle result = new Bundle();
                        result.putString("pelicula_id", pelicula.getId());
                        getParentFragmentManager().setFragmentResult("pelicula_descartada", result);
                        Toast.makeText(requireContext(), "Marcada como no me interesa", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(requireContext(), "Ya estaba marcada", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
