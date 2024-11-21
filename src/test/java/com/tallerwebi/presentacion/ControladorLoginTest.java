package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Rol;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

	private ControladorLogin controladorLogin;
	private Usuario usuarioMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;


	@BeforeEach
	public void init(){
		datosLoginMock = new DatosLogin("dami@unlam.com", "123");
		usuarioMock = mock(Usuario.class);
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		servicioLoginMock = mock(ServicioLogin.class);
		controladorLogin = new ControladorLogin(servicioLoginMock);
	}

	@Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente(){
		// preparacion
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
		verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
	}

	@Test
	public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente {

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
		verify(servicioLoginMock, times(1)).registrar(usuarioMock);
	}

	@Test
	public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
		// preparacion
		doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(usuarioMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Mail ya registrado, intente ingresando uno nuevo."));
	}

	@Test
	public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
		// preparacion
		doThrow(RuntimeException.class).when(servicioLoginMock).registrar(usuarioMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario"));
	}

	@Test
	public void loginConUsuarioYPasswordCorrectosDeberiaRedirigirAVistaReceta() {
		// Preparación
		when(servicioLoginMock.consultarUsuario(datosLoginMock.getEmail(), datosLoginMock.getPassword())).thenReturn(usuarioMock);
		when(usuarioMock.getRol()).thenReturn(Rol.USUARIO);
		when(usuarioMock.getUsername()).thenReturn("dami");

		// Simular sesión
		when(requestMock.getSession()).thenReturn(sessionMock);

		// Ejecución
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// Validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/vista-receta"));
		verify(sessionMock).setAttribute("ROL", Rol.USUARIO);
		verify(sessionMock).setAttribute("usuarioNombre", "dami");
	}

	@Test
	public void nuevoUsuarioDeberiaCargarVistaConFormulario() {
		// Ejecución
		ModelAndView modelAndView = controladorLogin.nuevoUsuario();

		// Validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("usuario"), is(instanceOf(Usuario.class)));
		assertThat(modelAndView.getModel().get("roles"), is(Rol.values()));
	}

	@Test
	public void logoutDeberiaInvalidarSesionYRedirigirALogin() {
		// Preparación
		when(requestMock.getSession()).thenReturn(sessionMock);

		// Ejecución
		String result = controladorLogin.logout(requestMock);

		// Validación
		verify(sessionMock, times(1)).invalidate();
		assertThat(result, equalToIgnoringCase("redirect:/login"));
	}

	@Test
	public void registrarUsuarioExistenteDeberiaMostrarMensajeDeError() throws UsuarioExistente {
		// Preparación
		doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(usuarioMock);

		// Ejecución
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);

		// Validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Mail ya registrado, intente ingresando uno nuevo."));
	}

	@Test
	public void noDeberiaPermitirRegistrarUsuarioConEmailExistente() throws UsuarioExistente {
		// Preparación
		doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(usuarioMock);

		// Ejecución
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);

		// Validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Mail ya registrado, intente ingresando uno nuevo."));
		assertThat(modelAndView.getModel().get("usuario"), is(usuarioMock));
		assertThat(modelAndView.getModel().get("roles"), is(Rol.values()));

		// Verificación
		verify(servicioLoginMock, times(1)).registrar(usuarioMock);
	}

	@Test
	public void loginConEmailIncorrectoDeberiaMostrarError() {
		// Preparación
		DatosLogin datosLogin = new DatosLogin("noexiste@correo.com", "123");
		when(servicioLoginMock.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword())).thenReturn(null);

		// Ejecución
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLogin, requestMock);

		// Validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
	}

	@Test
	public void loginConPasswordIncorrectoDeberiaMostrarError() {
		// Preparación
		DatosLogin datosLogin = new DatosLogin("dami@unlam.com", "incorrecta");
		when(servicioLoginMock.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword())).thenReturn(null);

		// Ejecución
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLogin, requestMock);

		// Validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
	}

	@Test
	public void logoutDeberiaInvalidarSesionCorrectamente() {
		// Preparación
		when(requestMock.getSession()).thenReturn(sessionMock);

		// Ejecución
		String resultado = controladorLogin.logout(requestMock);

		// Validación
		verify(sessionMock, times(1)).invalidate();
		assertThat(resultado, equalToIgnoringCase("redirect:/login"));
	}




}
