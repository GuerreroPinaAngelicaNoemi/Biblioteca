package com.prueba.biblioteca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.prueba.biblioteca.databinding.ActivityMainBinding;
import com.prueba.biblioteca.utils.LocalSp;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    public void init(){
        if(LocalSp.getData(getApplicationContext(), "iniciarconhuella").equals("1")){
            binding.iniciarConHuella.setChecked(true);
        }

        binding.crearLibro.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AgregarLibroActivity.class)));
        binding.verLibros.setOnClickListener(view ->  startActivity(new Intent(MainActivity.this, ListaLibrosActivity.class)));

        binding.cerrarSesion.setOnClickListener(view -> {
            LocalSp.setData(getApplicationContext(), "recordar", "0");
            LocalSp.setData(getApplicationContext(), "correo", "");
            LocalSp.setData(getApplicationContext(), "password","");
            finish();
        });

        binding.salir.setOnClickListener(view ->  finish());

        binding.iniciarConHuella.setOnClickListener(view -> {
            if(binding.iniciarConHuella.isChecked()){
                LocalSp.setData(getApplicationContext(), "iniciarconhuella", "1");
            }else{
                LocalSp.setData(getApplicationContext(), "iniciarconhuella", "0");
            }
        });

    }
}