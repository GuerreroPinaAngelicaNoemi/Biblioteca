package com.prueba.biblioteca;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.prueba.biblioteca.databinding.ActivityAgregarLibroBinding;
import com.prueba.biblioteca.room.AppDatabase;
import com.prueba.biblioteca.room.LibrosDB;
import com.prueba.biblioteca.utils.Constantes;
import com.prueba.biblioteca.utils.DatePickerFragment;
import com.prueba.biblioteca.utils.ValidarFormularios;

import java.io.IOException;

public class AgregarLibroActivity extends AppCompatActivity {

    private ActivityAgregarLibroBinding binding;
    private  Uri uri;
    private AppDatabase db;
    String resultImagen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgregarLibroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    public void init(){
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constantes.Database_Name).allowMainThreadQueries().build();
        binding.edFecha.setOnClickListener(view -> showDatePicker());

        ArrayAdapter<String> listPaises = new ArrayAdapter<>(
                this,
                R.layout.dropdown_menu_popup_item,
                getResources().getStringArray(R.array.paises)
        );
        binding.paises.setAdapter(listPaises);

        binding.btnCamara.setOnClickListener(view -> tomarFoto());

        binding.guardar.setOnClickListener(view -> guardFoto());

    }

    public void showDatePicker(){
        DatePickerFragment newFragment = DatePickerFragment.newInstance(((datePicker, year, month, day) -> {
            String mes;
            if(month+1 < 10){
                mes = "0"+(month+1);
            }else{
                mes = (month+1)+"";
            }
            final String fecha = day  + "/" + mes + "/" + year;
            binding.edFecha.setText(fecha);
        }));
        newFragment.show(this.getSupportFragmentManager(), "Fecha");
    }

    public void tomarFoto(){
        if(!checkPermissions()){
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripcion de la foto");
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 2);
    }

    public void guardFoto(){
        if(binding.paises.getText().toString().equals("Selecciona un país")){
            binding.tilRegion.setError("Debes seleccionar un pais");
            return;
        }
        if(ValidarFormularios.libro(binding.tilAutor, binding.tilNombreLibro, binding.tilIsbn)) {
            if(db.librosDao().sp_Count_Isbn(binding.edIsbn.getText().toString()) == 0) {
                LibrosDB libro = new LibrosDB(binding.edAutor.getText().toString(), binding.edNombreLibro.getText().toString(), binding.edIsbn.getText().toString(),
                        binding.edFecha.getText().toString(), binding.edGenero.getText().toString(), binding.paises.getText().toString(), resultImagen);

                db.librosDao().sp_Ins_Book(libro);
                limpiarFormulario();
            }else{
                Toast.makeText(getApplicationContext(),"El isbn ingresado ya se encuentra registrado", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void limpiarFormulario(){
        binding.edAutor.setText("");
        binding.edNombreLibro.setText("");
        binding.edIsbn.setText("");
        binding.edFecha.setText("");
        binding.edGenero.setText("");
        resultImagen = "";
        binding.foto.setVisibility(View.GONE);
    }

    public boolean checkPermissions(){
        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if(permissionStorage != PackageManager.PERMISSION_GRANTED || permissionCamera != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA }, 1);
        }else{
            return true;
        }

        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                if(resultCode == Activity.RESULT_OK){
                    resultImagen = String.valueOf(uri);

                    Bitmap btmImagen = null;

                    try {
                        btmImagen = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(resultImagen));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("ERROR EN LA IMAGEN", e.getMessage());
                    }

                    binding.foto.setImageBitmap(btmImagen);
                    binding.foto.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}