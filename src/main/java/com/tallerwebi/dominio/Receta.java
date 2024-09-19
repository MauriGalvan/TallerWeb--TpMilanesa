package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;
    private double tiempo_preparacion;
    private String categoria;
    private String imagen;
    private String ingredientes;
    private String descripcion;
    private String pasos;

    public Receta() {

    }
    public Receta(String titulo, double tiempo_preparacion, String categoria, String imagen,
                  String ingredientes, String descripcion, String pasos){
        this.titulo = titulo;
        this.tiempo_preparacion = tiempo_preparacion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.descripcion = descripcion;
        this.pasos = pasos;

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

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPasos() { return pasos; }

    public void setPasos(String pasos) { this.pasos = pasos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receta receta = (Receta) o;
        return id == receta.id && Double.compare(tiempo_preparacion, receta.tiempo_preparacion) == 0 && Objects.equals(titulo, receta.titulo) && Objects.equals(categoria, receta.categoria) && Objects.equals(imagen, receta.imagen) && Objects.equals(ingredientes, receta.ingredientes) && Objects.equals(descripcion, receta.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion);
    }
}
