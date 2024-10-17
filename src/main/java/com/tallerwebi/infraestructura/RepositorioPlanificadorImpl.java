package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Planificador;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.RepositorioPlanificador;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

@Repository
public class RepositorioPlanificadorImpl implements RepositorioPlanificador {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPlanificadorImpl(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

    @Override
    public Planificador obtenerPlanificador() {
        String hql = "FROM Planificador";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return (Planificador)query.getSingleResult();
    }

    @Override
    public void agregarReceta(Receta receta) { this.sessionFactory.getCurrentSession().save(receta); }

    @Override
    public void guardarPlanificador(Planificador planificador) { this.sessionFactory.getCurrentSession().save(planificador); }
}
