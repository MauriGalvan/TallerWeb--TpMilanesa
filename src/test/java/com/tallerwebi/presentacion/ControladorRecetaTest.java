package com.tallerwebi.presentacion;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class ControladorRecetaTest {

    @Test
    public void QueRetorneLaVistaRecetaCuandoSeEjecutaElMetodoIrARecetas(){
        //Dado
        ControladorReceta controladorReceta = new ControladorReceta();
        //Cuando
        ModelAndView modelAndView = controladorReceta.irARecetas();
        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
    }

    @Test
    public void QueRetorneLaVistaDetalleRecetaCuandoSeEjecutaElMetodoMostrarDetalleReceta(){
        //Dado
        ControladorDetalleReceta controladorDetalleRecetaReceta = new ControladorDetalleReceta();
        Receta receta = new Receta();
        //Cuando

        ModelAndView modelAndView = controladorDetalleRecetaReceta.mostrarDetalleReceta(receta);
        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("detalleReceta"));
    }
}
