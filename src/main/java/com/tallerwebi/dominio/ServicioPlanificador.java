package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPlanificador {

    Planificador obtenerPlanificador();

    void guardar(Planificador planificador);

    void eliminar(Planificador planificador);

    void actualizar(Planificador planificador);

    void agregarDetalle(Planificador planificador, DetallePlanificador detalle);

    List<DetallePlanificador> obtenerDetallesDelPlanificador();
}
