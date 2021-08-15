package com.prueba.biblioteca.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LibrosDao {

    @Insert
    void sp_Ins_Book(LibrosDB librosDB);

    @Query(" SELECT * FROM " + LibrosDB.TABLE_NAME)
    List<LibrosDB> sp_Sel_All();

    @Query(" SELECT COUNT(*) FROM " + LibrosDB.TABLE_NAME + " WHERE isbn = :isbn")
    int sp_Count_Isbn(String isbn);

    @Delete
    void sp_Del_Libro(LibrosDB librosDB);

    @Update
    void sp_Upd_Libro(LibrosDB librosDB);
}
