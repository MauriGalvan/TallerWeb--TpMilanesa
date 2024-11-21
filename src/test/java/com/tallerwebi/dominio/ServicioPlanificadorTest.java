package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ServicioPlanificadorTest {

    @Mock
    private RepositorioPlanificador repositorioPlanificador;
    private ServicioPlanificador servicioPlanificador;

    @BeforeEach
    public void inicializar() {
        this.repositorioPlanificador = mock(RepositorioPlanificador.class);
        this.servicioPlanificador = new ServicioPlanificadorImpl(repositorioPlanificador);
    }

    private List<Ingrediente> unosIngredientes() {
        return Arrays.asList(
                new Ingrediente("Carne", 1, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Papas", 10, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA),
                new Ingrediente("Pan rallado", 200, Unidad_De_Medida.GRAMOS, Tipo_Ingrediente.CEREAL_O_GRANO)
        );
    }

    private Planificador planificadorCreado() {
        byte[] imagen = new byte[]{0, 1};
        Receta receta1 = new Receta("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                imagen, this.unosIngredientes(), "Un clásico de las mañanas.", ".");
        DetallePlanificador detalle1 = new DetallePlanificador(Dia.MARTES, Categoria.DESAYUNO_MERIENDA, receta1, "Desayuno");
        Receta receta2 = new Receta("Tarta de jamón y queso", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                imagen, this.unosIngredientes(), "Deliciosa tarta de jamón y queso.", ".");
        DetallePlanificador detalle2 = new DetallePlanificador(Dia.DOMINGO, Categoria.ALMUERZO_CENA, receta2, "Cena");
        Planificador planificador = new Planificador();
        planificador.agregarDetalle(detalle1);
        planificador.agregarDetalle(detalle2);
        return planificador;
    }

    @Test
    public void queSePuedaGuardarUnPlanificador() {
        Planificador planificador = this.planificadorCreado();

        servicioPlanificador.guardar(planificador);

        Mockito.verify(repositorioPlanificador, times(1)).guardar(planificador);
    }

    @Test
    public void queSePuedaObtenerElPlanificadorSiExisteEnLaBaseDeDatos() {
        Planificador planificador = this.planificadorCreado();

        Mockito.when(repositorioPlanificador.obtenerPlanificador()).thenReturn(planificador);

        Planificador planificadorObtenido = servicioPlanificador.obtenerPlanificador();

        Mockito.verify(repositorioPlanificador, times(1)).obtenerPlanificador();
        assertEquals(planificador, planificadorObtenido);
        Mockito.verify(repositorioPlanificador, never()).guardar(planificador);
    }

    @Test
    public void queCreeYDevuelvaUnNuevoPlanificadorSiNoExiste() {
        when(repositorioPlanificador.obtenerPlanificador()).thenReturn(null);

        Planificador planificadorObtenido = servicioPlanificador.obtenerPlanificador();

        Mockito.verify(repositorioPlanificador).guardar(planificadorObtenido);
    }

    @Test
    public void queDevuelvaElMismoPlanificadorCreadoYGuardado() {
        when(repositorioPlanificador.obtenerPlanificador()).thenReturn(null);

        Planificador planificador1 = servicioPlanificador.obtenerPlanificador();

        when(repositorioPlanificador.obtenerPlanificador()).thenReturn(planificador1);

        Planificador planificador2 = servicioPlanificador.obtenerPlanificador();

        assertEquals(planificador1, planificador2);
        Mockito.verify(repositorioPlanificador, times(1)).guardar(planificador1);
    }

    @Test
    public void queSePuedaEliminarUnPlanificador() {
        Planificador planificador = this.planificadorCreado();

        servicioPlanificador.eliminar(planificador);

        Mockito.verify(repositorioPlanificador, times(1)).eliminar(planificador);
    }

    @Test
    public void queSePuedaActualizarUnPlanificador() {
        Planificador planificador = this.planificadorCreado();

        servicioPlanificador.actualizar(planificador);

        Mockito.verify(repositorioPlanificador, times(1)).actualizar(planificador);
    }

    @Test
    public void queSePuedaObtenerDetallesDelPlanificador() {
        Planificador planificador = this.planificadorCreado();
        List<DetallePlanificador> detalles = planificador.obtenerDetalles();

        Mockito.when(repositorioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(detalles);

        List<DetallePlanificador> detallesObtenidos = servicioPlanificador.obtenerDetallesDelPlanificador();

        Mockito.verify(repositorioPlanificador, times(1)).obtenerDetallesDelPlanificador();
        assertEquals(detalles, detallesObtenidos);
    }

    @Test
    public void queAgregueDetalleSiNoExisteEnElPlanificador() {
        Planificador planificador = new Planificador();
        DetallePlanificador detalleNuevo = new DetallePlanificador(Dia.LUNES, Categoria.DESAYUNO_MERIENDA, new Receta(), "Desayuno");

        servicioPlanificador.agregarDetalle(planificador, detalleNuevo);

        List<DetallePlanificador> detalles = planificador.obtenerDetalles();
        assertThat(detalles, hasSize(1));
        assertThat(detalles.get(0), equalTo(detalleNuevo));
    }

    @Test
    public void queReemplaceDetalleSiYaExisteEnElPlanificador() {
        Planificador planificador = new Planificador();
        DetallePlanificador detalleExistente = new DetallePlanificador(Dia.LUNES, Categoria.DESAYUNO_MERIENDA, new Receta(), "Desayuno");
        DetallePlanificador detalleNuevo = new DetallePlanificador(Dia.LUNES, Categoria.DESAYUNO_MERIENDA, new Receta(), "Desayuno");

        planificador.agregarDetalle(detalleExistente);

        servicioPlanificador.agregarDetalle(planificador, detalleNuevo);

        List<DetallePlanificador> detalles = planificador.obtenerDetalles();
        assertThat(detalles, hasSize(1));
        assertThat(detalles.get(0), equalTo(detalleNuevo));
    }

    @Test
    public void queNoHagaNadaSiElDetalleEsIgual() {
        Planificador planificador = new Planificador();
        DetallePlanificador detalleExistente = new DetallePlanificador(Dia.LUNES, Categoria.DESAYUNO_MERIENDA, new Receta(), "Desayuno");

        planificador.agregarDetalle(detalleExistente);

        servicioPlanificador.agregarDetalle(planificador, detalleExistente);

        List<DetallePlanificador> detalles = planificador.obtenerDetalles();
        assertThat(detalles, hasSize(1));
        assertThat(detalles.get(0), equalTo(detalleExistente));
    }
}
