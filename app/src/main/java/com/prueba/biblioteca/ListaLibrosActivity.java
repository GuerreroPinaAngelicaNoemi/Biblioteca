package com.prueba.biblioteca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.prueba.biblioteca.adapters.LibrosAdapter;
import com.prueba.biblioteca.databinding.ActivityListaLibrosBinding;
import com.prueba.biblioteca.room.AppDatabase;
import com.prueba.biblioteca.room.LibrosDB;
import com.prueba.biblioteca.utils.Alertas;
import com.prueba.biblioteca.utils.Constantes;
import com.prueba.biblioteca.utils.DatePickerFragment;

import java.util.ArrayList;
import java.util.List;

public class ListaLibrosActivity extends AppCompatActivity {

    private ActivityListaLibrosBinding binding;
    AppDatabase db;
    ArrayList<LibrosDB> listaLibros;

    private LibrosAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaLibrosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    public void init(){
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constantes.Database_Name).allowMainThreadQueries().build();
        listaLibros = (ArrayList<LibrosDB>) db.librosDao().sp_Sel_All();

        cargarLibros(listaLibros);

        binding.agregar.setOnClickListener(view -> startActivity(new Intent(ListaLibrosActivity.this, AgregarLibroActivity.class)));

        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(listaLibros.size() != 0){
                    if(!editable.toString().equals("")){
                        buscarLibro(editable.toString());
                    }else{
                        cargarLibros(listaLibros);
                    }
                }
            }
        });

    }

    public void cargarLibros(ArrayList<LibrosDB> lista ){

        binding.recyclerLibros.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        adapter = new LibrosAdapter(lista, getContentResolver());

        binding.recyclerLibros.setLayoutManager(mLayoutManager);
        binding.recyclerLibros.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> editarLibro(ListaLibrosActivity.this, getLayoutInflater(), db, lista.get(position)));

    }

    public void buscarLibro(String nombreLibro){
        ArrayList<LibrosDB> listaAux = new ArrayList<>();

        for(LibrosDB librosDB : listaLibros){
            if(librosDB.getNombreLibro().toLowerCase().contains(nombreLibro.toLowerCase())){
                listaAux.add(librosDB);
            }
        }
        cargarLibros(listaAux);
    }

    public void editarLibro(Activity context, LayoutInflater layoutInflater, AppDatabase db, LibrosDB librosDB){

        final AlertDialog alerEditar;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = layoutInflater.inflate(R.layout.alert_editar_libro, null);

        TextInputLayout autor = view.findViewById(R.id.tilAutor);
        TextInputLayout tilNombreLibro = view.findViewById(R.id.tilNombreLibro);
        TextInputLayout tilIsbn = view.findViewById(R.id.tilIsbn);
        TextInputLayout tilfecha = view.findViewById(R.id.tilFecha);
        TextInputLayout tilGenero = view.findViewById(R.id.tilGenero);
        Button guardar = view.findViewById(R.id.guardar);
        Button eliminar = view.findViewById(R.id.eliminar);

        //asignar valor
        autor.getEditText().setText(librosDB.getAutor());
        tilNombreLibro.getEditText().setText(librosDB.getNombreLibro());
        tilIsbn.getEditText().setText(librosDB.getIsbn());
        tilfecha.getEditText().setText(librosDB.getFecha());
        tilGenero.getEditText().setText(librosDB.getGenero());

        builder.setView(view);
        alerEditar = builder.create();

        guardar.setOnClickListener(view1 -> {
            LibrosDB libroGuardar = new LibrosDB(autor.getEditText().getText().toString(), tilNombreLibro.getEditText().getText().toString(), tilIsbn.getEditText().getText().toString(),
                    tilfecha.getEditText().getText().toString(), tilGenero.getEditText().getText().toString(), librosDB.getRegion(), librosDB.getImagen());
            db.librosDao().sp_Del_Libro(librosDB);
            db.librosDao().sp_Ins_Book(libroGuardar);
            listaLibros = (ArrayList<LibrosDB>) db.librosDao().sp_Sel_All();
            cargarLibros(listaLibros);
            alerEditar.dismiss();
        });
        eliminar.setOnClickListener(view1 -> {
            alerEditar.dismiss();
            Alertas.confirmar(ListaLibrosActivity.this, getLayoutInflater(), db, librosDB);

        });

        alerEditar.setCancelable(true);
        alerEditar.show();
    }

}