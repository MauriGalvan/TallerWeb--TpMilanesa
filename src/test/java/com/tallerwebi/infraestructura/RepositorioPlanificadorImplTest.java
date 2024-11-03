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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})

public class RepositorioPlanificadorImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioPlanificador repositorioPlanificador;

    @BeforeEach
    public void inicializar(){
        this.repositorioPlanificador = new RepositorioPlanificadorImpl(sessionFactory);
    }

    private Planificador planificadorCreado(){
        Receta receta1 = new Receta ("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg", ".", "Un clásico de las mañanas.", ".");
        DetallePlanificador detalle1 = new DetallePlanificador(Dia.MARTES, Categoria.DESAYUNO_MERIENDA, receta1);
        Receta receta2 = new Receta("Tarta de jamón y queso", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/tarta.jpg", ".", "Deliciosa tarta de jamón y queso.", ".");
        DetallePlanificador detalle2 = new DetallePlanificador(Dia.DOMINGO, Categoria.ALMUERZO_CENA, receta2);
        Planificador planificador = new Planificador();
        planificador.agregarDetalle(detalle1);
        planificador.agregarDetalle(detalle2);
        return planificador;
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnPlanificadorQueSePuedaGuardarElMismo(){
        Planificador planificador = this.planificadorCreado();

        this.repositorioPlanificador.guardar(planificador);

        String hql = "FROM Planificador";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        Planificador planificadorObtenido = (Planificador)query.getSingleResult();

        assertEquals(planificador, planificadorObtenido);
    }

    @Test
    @Rollback
    @Transactional
    public void queSePuedaObtenerElPlanificadorGuardado(){
        Planificador planificador = this.planificadorCreado();

        this.sessionFactory.getCurrentSession().save(planificador);

        Planificador planificadorObtenido = this.repositorioPlanificador.obtenerPlanificador();

        assertEquals(planificador, planificadorObtenido);
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnPlanificadorQueSePuedaBorrarElMismo(){
        Planificador planificador = this.planificadorCreado();

        this.sessionFactory.getCurrentSession().save(planificador);

        String hql1 = "FROM Planificador";
        Query query1 = this.sessionFactory.getCurrentSession().createQuery(hql1);
        Planificador planificadorObtenido1 = (Planificador)query1.getSingleResult();

        assertEquals(planificador, planificadorObtenido1);

        this.repositorioPlanificador.eliminar(planificador);

        String hql2 = "FROM Planificador";
        Query query2 = this.sessionFactory.getCurrentSession().createQuery(hql2);
        List<Planificador> planificadorObtenido2 = query2.getResultList();
        //list porque de otra forma no podemos verificar el resultado con un assert

        assertEquals(0, planificadorObtenido2.size());
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnPlanificadorQueSePuedaActualizarElMismo(){
        Planificador planificador = this.planificadorCreado();

        this.sessionFactory.getCurrentSession().save(planificador);

        String hql1 = "FROM Planificador";
        Query query1 = this.sessionFactory.getCurrentSession().createQuery(hql1);
        Planificador planificadorObtenido1 = (Planificador)query1.getSingleResult();

        assertEquals(planificador, planificadorObtenido1);
        assertEquals(2, planificadorObtenido1.obtenerDetalles().size());


        Receta receta = new Receta ("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/mila-papas.jpg", ".", "Milanesa con guarnición de papas fritas", ".");
        DetallePlanificador detalle = new DetallePlanificador(Dia.LUNES, Categoria.ALMUERZO_CENA, receta);
        planificador.agregarDetalle(detalle);

        this.repositorioPlanificador.actualizar(planificador);

        String hql2 = "FROM Planificador";
        Query query2 = this.sessionFactory.getCurrentSession().createQuery(hql2);
        Planificador planificadorObtenido2 = (Planificador)query2.getSingleResult();

        assertEquals(planificador, planificadorObtenido2);
        assertEquals(3, planificadorObtenido2.obtenerDetalles().size());
    }
}
