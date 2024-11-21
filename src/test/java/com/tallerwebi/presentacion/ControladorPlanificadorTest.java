package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorPlanificadorTest {

    private ControladorPlanificador controladorPlanificador;

    private ServicioReceta servicioRecetaMock;
    private ControladorReceta controladorReceta;

    @BeforeEach
    public void setup() {
        servicioRecetaMock = mock(ServicioReceta.class);
        controladorPlanificador = new ControladorPlanificador(servicioRecetaMock); 
        this.controladorReceta = new ControladorReceta(servicioRecetaMock);
    }

    @Test
    public void QueRetorneLaVistaPlanificadorCuandoSeEjecutaElMetodoMostrarIrAPlanificador(){

        // Crear un HttpServletRequest simulado
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        //Cuando
        ModelAndView modelAndView = controladorPlanificador.irAPlanificador(requestMock);
        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaPlanificador"));
    }


    @Test
    public void QueRetorneUnaListaDeRecetasCategoriaDesayunoCuandoSePresionaElIconoMasEnDesayunoOMerienda() {
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


}
