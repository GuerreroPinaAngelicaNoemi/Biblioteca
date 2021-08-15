package com.prueba.biblioteca.utils;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ValidarFormularios {

    public static boolean login (TextInputLayout correo, TextInputLayout contrasena){
        boolean valido = true;

        if(correo.getEditText().getText().toString().equals("")){
            valido = false;
            correo.setError("El campo es requerido");
        }else{
            if(!Utilidades.validarEmail(correo.getEditText().getText().toString())){
                valido = false;
                correo.setError("Porfavor ingrese un correo");
            }else{
                correo.setError(null);
            }
        }
        if(contrasena.getEditText().getText().toString().equals("")){
            valido = false;
            contrasena.setError("El campo es requerido");
        }else{
            contrasena.setError(null);
        }

        return valido;
    }

    public static boolean libro(TextInputLayout autor, TextInputLayout nombreLibro, TextInputLayout isbn){ // indicar los campos requeridos
        boolean valido = true;

        if(autor.getEditText().getText().toString().equals("")){
            valido = false;
            autor.setError("El campo es requerido");
        }else{
            autor.setError(null);
        }

        if(nombreLibro.getEditText().getText().toString().equals("")){
            valido = false;
            nombreLibro.setError("El campo es requerido");
        }else{
            nombreLibro.setError(null);
        }

        if(isbn.getEditText().getText().toString().equals("")){
            valido = false;
            isbn.setError("El campo es requerido");
        }else{
            isbn.setError(null);
        }


        return valido;
    }
}
