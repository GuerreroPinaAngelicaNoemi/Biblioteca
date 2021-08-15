package com.prueba.biblioteca.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalSp {

    public static void setData(Context context, String name, String value){
        SharedPreferences prefs = context.getSharedPreferences(Constantes.SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name,value);
        editor.apply();
    }

    public static String getData(Context context, String name){
        SharedPreferences prefs = context.getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(name, "");
    }
}
