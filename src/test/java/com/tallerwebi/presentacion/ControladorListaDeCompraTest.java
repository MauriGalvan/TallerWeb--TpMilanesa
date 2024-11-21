package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ControladorListaDeCompraTest {

    private ControladorListaDeCompra controladorListaDeCompra;
    private ServicioPlanificador servicioPlanificador;

    @BeforeEach
    public void setUp() {
        servicioPlanificador = mock(ServicioPlanificador.class);
        controladorListaDeCompra = new ControladorListaDeCompra(servicioPlanificador);
    }

    @Test
    public void debeMostrarAccesoDenegadoSiNoEsUsuarioPremium() {
        // Dado
        String rolNoPremium = "USUARIO_REGULAR";
        when(servicioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(Arrays.asList());

        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolNoPremium);

        // Entonces
        Boolean accesoDenegado = (Boolean) modelAndView.getModel().get("accesoDenegado");
        assertThat(accesoDenegado, equalTo(true));
        assertThat(modelAndView.getViewName(), equalTo("vistaPlanificador"));
    }

    @Test
    public void debeMostrarListaDeComprasSiEsUsuarioPremium() {
        // Dado
        String rolPremium = "USUARIO_PREMIUM";
        List<String> dias = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        Receta receta = new Receta("Torta", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA, null, null, "", "");
        List<DetallePlanificador> detallesPlanificador = Arrays.asList(
                new DetallePlanificador(Dia.LUNES, Categoria.ALMUERZO_CENA, receta, "Cena")
        );
        when(servicioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(detallesPlanificador);

        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolPremium);

        // Entonces
        List<DetallePlanificador> detalles = (List<DetallePlanificador>) modelAndView.getModel().get("detalles");
        assertThat(detalles, hasSize(1));
        assertThat(modelAndView.getViewName(), equalTo("vistaListaDeCompras"));
    }

    @Test
    public void debeMostrarLosDiasDeLaSemanaParaUsuarioPremium() {
        // Dado
        String rolPremium = "USUARIO_PREMIUM";
        List<String> dias = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        Receta receta = new Receta("Torta", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA, null, null, "", "");
        List<DetallePlanificador> detallesPlanificador = Arrays.asList(
                new DetallePlanificador(Dia.LUNES, Categoria.ALMUERZO_CENA, receta, "Cena"),
                new DetallePlanificador(Dia.MARTES, Categoria.DESAYUNO_MERIENDA, receta, "Desayuno")
        );
        when(servicioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(detallesPlanificador);

        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolPremium);

        // Entonces
        List<String> diasSemana = (List<String>) modelAndView.getModel().get("dias");
        assertThat(diasSemana, equalTo(dias));
        List<DetallePlanificador> detalles = (List<DetallePlanificador>) modelAndView.getModel().get("detalles");
        assertThat(detalles, containsInAnyOrder(
                hasProperty("dia", equalTo(Dia.LUNES)),
                hasProperty("dia", equalTo(Dia.MARTES))
        ));
    }

    @Test
    public void debeMostrarLosIngredientesParaCadaRecetaSiEsUsuarioPremium() {
        // Dado
        String rolPremium = "USUARIO_PREMIUM";
        Ingrediente zanahoria = new Ingrediente("Zanahoria", 3.0, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA);
        Ingrediente pollo = new Ingrediente("Pollo", 1.0, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL);
        Receta receta1 = new Receta("Ensalada", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA, null,
                Arrays.asList(zanahoria, pollo), "", "");

        List<DetallePlanificador> detallesPlanificador = Arrays.asList(
                new DetallePlanificador(Dia.LUNES, Categoria.DESAYUNO_MERIENDA, receta1, "Desayuno")
        );
        when(servicioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(detallesPlanificador);

        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolPremium);

        // Entonces
        List<DetallePlanificador> detalles = (List<DetallePlanificador>) modelAndView.getModel().get("detalles");
        assertThat(detalles, hasItems(
                hasProperty("receta", hasProperty("ingredientes", containsInAnyOrder(
                        allOf(
                                hasProperty("nombre", equalTo("Zanahoria")),
                                hasProperty("cantidad", equalTo(3.0)),
                                hasProperty("unidad_de_medida", equalTo(Unidad_De_Medida.UNIDAD)),
                                hasProperty("tipo", equalTo(Tipo_Ingrediente.VERDURA))
                        ),
                        allOf(
                                hasProperty("nombre", equalTo("Pollo")),
                                hasProperty("cantidad", equalTo(1.0)),
                                hasProperty("unidad_de_medida", equalTo(Unidad_De_Medida.KILOGRAMOS)),
                                hasProperty("tipo", equalTo(Tipo_Ingrediente.PROTEINA_ANIMAL))
                        )
                )))
        ));
    }
}
