package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "receta_id")
    private Receta receta;

    @Enumerated(EnumType.STRING)
    private Unidad_De_Medida unidad_de_medida;

    @Enumerated(EnumType.STRING)
    private Tipo_Ingrediente tipo;

    private String nombre;
    private double cantidad;
//    private Boolean disponible;

    public Ingrediente(){
    }
    public Ingrediente(String nombre, double cantidad, Unidad_De_Medida unidad_de_medida, Tipo_Ingrediente tipo){
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.unidad_de_medida = unidad_de_medida;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Unidad_De_Medida getUnidad_de_medida() {
        return unidad_de_medida;
    }

    public void setUnidad_de_medida(Unidad_De_Medida unidad_de_medida) {
        this.unidad_de_medida = unidad_de_medida;
    }

    public Tipo_Ingrediente getTipo() {
        return tipo;
    }

    public void setTipo(Tipo_Ingrediente tipo) {
        this.tipo = tipo;
    }

//    public Boolean isDisponible() {
//        return disponible;
//    }
//
//    public void setDisponible(Boolean disponible) {
//        this.disponible = disponible;
//    }
}
