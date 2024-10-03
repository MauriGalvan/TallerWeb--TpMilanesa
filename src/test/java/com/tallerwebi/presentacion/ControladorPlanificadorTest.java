package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

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
        controladorPlanificador = new ControladorPlanificador(servicioRecetaMock); // Uncomment this line
        this.controladorReceta = new ControladorReceta(servicioRecetaMock);
    }

    @Test
    public void QueRetorneLaVistaPlanificadorCuandoSeEjecutaElMetodoMostrarIrAPlanificador(){
        //Cuando
        ModelAndView modelAndView = controladorPlanificador.irAPlanificador();
        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaPlanificador"));
    }

    @Test
    public void QueRetorneUnaListaDeRecetasCategoriaDesayunoCuandoSePresionaElIconoMasEnLaVistaModal() {
        // Dado
        List<Receta> recetasMock = new ArrayList<>();
        Receta desayuno = new Receta();
        desayuno.setTitulo("Soy un desayuno");
        desayuno.setCategoria(Categoria.DESAYUNO_MERIENDA);
        recetasMock.add(desayuno);

        // Cuando
        when(servicioRecetaMock.getRecetasPorCategoria(Categoria.DESAYUNO_MERIENDA)).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.irARecetas("DESAYUNO_MERIENDA", null);

        // Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");

        assertThat(recetas, hasSize(1));
        assertThat(recetas.get(0).getTitulo(), equalTo("Soy un desayuno"));
    }




}
