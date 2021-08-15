package com.prueba.biblioteca.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class Utilidades {

    public static boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return  pattern.matcher(email).matches();
    }
}
