package com.example.pruevadb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void acceder(View view) {
        TextView labelnombre = findViewById(R.id.nombre);
        TextView contraseña = findViewById(R.id.contraseña);
        TextView aviso = findViewById(R.id.aviso);

        // Suponiendo que ya tengas la referencia a tu base de datos de Firebase
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean usuarioEncontrado = false;

                for (DataSnapshot usuarioSnapshot : snapshot.getChildren()) {
                    // Obtener los datos del usuario del snapshot
                    String nombre = usuarioSnapshot.child("nombreUsuario").getValue(String.class);
                    String correo = usuarioSnapshot.child("correo").getValue(String.class);
                    String contraseñaUsuario = usuarioSnapshot.child("contraseña").getValue(String.class);
                    String tipoUser = usuarioSnapshot.child("tipUser").getValue(String.class);
                    String dni=usuarioSnapshot.child("dni").getValue(String.class);
                    // Comparar el nombre de usuario y contraseña
                    if (labelnombre.getText().toString().equals(nombre) && contraseña.getText().toString().equals(contraseñaUsuario)) {
                        usuarioEncontrado = true;
                        if (tipoUser.equals("dueño")) {
                            aviso.setText("El acceso es correcto para el usuario dueño");
                            Intent i = new Intent(MainActivity.this,verRestaurante.class);
                            Usuario u = new Usuario( nombre,  correo,  contraseñaUsuario, dni,  tipoUser);
                            i.putExtra("usuario", u);

                            startActivity(i);
                        } else {
                            aviso.setText("Los no dueños no pueden acceder");
                            return; // Salir del método si el usuario no es dueño
                        }
                        break; // Salir del bucle una vez que se encuentra el usuario
                    }
                }

                if (!usuarioEncontrado) {
                    aviso.setText("Usuario o contraseña incorrectos");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                aviso.setText("Error al acceder a la base de datos");
            }
        });
    }

    public void registrarse(View view) {
        Intent i = new Intent(MainActivity.this,RegistroUsuario.class);
        startActivity(i);
    }
}