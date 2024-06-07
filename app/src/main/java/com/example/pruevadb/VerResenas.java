package com.example.pruevadb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.util.ArrayList;
import java.util.List;

public class VerResenas extends AppCompatActivity {
    private Usuario usuario;
    private Restaurante restaurante;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_resenas);
        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");

        restaurante = (Restaurante) intent.getSerializableExtra("restaurante");

        cargar();

    }

    public void principal(View view){
        Intent i= new Intent(VerResenas.this,verRestaurante.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
    }
    public void cargar() {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(restaurante.getId());

        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<Reseña>> genericTypeIndicator2 = new GenericTypeIndicator<ArrayList<Reseña>>() {};
                ArrayList<Reseña> reseñas = dataSnapshot.child("reseñas").getValue(genericTypeIndicator2);
                String nombre=dataSnapshot.child("nombre").getValue(String.class);

                if(reseñas!=null) {
                    RecyclerView recyclerView = findViewById(R.id.recyclermenuReseñas);
                    ReseñaAdapter adapter = new ReseñaAdapter(getApplicationContext(), reseñas);
                    recyclerView.addItemDecoration(new SpaceItemDecoration(50));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);
                } else{
                    Toast.makeText(VerResenas.this, "no hay reseñas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores de Firebase
            }
        });
    }
}