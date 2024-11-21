package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.DetallePlanificador;
import com.tallerwebi.dominio.Planificador;
import com.tallerwebi.dominio.RepositorioPlanificador;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class RepositorioPlanificadorImpl implements RepositorioPlanificador {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPlanificadorImpl(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

    @Override
    public Planificador obtenerPlanificador() {
        String hql = "FROM Planificador";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        List<Planificador> resultados = query.getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Override
    public void guardar(Planificador planificador) {
        this.sessionFactory.getCurrentSession().save(planificador);
    }

    @Override
    public void eliminar(Planificador planificador) {
        this.sessionFactory.getCurrentSession().delete(planificador);
    }

    @Override
    public void actualizar(Planificador planificador) {
        this.sessionFactory.getCurrentSession().update(planificador);
    }

    @Override
    public List<DetallePlanificador> obtenerDetallesDelPlanificador() {
        String hql = "SELECT dp FROM Planificador p JOIN p.detallesPlanificador dp";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        List<DetallePlanificador> detalles = query.getResultList();

        return detalles;
    }
}
