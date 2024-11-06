package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private List<Ingrediente> unosIngredientes(){
        return Arrays.asList(
                new Ingrediente("Carne", 1, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Papas", 10, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA),
                new Ingrediente("Pan rallado", 200, Unidad_De_Medida.GRAMOS, Tipo_Ingrediente.CEREAL_O_GRANO)
        );
    }
    private Receta recetaMilanesaNapolitanaDeTreintaMinCreada(){
        return new Receta ("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", this.unosIngredientes(), "Esto es una descripción de mila napo", ".");
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
        receta.setTitulo("");

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

        when(servicioRecetaMock.getUnaRecetaPorId(1)).thenReturn(recetaMock);

        //CUANDO
        ModelAndView modelAndView = controlador.eliminarReceta(recetaMock.getId());

        //ENTONCES
        verify(servicioRecetaMock, times(1)).getUnaRecetaPorId(recetaMock.getId());
        verify(servicioRecetaMock, times(1)).eliminarReceta(recetaMock);
        assertThat(modelAndView.getViewName(), equalTo("redirect:/vista-receta"));
    }

}
