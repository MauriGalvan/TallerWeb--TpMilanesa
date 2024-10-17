package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;

@Entity
public class Planificador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Dia dia;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @OneToMany
//    @JoinColumn(name = "receta_id")
    private List<Receta> receta;

    public Planificador() {}

    public Planificador(Dia dia, Categoria categoria, List<Receta> receta) {
        this.dia = dia;
        this.categoria = categoria;
        this.receta = receta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Receta> getReceta() {
        return receta;
    }

    public void setReceta(List<Receta> receta) {
        this.receta = receta;
    }
}
