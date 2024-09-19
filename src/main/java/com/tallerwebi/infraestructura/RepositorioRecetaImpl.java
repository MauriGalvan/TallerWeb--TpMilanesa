package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.RepositorioReceta;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioRecetaImpl implements RepositorioReceta {

    @Autowired
    private SessionFactory sessionFactory;

    public RepositorioRecetaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Receta receta) {
        if (receta == null) {
            throw new NullPointerException("La receta no puede ser null");
        }
        sessionFactory.getCurrentSession().save(receta);
    }


    @Override
    public void eliminar(Receta receta) {
        sessionFactory.getCurrentSession().delete(receta);
    }


    @Override
    public List<Receta> getRecetas() {
        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        return query.getResultList();
    }

    @Transactional
    @Override
    public Receta getRecetaPorId(int id) {
        List<Receta> recetas = getRecetas();
        for (Receta receta : recetas){
            if (id == receta.getId()){
                return receta;
            }
        }
        return null;
    }
}
