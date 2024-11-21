package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        byte[] imagen = new byte[]{0, 1};
        return new Receta ("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                imagen, this.unosIngredientes(), "Esto es una descripción de mila napo", ".");
    }

    MockMultipartFile imagenMock = new MockMultipartFile("imagen", new byte[0]);

    @Test
    public void DebeRetornarVistaConTituloImagenIngredientesYPasosCuandoSeMuestraDetalleReceta(){
        //DADO
        int id = 1;
        Receta recetaMock = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        recetaMock.setId(id);

        //CUANDO
        when(servicioRecetaMock.getUnaRecetaPorId(id)).thenReturn(recetaMock);
        ModelAndView modelAndView = controlador.mostrarDetalleReceta(Integer.valueOf(recetaMock.getId()));
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

        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(Rol.PROFESIONAL);

        MultipartFile imagenMock = mock(MultipartFile.class);

        // Mock de HttpServletRequest y HttpSession
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // CUANDO
        ModelAndView modelAndView = controlador.modificarReceta(recetaMock, imagenMock, request);

        // ENTONCES
        verify(servicioRecetaMock, times(1)).actualizarReceta(recetaMock);
        assertThat(modelAndView.getViewName(), equalTo("redirect:/detalleReceta?id=1"));
    }

    @Test
    public void QueAparezaUnMensajeDeErrorYNoSePuedaActualizarEnLaBaseDeDatosSiSeModificaElTituloYLoDejaVacio(){
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        receta.setTitulo("");

        // Mock del usuario con rol USUARIO_PREMIUM
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(Rol.PROFESIONAL);

        MultipartFile imagenMock = mock(MultipartFile.class);

        // Mock del request para obtener el usuario de la sesión
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // Ejecución del método modificarReceta del controlador
        ModelAndView modelAndView = controlador.modificarReceta(receta, imagenMock, request);

        // Verificación de que el servicio no intente actualizar la receta con título vacío
        verify(servicioRecetaMock, times(0)).actualizarReceta(receta);

        // Verificaciones del ModelAndView devuelto
        assertThat(modelAndView.getViewName(), equalTo("detalleReceta"));
        assertThat(modelAndView.getModel().get("unaReceta"), equalTo(receta));
        assertThat(modelAndView.getModel().get("mensajeError"), equalTo("La receta no fue modificada, verifique que los campos no estén vacíos."));
    }

//    @Test
//    public void DebeEliminarRecetaYRedirigirAVistaCorrecta() {
//        //DADO
//        Receta recetaMock = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
//        recetaMock.setId(1);
//
//        HttpServletRequest request = mock(HttpServletRequest.class);
//
//        //CUANDO
//        ModelAndView modelAndView = controlador.eliminarReceta(recetaMock, request);
//
//        //ENTONCES
//        verify(servicioRecetaMock, times(1)).eliminarReceta(recetaMock);
//        assertThat(modelAndView.getViewName(), equalTo("redirect:/vista-receta"));
//    }

}
