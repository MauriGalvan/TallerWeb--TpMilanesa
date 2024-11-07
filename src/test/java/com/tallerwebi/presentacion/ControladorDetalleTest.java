package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        ModelAndView modelAndView = controlador.mostrarDetalleReceta(Integer.valueOf(recetaMock.getId()));
        //ENTONCES
        Receta recetaDelModelo = (Receta) modelAndView.getModel().get("unaReceta");
        assertThat(recetaDelModelo.getTitulo(), equalTo(titulo));
        assertThat(recetaDelModelo.getImagen(), equalTo(imagen));
        assertThat(recetaDelModelo.getPasos(), equalTo(pasos));
    }

    @Test
    public void DebeModificarRecetaYRetornarVistaConMensajeDeExito() {
        // DADO
        Receta recetaMock = new Receta("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", "Jamón, Queso", "Descripción", "Pasos");
        recetaMock.setId(1);

        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(Rol.USUARIO_PREMIUM);

        // Mock de HttpServletRequest y HttpSession
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // CUANDO
        ModelAndView modelAndView = controlador.modificarReceta(recetaMock, request);

        // ENTONCES
        verify(servicioRecetaMock, times(1)).actualizarReceta(recetaMock);
        assertThat(modelAndView.getViewName(), equalTo("detalleReceta"));
        assertThat(modelAndView.getModel().get("unaReceta"), equalTo(recetaMock));
        assertThat(modelAndView.getModel().get("mensajeExito"), equalTo("La receta fue modificada correctamente."));
    }



    @Test
    public void QueAparezaUnMensajeDeErrorYNoSePuedaActualizarEnLaBaseDeDatosSiSeModificaElTituloYLoDejaVacio() {
        // Configuración de la receta con título vacío
        Receta receta = new Receta("", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", "Jamón, Queso", "Descripción", "Pasos");

        // Mock del usuario con rol USUARIO_PREMIUM
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(Rol.USUARIO_PREMIUM);

        // Mock del request para obtener el usuario de la sesión
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // Ejecución del método modificarReceta del controlador
        ModelAndView modelAndView = controlador.modificarReceta(receta, request);

        // Verificación de que el servicio no intente actualizar la receta con título vacío
        verify(servicioRecetaMock, times(0)).actualizarReceta(receta);

        // Verificaciones del ModelAndView devuelto
        assertThat(modelAndView.getViewName(), equalTo("detalleReceta"));
        assertThat(modelAndView.getModel().get("unaReceta"), equalTo(receta));
        assertThat(modelAndView.getModel().get("mensajeError"), equalTo("La receta no fue modificada, verifique que los campos no estén vacíos."));
    }


}
