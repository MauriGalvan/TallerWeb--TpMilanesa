package com.tallerwebi.presentacion;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ControladorDetalleTest {

    @Test
    public void DebeRetornarLaVistaDetalleRecetaYSolamenteElNombreDeEsaComidaCuandoSeEjecutaElMetodoMostrarDetalleReceta(){
        //DADO
        ControladorDetalleReceta controladorDetalleRecetaReceta = new ControladorDetalleReceta();
        Receta receta = crearMilanesaNapolitana();
        //CUANDO
        ModelAndView modelAndView = controladorDetalleRecetaReceta.mostrarDetalleReceta(receta);
        //ENTONCES
        assertThat(modelAndView.getModel().get("titulo"), equalTo("Milanesa napolitana"));
    }

    @Test
    public void DebeRetornarLaVistaDetalleRecetaYSolamenteElNombreYLaImagenDeEsaComidaCuandoSeEjecutaElMetodoMostrarDetalleReceta(){
        //DADO
        ControladorDetalleReceta controladorDetalleRecetaReceta = new ControladorDetalleReceta();
        Receta receta = crearMilanesaNapolitana();
        //CUANDO
        ModelAndView modelAndView = controladorDetalleRecetaReceta.mostrarDetalleReceta(receta);
        //ENTONCES
        assertThat(modelAndView.getModel().get("imagen"), equalTo("https://i.postimg.cc/7hbGvN2c/mila-napo.webp"));
    }


    @Test
    public void DebeRetornarLaVistaDetalleRecetaYSolamenteElNombreLaImagenYlosIngredientesDeEsaComidaCuandoSeEjecutaElMetodoMostrarDetalleReceta(){
        //DADO
        ControladorDetalleReceta controladorDetalleRecetaReceta = new ControladorDetalleReceta();
        Receta receta = crearMilanesaNapolitana();

        //CUANDO

        ModelAndView modelAndView = controladorDetalleRecetaReceta.mostrarDetalleReceta(receta);
        //ENTONCES

        assertThat(modelAndView.getModel().get("ingredientes"), equalTo(receta.getIngredientes()));
    }

    private static Receta crearMilanesaNapolitana() {
        ArrayList<String> ingredientes = new ArrayList<>(Arrays.asList
                ("Carne", "Huevo", "Pan rallado", "Perejil", "Papas"));

        return      new Receta("Milanesa napolitana", 1, "almuerzo",
                "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", ingredientes,
                "Esto es una descripción de mila napo.");
    }

}
