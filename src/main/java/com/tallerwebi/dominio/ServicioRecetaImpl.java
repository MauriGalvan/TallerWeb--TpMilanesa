package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Receta guardarReceta(Receta receta) {
     this.repositorioReceta.guardar(receta);
        return receta;
    }

    @Override
    public List<Receta> getRecetasPorCategoria(Categoria categoria) {
        return this.repositorioReceta.getRecetasPorCategoria(categoria);
    }

    @Override
    public List<Receta> getRecetasPorTiempoDePreparacion(TiempoDePreparacion tiempoPreparacion) {
        return this.repositorioReceta.getRecetasPorTiempoDePreparacion(tiempoPreparacion);
    }

    @Override
    public List<Receta> getRecetasPorCategoriaYTiempoDePreparacion(Categoria categoria, TiempoDePreparacion tiempoPreparacion) {
        return this.repositorioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempoPreparacion);
    }

    @Override
    public Receta getUnaRecetaPorId(int id) {
        return this.repositorioReceta.getRecetaPorId(id);
    }
}
