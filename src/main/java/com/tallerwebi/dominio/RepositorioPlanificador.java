package com.tallerwebi.dominio;

public interface RepositorioPlanificador {

    Planificador obtenerPlanificador();

    void agregarReceta(Receta receta);

    void guardarPlanificador(Planificador planificador);
}
