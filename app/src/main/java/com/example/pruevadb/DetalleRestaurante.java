package com.example.pruevadb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DetalleRestaurante extends AppCompatActivity {
    Usuario usuario;
    private Button reservas;
    private Button Historialreservas;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_restaurante);
        cargar("reservas",70);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        reservas=findViewById(R.id.reservas);
        Historialreservas=findViewById(R.id.verHistorialReservas);
    }
    public void cargar(String valor,int espacio) {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Reserva> reservas = new ArrayList<>();
                for (DataSnapshot restauranteSnapshot : dataSnapshot.getChildren()) {
                    // Obtener los datos del restaurante del snapshot
                    GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Reserva>>() {};
                    List<Reserva> reservasRestaurante = restauranteSnapshot.child(valor).getValue(genericTypeIndicator);
                    if (reservasRestaurante != null) {
                        reservas.addAll(reservasRestaurante);
                    }
                }

                // Ordenar la lista de reservas por fecha ascendente
                Collections.sort(reservas, new Comparator<Reserva>() {
                    @Override
                    public int compare(Reserva reserva1, Reserva reserva2) {
                        return reserva1.getDia().compareTo(reserva2.getDia());
                    }
                });

                // Inicializar el RecyclerView y configurar el adaptador
                RecyclerView recyclerView = findViewById(R.id.recyclermenuReseñas);
                DetalleRestauranteAdapter adapter = new DetalleRestauranteAdapter(DetalleRestaurante.this, reservas,valor);
                recyclerView.addItemDecoration(new SpaceItemDecoration(espacio));
                recyclerView.setLayoutManager(new LinearLayoutManager(DetalleRestaurante.this));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
                Toast.makeText(DetalleRestaurante.this, "Error al cargar los datos de los restaurantes", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void reservas(View view) {
        reservas.setVisibility(View.INVISIBLE);
        Historialreservas.setVisibility(View.VISIBLE);
        cargar("reservas",0);
    }

    public void HistorialReserva(View view) {
        reservas.setVisibility(View.VISIBLE);
        Historialreservas.setVisibility(View.INVISIBLE);
        cargar("historialReservas",0);
    }

    public void principal(View view){
        Intent i= new Intent(DetalleRestaurante.this,verRestaurante.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
    }


}