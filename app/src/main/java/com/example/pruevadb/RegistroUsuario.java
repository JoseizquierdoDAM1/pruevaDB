package com.example.pruevadb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistroUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
    }
    public void registro(View view){
        final EditText nombreuser = findViewById(R.id.labelNombre);
        final EditText email = findViewById(R.id.labelEmail);
        final EditText dni = findViewById(R.id.dni);
        final EditText contraseña = findViewById(R.id.labelContraseña);
        final EditText confirmarcontraseña = findViewById(R.id.labelConfContraseña);
        final TextView error = findViewById(R.id.labelerrorregistroUser);

        final String nombreUsuario = nombreuser.getText().toString();

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean usuarioExistente = false;
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    String nombre = usuarioSnapshot.child("nombreUsuario").getValue(String.class);
                    if (nombreUsuario.equals(nombre)) {
                        usuarioExistente = true;
                        break;
                    }
                }

                if (!usuarioExistente) {
                    if (contraseña.getText().toString().equals(confirmarcontraseña.getText().toString())) {
                        Usuario u = new Usuario(nombreUsuario, email.getText().toString(), contraseña.getText().toString(), dni.getText().toString(), "dueño");
                        usuariosRef.child(nombreUsuario).setValue(u);
                        error.setText("¡Usuario registrado exitosamente!");
                        error.setVisibility(View.VISIBLE);
                        Intent i = new Intent(RegistroUsuario.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        error.setText("Las contraseñas no coinciden");
                        error.setVisibility(View.VISIBLE);
                    }
                } else {
                    error.setText("El nombre de usuario ya existe");
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Manejo de errores
            }
        });
    }
}