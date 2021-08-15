package com.prueba.biblioteca.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.prueba.biblioteca.R;

public class AlertCargando {

    AlertDialog cargando;

    String texto;
    Activity activity;
    LayoutInflater layoutInflater;

    public AlertCargando(String texto, Activity activity, LayoutInflater layoutInflater) {
        this.texto = texto;
        this.activity = activity;
        this.layoutInflater = layoutInflater;
    }

    public void crearCarga(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater =layoutInflater;
        View view = inflater.inflate(R.layout.alert_carga, null);

        builder.setView(view);

        cargando = builder.create();
        cargando.setCancelable(false);

    }

    public void mostrar(){
        if(!cargando.isShowing()){
            cargando.show();
        }
    }

    public void ocultar(){
        if(cargando.isShowing()){
            cargando.dismiss();
        }
    }
}
