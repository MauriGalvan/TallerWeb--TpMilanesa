package com.tallerwebi.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorListaDeCompraTest {

    private ControladorListaDeCompra controladorListaDeCompra;

    @BeforeEach
    public void  setUp(){
        this.controladorListaDeCompra = new ControladorListaDeCompra();
    }
    @Test
    public void debeRetornarLaPaginaListaDeComprasCuandoSeSeleccionaListaDeComprasEnElMenu(){
        //dado
        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        //cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(requestMock);
        //entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaListaDeCompras"));
    }

    @Test
    public void debeMostrarLosDiasDeLaSemana(){
        //dado
        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        //cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(requestMock);
        //entonces
        List<String> diasDeLaSemana = (List<String>) modelAndView.getModel().get("dias");
        assertThat(diasDeLaSemana, hasItems("Lunes", "Martes","Miércoles"));
    }

    @Test
    public void debeMostrarLosNombresDeLaRecetas(){
        //dado
        List<String> recetas = new ArrayList<>();
        recetas.add("Torta de manzana");
        recetas.add("Ensalada");
        recetas.add("Yogurt");
        recetas.add("Pizza");

        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        //cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(requestMock);
        //entonces
        List<String> recetasPara= (List<String>) modelAndView.getModel().get("recetas");
        assertThat(recetasPara, hasItems("Torta de manzana", "Ensalada","Yogurt","Pizza"));
    }

    @Test
    public void seDebeMostrarLosIngredientesParaCadaReceta(){
        //dado
        List<String> ingredientes = new ArrayList<>();
        ingredientes.add("Harina");
        ingredientes.add("lechuga");
        ingredientes.add("sal");
        ingredientes.add("Tomate");
        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        //cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(requestMock);
        //entonces
        List<String> ingredientesTodos= (List<String>) modelAndView.getModel().get("ingredientes");
        assertThat(ingredientesTodos, hasItems("Harina", "lechuga","sal","Tomate"));
    }

}
