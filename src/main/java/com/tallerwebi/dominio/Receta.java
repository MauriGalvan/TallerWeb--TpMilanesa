package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private TiempoDePreparacion tiempo_preparacion;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private String titulo;
    private String imagen;
    private String ingredientes;
    private String descripcion;
    private String pasos;
    private int contadorClicks;

    public Receta() {

    }
    public Receta(String titulo, TiempoDePreparacion tiempo_preparacion, Categoria categoria, String imagen,
                  String ingredientes, String descripcion, String pasos){
        this.titulo = titulo;
        this.tiempo_preparacion = tiempo_preparacion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.descripcion = descripcion;
        this.pasos = pasos;
        this.contadorClicks = 0;

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

    public TiempoDePreparacion getTiempo_preparacion() {
        return tiempo_preparacion;
    }

    public void setTiempo_preparacion(TiempoDePreparacion TiempoDePreparacion) {
        this.tiempo_preparacion = TiempoDePreparacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
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

    public int getContadorClicks() {return this.contadorClicks; }

    public void incrementarClicks() { this.contadorClicks++; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receta receta = (Receta) o;
        return id == receta.id && Objects.equals(titulo, receta.titulo) && tiempo_preparacion == receta.tiempo_preparacion && categoria == receta.categoria && Objects.equals(imagen, receta.imagen) && Objects.equals(ingredientes, receta.ingredientes) && Objects.equals(descripcion, receta.descripcion) && Objects.equals(pasos, receta.pasos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion, pasos);
    }
}
