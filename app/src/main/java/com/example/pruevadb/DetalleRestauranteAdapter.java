package com.example.pruevadb;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class DetalleRestauranteAdapter extends RecyclerView.Adapter<DetalleRestauranteAdapter.DetalleRestauranteViewHolder> {

    Context applicationContext;
    private List<Reserva> reservas;
    String tipo;

    // Constructor del adaptador
    public DetalleRestauranteAdapter(Context applicationContext, List<Reserva> reservas, String valor) {
        this.applicationContext = applicationContext;
        this.reservas = reservas;
        this.tipo=valor;
    }

    @NonNull
    @Override
    public DetalleRestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detallerestaurante, parent, false);
        return new DetalleRestauranteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleRestauranteViewHolder holder, int position) {
        Reserva reserva = reservas.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = sdf.format(reserva.getDia());

        holder.nomreUsuario.setText(reserva.getNombreUsuario());
        holder.fechaReservas.setText(fechaFormateada);
        holder.horaReservas.setText(reserva.getHora());
        holder.comensalesreservas.setText(String.valueOf(reserva.getComensales()));


        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(reserva.getIdUsuario());

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String urlImagen = snapshot.child("urlImagen").getValue(String.class);
               if(urlImagen!=null) {
                   Glide.with(applicationContext).load(urlImagen).into(holder.imageViewmenuReseñas);
               }
               else {
                   Glide.with(applicationContext).load(R.drawable.perfil).into(holder.imageViewmenuReseñas);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       }
        );

        if(tipo.equals("reservas")) {
            holder.eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(applicationContext);
                    dialogo1.setTitle("Alerta");
                    dialogo1.setMessage("¿ Quieres eliminar esta reserva ?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            reservas.remove(reserva);
                            guardarReservas(reserva.getIdRestaurante(), reservas);
                            guardarMensaje(reserva);
                            notifyDataSetChanged(); // Actualiza el RecyclerView
                            guardarHistorial(reserva);
                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();
                }
            });
        }else{
            holder.eliminar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    public static class DetalleRestauranteViewHolder extends RecyclerView.ViewHolder {
        TextView fechaReservas,horaReservas,comensalesreservas,nomreUsuario;
        ImageView imageViewmenuReseñas,eliminar;

        public DetalleRestauranteViewHolder(@NonNull View itemView) {
            super(itemView);
            fechaReservas = itemView.findViewById(R.id.fechaReservas);
            horaReservas = itemView.findViewById(R.id.horaReservas);
            comensalesreservas = itemView.findViewById(R.id.comensalesreservas);
            nomreUsuario = itemView.findViewById(R.id.nomreUsuario);
            imageViewmenuReseñas = itemView.findViewById(R.id.imageViewmenuReseñas);
            eliminar = itemView.findViewById(R.id.eliminar);

        }
    }

    public void guardarReservas(String idRestaurante, List<Reserva> reservass) {
        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idRestaurante);
        restauranteRef.child("reservas").setValue(reservass);

    }

    public void guardarHistorial(Reserva reserva) {
        String idRestaurante = reserva.getIdRestaurante();
        if (idRestaurante == null || idRestaurante.isEmpty()) {
            Toast.makeText(applicationContext, "ID de restaurante no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idRestaurante);

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {};
                List<Reserva> reservas = snapshot.child("historialReservas").getValue(genericTypeIndicator);
                if (reservas == null) {
                    reservas = new ArrayList<>();
                }
                reservas.add(reserva);

                // Asegúrate de que la referencia del nodo es la correcta
                DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(idRestaurante);
                restauranteRef.child("historialReservas").setValue(reservas).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                    } else {
                        Toast.makeText(applicationContext, "Error al guardar historial", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(applicationContext, "Error en la consulta: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void guardarMensaje(Reserva r) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(r.getIdUsuario());

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> mensajes = new ArrayList<>();
                GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                List<String> mensajesUser = dataSnapshot.child("mensajes").getValue(genericTypeIndicator);

                if (mensajesUser != null) {
                    mensajes.addAll(mensajesUser);
                }

                String mensaje = "Su reserva para el " + new SimpleDateFormat("dd/MM/yyyy").format(r.getDia()) + " a las " + r.getHora() + " ha sido cancelada";
                mensajes.add(mensaje);


                usuariosRef.child("mensajes").setValue(mensajes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "Mensaje guardado correctamente");
                        } else {
                            Log.e("Firebase", "Error al guardar el mensaje", task.getException());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(applicationContext, "Error al guardar el mensaje", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
