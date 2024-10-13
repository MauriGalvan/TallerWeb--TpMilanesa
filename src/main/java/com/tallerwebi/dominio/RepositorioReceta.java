package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioReceta {

    void guardar(Receta receta);

    void eliminar(Receta receta);

    void actualizar(Receta receta);

    List<Receta> getRecetas();

    Receta getRecetaPorId(int id);

    List<Receta> getRecetasPorCategoria(Categoria categoria);

    List<Receta> getRecetasPorTiempoDePreparacion(TiempoDePreparacion tiempo);

    List<Receta> getRecetasPorCategoriaYTiempoDePreparacion(Categoria categoria, TiempoDePreparacion tiempo);

    List<Receta> buscarRecetasPorTitulo(String titulo);

    List<Receta> listRecetaRecomendadas(List<String> titulos);
}
