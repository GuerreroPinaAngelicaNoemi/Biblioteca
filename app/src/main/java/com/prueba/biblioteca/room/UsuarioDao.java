package com.prueba.biblioteca.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDao {

    @Insert
    void sp_Ins_User(UsuariosDB usuariosDB);

    @Delete
    void sp_Del_User(UsuariosDB usuariosDB);

    @Query("SELECT COUNT(*) FROM " + UsuariosDB.TABLE_NAME + " WHERE email = :email")
    int sp_Count_Email(String email);

    @Query("SELECT COUNT(*) FROM " + UsuariosDB.TABLE_NAME + " WHERE email = :email AND password = :password")
    int sp_Exists_User(String email, String password);


}
