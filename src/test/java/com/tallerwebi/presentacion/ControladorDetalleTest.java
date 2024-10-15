package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ControladorDetalleTest {

    private ControladorDetalleReceta controlador;
    private ServicioReceta servicioRecetaMock;

    @BeforeEach
    public void setup(){
        servicioRecetaMock = mock(ServicioReceta.class);
        controlador = new ControladorDetalleReceta(servicioRecetaMock);
    }

    private Receta recetaMilanesaNapolitanaDeTreintaMinCreada(){
        byte[] imagen = new byte[]{0, 1};
        return new Receta ("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                imagen, ".", "Esto es una descripción de mila napo", ".");
    }

    @Test
    public void DebeRetornarVistaConTituloImagenIngredientesYPasosCuandoSeMuestraDetalleReceta(){
        //DADO
        int id = 1;
        Receta recetaMock = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        recetaMock.setId(id);

        //CUANDO
        when(servicioRecetaMock.getUnaRecetaPorId(id)).thenReturn(recetaMock);
        ModelAndView modelAndView = controlador.mostrarDetalleReceta(recetaMock.getId());
        //ENTONCES
        Receta recetaDelModelo = (Receta) modelAndView.getModel().get("unaReceta");
        assertThat(recetaDelModelo.getTitulo(), equalTo(recetaMock.getTitulo()));
        assertThat(recetaDelModelo.getImagen(), equalTo(recetaMock.getImagen()));
        assertThat(recetaDelModelo.getPasos(), equalTo(recetaMock.getPasos()));
    }

    @Test
    public void DebeModificarRecetaYRetornarVistaConMensajeDeExito() {
        //DADO
        Receta recetaMock = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        recetaMock.setId(1);

        //CUANDO
        ModelAndView modelAndView = controlador.modificarReceta(recetaMock);

        //ENTONCES
        verify(servicioRecetaMock, times(1)).actualizarReceta(recetaMock);
        assertThat(modelAndView.getViewName(), equalTo("detalleReceta"));
        assertThat(modelAndView.getModel().get("unaReceta"), equalTo(recetaMock));
        assertThat(modelAndView.getModel().get("mensajeExito"), equalTo("La receta fue modificada correctamente."));
    }

    @Test
    public void QueAparezaUnMensajeDeErrorYNoSePuedaActualizarEnLaBaseDeDatosSiSeModificaElTituloYLoDejaVacio(){
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        receta.setTitulo(""); //titulo vacío

        ModelAndView modelAndView = controlador.modificarReceta(receta);

        verify(servicioRecetaMock, times(0)).actualizarReceta(receta);
        assertThat(modelAndView.getViewName(), equalTo("detalleReceta"));
        assertThat(modelAndView.getModel().get("unaReceta"), equalTo(receta));
        assertThat(modelAndView.getModel().get("mensajeError"), equalTo("La receta no fue modificada, verifique que los campos no estén vacíos."));
    }

    @Test
    public void DebeEliminarRecetaYRedirigirAVistaCorrecta() {
        //DADO
        Receta recetaMock = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        recetaMock.setId(1);

        //CUANDO
        ModelAndView modelAndView = controlador.eliminarReceta(recetaMock);

        //ENTONCES
        verify(servicioRecetaMock, times(1)).eliminarReceta(recetaMock);
        assertThat(modelAndView.getViewName(), equalTo("redirect:/vista-receta"));
    }

}
