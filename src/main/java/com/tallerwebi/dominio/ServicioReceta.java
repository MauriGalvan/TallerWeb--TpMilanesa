package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioReceta {

    List<Receta> getTodasLasRecetas();

    void guardarReceta(Receta receta);

    List<Receta> getRecetasPorCategoria(Categoria categoria);

    List<Receta> getRecetasPorTiempoDePreparacion(TiempoDePreparacion tiempoPreparacion);

    List<Receta> getRecetasPorCategoriaYTiempoDePreparacion(Categoria categoria, TiempoDePreparacion tiempoPreparacion);

    Receta getUnaRecetaPorId(int id);

    void eliminarReceta(Receta receta);

    void actualizarReceta(Receta receta);

    List<Receta> buscarRecetasPorTitulo(String titulo);

//    List<Receta> ordenarPorPopularidad(List<Receta> recetas);

    void actualizarClicksDeReceta(Receta receta);
}
