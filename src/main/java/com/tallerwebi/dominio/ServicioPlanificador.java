package com.tallerwebi.dominio;

public interface ServicioPlanificador {
    Planificador obtenerPlanificador();

    void agregarReceta(Receta receta);

    void guardarPlanificador(Planificador planificador);
}
