package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    @Lob
    private byte[] imagen;

    @Transient
    private String imagenBase64;

    @OneToMany (mappedBy = "receta", fetch = FetchType.EAGER, cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Ingrediente> ingredientes = new ArrayList<>();

    private String titulo;
    private String descripcion;
    private String pasos;
    private int contador_visitas;
    private String autor;
    public Receta() {

    }
    public Receta(String titulo, TiempoDePreparacion tiempo_preparacion, Categoria categoria, byte[] imagen,
                  List<Ingrediente> ingredientes, String descripcion, String pasos){
        this.titulo = titulo;
        this.tiempo_preparacion = tiempo_preparacion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.descripcion = descripcion;
        this.pasos = pasos;
        this.contador_visitas = 0;

    }
    public Receta(String titulo, TiempoDePreparacion tiempo_preparacion, Categoria categoria, byte[] imagen,
                  List<Ingrediente> ingredientes, String descripcion, String pasos,String autor){
        this.titulo = titulo;
        this.tiempo_preparacion = tiempo_preparacion;
        this.categoria = categoria;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.descripcion = descripcion;
        this.pasos = pasos;
        this.contador_visitas = 0;
        this.autor = autor;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
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

    public void setTiempo_preparacion(TiempoDePreparacion tiempo_preparacion) {
        this.tiempo_preparacion = tiempo_preparacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void addIngrediente(Ingrediente ingrediente) {
        ingrediente.setReceta(this);
        this.ingredientes.add(ingrediente);
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes.clear();
        if (ingredientes != null) {
            ingredientes.forEach(this::addIngrediente);
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPasos() { return pasos; }

    public void setPasos(String pasos) { this.pasos = pasos; }

    public int getContadorVisitas() {return this.contador_visitas; }

    public void incrementarVisitas() { this.contador_visitas++; }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

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
