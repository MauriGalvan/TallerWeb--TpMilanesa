package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ControladorListaDeCompraTest {

    private ControladorListaDeCompra controladorListaDeCompra;
    private ServicioPlanificador servicioPlanificador;
    private ServicioIngrediente servicioIngrediente;

    @BeforeEach
    public void setUp() {
        servicioPlanificador = mock(ServicioPlanificador.class);
        servicioIngrediente = mock(ServicioIngrediente.class);
        controladorListaDeCompra = new ControladorListaDeCompra(servicioPlanificador, servicioIngrediente);
    }

    @Test
    public void debeMostrarAccesoDenegadoSiNoEsUsuarioPremium() {
        // Dado
        String rolNoPremium = "USUARIO_REGULAR";
        when(servicioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(Arrays.asList());

        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolNoPremium, requestMock);

        // Entonces
        Boolean accesoDenegado = (Boolean) modelAndView.getModel().get("accesoDenegado");

        assertThat(accesoDenegado, equalTo(true));
        assertThat(modelAndView.getViewName(), equalTo("vistaPlanificador"));
    }

    @Test
    public void debeMostrarListaDeComprasSiEsUsuarioPremium() {
        // Dado
        String rolPremium = "USUARIO_PREMIUM";
        List<String> dias = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        Receta receta = new Receta("Torta", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA, null, null, "", "");
        List<DetallePlanificador> detallesPlanificador = Arrays.asList(
                new DetallePlanificador(Dia.LUNES, Categoria.ALMUERZO_CENA, receta, "Cena")
        );
        when(servicioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(detallesPlanificador);
        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolPremium,requestMock);

        // Entonces
        List<DetallePlanificador> detalles = (List<DetallePlanificador>) modelAndView.getModel().get("detalles");
        assertThat(detalles, hasSize(1));
        assertThat(modelAndView.getViewName(), equalTo("vistaListaDeCompras"));
    }

    @Test
    public void debeMostrarLosDiasDeLaSemanaParaUsuarioPremium() {
        // Dado
        String rolPremium = "USUARIO_PREMIUM";
        List<String> dias = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        Receta receta = new Receta("Torta", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA, null, null, "", "");
        List<DetallePlanificador> detallesPlanificador = Arrays.asList(
                new DetallePlanificador(Dia.LUNES, Categoria.ALMUERZO_CENA, receta, "Cena"),
                new DetallePlanificador(Dia.MARTES, Categoria.DESAYUNO_MERIENDA, receta, "Desayuno")
        );
        when(servicioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(detallesPlanificador);
        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolPremium,requestMock);

        // Entonces
        List<String> diasSemana = (List<String>) modelAndView.getModel().get("dias");
        assertThat(diasSemana, equalTo(dias));
        List<DetallePlanificador> detalles = (List<DetallePlanificador>) modelAndView.getModel().get("detalles");
        assertThat(detalles, containsInAnyOrder(
                hasProperty("dia", equalTo(Dia.LUNES)),
                hasProperty("dia", equalTo(Dia.MARTES))
        ));
    }

    @Test
    public void debeMostrarLosIngredientesParaCadaRecetaSiEsUsuarioPremium() {
        // Dado
        String rolPremium = "USUARIO_PREMIUM";
        Ingrediente zanahoria = new Ingrediente("Zanahoria", 3.0, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA);
        Ingrediente pollo = new Ingrediente("Pollo", 1.0, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL);
        Receta receta1 = new Receta("Ensalada", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA, null,
                Arrays.asList(zanahoria, pollo), "", "");

        List<DetallePlanificador> detallesPlanificador = Arrays.asList(
                new DetallePlanificador(Dia.LUNES, Categoria.DESAYUNO_MERIENDA, receta1, "Desayuno")
        );
        when(servicioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(detallesPlanificador);
        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolPremium,requestMock);

        // Entonces
        List<DetallePlanificador> detalles = (List<DetallePlanificador>) modelAndView.getModel().get("detalles");
        assertThat(detalles, hasItems(
                hasProperty("receta", hasProperty("ingredientes", containsInAnyOrder(
                        allOf(
                                hasProperty("nombre", equalTo("Zanahoria")),
                                hasProperty("cantidad", equalTo(3.0)),
                                hasProperty("unidad_de_medida", equalTo(Unidad_De_Medida.UNIDAD)),
                                hasProperty("tipo", equalTo(Tipo_Ingrediente.VERDURA))
                        ),
                        allOf(
                                hasProperty("nombre", equalTo("Pollo")),
                                hasProperty("cantidad", equalTo(1.0)),
                                hasProperty("unidad_de_medida", equalTo(Unidad_De_Medida.KILOGRAMOS)),
                                hasProperty("tipo", equalTo(Tipo_Ingrediente.PROTEINA_ANIMAL))
                        )
                )))
        ));
    }

    @Test
    public void debeDenegarAccesoSiNoTieneRol() {
        // Dado
        String rol = null; // Usuario no tiene rol asignado
        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rol,requestMock);

        // Entonces
        Boolean accesoDenegado = (Boolean) modelAndView.getModel().get("accesoDenegado");
        assertThat(accesoDenegado, equalTo(true));
        assertThat(modelAndView.getViewName(), equalTo("vistaPlanificador"));
    }

    @Test
    public void debeDenegarAccesoSiElRolEsInvalido() {
        // Dado
        String rolInvalido = "ADMIN";
        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolInvalido,requestMock);

        // Entonces
        Boolean accesoDenegado = (Boolean) modelAndView.getModel().get("accesoDenegado");
        assertThat(accesoDenegado, equalTo(true));
        assertThat(modelAndView.getViewName(), equalTo("vistaPlanificador"));
    }

    @Test
    public void debePermitirAccesoAUsuarioPremiumYMostrarListaDeCompras() {
        // Dado
        String rolPremium = "USUARIO_PREMIUM";

        // Detalles del planificador simulados
        Receta receta = new Receta("Pizza", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA, null, null, "", "");
        List<DetallePlanificador> detallesPlanificador = Arrays.asList(
                new DetallePlanificador(Dia.LUNES, Categoria.ALMUERZO_CENA, receta, "Cena")
        );
        List<String> dias = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");

        // Configuración del mock
        when(servicioPlanificador.obtenerDetallesDelPlanificador()).thenReturn(detallesPlanificador);
        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        // Cuando
        ModelAndView modelAndView = controladorListaDeCompra.irAListaDeCompras(rolPremium,requestMock);

        // Entonces
        // Verificar que el acceso no sea denegado
        Boolean accesoDenegado = (Boolean) modelAndView.getModel().get("accesoDenegado");
        assertThat(accesoDenegado, equalTo(false));

        // Verificar que la vista es la correcta
        assertThat(modelAndView.getViewName(), equalTo("vistaListaDeCompras"));

        // Verificar que los días de la semana están presentes en el modelo
        List<String> diasEnModelo = (List<String>) modelAndView.getModel().get("dias");
        assertThat(diasEnModelo, equalTo(dias));

        // Verificar que los detalles del planificador se cargaron correctamente
        List<DetallePlanificador> detallesEnModelo = (List<DetallePlanificador>) modelAndView.getModel().get("detalles");
        assertThat(detallesEnModelo, hasSize(1));
        assertThat(detallesEnModelo.get(0).getDia(), equalTo(Dia.LUNES));

    }

    @Test
    public void debeIrAEncargarProducto() {
        // Configuración de datos de prueba
        String ingredientesStr = "1,2,3";
        Ingrediente ingrediente1 = new Ingrediente("Carne", 1, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL);
        Ingrediente ingrediente2 = new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.PROTEINA_ANIMAL);
        Ingrediente ingrediente3 = new Ingrediente("Papas", 10, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA);

        // Mockear servicio y request
        when(servicioIngrediente.getIngredientePorId(1)).thenReturn(ingrediente1);
        when(servicioIngrediente.getIngredientePorId(2)).thenReturn(ingrediente2);
        when(servicioIngrediente.getIngredientePorId(3)).thenReturn(ingrediente3);

        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        // Simular sesión y usuario
        when(requestMock.getSession()).thenReturn(sessionMock);
        Usuario usuarioMock = new Usuario();
        usuarioMock.setUsername("Invitado");
        when(sessionMock.getAttribute("usuarioActual")).thenReturn(usuarioMock);

        // Ejecutar el método
        ModelAndView modelAndView = controladorListaDeCompra.irAEncargarProducto(
                ingredientesStr,
                "Usuario",
                requestMock
        );

        // Verificaciones
        assertEquals("vistaEncargarProducto", modelAndView.getViewName());

        // Validar el modelo
        ModelMap modelo = modelAndView.getModelMap();
        List<Ingrediente> ingredientesEnModelo = (List<Ingrediente>) modelo.get("ingredientes");
        Usuario usuarioEnModelo = (Usuario) modelo.get("usuario");

        assertNotNull(ingredientesEnModelo);
        assertEquals(3, ingredientesEnModelo.size());
        assertEquals(ingrediente1, ingredientesEnModelo.get(0));
        assertEquals(ingrediente2, ingredientesEnModelo.get(1));
        assertEquals(ingrediente3, ingredientesEnModelo.get(2));

        assertNotNull(usuarioEnModelo);
        assertEquals("Invitado", usuarioEnModelo.getUsername());

        // Verificar que el servicio fue llamado con los IDs correctos
        verify(servicioIngrediente, times(1)).getIngredientePorId(1);
        verify(servicioIngrediente, times(1)).getIngredientePorId(2);
        verify(servicioIngrediente, times(1)).getIngredientePorId(3);
    }


}
