package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioReceta {

    void guardar(Receta receta);

    void eliminar(Receta receta);

    List<Receta> getRecetas();
}