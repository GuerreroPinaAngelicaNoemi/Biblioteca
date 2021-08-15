package com.prueba.biblioteca.room;

import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = LibrosDB.TABLE_NAME)
public class LibrosDB {

    public static final String TABLE_NAME = "book_db";

    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( index = true, name = COLUMN_ID)
    private int id;


    String autor;
    String nombreLibro;
    String isbn;
    String fecha;
    String genero;
    String region;
    String imagen;


    public LibrosDB(String autor, String nombreLibro, String isbn, String fecha, String genero, String region, String imagen) {
        this.autor = autor;
        this.nombreLibro = nombreLibro;
        this.isbn = isbn;
        this.fecha = fecha;
        this.genero = genero;
        this.region = region;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNombreLibro() {
        return nombreLibro;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
