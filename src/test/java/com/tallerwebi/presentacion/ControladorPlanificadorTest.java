package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
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

    private Planificador planificadorCreado(){
        Receta receta1 = new Receta ("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg", ".", "Un clásico de las mañanas.", ".");
        DetallePlanificador detalle1 = new DetallePlanificador(Dia.MARTES, Categoria.DESAYUNO_MERIENDA, receta1, "Desayuno");
        Receta receta2 = new Receta("Tarta de jamón y queso", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/tarta.jpg", ".", "Deliciosa tarta de jamón y queso.", ".");
        DetallePlanificador detalle2 = new DetallePlanificador(Dia.DOMINGO, Categoria.ALMUERZO_CENA, receta2, "Cena");
        Planificador planificador = new Planificador();
        planificador.agregarDetalle(detalle1);
        planificador.agregarDetalle(detalle2);
        return planificador;
    }

    @Test
    public void queRetorneLaVistaPlanificadorCuandoSeEjecutaElMetodoMostrarIrAPlanificador(){
        Planificador planificador = new Planificador();
        when(servicioPlanificadorMock.obtenerPlanificador()).thenReturn(planificador);
        //Cuando
        ModelAndView modelAndView = controladorPlanificador.irAPlanificador();
        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaPlanificador"));
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
        Receta receta = new Receta ("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/mila-papas.jpg", ".", "Milanesa con guarnición de papas fritas", ".");
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
}
