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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorDetalleTest {

    private ControladorDetalleReceta controlador;
    private ServicioReceta servicioRecetaMock;

    @BeforeEach
    public void setup(){
        servicioRecetaMock = mock(ServicioReceta.class);
        controlador = new ControladorDetalleReceta(servicioRecetaMock);
    }

    @Test
    public void DebeRetornarVistaConTituloImagenIngredientesYPasosCuandoSeMuestraDetalleReceta(){
        //DADO
        int id = 1;
        String titulo = "Milanesa napolitana";
        TiempoDePreparacion tiempo_preparacion = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = "Aplasta la carne y condimenta con sal y pimienta. Bate un huevo y mezcla pan rallado con perejil. Pasa cada filete por el huevo y luego por el pan rallado. Fríe en aceite caliente hasta dorar. Acompaña con papas fritas o hervidas y añade salsa de tomate, jamón y queso.";

        Receta recetaMock = new Receta(titulo,tiempo_preparacion,categoria,imagen,ingredientes,descripcion,pasos);
        recetaMock.setId(id);

        //CUANDO
        when(servicioRecetaMock.getUnaRecetaPorId(id)).thenReturn(recetaMock);
        ModelAndView modelAndView = controlador.mostrarDetalleReceta(recetaMock.getId());
        //ENTONCES
        Receta recetaDelModelo = (Receta) modelAndView.getModel().get("unaReceta");
        assertThat(recetaDelModelo.getTitulo(), equalTo(titulo));
        assertThat(recetaDelModelo.getImagen(), equalTo(imagen));
        assertThat(recetaDelModelo.getPasos(), equalTo(pasos));
    }

}
