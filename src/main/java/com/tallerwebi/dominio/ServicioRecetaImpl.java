package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    @Transactional
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

    @Transactional
    @Override
    public void eliminarReceta(Receta receta) {
        this.repositorioReceta.eliminar(receta);
    }

    @Transactional
    @Override
    public void actualizarReceta(Receta receta) {
        Receta recetaExistente = repositorioReceta.getRecetaPorId(receta.getId());
        if (recetaExistente != null) {
            recetaExistente.setTitulo(receta.getTitulo());
            recetaExistente.setTiempo_preparacion(receta.getTiempo_preparacion());
            recetaExistente.setIngredientes(receta.getIngredientes());
            recetaExistente.setPasos(receta.getPasos());
            recetaExistente.setImagen(receta.getImagen());
            System.out.println("Tiempo de preparación después de actualizar: " + recetaExistente.getTiempo_preparacion());
            repositorioReceta.guardar(recetaExistente);
        }
    }


    @Override
    public List<Receta> buscarRecetasPorTitulo(String titulo) {
        return repositorioReceta.buscarRecetasPorTitulo(titulo);
    }

}
