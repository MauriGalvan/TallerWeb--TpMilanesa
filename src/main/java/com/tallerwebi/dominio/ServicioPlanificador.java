package com.tallerwebi.dominio;

public interface ServicioPlanificador {

    Planificador obtenerPlanificador();

    Planificador obtenerPlanificadorConDetalles();

    void guardar(Planificador planificador);

    void eliminar(Planificador planificador);

    void actualizar(Planificador planificador);

    void agregarDetalle(Planificador planificador, DetallePlanificador detalle);
}
