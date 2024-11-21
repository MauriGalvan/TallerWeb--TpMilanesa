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

import java.util.Arrays;
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

    private List<Ingrediente> unosIngredientes(){
        return Arrays.asList(
                new Ingrediente("Carne", 1, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Papas", 10, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA),
                new Ingrediente("Pan rallado", 200, Unidad_De_Medida.GRAMOS, Tipo_Ingrediente.CEREAL_O_GRANO)
        );
    }
    private Planificador planificadorCreado(){
        byte[] imagen = new byte[]{0, 1};
        Receta receta1 = new Receta ("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                imagen, this.unosIngredientes(), "Un clásico de las mañanas.", ".");
        this.sessionFactory.getCurrentSession().save(receta1);
        DetallePlanificador detalle1 = new DetallePlanificador(Dia.MARTES, Categoria.DESAYUNO_MERIENDA, receta1, "Desayuno");
        Receta receta2 = new Receta("Tarta de jamón y queso", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                imagen, this.unosIngredientes(), "Deliciosa tarta de jamón y queso.", ".");
        this.sessionFactory.getCurrentSession().save(receta2);
        DetallePlanificador detalle2 = new DetallePlanificador(Dia.DOMINGO, Categoria.ALMUERZO_CENA, receta2, "Cena");
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

        byte[] imagen = new byte[]{0, 1};
        Receta receta = new Receta ("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                imagen, this.unosIngredientes(), "Milanesa con guarnición de papas fritas", ".");
        this.sessionFactory.getCurrentSession().save(receta);
        DetallePlanificador detalle = new DetallePlanificador(Dia.LUNES, Categoria.ALMUERZO_CENA, receta, "Almuerzo");
        planificador.agregarDetalle(detalle);

        this.repositorioPlanificador.actualizar(planificador);

        String hql2 = "FROM Planificador";
        Query query2 = this.sessionFactory.getCurrentSession().createQuery(hql2);
        Planificador planificadorObtenido2 = (Planificador)query2.getSingleResult();

        assertEquals(planificador, planificadorObtenido2);
        assertEquals(3, planificadorObtenido2.obtenerDetalles().size());
    }
}
