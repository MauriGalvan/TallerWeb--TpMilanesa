package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ServicioRecetaImpl implements ServicioReceta {

    private final RepositorioReceta repositorioReceta;

    @Autowired
    public ServicioRecetaImpl(RepositorioReceta repositorioReceta) {
        this.repositorioReceta = repositorioReceta;
    }

    @Override
    public List<Receta> getTodasLasRecetas() {
        return this.repositorioReceta.getRecetas();
    }

    @Override
    public void guardarReceta(Receta receta) {
        this.repositorioReceta.guardar(receta);
    }

    @Override
    public List<Receta> getRecetasPorCategoria(String categoria) {
        List<Receta> recetasFiltradas = new ArrayList<>();
        List<Receta> todasLasRecetas = this.repositorioReceta.getRecetas();
        for (Receta receta : todasLasRecetas) {
            if (receta.getCategoria().equals(categoria)) {
                recetasFiltradas.add(receta);
            }
        }
        return recetasFiltradas;
    }

    @Override
    public List<Receta> getRecetasPorTiempoDePreparacion(double tiempoPreparacion) {
        List<Receta> recetasFiltradas = new ArrayList<>();
        List<Receta> todasLasRecetas = this.repositorioReceta.getRecetas();
        for (Receta receta : todasLasRecetas) {
            if (receta.getTiempo_preparacion() == tiempoPreparacion) {
                recetasFiltradas.add(receta);
            }
        }
        return recetasFiltradas;
    }
}
