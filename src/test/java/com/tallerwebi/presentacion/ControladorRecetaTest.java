package com.tallerwebi.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControladorRecetaTest {

    private ControladorReceta controladorReceta;
    private ControladorDetalleReceta controladorDetalleReceta;

    @BeforeEach
    public void setup() {
        controladorReceta = new ControladorReceta();
        controladorDetalleReceta = new ControladorDetalleReceta();
    }

    @Test
    public void QueRetorneLaVistaRecetaCuandoSeEjecutaElMetodoIrARecetas(){
        //Dado
        ControladorReceta controladorReceta = new ControladorReceta();
        //Cuando
        ModelAndView modelAndView = controladorReceta.irARecetas(null);
        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
    }

    @Test
    public void QueRetorneLaVistaDetalleRecetaCuandoSeEjecutaElMetodoMostrarDetalleReceta(){
        //Dado
        Receta receta = new Receta();
        //Cuando
        ModelAndView modelAndView = controladorDetalleReceta.mostrarDetalleReceta(receta);
        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("detalleReceta"));
    }

    @Test
    public void QueRetorneTodasLasRecetasCuandoNoHayNingunFiltroSeleccionadoEnCategorias() {
        //Dado
        ModelAndView modelAndView = controladorReceta.irARecetas(null);

        //Cuando

        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("recetas");

        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(3));  // Verificamos que hay 3 recetas
        assertThat(recetas.get(0).getTitulo(), equalTo("Milanesa napolitana"));
        assertThat(recetas.get(1).getTitulo(), equalTo("Tarta jamón y queso"));
        assertThat(recetas.get(2).getTitulo(), equalTo("Café cortado con tostadas"));
    }


    @Test
    public void QueRetorneLasRecetasDeAlmuerzoCuandoElFiltroDeCategoriaEsteSeleccionadoEnAlmmuerzo() {
        //Dado
        ModelAndView modelAndView = controladorReceta.irARecetas("almuerzo");

        //Cuando
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("recetas");

        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(2));
        assertThat(recetas.get(0).getTitulo(), equalTo("Milanesa napolitana"));
        assertThat(recetas.get(1).getTitulo(), equalTo("Tarta jamón y queso"));
        assertThat(recetas.get(0).getCategoria(), equalTo("almuerzo"));
        assertThat(recetas.get(1).getCategoria(), equalTo("almuerzo"));
    }
}