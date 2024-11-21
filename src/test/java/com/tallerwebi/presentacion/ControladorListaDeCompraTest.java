package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPlanificador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ControladorListaDeCompraTest {

    private ControladorListaDeCompra controladorListaDeCompra;
    private ServicioPlanificador servicioPlanificador;

    @BeforeEach
    public void  setUp(){
        servicioPlanificador = mock(ServicioPlanificador.class);
        this.controladorListaDeCompra = new ControladorListaDeCompra(servicioPlanificador);
    }
    @Test
    public void debeRetornarLaPaginaListaDeComprasCuandoSeSeleccionaListaDeComprasEnElMenu(){
        //dado
        //cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras();
        //entonces
        verify(servicioPlanificador, times(1)).obtenerDetallesDelPlanificador();
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaListaDeCompras"));
    }

    @Test
    public void debeMostrarLosDiasDeLaSemana(){
        //dado

        //cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras();
        //entonces
        List<String> diasDeLaSemana = (List<String>) modelAndView.getModel().get("dias");
        assertThat(diasDeLaSemana, hasItem("Lunes"));
        assertThat(diasDeLaSemana, hasItem("Martes"));
        assertThat(diasDeLaSemana, hasItem("Miercoles"));
        assertThat(diasDeLaSemana, hasItem("Jueves"));
        assertThat(diasDeLaSemana, hasItem("Viernes"));
        assertThat(diasDeLaSemana, hasItem("Sabado"));
        assertThat(diasDeLaSemana, hasItem("Domingo"));
    }
}
