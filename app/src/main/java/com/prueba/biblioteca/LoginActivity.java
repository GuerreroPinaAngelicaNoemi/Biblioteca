package com.prueba.biblioteca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.prueba.biblioteca.databinding.ActivityLoginBinding;
import com.prueba.biblioteca.fingerLogin.FingerClass;
import com.prueba.biblioteca.fingerLogin.FingerprintHandler;
import com.prueba.biblioteca.room.AppDatabase;
import com.prueba.biblioteca.utils.AlertCargando;
import com.prueba.biblioteca.utils.Alertas;
import com.prueba.biblioteca.utils.Constantes;
import com.prueba.biblioteca.utils.LocalSp;
import com.prueba.biblioteca.utils.ValidarFormularios;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private AppDatabase db;

    /** Variables para desbloqueo por huella digital **/
    KeyguardManager keyguardManager;
    FingerprintManager fingerprintManager;
    FingerClass fingerClass;

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

        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        if(LocalSp.getData(getApplicationContext(), "iniciarconhuella").equals("1")){
            ingresarConHuella();
        }else{
            binding.huella.setVisibility(View.INVISIBLE);
        }

        if(fingerprintManager.isHardwareDetected()) {
            binding.huella.setOnClickListener(view -> ingresarConHuella());
        }else{
            binding.huella.setVisibility(View.INVISIBLE);
        }

    }

    public void ingresarConHuella(){
        // Check whether the device has a Fingerprint sensor.

        // Checks whether fingerprint permission is set on manifest
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            //      Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
            //  textView.setText("Fingerprint authentication permission not enabled");
        } else {
            //     Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
            // Check whether at least one fingerprint is registered
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                //      Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
                //   textView.setText("Register at least one fingerprint in Settings");
            } else {
                //      Toast.makeText(getContext(), "4", Toast.LENGTH_SHORT).show();
                // Checks whether lock screen security is enabled or not
                if (!keyguardManager.isKeyguardSecure()) {
                    //       Toast.makeText(getContext(), "5", Toast.LENGTH_SHORT).show();
                    //   textView.setText("Lock screen security not enabled in Settings");
                } else {
                    //    Toast.makeText(getContext(), "6", Toast.LENGTH_SHORT).show();
                    fingerClass = new FingerClass();
                    fingerClass.generateKey();


                    if (fingerClass.cipherInit()) {
                        //          Toast.makeText(getContext(), "7", Toast.LENGTH_SHORT).show();
                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(fingerClass.getCipher());
                        FingerprintHandler helper = new FingerprintHandler(LoginActivity.this);
                        helper.startAuth(fingerprintManager, cryptoObject);
                    }else{
                        //       Toast.makeText(getContext(), "8", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }

    public void recordarCredenciales(){
        if(binding.recordar.isChecked()){
            LocalSp.setData(getApplicationContext(), "correo", binding.edCorreo.getText().toString());
            LocalSp.setData(getApplicationContext(), "password", binding.edContrasena.getText().toString());
            LocalSp.setData(getApplicationContext(), "recordar", "1");
        }
    }
}