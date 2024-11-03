package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Planificador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    private List<DetallePlanificador> detallesPlanificador = new ArrayList<>();

    public Planificador() {
    }

    public void agregarDetalle(DetallePlanificador detalle){
        detallesPlanificador.add(detalle);
    }

    public void eliminarDetalle(DetallePlanificador detalle){
        detallesPlanificador.remove(detalle);
    }

    public List<DetallePlanificador> obtenerDetalles(){
        return this.detallesPlanificador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planificador that = (Planificador) o;
        return id == that.id && Objects.equals(detallesPlanificador, that.detallesPlanificador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, detallesPlanificador);
    }
}