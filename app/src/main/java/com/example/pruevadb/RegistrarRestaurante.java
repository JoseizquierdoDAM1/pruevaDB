package com.example.pruevadb;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class RegistrarRestaurante extends AppCompatActivity {

    private String comunidadAutonoma;
    private String provincia;
    private String ciudad;
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
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_restaurante);
        mUploadBtn = findViewById(R.id.botonSubir);
        mStorage = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        botonGuaradarTurno=findViewById(R.id.guardarTurno);
        botonGuarRestaurante=findViewById(R.id.botonSubir);
        botonsiguienteTurno=findViewById(R.id.button2);

        // Inicializar las listas
        horastdesalluno = new ArrayList<>();
        horastcomida = new ArrayList<>();
        horastcena = new ArrayList<>();
        ArrayList <String> data= new ArrayList<>();
        data.add("Desalluno");
        data.add("Comida");
        data.add("Cena");
        rellenarSpinners();
        rellenarCiudad();



    }

    public void rellenarCiudad() {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyC2BIa9iq_0bSgU11dORK9gS-DJ9k7QjSs")
                .setApplicationId("1:783759569627:android:739fa870fce6e73274d2fe")
                .setDatabaseUrl("https://ciudadesfinalrestaurante-default-rtdb.europe-west1.firebasedatabase.app/")
                .build();

        FirebaseApp secondApp = FirebaseApp.initializeApp(this, options, "second app");

// Obtener la instancia de la segunda base de datos
        FirebaseDatabase secondDatabase = FirebaseDatabase.getInstance(secondApp);

// Hacer referencia al nodo "comunidades" en la segunda base de datos
        // Define un ArrayList para almacenar las comunidades
        ArrayList<CCA> comunidadesList = new ArrayList<>();

// Obtener la referencia al nodo "comunidades" en la base de datos
        // Define un ArrayList para almacenar los nombres de las comunidades
        ArrayList<String> nombresComunidades = new ArrayList<>();

// Obtener la referencia al nodo "comunidades" en la base de datos
        DatabaseReference databaseReferenceRef = secondDatabase.getReference().child("comunidades");

        databaseReferenceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterar sobre los datos dentro del nodo "comunidades"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtener el nombre de cada comunidad
                    String parentCode = snapshot.child("parent_code").getValue(String.class);
                    String label = snapshot.child("label").getValue(String.class);
                    String code = snapshot.child("code").getValue(String.class);

                    // Crear un objeto Comunidad y agregarlo al ArrayList
                    CCA comunidad = new CCA(code,parentCode, label);
                    comunidadesList.add(comunidad);
                    // Agregar el nombre al ArrayList de nombres de comunidades
                    nombresComunidades.add(label);
                }

                // Ahora que tienes los nombres de las comunidades en el ArrayList, puedes poblar el Spinner
                Spinner spinnerComunidades = findViewById(R.id.spinner_comunidades);

// Crear un ArrayAdapter para el Spinner utilizando el ArrayList de nombres de comunidades
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrarRestaurante.this, android.R.layout.simple_spinner_item, nombresComunidades);

// Especificar el diseño del dropdown del Spinner
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Asignar el ArrayAdapter al Spinner
                spinnerComunidades.setAdapter(adapter);

// Agregar un listener al Spinner para detectar la selección de una comunidad
                spinnerComunidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // Obtener el nombre de la comunidad seleccionada
                        String comunidadSeleccionada = nombresComunidades.get(position);

                        comunidadAutonoma=comunidadSeleccionada;
                        // Llamar al método provincias y pasar el nombre de la comunidad como argumento
                        provincias(comunidadesList.get(position),secondDatabase);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Manejar el caso en el que no se seleccione ninguna comunidad
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores
            }
        });






    }

    public void provincias(CCA comunidadSeleccionada, FirebaseDatabase secondDatabase){

        ArrayList<province> provinceList = new ArrayList<>();

        // Obtener la referencia al nodo "comunidades" en la base de datos
        // Define un ArrayList para almacenar los nombres de las comunidades
        ArrayList<String> nombreProvince = new ArrayList<>();
        DatabaseReference provinciasRef = secondDatabase.getReference().child("provincias");

        Query query = provinciasRef.orderByChild("parent_code").equalTo(comunidadSeleccionada.getCode());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterar sobre los datos de las provincias
                for (DataSnapshot provinciaSnapshot : dataSnapshot.getChildren()) {
                    // Obtener los datos de cada provincia
                    String parentCode = provinciaSnapshot.child("parent_code").getValue(String.class);
                    String label = provinciaSnapshot.child("label").getValue(String.class);
                    String code = provinciaSnapshot.child("code").getValue(String.class);

                    province p= new province(code,label,parentCode);
                    provinceList.add(p);
                    nombreProvince.add(label);

                }
                // Ahora que tienes los nombres de las comunidades en el ArrayList, puedes poblar el Spinner
                Spinner spinnerComunidades = findViewById(R.id.spinner_provincias);

// Crear un ArrayAdapter para el Spinner utilizando el ArrayList de nombres de comunidades
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrarRestaurante.this, android.R.layout.simple_spinner_item, nombreProvince);

// Especificar el diseño del dropdown del Spinner
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Asignar el ArrayAdapter al Spinner
                spinnerComunidades.setAdapter(adapter);

