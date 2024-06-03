package com.example.pruevadb;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {
    private List<Restaurante> restaurantes;
    private Context applicationContext;

    private Usuario usuario;
    private View.OnClickListener listener;

    public RestauranteAdapter(Context applicationContext, List<Restaurante> restaurantes, Usuario usuario) {
        this.applicationContext = applicationContext;
        this.restaurantes = restaurantes;
        this.usuario = usuario;
    }

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

        if(restaurante.getValoracion()==1){
            holder.ell1.setVisibility(View.VISIBLE);
        }
        if(restaurante.getValoracion()==2){
            holder.ell1.setVisibility(View.VISIBLE);
            holder.ell2.setVisibility(View.VISIBLE);
        }
        if(restaurante.getValoracion()==3){
            holder.ell1.setVisibility(View.VISIBLE);
            holder.ell2.setVisibility(View.VISIBLE);
            holder.ell3.setVisibility(View.VISIBLE);
        }
        if(restaurante.getValoracion()==4){
            holder.ell1.setVisibility(View.VISIBLE);
            holder.ell2.setVisibility(View.VISIBLE);
            holder.ell3.setVisibility(View.VISIBLE);
            holder.ell4.setVisibility(View.VISIBLE);
        }
        if(restaurante.getValoracion()==5){
            holder.ell1.setVisibility(View.VISIBLE);
            holder.ell2.setVisibility(View.VISIBLE);
            holder.ell3.setVisibility(View.VISIBLE);
            holder.ell4.setVisibility(View.VISIBLE);
            holder.ell5.setVisibility(View.VISIBLE);
        }




        holder.nombreTextView.setText(restaurante.getNombre());
        holder.tipoTextView.setText(restaurante.getTipo());
        holder.ciudadTextView.setText(restaurante.getCiudad());

        // Cargar imagen usando Glide con manejo de errores
        Glide.with(holder.itemView.getContext())
                .load(restaurante.getImagen())
                //.error(R.drawable.error_image) // Imagen de error en caso de fallo
                .into(holder.imagenImageView);


        holder.button7.setOnClickListener(view -> {
            Context context = view.getContext();
            Restaurante restauranteSeleccionado = restaurantes.get(holder.getAdapterPosition());
            Intent intent = new Intent(context, DetalleRestaurante.class);
            intent.putExtra("restaurante", restauranteSeleccionado);
            intent.putExtra("usuario", usuario);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });


        holder.buttonreseñas.setOnClickListener(view -> {
            Context context = view.getContext();
            Restaurante restauranteSeleccionado = restaurantes.get(holder.getAdapterPosition());
            Intent intent = new Intent(context, VerResenas.class);
            intent.putExtra("restaurante", restauranteSeleccionado);
            intent.putExtra("usuario", usuario);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return restaurantes.size();
    }

    public static class RestauranteViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenImageView;
        ImageView ell1,ell2,ell3,ell4,ell5;;

        TextView nombreTextView, tipoTextView, ciudadTextView;
        Button button7,buttonreseñas;

        public RestauranteViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.labelNombreRecycler);
            tipoTextView = itemView.findViewById(R.id.labelTipoRecycler);
            ciudadTextView = itemView.findViewById(R.id.labelCiudadRecycler);
            imagenImageView = itemView.findViewById(R.id.imagenRecycler);
            button7 = itemView.findViewById(R.id.button7);
            buttonreseñas=itemView.findViewById(R.id.buttonreseñas);

            ell1=itemView.findViewById(R.id.ell1);
            ell2=itemView.findViewById(R.id.ell2);
            ell3=itemView.findViewById(R.id.ell3);
            ell4=itemView.findViewById(R.id.ell4);
            ell5=itemView.findViewById(R.id.ell5);
        }
    }
}