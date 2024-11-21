package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioPlanificador {

    Planificador obtenerPlanificador();

    void guardar(Planificador planificador);

    void eliminar(Planificador planificador);

    void actualizar(Planificador planificador);

    List<DetallePlanificador> obtenerDetallesDelPlanificador();
}
