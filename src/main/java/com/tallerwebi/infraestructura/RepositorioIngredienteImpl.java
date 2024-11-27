package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Ingrediente;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.RepositorioIngrediente;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository
public class RepositorioIngredienteImpl implements RepositorioIngrediente {

    @Autowired
    private SessionFactory sessionFactory;

    public RepositorioIngredienteImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void eliminar(Ingrediente ingrediente) {
        this.sessionFactory.getCurrentSession().delete(ingrediente);
    }

    @Override
    public void guardar(Ingrediente ingrediente) {
        this.sessionFactory.getCurrentSession().save(ingrediente);
    }

    @Transactional
    @Override
    public Ingrediente getIngredientePorId(int id) {
        String hql = "FROM Ingrediente i WHERE i.id = :id";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("id", id);
        return (Ingrediente)query.getSingleResult();
    }
}
