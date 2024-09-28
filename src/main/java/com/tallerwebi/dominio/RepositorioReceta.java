package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioReceta {

    void guardar(Receta receta);

    void eliminar(Receta receta);

    List<Receta> getRecetas();

    Receta getRecetaPorId(int id);

    List<Receta> getRecetasPorCategoria(String categoria);

    List<Receta> getRecetasPorTiempoDePreparacion(double tiempo);

    List<Receta> getRecetasPorCategoriaYTiempoDePreparacion(String categoria, double tiempo);

    List<Receta> buscarRecetasPorTitulo(String titulo);
}
