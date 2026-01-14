package com.example.moviematch.ui.topgeneros;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviematch.ActividadPrincipal;
import com.example.moviematch.R;
import com.example.moviematch.datos.modelo.Pelicula;
import com.example.moviematch.ui.common.JuegoAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TopGenerosFragment extends Fragment {

    private static final int LIMITE_TOP = 5;

    private final Type tipoLista = new TypeToken<List<Pelicula>>() {}.getType();
    private JuegoAdapter peliculaAdapter;
    private TextView txtEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_genres, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = view.findViewById(R.id.spinnerGenres);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerTopGenres);
        txtEmpty = view.findViewById(R.id.txtEmptyTopGenres);

        peliculaAdapter = new JuegoAdapter(new JuegoAdapter.OnPeliculaClickListener() {
            @Override
            public void onPeliculaClick(Pelicula pelicula) {
                ((ActividadPrincipal) requireActivity()).irADetalle(pelicula);
            }

            @Override
            public void onPeliculaLongClick(Pelicula pelicula) {
            }
        });
        recyclerView.setAdapter(peliculaAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.game_genres,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                CharSequence selected = adapter.getItem(position);
                if (selected != null) {
                    mostrarTopPorGenero(selected.toString());
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        if (adapter.getCount() > 0) {
            spinner.setSelection(0);
        }
    }

    private void mostrarTopPorGenero(String genero) {
        List<Pelicula> peliculas = cargarPeliculas(requireContext());
        List<Pelicula> filtradas = new ArrayList<>();
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getGeneros() != null && pelicula.getGeneros().contains(genero)) {
                filtradas.add(pelicula);
            }
        }

        filtradas.sort(Comparator.comparingInt(Pelicula::getAnio).reversed());

        List<Pelicula> top = filtradas.subList(0, Math.min(LIMITE_TOP, filtradas.size()));
        peliculaAdapter.actualizarPeliculas(top);
        txtEmpty.setVisibility(top.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private List<Pelicula> cargarPeliculas(Context context) {
        try (InputStream is = context.getAssets().open("peliculas.json");
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            List<Pelicula> peliculas = gson.fromJson(reader, tipoLista);
            return peliculas != null ? peliculas : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

}
