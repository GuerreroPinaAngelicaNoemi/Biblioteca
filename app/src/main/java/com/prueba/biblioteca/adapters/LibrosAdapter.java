package com.prueba.biblioteca.adapters;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prueba.biblioteca.R;
import com.prueba.biblioteca.room.LibrosDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibrosAdapter extends RecyclerView.Adapter<LibrosAdapter.exViewHolder> {

    private ArrayList<LibrosDB> listaLibros;
    private OnItemClickListener mListener;
    private ContentResolver contentResolver;

    public LibrosAdapter(ArrayList<LibrosDB> listaLibros, ContentResolver contentResolver) {
        this.listaLibros = listaLibros;
        this.contentResolver = contentResolver;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class exViewHolder extends RecyclerView.ViewHolder{

        TextView autor;
        TextView nomLibro;
        TextView fecha;
        TextView genero;
        TextView isbn;
        TextView pais;
        ImageView imagen;

        public exViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            autor = itemView.findViewById(R.id.autor);
            nomLibro = itemView.findViewById(R.id.nomLibro);
            fecha = itemView.findViewById(R.id.fecha);
            genero = itemView.findViewById(R.id.genero);
            isbn = itemView.findViewById(R.id.isbn);
            pais = itemView.findViewById(R.id.pais);
            imagen = itemView.findViewById(R.id.imagen);

            itemView.setOnClickListener(view -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });

        }
    }

    @Override
    public exViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_libros, parent, false);
        exViewHolder evh = new exViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(exViewHolder holder, int position) {
        LibrosDB libro = listaLibros.get(position);

        holder.autor.setText(libro.getAutor());
        holder.nomLibro.setText(libro.getNombreLibro());
        holder.fecha.setText(libro.getFecha());
        holder.genero.setText(libro.getGenero());
        holder.isbn.setText(libro.getIsbn());
        holder.pais.setText(libro.getRegion());
        Bitmap btmImagen = null;
        try {
            btmImagen = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(libro.getImagen()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(btmImagen != null){
            holder.imagen.setImageBitmap(btmImagen);
        }

    }

    @Override
    public int getItemCount() {

        if(listaLibros != null){
            return listaLibros.size();
        }else{
            return  0;
        }
    }
}
