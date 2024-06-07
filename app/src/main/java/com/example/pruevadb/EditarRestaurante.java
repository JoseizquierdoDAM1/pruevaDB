package com.example.pruevadb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class EditarRestaurante extends AppCompatActivity {
    private Usuario usuario;
    private Restaurante restaurante;


    Restaurante r;

    Spinner horaInicio;
    Spinner horaFin;
    Spinner intervalo;
    TextView turno;

    ArrayList<String> turnos;
    ArrayList<String> horastdesayuno;
    ArrayList<String> horastcomida;
    ArrayList<String> horastcena;

    EditText Editnombre;
    EditText Edittipo;
    EditText Editcomensales;
    int numturno=1;
    private StorageReference mStorage;
    private static  final int GALLERY_INTENT =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_restaurante);

        Intent intent = getIntent();
        usuario = (Usuario) intent.getSerializableExtra("usuario");
        restaurante = (Restaurante) intent.getSerializableExtra("restaurante");
        turno=findViewById(R.id.labeltipoturnoEditar);

         Editnombre=findViewById(R.id.labelNombreRestauranteEditar);
         Edittipo=findViewById(R.id.labelTipoEditar);
         Editcomensales=findViewById(R.id.labelNumeroComensalesEditar);

        mStorage = FirebaseStorage.getInstance().getReference();

        cargar();

    }

    public void cargar() {
        DatabaseReference restaurantesRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(restaurante.getId());


        restaurantesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot restauranteSnapshot) {

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

                GenericTypeIndicator<ArrayList<Reserva>> genericTypeIndicatorReservas = new GenericTypeIndicator<ArrayList<Reserva>>() {};
                ArrayList<Reserva> reservas= restauranteSnapshot.child("reservas").getValue(genericTypeIndicatorReservas);
                ArrayList<Reserva> historialReservas= restauranteSnapshot.child("historialReservas").getValue(genericTypeIndicatorReservas);

                GenericTypeIndicator<ArrayList<Reseña>> genericTypeIndicatorReseñas= new GenericTypeIndicator<ArrayList<Reseña>>() {};
                ArrayList<Reseña> reseñas= restauranteSnapshot.child("reseñas").getValue(genericTypeIndicatorReseñas);

                GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
                 turnos = restauranteSnapshot.child("turnos").getValue(genericTypeIndicator);

                horastdesayuno = restauranteSnapshot.child("horastdesayuno").getValue(genericTypeIndicator);
                horastcomida = restauranteSnapshot.child("horastcomida").getValue(genericTypeIndicator);
                horastcena = restauranteSnapshot.child("horastcena").getValue(genericTypeIndicator);
                r = new Restaurante(id,nombre,  tipo,comunidadA,provincia,ciudad, dniUsuario, imageUrl,comensales,valoracion);
                r.setReservas(reservas);
                r.setHistorialReservas(historialReservas);
                r.setReseñas(reseñas);
                r.setTurnos(turnos);

                if(turnos.get(0).equals("Desayuno")){
                    r.setHorastdesayuno(horastdesayuno);
                }
                if(turnos.get(0).equals("Comida")){
                    r.setHorastcomida(horastcomida);
                }
                if(turnos.get(0).equals("Cena")){
                    r.setHorastcena(horastcena);
                }
                if(turnos.size()>1) {
                    if (turnos.get(0).equals("Comida") || turnos.get(1).equals("Comida")) {
                        r.setHorastcomida(horastcomida);
                    }

                    if (turnos.get(0).equals("Cena") || turnos.get(1).equals("Cena") || turnos.get(2).equals("Cena")) {
                        r.setHorastcena(horastcena);

                    }
                }




                Editnombre.setText(nombre);
                Edittipo.setText(tipo);
                Editcomensales.setText(String.valueOf(comensales));

                ArrayList<String> nombresComunidades= new ArrayList<>();
                nombresComunidades.add(comunidadA);
                Spinner spinnerComunidades = findViewById(R.id.spinner_comunidadesEditar);
                ArrayAdapter<String> adaptercomunidades = new ArrayAdapter<>(EditarRestaurante.this, android.R.layout.simple_spinner_item, nombresComunidades);
                spinnerComunidades.setAdapter(adaptercomunidades);

                ArrayList<String> nombreProvince = new ArrayList<>();
                nombreProvince.add(provincia);
                Spinner spinnerProvincias = findViewById(R.id.spinner_provinciasEditar);
                ArrayAdapter<String> adapterprovincias = new ArrayAdapter<>(EditarRestaurante.this, android.R.layout.simple_spinner_item, nombreProvince);
                spinnerProvincias.setAdapter(adapterprovincias);


                ArrayList<String> nombreCiudad = new ArrayList<>();
                nombreCiudad.add(ciudad);
                Spinner spinnerCiudad = findViewById(R.id.spinner_ciudadesEditar);
                ArrayAdapter<String> adapterCiudad = new ArrayAdapter<>(EditarRestaurante.this, android.R.layout.simple_spinner_item, nombreCiudad);
                spinnerCiudad.setAdapter(adapterCiudad);


               rellenarSpinners();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejo de errores
            }
        });
    }

    public void rellenarCiudad(View view) {
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
                Spinner spinnerComunidades = findViewById(R.id.spinner_comunidadesEditar);

// Crear un ArrayAdapter para el Spinner utilizando el ArrayList de nombres de comunidades
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarRestaurante.this, android.R.layout.simple_spinner_item, nombresComunidades);

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

                        r.setComunidadaAutonoma(comunidadSeleccionada);

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
                Spinner spinnerProvincias = findViewById(R.id.spinner_provinciasEditar);

