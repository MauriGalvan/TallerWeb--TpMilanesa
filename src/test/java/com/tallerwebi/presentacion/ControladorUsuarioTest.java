package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class ControladorUsuarioTest {

    private ControladorUsuario controladorUsuario;
    private ServicioLogin servicioLoginMock;

    @BeforeEach
    public void setup() {
        servicioLoginMock = mock(ServicioLogin.class);
        controladorUsuario = new ControladorUsuario(servicioLoginMock);
    }

    @Test
    public void queRedirijaALoginSiUsuarioNoEstaEnSesionEnVerPerfil() {
        // Given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioActual")).thenReturn(null);

        // When
        ModelAndView modelAndView = controladorUsuario.verPerfil(requestMock);

        // Then
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queMuestrePerfilSiUsuarioEstaEnSesion() {
        // Given
        Usuario usuarioMock = mock(Usuario.class);
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // When
        ModelAndView modelAndView = controladorUsuario.verPerfil(requestMock);

        // Then
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("perfil"));
        assertThat(modelAndView.getModel().get("usuario"), equalTo(usuarioMock));
    }

    @Test
    public void queRedirijaALoginSiUsuarioNoEstaEnSesionEnMostrarPagoPremium() {
        // Given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioActual")).thenReturn(null);

        // When
        ModelAndView modelAndView = controladorUsuario.mostrarPagoPremium(requestMock);

        // Then
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    @Test
    public void queMuestreVistaPagoPremiumSiUsuarioEstaEnSesion() {
        // Given
        Usuario usuarioMock = mock(Usuario.class);
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // When
        ModelAndView modelAndView = controladorUsuario.mostrarPagoPremium(requestMock);

        // Then
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("pago-premium"));
        assertThat(modelAndView.getModel().get("usuario"), equalTo(usuarioMock));
    }

    @Test
    public void queActualiceUsuarioAPremiumYRetorneVistaConMensajeExitoso() {
        // Given
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(Rol.USUARIO);
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // When
        ModelAndView modelAndView = controladorUsuario.procesarPagoPremium(
                "VISA", "1234567812345678", "12/25", "123", requestMock);

        // Then
        verify(servicioLoginMock).actualizarUsuario(usuarioMock);
        assertThat(usuarioMock.getRol(), equalTo(Rol.USUARIO_PREMIUM));
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("pago-premium"));
        assertThat((String) modelAndView.getModel().get("mensaje"), equalTo("¡Pago realizado exitosamente! Ya eres un usuario Premium."));
    }


    @Test
    public void queNoCambieRolYRetorneErrorSiPagoFallaPorDatosIncorrectos() {
        // Given
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(Rol.USUARIO);
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // When
        ModelAndView modelAndView = controladorUsuario.procesarPagoPremium(
                "VISA", "123", "12/25", "12", requestMock);

        // Then
        assertThat(usuarioMock.getRol(), equalTo(Rol.USUARIO));
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("pago-premium"));
        assertThat((String) modelAndView.getModel().get("mensaje"), equalTo("Error en el pago. Verifica tus datos e intenta nuevamente."));
    }

    @Test
    public void queNoCambieRolSiCvvEsInvalido() {
        // Given
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(Rol.USUARIO);
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // When
        ModelAndView modelAndView = controladorUsuario.procesarPagoPremium(
                "VISA", "1234567812345678", "12/25", "12A", requestMock);

        // Then
        assertThat(usuarioMock.getRol(), equalTo(Rol.USUARIO));
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("pago-premium"));
        assertThat((String) modelAndView.getModel().get("mensaje"), equalTo("Error en el pago. Verifica tus datos e intenta nuevamente."));
    }

    @Test
    public void queNoCambieRolSiFechaDeVencimientoEsInvalida() {
        // Given
        Usuario usuarioMock = new Usuario();
        usuarioMock.setRol(Rol.USUARIO);
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // When
        ModelAndView modelAndView = controladorUsuario.procesarPagoPremium(
                "VISA", "1234567812345678", "invalid-date", "123", requestMock);

        // Then
        assertThat(usuarioMock.getRol(), equalTo(Rol.USUARIO));
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("pago-premium"));
        assertThat((String) modelAndView.getModel().get("mensaje"), equalTo("Error en el pago. Verifica tus datos e intenta nuevamente."));
    }


}
