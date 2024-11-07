package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Query;
import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})

public class RepositorioIngredienteImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioIngrediente repositorioIngrediente;

    @BeforeEach
    public void inicializar(){
        this.repositorioIngrediente = new RepositorioIngredienteImpl(sessionFactory);
    }

    private Ingrediente crearIngredienteCarne(){
        return new Ingrediente("Carne", 1, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL);
    }
    private Ingrediente crearIngredienteHuevo(){
        return new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD,Tipo_Ingrediente.PROTEINA_ANIMAL);
    }

    @Test
    @Rollback
    @Transactional
    public void cuandoGuardoUnIngredienteEntoncesLoEncuentroEnLaBaseDeDatos(){
        Ingrediente ingrediente = this.crearIngredienteCarne();

        this.repositorioIngrediente.guardar(ingrediente);

        String hql = "FROM Ingrediente";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        Ingrediente ingredienteObtenido = (Ingrediente)query.getSingleResult();

        assertEquals(ingrediente, ingredienteObtenido);
    }

    @Test
    @Rollback
    @Transactional
    public void cuandoGuardoDosIngredientesYEliminoUnoEntoncesEncuentroUnoSoloEnLaBaseDeDatos(){
        Ingrediente ingrediente1 = this.crearIngredienteCarne();
        Ingrediente ingrediente2 = this.crearIngredienteHuevo();

        this.sessionFactory.getCurrentSession().save(ingrediente1);
        this.sessionFactory.getCurrentSession().save(ingrediente2);

        String hql = "FROM Ingrediente";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        List<Ingrediente> ingredientesObtenidos1 = query.getResultList();

        assertThat(ingredientesObtenidos1, hasItem(ingrediente1));
        assertThat(ingredientesObtenidos1, hasItem(ingrediente2));

        this.repositorioIngrediente.eliminar(ingrediente1);

        String hql1 = "FROM Ingrediente";
        Query query1 = this.sessionFactory.getCurrentSession().createQuery(hql1);
        List<Ingrediente> ingredientesObtenidos2 = query1.getResultList();

        assertThat(ingredientesObtenidos2, not(hasItem(ingrediente1)));
        assertThat(ingredientesObtenidos2, hasItem(ingrediente2));
    }
}
