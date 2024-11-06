package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

public class ControladorHomeTest {


    private ControladorHome controladorHome;
    private ServicioReceta servicioRecetaMock;



    @BeforeEach
    public void setup() {
        servicioRecetaMock = mock(ServicioReceta.class);
        controladorHome = new ControladorHome(servicioRecetaMock);
    }
    @Test
    public void queRetorneLaVistaDeLasRecetasPorAutor() {
        Receta receta1 = new Receta();
        receta1.setAutor("mauri");
        receta1.setTitulo("Milanesa napolitana");
        Receta receta2 = new Receta();
        receta2.setAutor("mauri");
        receta2.setTitulo("papas fritas");

        List<Receta> recetasmock = Arrays.asList(receta1, receta2);
        when(servicioRecetaMock.buscarRecetaPorAutor("mauri")).thenReturn(recetasmock);

        //  se llama al controlador con el autor mauri
        ModelAndView modelAndView = controladorHome.verRecetasPorAutor("mauri");

        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("recetas");

        assertThat(recetas, hasSize(2));
        assertThat(recetas.get(0).getTitulo(), equalTo("Milanesa napolitana"));
        assertThat(recetas.get(1).getTitulo(), equalTo("papas fritas"));

        assertThat(recetas.get(0).getAutor(), equalTo("mauri"));
        assertThat(recetas.get(1).getAutor(), equalTo("mauri"));

        assertThat(modelAndView.getModel().get("autor"), equalTo("mauri"));
        assertThat(modelAndView.getViewName(), equalTo("recetasPorAutor"));
    }
}
