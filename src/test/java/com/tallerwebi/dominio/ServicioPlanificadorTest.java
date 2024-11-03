package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ServicioPlanificadorTest {

    @Mock
    private RepositorioPlanificador repositorioPlanificador;
    private ServicioPlanificador servicioPlanificador;

    @BeforeEach
    public void inicializar(){
        this.repositorioPlanificador = mock(RepositorioPlanificador.class);
        this.servicioPlanificador = new ServicioPlanificadorImpl(repositorioPlanificador);
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
    public void queSePuedaGuardarUnPlanificador(){
        Planificador planificador = this.planificadorCreado();

        servicioPlanificador.guardar(planificador);

        Mockito.verify(repositorioPlanificador, times(1)).guardar(planificador);
    }

    @Test
    public void queSePuedaObtenerElPlanificador(){
        Planificador planificador = this.planificadorCreado();

        Mockito.when(repositorioPlanificador.obtenerPlanificador()).thenReturn(planificador);

        Planificador planificadorObtenido = servicioPlanificador.obtenerPlanificador();

        assertEquals(planificador, planificadorObtenido);
    }

    @Test
    public void queSePuedaEliminarUnPlanificador(){
        Planificador planificador = this.planificadorCreado();

        servicioPlanificador.eliminar(planificador);

        Mockito.verify(repositorioPlanificador, times(1)).eliminar(planificador);
    }

    @Test
    public void queSePuedaActualizarUnPlanificador(){
        Planificador planificador = this.planificadorCreado();

        servicioPlanificador.actualizar(planificador);

        Mockito.verify(repositorioPlanificador, times(1)).actualizar(planificador);
    }



}