// Crear un ArrayAdapter para el Spinner utilizando el ArrayList de nombres de comunidades
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarRestaurante.this, android.R.layout.simple_spinner_item, nombreProvince);

// Especificar el diseño del dropdown del Spinner
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Asignar el ArrayAdapter al Spinner
                spinnerProvincias.setAdapter(adapter);

// Agregar un listener al Spinner para detectar la selección de una comunidad
                spinnerProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Obtener el nombre de la comunidad seleccionada
                        String provinciaSeleccionada = nombreProvince.get(position);

                        r.setProvincia(provinciaSeleccionada);



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
                Spinner spinnerComunidades = findViewById(R.id.spinner_ciudadesEditar);

// Crear un ArrayAdapter para el Spinner utilizando el ArrayList de nombres de comunidades
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarRestaurante.this, android.R.layout.simple_spinner_item, nombreTown);

// Especificar el diseño del dropdown del Spinner
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Asignar el ArrayAdapter al Spinner
                spinnerComunidades.setAdapter(adapter);

                spinnerComunidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Obtener el nombre de la comunidad seleccionada
                        String ciudaadSeleccionada = nombreTown.get(position);

                        r.setCiudad(ciudaadSeleccionada);


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
        String horainicioS = "";
        String horafinS = "";


        if(numturno==1){
            turno.setText("Desayuno");
            if(turnos.get(numturno-1).equals("Desayuno")){
                horainicioS=horastdesayuno.get(0);
                horafinS=horastdesayuno.get(horastdesayuno.size()-1);
                horainicioS=horainicioS.split(":")[0];
                horafinS=horafinS.split(":")[0];
            }
        }

        if(numturno==2){
            turno.setText("Comida");
            if(turnos.size()>1) {
                if (turnos.get(0).equals("Comida")||turnos.get(1).equals("Comida")) {
                    horainicioS = horastcomida.get(0);
                    horafinS = horastcomida.get(horastcomida.size() - 1);
                    horainicioS = horainicioS.split(":")[0];
                    horafinS = horafinS.split(":")[0];
                }
            }else{
                if (turnos.get(0).equals("Comida")) {
                    horainicioS = horastcomida.get(0);
                    horafinS = horastcomida.get(horastcomida.size() - 1);
                    horainicioS = horainicioS.split(":")[0];
                    horafinS = horafinS.split(":")[0];
                }
            }
        }

        if(numturno==3){
            turno.setText("Cena");

            if(turnos.size()>2) {
                if ( turnos.get(2).equals("Cena")) {
                    horainicioS = horastcena.get(0);
                    horafinS = horastcena.get(horastcena.size() - 1);
                    horainicioS = horainicioS.split(":")[0];
                    horafinS = horafinS.split(":")[0];
                }
            }else{
                if(turnos.size()>1) {
                    if (turnos.get(0).equals("Cena") || turnos.get(1).equals("Cena")) {
                        horainicioS = horastcena.get(0);
                        horafinS = horastcena.get(horastcena.size() - 1);
                        horainicioS = horainicioS.split(":")[0];
                        horafinS = horafinS.split(":")[0];
                    }
                }else{
                    if (turnos.get(0).equals("Cena")) {
                        horainicioS = horastcena.get(0);
                        horafinS = horastcena.get(horastcena.size() - 1);
                        horainicioS = horainicioS.split(":")[0];
                        horafinS = horafinS.split(":")[0];
                    }
                }
            }
        }

        String[] data1=null;
        if(!horainicioS.equals("")) {
             data1 = new String[]{horainicioS, "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        }else{
            data1 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        }

        horaInicio = (Spinner) findViewById(R.id.spinnerHoraInicioEditar);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        horaInicio.setAdapter(adapter1);

        String[] data2=null;
        if(!horainicioS.equals("")) {
            data2 = new String[]{horafinS, "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        }else{
            data2 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        }
        horaFin = (Spinner) findViewById(R.id.spinnerHoraFinEditar);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        horaFin.setAdapter(adapter2);

        intervalo = (Spinner) findViewById(R.id.intervaloEditar);



        String[] data3 = {"1 hora","2 horas","3 horas"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intervalo.setAdapter(adapter3);
    }

    public void siguienteTurnoEditar(View view){
        siguiente(0);

    }


    public void guardarTurno(View view) {
        String horaInicios = (String) horaInicio.getSelectedItem();
        String horaFins = (String) horaFin.getSelectedItem(); // Acceder al spinner correcto
        try {
            int inicio = Integer.valueOf(horaInicios);
            int fin = Integer.valueOf(horaFins);
            if(turno.getText().toString().equals("Desayuno")){
                horastdesayuno=new ArrayList<>();
                for (int i = inicio; i <= fin; i++) { // Iterar desde inicio (inclusivo) hasta fin (exclusivo)
                    horastdesayuno.add(String.format("%02d:00", i)); // Usar String.format para un formato consistente
                }
                r.setHorastdesayuno(horastdesayuno);
                if(!turnos.get(0).equals("Desayuno")) {
                    ArrayList t = new ArrayList<>();
                    t.add("Desayuno");
                    for (String s : turnos) {
                        t.add(s);
                    }
                    turnos = new ArrayList<>();
                    turnos.addAll(t);
                }


            }

            if(turno.getText().toString().equals("Comida")){
                horastcomida=new ArrayList<>();
                for (int i = inicio; i <= fin; i++) { // Iterar desde inicio (inclusivo) hasta fin (exclusivo)
                    horastcomida.add(String.format("%02d:00", i)); // Usar String.format para un formato consistente
                }
                r.setHorastcomida(horastcomida);
                ArrayList t= new ArrayList<>();

                if(turnos.get(0).equals("Desayuno")){
                    t.add("Desayuno");
                    if(turnos.size()>1){
                        if(turnos.get(1).equals("Comida")){
                            t.add("Comida");
                        }else{
                            if(turnos.get(1).equals("Cena")){
                                t.add("Comida");
                                t.add("Cena");
                            }
                        }
                    }else{
                        t.add("Comida");
                    }
                }

                turnos=new ArrayList<>();
                turnos.addAll(t);
            }

            if(turno.getText().toString().equals("Cena")){
                horastcena=new ArrayList<>();
                for (int i = inicio; i <= fin; i++) { // Iterar desde inicio (inclusivo) hasta fin (exclusivo)
                    horastcena.add(String.format("%02d:00", i)); // Usar String.format para un formato consistente
                }
                r.setHorastcena(horastcena);
                if(turnos.get(0).equals("Cena")||turnos.get(1).equals("Cena")||turnos.get(2).equals("Cena")){
                    turnos.add("Cena");
                }

            }
        } catch (NumberFormatException e) {
            // Manejar la excepción potencial si se seleccionan valores no numéricos
            Toast.makeText(this, "Error: Formato de hora inválido", Toast.LENGTH_SHORT).show();
        }


        Button b1=findViewById(R.id.guardarTurno);




        if(numturno==3){
            b1.setVisibility(View.INVISIBLE);
        }

        siguiente(0);
    }

    public void siguiente(int i){
        Button b=findViewById(R.id.siguienteTurno);
        Button b1=findViewById(R.id.guardarTurno);
        Button b2=findViewById(R.id.botonSubir);

        numturno++;
        rellenarSpinners();

        if(numturno==3){
            b.setVisibility(View.INVISIBLE);
        }


    }




    public void menu(View view){
        Intent i= new Intent(EditarRestaurante.this,verRestaurante.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
    }

    public void subir(View view) {
        // Inflar el layout personalizado para el diálogo
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        ImageView imageView = dialogView.findViewById(R.id.imageView25);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        TextView textView = dialogView.findViewById(R.id.textView); // Obtener referencia al TextView en el diseño personalizado

        // Usar Glide para cargar la imagen desde la URL y ajustar el tamaño
        Glide.with(this)
                .load(r.getImagen())
                .into(imageView);

        // Establecer el texto en el TextView
        textView.setText("¿Quieres cambiar la imagen del restaurante?");

        // Crear el diálogo
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();

        // Configurar las acciones de los botones
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
                // Cerrar el diálogo después de hacer clic en el botón
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarEnFirestore();
                dialog.dismiss(); // Cerrar el diálogo después de hacer clic en el botón
            }
        });

        // Mostrar el diálogo
        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();
            if (uri != null) {
                StorageReference filePath = mStorage.child("fotos").child(uri.getLastPathSegment());

                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Subida exitosa, obtener la URL de descarga
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // URL de descarga obtenida, ahora guardar en Firestore junto con tu nombre y apellidos
                                r.setImagen(downloadUri.toString());
                                guardarEnFirestore();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar la falla de la carga de la imagen
                        Toast.makeText(getApplicationContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    public void guardarEnFirestore(){
        r.setNombre(Editnombre.getText().toString());
        r.setTipo(Edittipo.getText().toString());
        r.setComensales(Integer.valueOf(Editcomensales.getText().toString()));
        r.setTurnos(turnos);

        DatabaseReference restauranteRef = FirebaseDatabase.getInstance().getReference("Restaurantes").child(r.getId());

        // Guardar el objeto Restaurante directamente en la base de datos

        restauranteRef.setValue(r).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent i= new Intent(EditarRestaurante.this,verRestaurante.class);
                i.putExtra("usuario",usuario);
                startActivity(i);
            } else {
                Toast.makeText(EditarRestaurante.this, "Error al actualizar el restaurante", Toast.LENGTH_SHORT).show();
            }
        });

    }



}