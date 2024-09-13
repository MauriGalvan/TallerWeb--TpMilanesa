package com.tallerwebi.presentacion;

import java.util.ArrayList;

public class Receta {
    private String titulo;
    private int id;
    private double tiempo_preparacion;
    private String categoria;
    private String imagen;
    private ArrayList<String> ingredientes;
    private String descripcion;

    public Receta() {

    }
    public Receta(String titulo, double tiempo_preparacion, String categoria, String imagen,
                  ArrayList<String> ingredientes, String descripcion){
        this.titulo = titulo;
        this.tiempo_preparacion = tiempo_preparacion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTiempo_preparacion() {
        return tiempo_preparacion;
    }

    public void setTiempo_preparacion(double tiempo_preparacion) {
        this.tiempo_preparacion = tiempo_preparacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
