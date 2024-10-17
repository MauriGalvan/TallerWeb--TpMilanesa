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
    public void agregarReceta(Receta receta) { repositorioPlanificador.agregarReceta(receta); }

    @Override
    public void guardarPlanificador(Planificador planificador) { this.repositorioPlanificador.guardarPlanificador(planificador); }
}
