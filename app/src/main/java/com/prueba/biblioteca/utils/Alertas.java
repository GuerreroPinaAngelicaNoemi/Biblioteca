package com.prueba.biblioteca.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.prueba.biblioteca.ListaLibrosActivity;
import com.prueba.biblioteca.R;
import com.prueba.biblioteca.room.AppDatabase;
import com.prueba.biblioteca.room.LibrosDB;
import com.prueba.biblioteca.room.UsuariosDB;

import java.util.List;

public class Alertas {

    public static void registro(Activity context, LayoutInflater layoutInflater, AppDatabase db){

        final AlertDialog alertRegistro;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = layoutInflater.inflate(R.layout.form_registro, null);

        TextInputLayout correo = view.findViewById(R.id.tilCorreo);
        TextInputLayout contrasena = view.findViewById(R.id.tilContrasena);
        Button registrar = view.findViewById(R.id.registrar);

        builder.setView(view);
        alertRegistro = builder.create();


        registrar.setOnClickListener(view1 -> {
            if(ValidarFormularios.login(correo, contrasena)){
                if(db.usuarioDao().sp_Count_Email(correo.getEditText().getText().toString()) > 0){
                    Toast.makeText(alertRegistro.getContext(), "El correo ya existe", Toast.LENGTH_LONG).show();
                }else{
                    UsuariosDB usuariosDB = new UsuariosDB(correo.getEditText().getText().toString(), contrasena.getEditText().getText().toString());
                    db.usuarioDao().sp_Ins_User(usuariosDB);
                    alertRegistro.dismiss();
                    Toast.makeText(alertRegistro.getContext(), "Registro Correcto", Toast.LENGTH_LONG).show();
                }

            }
        });

        alertRegistro.setCancelable(true);
        alertRegistro.show();
    }

    public static void confirmar(ListaLibrosActivity context, LayoutInflater layoutInflater, AppDatabase db, LibrosDB librosDB){

        final AlertDialog alertConfirmar;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = layoutInflater.inflate(R.layout.alert_confirmar_eliminar, null);
        Button eliminar = view.findViewById(R.id.eliminar);
        Button cancelar = view.findViewById(R.id.cancelar);

        builder.setView(view);
        alertConfirmar = builder.create();

        eliminar.setOnClickListener(view1 -> {
            db.librosDao().sp_Del_Libro(librosDB);
            db.librosDao().sp_Del_Libro(librosDB);
            List<LibrosDB> listaLibros = db.librosDao().sp_Sel_All();
            context.cargarLibros(listaLibros);
            alertConfirmar.dismiss();
        });

        cancelar.setOnClickListener(view1 -> {
            alertConfirmar.dismiss();
        });

        alertConfirmar.setCancelable(true);
        alertConfirmar.show();
    }

}
