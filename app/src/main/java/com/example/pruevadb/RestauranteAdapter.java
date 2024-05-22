package com.example.pruevadb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {
    private List<Restaurante> restaurantes;
    private View.OnClickListener listener; // Variable para almacenar el OnClickListener

    // Constructor del adaptador
    public RestauranteAdapter(Context applicationContext, List<Restaurante> restaurantes) {
        this.restaurantes = restaurantes;
    }

    // Método para establecer el OnClickListener
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurante, parent, false);
        return new RestauranteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestauranteViewHolder holder, int position) {
        Restaurante restaurante = restaurantes.get(position);
        holder.nombreTextView.setText(restaurante.getNombre());
        holder.tipoTextView.setText(restaurante.getTipo());
        holder.ciudadTextView.setText(restaurante.getCiudad());
        // Cargar imagen usando Glide o Picasso
        Glide.with(holder.itemView.getContext())
                .load(restaurante.getImagen())
                .into(holder.imagenImageView);

        // Establecer el OnClickListener en el item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RestauranteAdapter", "Item clicked: " + restaurante.getNombre()); // Agregar registro de log
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantes.size();
    }

    public static class RestauranteViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenImageView;
        TextView nombreTextView, tipoTextView, ciudadTextView;

        public RestauranteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.labelNombreRecycler);
            tipoTextView = itemView.findViewById(R.id.labelTipoRecycler);
            ciudadTextView = itemView.findViewById(R.id.labelCiudadRecycler);
            imagenImageView = itemView.findViewById(R.id.imagenRecycler);

        }
    }
}