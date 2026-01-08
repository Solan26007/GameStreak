package com.example.moviematch.ui.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviematch.R;
import com.example.moviematch.datos.modelo.Pelicula;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PeliculaAdapter extends RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder> {

    public interface OnPeliculaClickListener {
        void onPeliculaClick(Pelicula pelicula);

        void onPeliculaLongClick(Pelicula pelicula);
    }

    private final List<Pelicula> peliculas = new ArrayList<>();
    private final OnPeliculaClickListener listener;

    public PeliculaAdapter(OnPeliculaClickListener listener) {
        this.listener = listener;
    }

    public void actualizarPeliculas(List<Pelicula> nuevasPeliculas) {
        peliculas.clear();
        if (nuevasPeliculas != null) {
            peliculas.addAll(nuevasPeliculas);
        }
        notifyDataSetChanged();
    }

    public void eliminarPorId(String peliculaId) {
        for (int i = 0; i < peliculas.size(); i++) {
            if (peliculas.get(i).getId().equals(peliculaId)) {
                peliculas.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public Pelicula obtenerEnPosicion(int posicion) {
        return peliculas.get(posicion);
    }

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new PeliculaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        Pelicula pelicula = peliculas.get(position);
        holder.bind(pelicula, listener);
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

    static class PeliculaViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgPoster;
        private final TextView txtTitulo;
        private final TextView txtInfo;
        private final TextView txtMood;
        private final TextView txtPlataformas;

        PeliculaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtTitulo = itemView.findViewById(R.id.txtMovieTitle);
            txtInfo = itemView.findViewById(R.id.txtMovieInfo);
            txtMood = itemView.findViewById(R.id.txtMovieMood);
            txtPlataformas = itemView.findViewById(R.id.txtMoviePlatforms);
        }

        void bind(Pelicula pelicula, OnPeliculaClickListener listener) {
            Context context = itemView.getContext();
            txtTitulo.setText(pelicula.getTitulo());
            String generosTexto = pelicula.getGeneros() != null ? String.join(", ", pelicula.getGeneros()) : "";
            String info = String.format(Locale.getDefault(), "%d · %dm · %s", pelicula.getAnio(), pelicula.getDuracionMin(), generosTexto);
            txtInfo.setText(info);
            String mood = pelicula.getMood() != null ? pelicula.getMood() : "";
            txtMood.setText(String.format(Locale.getDefault(), "Mood: %s", mood));
            String plataformasTexto = pelicula.getPlataformas() != null
                    ? pelicula.getPlataformas().stream().collect(Collectors.joining(", "))
                    : "";
            txtPlataformas.setText(String.format(Locale.getDefault(), "Disponible en: %s", plataformasTexto));

            Glide.with(context)
                    .load(pelicula.getPosterUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imgPoster);

            itemView.setOnClickListener(v -> listener.onPeliculaClick(pelicula));
            itemView.setOnLongClickListener(v -> {
                listener.onPeliculaLongClick(pelicula);
                return true;
            });
        }
    }
}
