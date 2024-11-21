package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorPlanificadorTest {

    private ControladorPlanificador controladorPlanificador;

    private ServicioReceta servicioRecetaMock;
    private ServicioPlanificador servicioPlanificadorMock;
    private ControladorReceta controladorReceta;

    @BeforeEach
    public void setup() {
        servicioRecetaMock = mock(ServicioReceta.class);
        servicioPlanificadorMock = mock(ServicioPlanificador.class);
        controladorPlanificador = new ControladorPlanificador(servicioRecetaMock, servicioPlanificadorMock); // Uncomment this line
        this.controladorReceta = new ControladorReceta(servicioRecetaMock);
    }

    private List<Ingrediente> unosIngredientes(){
        return Arrays.asList(
                new Ingrediente("Carne", 1, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Papas", 10, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA),
                new Ingrediente("Pan rallado", 200, Unidad_De_Medida.GRAMOS, Tipo_Ingrediente.CEREAL_O_GRANO)
        );
    }
    private Planificador planificadorCreado(){
        byte[] imagen = new byte[]{0, 1};
        Receta receta1 = new Receta ("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                imagen, this.unosIngredientes(), "Un clásico de las mañanas.", ".");
        DetallePlanificador detalle1 = new DetallePlanificador(Dia.MARTES, Categoria.DESAYUNO_MERIENDA, receta1, "Desayuno");
        Receta receta2 = new Receta("Tarta de jamón y queso", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                imagen, this.unosIngredientes(), "Deliciosa tarta de jamón y queso.", ".");
        DetallePlanificador detalle2 = new DetallePlanificador(Dia.DOMINGO, Categoria.ALMUERZO_CENA, receta2, "Cena");
        Planificador planificador = new Planificador();
        planificador.agregarDetalle(detalle1);
        planificador.agregarDetalle(detalle2);
        return planificador;
    }

    @Test
    public void queRetorneLaVistaPlanificadorCuandoSeEjecutaElMetodoMostrarIrAPlanificador() {
        // Configurar los mocks
        Planificador planificador = new Planificador();
        when(servicioPlanificadorMock.obtenerPlanificador()).thenReturn(planificador);

        // Crear el objeto MockHttpServletRequest y agregar el atributo de sesión "ROL"
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("ROL", "USUARIO_PREMIUM");

        // Cuando
        ModelAndView modelAndView = controladorPlanificador.irAPlanificador(request.getSession().getAttribute("ROL").toString());

        // Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaPlanificador"));
        assertThat(modelAndView.getModel().get("accesoDenegado"), equalTo(false)); // Verificar que accesoDenegado esté en false
    }

    @Test
    public void queRetorneUnaListaDeRecetasCategoriaDesayunoCuandoSePresionaElIconoMasEnDesayunoOMerienda() {
        // Dado
        List<Receta> recetasMock = new ArrayList<>();
        Receta desayuno = new Receta();
        desayuno.setTitulo("Budin de chocolate");
        desayuno.setCategoria(Categoria.DESAYUNO_MERIENDA);
        recetasMock.add(desayuno);

        when(servicioRecetaMock.getRecetasPorCategoria(Categoria.DESAYUNO_MERIENDA)).thenReturn(recetasMock);

        // Simular HttpServletRequest
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getSession().setAttribute("ROL", null);  // Simular que el usuario no tiene rol

        // Cuando
        ModelAndView modelAndView = controladorReceta.irARecetas("DESAYUNO_MERIENDA", null, null, request);

        // Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");

        assertThat(recetas, hasSize(1));
        assertThat(recetas.get(0).getTitulo(), equalTo("Budin de chocolate"));
    }

    @Test
    public void queSeActualiceElPlanificadorCuandoQueSeGuardaUnaReceta(){
        Planificador planificador = this.planificadorCreado();
        byte[] imagen = new byte[]{0, 1};
        Receta receta = new Receta ("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                imagen, this.unosIngredientes(), "Milanesa con guarnición de papas fritas", ".");
        Dia dia = Dia.SABADO;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        String categoriaDelPlanificador = "Cena";
        DetallePlanificador detalle = new DetallePlanificador(dia, categoria, receta, categoriaDelPlanificador);

        when(servicioRecetaMock.getUnaRecetaPorId(receta.getId())).thenReturn(receta);
        when(servicioPlanificadorMock.obtenerPlanificador()).thenReturn(planificador);
        ModelAndView modelAndView = controladorPlanificador.guardarPlanificador(dia.toString(), String.valueOf(receta.getId()), categoria.toString());

        //no se guarda el mismo por el hash code
        verify(servicioPlanificadorMock, times(1)).agregarDetalle(any(Planificador.class), any(DetallePlanificador.class));
        verify(servicioPlanificadorMock, times(1)).actualizar(planificador);
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:vista-planificador"));
    }

    @Test
    public void queRetorneUnaListaDeRecetasParaCategoriaDesayunoYMerienda() {
        // Dado
        List<Receta> recetasMock = new ArrayList<>();
        byte[] imagen = new byte[]{0, 1};
        Receta receta1 = new Receta();
        receta1.setTitulo("Budín de chocolate");
        receta1.setCategoria(Categoria.DESAYUNO_MERIENDA);
        receta1.setImagen(imagen);

        Receta receta2 = new Receta();
        receta2.setTitulo("Tostadas con mermelada");
        receta2.setCategoria(Categoria.DESAYUNO_MERIENDA);
        receta2.setImagen(imagen);

        recetasMock.add(receta1);
        recetasMock.add(receta2);

        when(servicioRecetaMock.getRecetasPorCategoria(Categoria.DESAYUNO_MERIENDA)).thenReturn(recetasMock);

        ModelAndView modelAndView = controladorPlanificador.obtenerRecetasPorCategoria("DESAYUNO", "Lunes");

        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("recetas");
        assertThat(recetas, hasSize(2));

        Categoria categoriaSeleccionada = (Categoria) modelAndView.getModel().get("categoriaSeleccionada");
        assertThat(categoriaSeleccionada, equalTo(Categoria.DESAYUNO_MERIENDA));

        String dia = (String) modelAndView.getModel().get("dia");
        assertThat(dia, equalTo("Lunes"));
    }

    @Test
    public void debeRetornarLaVistaListaDeComprasCuandoSeEjecuteSuMetodo(){
        ModelAndView modelAndView = controladorPlanificador.irAListaCompra();

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaLista"));
    }

    @Test
    public void queSeGuardeElPlanificadorConLosDetallesCorrectos(){
        String diasStr = "MARTES,DOMINGO";
        String recetasStr = "1,2";
        String categoriasStr = "Desayuno,Cena";

        List<String> diasEsperados = Arrays.asList("MARTES", "DOMINGO");
        List<String> categoriasEsperadas = Arrays.asList("Desayuno", "Cena");

        Planificador planificador = this.planificadorCreado();
        List<DetallePlanificador> detalles = planificador.obtenerDetalles();
        DetallePlanificador detalle1 = detalles.get(0);
        DetallePlanificador detalle2 = detalles.get(1);
        Receta receta1 = detalle1.getReceta();
        Receta receta2 = detalle2.getReceta();

        when(servicioPlanificadorMock.obtenerPlanificador()).thenReturn(planificador);
        when(servicioRecetaMock.getUnaRecetaPorId(1)).thenReturn(receta1);
        when(servicioRecetaMock.getUnaRecetaPorId(2)).thenReturn(receta2);

        ModelAndView modelAndView = controladorPlanificador.guardarPlanificador(diasStr, recetasStr, categoriasStr);

        verify(servicioPlanificadorMock, times(1)).agregarDetalle(planificador, detalle1);
        verify(servicioPlanificadorMock, times(1)).agregarDetalle(planificador, detalle2);

        assertThat(detalle1.getDia(), equalTo(Dia.MARTES));
        assertThat(detalle1.getReceta(), equalTo(receta1));
        assertThat(detalle1.getCategoriaDelPlanificador(), equalTo("Desayuno"));

        assertThat(detalle2.getDia(), equalTo(Dia.DOMINGO));
        assertThat(detalle2.getReceta(), equalTo(receta2));
        assertThat(detalle2.getCategoriaDelPlanificador(), equalTo("Cena"));

        verify(servicioPlanificadorMock).actualizar(planificador);

        assertThat(modelAndView.getViewName(), equalTo("redirect:vista-planificador"));
    }

}
