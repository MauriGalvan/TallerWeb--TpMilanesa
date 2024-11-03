package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioPlanificadorImpl implements ServicioPlanificador{

    private RepositorioPlanificador repositorioPlanificador;

    @Autowired
    public ServicioPlanificadorImpl(RepositorioPlanificador repositorioPlanificador) {this.repositorioPlanificador = repositorioPlanificador;}

    @Override
    public Planificador obtenerPlanificador() {
        return this.repositorioPlanificador.obtenerPlanificador();
    }

    @Override
    public void guardar(Planificador planificador) { this.repositorioPlanificador.guardar(planificador); }

    @Override
    public void eliminar(Planificador planificador) { this.repositorioPlanificador.eliminar(planificador); }

    @Override
    public void actualizar(Planificador planificador) { this.repositorioPlanificador.actualizar(planificador); }

    @Override
    public void agregarDetalle(Planificador planificador, DetallePlanificador detalle) {
        for (DetallePlanificador cadaDetalle : planificador.obtenerDetalles()){
            if (cadaDetalle.getDia() == detalle.getDia() && cadaDetalle.getCategoria() == detalle.getCategoria()){
                planificador.eliminarDetalle(cadaDetalle);
                break;
            }
        }
        planificador.agregarDetalle(detalle);
    }

}
