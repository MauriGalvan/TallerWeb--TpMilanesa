package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioReceta {

    List<Receta> getTodasLasRecetas();

    void guardarReceta(Receta receta);

    List<Receta> getRecetasPorCategoria(String categoria);

    List<Receta> getRecetasPorTiempoDePreparacion(double tiempoPreparacion);

    List<Receta> getRecetasPorCategoriaYTiempoDePreparacion(String categoria, double tiempoPreparacion);
}
