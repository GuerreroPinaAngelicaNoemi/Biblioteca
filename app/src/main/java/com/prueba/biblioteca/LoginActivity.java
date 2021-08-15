package com.prueba.biblioteca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.prueba.biblioteca.databinding.ActivityLoginBinding;
import com.prueba.biblioteca.room.AppDatabase;
import com.prueba.biblioteca.utils.AlertCargando;
import com.prueba.biblioteca.utils.Alertas;
import com.prueba.biblioteca.utils.Constantes;
import com.prueba.biblioteca.utils.LocalSp;
import com.prueba.biblioteca.utils.ValidarFormularios;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    public void init(){

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constantes.Database_Name).allowMainThreadQueries().build();
        if(LocalSp.getData(getApplicationContext(), "recordar").equals("1")){
            if(db.usuarioDao().sp_Exists_User(LocalSp.getData(getApplicationContext(), "correo"),LocalSp.getData(getApplicationContext(), "password")) > 0) {
               startActivity(new Intent(LoginActivity.this, MainActivity.class));
               finish();
            }
        }

        AlertCargando cargando = new AlertCargando("Validando", LoginActivity.this, getLayoutInflater());
        cargando.crearCarga();

        binding.ingresar.setOnClickListener(view -> {
            cargando.mostrar();
            new Handler().postDelayed(()->{
                if(ValidarFormularios.login(binding.tilCorreo, binding.tilContrasena)){
                    if(db.usuarioDao().sp_Exists_User(binding.edCorreo.getText().toString(),binding.edContrasena.getText().toString()) > 0) {
                        recordarCredenciales();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(getApplicationContext(), "Las credenciales son incorrectas", Toast.LENGTH_LONG).show();
                    }
                }
                cargando.ocultar();
            }, 3000);
        });

        binding.registrar.setOnClickListener(view -> Alertas.registro(LoginActivity.this, getLayoutInflater(), db));

    }

    public void recordarCredenciales(){
        if(binding.recordar.isChecked()){
            LocalSp.setData(getApplicationContext(), "correo", binding.edCorreo.getText().toString());
            LocalSp.setData(getApplicationContext(), "password", binding.edContrasena.getText().toString());
            LocalSp.setData(getApplicationContext(), "recordar", "1");
        }
    }
}