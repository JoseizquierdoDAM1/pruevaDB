package com.example.pruevadb;



import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class verRestaurante extends AppCompatActivity {
     private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_restaurante);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView t=findViewById(R.id.labelVerNombre);
        t.setText("Bienvenido "+usuario.getNombreUsuario());

        cargar();
    }



    public void cargar() {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes");

        // Aplicar un filtro para obtener solo los restaurantes del dueño específico
        Query query = restaurantesRef.orderByChild("dniUsuario").equalTo(usuario.getDni());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Restaurante> restaurantes = new ArrayList<>();
                for (DataSnapshot restauranteSnapshot : dataSnapshot.getChildren()) {
                    // Obtener los datos del restaurante del snapshot
                    String id = restauranteSnapshot.child("id").getValue(String.class);
                    String nombre = restauranteSnapshot.child("nombre").getValue(String.class);
                    String comunidadA = restauranteSnapshot.child("comunidadaAutonoma").getValue(String.class);
                    String provincia = restauranteSnapshot.child("provincia").getValue(String.class);
                    String ciudad = restauranteSnapshot.child("ciudad").getValue(String.class);
                    String tipo = restauranteSnapshot.child("tipo").getValue(String.class);
                    String dniUsuario = restauranteSnapshot.child("dniUsuario").getValue(String.class);
                    String imageUrl = restauranteSnapshot.child("imagen").getValue(String.class);
                    int valoracion= restauranteSnapshot.child("valoracion").getValue(Integer.class);
                    int comensales=restauranteSnapshot.child("comensales").getValue(Integer.class);

                    Restaurante r = new Restaurante(id,nombre,  tipo,comunidadA,provincia,ciudad, dniUsuario, imageUrl,comensales,valoracion);
                    restaurantes.add(r);
                }
                RecyclerView recyclerView = findViewById(R.id.recyclermenuReseñas);
                RestauranteAdapter adapter = new RestauranteAdapter(getApplicationContext(), restaurantes,usuario);
                recyclerView.addItemDecoration(new SpaceItemDecoration(70));

                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Obtener el restaurante seleccionado
                        int position = recyclerView.getChildAdapterPosition(view);
                        Restaurante restaurante = restaurantes.get(position);

                        // Abrir la actividad DetalleRestaurante y pasar el restaurante seleccionado como extra
                        Intent intent = new Intent(verRestaurante.this, DetalleRestaurante.class);
                        intent.putExtra("restaurante", restaurante);
                        intent.putExtra("usuario", usuario);
                        startActivity(intent);


                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejo de errores
            }
        });
    }



    public void añadirRestaurante(View view){
        Intent i = new Intent(verRestaurante.this, RegistrarRestaurante.class);
        i.putExtra("usuario", this.usuario);
        startActivity(i);
    }





}