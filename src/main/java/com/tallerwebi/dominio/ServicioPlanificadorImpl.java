package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServicioPlanificadorImpl implements ServicioPlanificador{

    private RepositorioPlanificador repositorioPlanificador;

    @Autowired
    public ServicioPlanificadorImpl(RepositorioPlanificador repositorioPlanificador) {this.repositorioPlanificador = repositorioPlanificador;}


    @Transactional
    @Override
    public Planificador obtenerPlanificador() {
        Planificador planificador = repositorioPlanificador.obtenerPlanificador();
        if (planificador == null) {
            planificador = new Planificador();
            repositorioPlanificador.guardar(planificador);
        }
        return planificador;
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
            if (cadaDetalle.getDia() == detalle.getDia() && cadaDetalle.getCategoria() == detalle.getCategoria() && cadaDetalle.getCategoriaDelPlanificador().equals(detalle.getCategoriaDelPlanificador())){
                planificador.eliminarDetalle(cadaDetalle);
                break;
            }
        }
        planificador.agregarDetalle(detalle);
    }

    @Override
    public List<DetallePlanificador> obtenerDetallesDelPlanificador() {
        return this.repositorioPlanificador.obtenerDetallesDelPlanificador();
    }

}
