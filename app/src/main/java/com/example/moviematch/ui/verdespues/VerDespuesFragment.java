package com.example.moviematch.ui.verdespues;

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
import com.example.moviematch.datos.db.RepositorioWatchlistSQLite;
import com.example.moviematch.datos.modelo.Pelicula;
import com.example.moviematch.ui.common.PeliculaAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VerDespuesFragment extends Fragment {

    private RepositorioWatchlistSQLite repositorioWatchlistSQLite;
    private ExecutorService executorService;
    private PeliculaAdapter peliculaAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_watchlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repositorioWatchlistSQLite = new RepositorioWatchlistSQLite(requireContext());
        executorService = Executors.newSingleThreadExecutor();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerWatchlist);
        peliculaAdapter = new PeliculaAdapter(new PeliculaAdapter.OnPeliculaClickListener() {
            @Override
            public void onPeliculaClick(Pelicula pelicula) {
                ((ActividadPrincipal) requireActivity()).irADetalle(pelicula);
            }

            @Override
            public void onPeliculaLongClick(Pelicula pelicula) {
                eliminarPelicula(pelicula);
            }
        });
        recyclerView.setAdapter(peliculaAdapter);

        cargarWatchlist();
    }

    private void cargarWatchlist() {
        executorService.execute(() -> {
            List<Pelicula> peliculas = repositorioWatchlistSQLite.obtenerWatchlist();
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> peliculaAdapter.actualizarPeliculas(peliculas));
            }
        });
    }

    private void eliminarPelicula(Pelicula pelicula) {
        executorService.execute(() -> {
            boolean eliminada = repositorioWatchlistSQLite.eliminarPeliculaPorId(pelicula.getId());
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    if (eliminada) {
                        peliculaAdapter.eliminarPorId(pelicula.getId());
                        Toast.makeText(requireContext(), "Eliminada de Ver después", Toast.LENGTH_SHORT).show();
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