// Agregar un listener al Spinner para detectar la selección de una comunidad
                spinnerComunidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Obtener el nombre de la comunidad seleccionada
                        String provinciaSeleccionada = nombreProvince.get(position);

                        provincia=provinciaSeleccionada;


                        // Llamar al método provincias y pasar el nombre de la comunidad como argumento
                        ciudades(provinceList.get(position),secondDatabase);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Manejar el caso en el que no se seleccione ninguna comunidad
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores
            }
        });

    }
    public void ciudades(province p,FirebaseDatabase secondDatabase){
        ArrayList<Town> townList = new ArrayList<>();

        // Obtener la referencia al nodo "comunidades" en la base de datos
        // Define un ArrayList para almacenar los nombres de las comunidades
        ArrayList<String> nombreTown = new ArrayList<>();
        DatabaseReference TownRef = secondDatabase.getReference().child("poblaciones");

        Query query = TownRef.orderByChild("parent_code").equalTo(p.getCode());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterar sobre los datos de las provincias
                for (DataSnapshot provinciaSnapshot : dataSnapshot.getChildren()) {
                    // Obtener los datos de cada provincia
                    String parentCode = provinciaSnapshot.child("parent_code").getValue(String.class);
                    String label = provinciaSnapshot.child("label").getValue(String.class);
                    String code = provinciaSnapshot.child("code").getValue(String.class);

                    Town town= new Town(code,label,parentCode);
                    townList.add(town);
                    nombreTown.add(label);

                }
                // Ahora que tienes los nombres de las comunidades en el ArrayList, puedes poblar el Spinner
                Spinner spinnerComunidades = findViewById(R.id.spinner_ciudades);

// Crear un ArrayAdapter para el Spinner utilizando el ArrayList de nombres de comunidades
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrarRestaurante.this, android.R.layout.simple_spinner_item, nombreTown);

// Especificar el diseño del dropdown del Spinner
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Asignar el ArrayAdapter al Spinner
                spinnerComunidades.setAdapter(adapter);

                spinnerComunidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Obtener el nombre de la comunidad seleccionada
                        String ciudaadSeleccionada = nombreTown.get(position);

                        ciudad=ciudaadSeleccionada;


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Manejar el caso en el que no se seleccione ninguna comunidad
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores
            }
        });
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
        EditText nombre =  findViewById(R.id.labelNombreRestaurante);
        EditText tipo = findViewById(R.id.labelTipo);
        EditText comensales=findViewById(R.id.labelNumeroComensales);
        if(nombre.getText().toString().equals("")||tipo.getText().toString().equals("")||comensales.getText().toString().equals("")){
            Toast.makeText(RegistrarRestaurante.this, "Tienes que rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        }

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
        EditText nombre =  findViewById(R.id.labelNombreRestaurante);
        EditText tipo = findViewById(R.id.labelTipo);
        EditText comensales=findViewById(R.id.labelNumeroComensales);
        Restaurante r= new Restaurante();
        // Crear un objeto restaurante con la información
         r.setNombre(nombre.getText().toString());
         r.setTipo(tipo.getText().toString());
         r.setComunidadaAutonoma(this.comunidadAutonoma);
         r.setProvincia(this.provincia);
         r.setCiudad(this.ciudad);
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
        if(horaInicios.equals("0")&&horaFins.equals("0")) {
            Toast.makeText(RegistrarRestaurante.this, "para guardar el turno tienes que seleccionar una hora", Toast.LENGTH_SHORT).show();
        }else{
            try {
                int inicio = Integer.valueOf(horaInicios);
                int fin = Integer.valueOf(horaFins);
                if (numturno == 1) {
                    for (int i = inicio; i <= fin; i++) { // Iterar desde inicio (inclusivo) hasta fin (exclusivo)
                        horastdesalluno.add(String.format("%02d:00", i)); // Usar String.format para un formato consistente
                    }
                    numturnos = 1;
                    turnos.add("Desayuno");
                }

                if (numturno == 2) {
                    for (int i = inicio; i <= fin; i++) { // Iterar desde inicio (inclusivo) hasta fin (exclusivo)
                        horastcomida.add(String.format("%02d:00", i)); // Usar String.format para un formato consistente
                    }
                    numturnos = Integer.valueOf(numturnos + "2");
                    turnos.add("Comida");
                }

                if (numturno == 3) {
                    for (int i = inicio; i <= fin; i++) { // Iterar desde inicio (inclusivo) hasta fin (exclusivo)
                        horastcena.add(String.format("%02d:00", i)); // Usar String.format para un formato consistente
                    }
                    numturnos = Integer.valueOf(numturnos + "3");
                    turnos.add("Cena");
                }
            } catch (NumberFormatException e) {
                // Manejar la excepción potencial si se seleccionan valores no numéricos
                Toast.makeText(this, "Error: Formato de hora inválido", Toast.LENGTH_SHORT).show();
            }
            numturno++;
            if (numturno == 4) {
                botonGuaradarTurno.setVisibility(View.INVISIBLE);
            }
            if (numturno == 3) {
                botonsiguienteTurno.setVisibility(View.INVISIBLE);
            }
            botonGuarRestaurante.setVisibility(View.VISIBLE);


            TextView t = findViewById(R.id.labeltipoturno);
            if (numturno == 1) {
                t.setText("Desayuno");
            }
            if (numturno == 2) {
                t.setText("Comida");
            }
            if (numturno == 3) {
                t.setText("Cena");
                botonsiguienteTurno.setVisibility(View.INVISIBLE);
            }
            rellenarSpinners();
        }
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
        botonsiguienteTurno.setVisibility(View.INVISIBLE);
    }


rellenarSpinners();
    }

    public void menu(View view){
        Intent i= new Intent(RegistrarRestaurante.this,verRestaurante.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
    }


}