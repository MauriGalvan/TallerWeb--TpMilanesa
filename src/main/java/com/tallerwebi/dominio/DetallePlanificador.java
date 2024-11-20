package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class DetallePlanificador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private Dia dia;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private String categoriaDelPlanificador;

    @OneToOne
    private Receta receta;

    public DetallePlanificador(Dia dia, Categoria categoria, Receta receta, String categoriaDelPlanificador) {
        this.dia = dia;
        this.categoria = categoria;
        this.receta = receta;
        this.categoriaDelPlanificador = categoriaDelPlanificador;
    }

    public DetallePlanificador() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Receta getReceta() { return receta; }

    public void setReceta(Receta recetas) { this.receta = receta; }

    public String getCategoriaDelPlanificador() { return categoriaDelPlanificador; }

    public void setCategoriaDelPlanificador(String categoriaDelPlanificador) { this.categoriaDelPlanificador = categoriaDelPlanificador; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallePlanificador that = (DetallePlanificador) o;
        return id == that.id && dia == that.dia && categoria == that.categoria && Objects.equals(categoriaDelPlanificador, that.categoriaDelPlanificador) && Objects.equals(receta, that.receta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dia, categoria, categoriaDelPlanificador, receta);
    }
}


