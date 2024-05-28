package com.example.pruevadb;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RegistrarRestaurante extends AppCompatActivity {
    private Button mUploadBtn;
    private StorageReference mStorage;
    private Usuario usuario;
    String urlImagen;
    int numturnos;
    Spinner horaInicio;
    Spinner horaFin;
    Spinner intervalo;
    Button botonGuaradarTurno;
    Button botonsiguienteTurno;
    Button botonGuarRestaurante;
    int numturno=1;
    Restaurante r = new Restaurante();
    private ArrayList<String> horastdesalluno;
    private ArrayList<String>horastcomida;
    private ArrayList<String>horastcena;
    private ArrayList <String>turnos= new ArrayList<>();

    private static  final int GALLERY_INTENT =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_restaurante);
        mUploadBtn = findViewById(R.id.botonSubir);
        mStorage = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        botonGuaradarTurno=findViewById(R.id.button7);
        botonGuarRestaurante=findViewById(R.id.botonSubir);
        botonsiguienteTurno=findViewById(R.id.button4);

        botonGuarRestaurante.setEnabled(false);
        // Inicializar las listas
        horastdesalluno = new ArrayList<>();
        horastcomida = new ArrayList<>();
        horastcena = new ArrayList<>();
        ArrayList <String> data= new ArrayList<>();
        data.add("Desalluno");
        data.add("Comida");
        data.add("Cena");
        rellenarSpinners();

    }

    public void rellenarSpinners(){

        horaInicio = (Spinner) findViewById(R.id.spinnerHoraInicio);

        String[] data1 = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        horaInicio.setAdapter(adapter1);

        horaFin = (Spinner) findViewById(R.id.spinnerHoraFin);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data1);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        horaFin.setAdapter(adapter2);

        intervalo = (Spinner) findViewById(R.id.intervalo);

        String[] data3 = {"1 hora","2 horas","3 horas"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalo.setAdapter(adapter3);
    }

    public void subir(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            final Uri uri = data.getData();

            StorageReference filePath = mStorage.child("fotos").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Subida exitosa, obtener la URL de descarga
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // URL de descarga obtenida, ahora guardar en Firestore junto con tu nombre y apellidos
                            urlImagen=downloadUri.toString();
                            guardarEnFirestore();
                        }
                    });
                    Toast.makeText(RegistrarRestaurante.this, usuario.getDni(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




    public void guardarEnFirestore() {

        //Toast.makeText(RegistrarRestaurante.this,  horast1.get(0), Toast.LENGTH_SHORT).show();
        TextView nombre =  findViewById(R.id.labelNombreRestaurante);
        TextView tipo = findViewById(R.id.labelTipo);
        TextView ciudad = findViewById(R.id.labelCiudad);
        TextView comensales=findViewById(R.id.labelNumeroComensales);
        Restaurante r= new Restaurante();
        // Crear un objeto restaurante con la información
         r.setNombre(nombre.getText().toString());
         r.setTipo(tipo.getText().toString());
         r.setCiudad(ciudad.getText().toString());
         r.setDniUsuario(usuario.getDni());
         r.setImagen(urlImagen);
         r.setComensales(Integer.valueOf(comensales.getText().toString()));
         r.setValoracion(0);
         if(numturnos==1){
             r.setHorastdesayuno(horastdesalluno);
         }
        if(numturnos==2){
            r.setHorastcomida(horastcomida);
        }
        if(numturnos==3){
            r.setHorastcena(horastcena);
        }
        if(numturnos==123){
            r.setHorastdesayuno(horastdesalluno);
            r.setHorastcomida(horastcomida);
            r.setHorastcena(horastcena);
        }
        if(numturnos==13){
            r.setHorastdesayuno(horastdesalluno);
            r.setHorastcena(horastcena);
        }
        if(numturnos==23){
            r.setHorastcomida(horastcomida);
            r.setHorastcena(horastcena);
        }
        if(numturnos==12){
            r.setHorastdesayuno(horastdesalluno);
            r.setHorastcomida(horastcomida);
        }
        r.setTurnos(this.turnos);

        // Guardar en Firestore
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Restaurantes");

        String restauranteId = myRef.push().getKey();
// Asignar el identificador único al restaurante
        r.setId(restauranteId);

        myRef.child(restauranteId).setValue(r);

        Intent i = new Intent(RegistrarRestaurante.this,verRestaurante.class);
        i.putExtra("usuario", usuario);
        startActivity(i);
    }

    public void guardarTurno(View view) {
        String horaInicios = (String) horaInicio.getSelectedItem();
        String horaFins = (String) horaFin.getSelectedItem(); // Acceder al spinner correcto
        try {
            int inicio = Integer.valueOf(horaInicios);
            int fin = Integer.valueOf(horaFins);
            if(numturno==1){
            for (int i = inicio; i <= fin; i++) { // Iterar desde inicio (inclusivo) hasta fin (exclusivo)
                horastdesalluno.add(String.format("%02d:00", i)); // Usar String.format para un formato consistente
            }
           numturnos=1;
            turnos.add("Desayuno");
            }

            if(numturno==2){
                for (int i = inicio; i <= fin; i++) { // Iterar desde inicio (inclusivo) hasta fin (exclusivo)
                    horastcomida.add(String.format("%02d:00", i)); // Usar String.format para un formato consistente
                }
                numturnos=Integer.valueOf(numturnos+"2");
                turnos.add("Comida");
            }

            if(numturno==3){
                for (int i = inicio; i <= fin; i++) { // Iterar desde inicio (inclusivo) hasta fin (exclusivo)
                    horastcena.add(String.format("%02d:00", i)); // Usar String.format para un formato consistente
                }
                numturnos=Integer.valueOf(numturnos+"3");
                turnos.add("Cena");
            }
        } catch (NumberFormatException e) {
            // Manejar la excepción potencial si se seleccionan valores no numéricos
            Toast.makeText(this, "Error: Formato de hora inválido", Toast.LENGTH_SHORT).show();
        }
        numturno++;
        if(numturno==4){
            botonGuaradarTurno.setEnabled(false);
        }
        if(numturno==3){
            botonsiguienteTurno.setEnabled(false);
        }
        botonGuarRestaurante.setEnabled(true);


        TextView t= findViewById(R.id.labeltipoturno);
        if(numturno==1){
            t.setText("Desayuno");
        }
        if(numturno==2){
            t.setText("Comida");
        }
        if(numturno==3){
            t.setText("Cena");
        }
        rellenarSpinners();
    }

    public void siguienteTurno(View view) {
    numturno++;
    TextView t= findViewById(R.id.labeltipoturno);
    if(numturno==1){
        t.setText("Desayuno");
    }
    if(numturno==2){
        t.setText("Comida");
    }
    if(numturno==3){
        t.setText("Cena");
        botonsiguienteTurno.setEnabled(false);
    }
rellenarSpinners();
    }
}