package com.tallerwebi.dominio;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ServicioReceta {

    List<Receta> getTodasLasRecetas();

    void guardarReceta(Receta receta, MultipartFile imagen);

    List<Receta> getRecetasPorCategoria(Categoria categoria);

    List<Receta> getRecetasPorTiempoDePreparacion(TiempoDePreparacion tiempoPreparacion);

    List<Receta> getRecetasPorCategoriaYTiempoDePreparacion(Categoria categoria, TiempoDePreparacion tiempoPreparacion);

    Receta getUnaRecetaPorId(int id);

    void eliminarReceta(Receta receta);

    void actualizarReceta(Receta receta);

    List<Receta> buscarRecetasPorTitulo(String titulo);

//    List<Receta> ordenarPorPopularidad(List<Receta> recetas);

    void actualizarVisitasDeReceta(Receta receta);

    List<Receta> buscarRecetasPorTituloCategoriaYTiempo(String titulo, Categoria categoriaEnum, TiempoDePreparacion tiempoEnum);

    List<Receta> buscarRecetasPorTituloYCategoria(String titulo, Categoria categoriaEnum);

    List<Receta> buscarRecetasPorTituloYTiempo(String titulo, TiempoDePreparacion tiempoEnum);
}
