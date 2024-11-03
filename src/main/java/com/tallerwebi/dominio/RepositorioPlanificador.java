package com.tallerwebi.dominio;

public interface RepositorioPlanificador {

    Planificador obtenerPlanificador();

    void guardar(Planificador planificador);

    void eliminar(Planificador planificador);

    void actualizar(Planificador planificador);
}
