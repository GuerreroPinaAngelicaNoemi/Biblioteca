package com.prueba.biblioteca.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UsuariosDB.class, LibrosDB.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    public abstract UsuarioDao usuarioDao();
    public abstract LibrosDao librosDao();

}
