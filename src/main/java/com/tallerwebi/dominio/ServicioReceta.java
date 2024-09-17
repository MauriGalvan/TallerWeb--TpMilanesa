package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioReceta {

    List<Receta> getTodasLasRecetas();

    void guardarReceta(Receta receta);
}
