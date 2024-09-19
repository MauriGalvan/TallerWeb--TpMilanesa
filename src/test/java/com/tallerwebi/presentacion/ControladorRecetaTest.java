package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControladorRecetaTest {

    private ControladorReceta controladorReceta;
    private ControladorDetalleReceta controladorDetalleReceta;
    private ServicioReceta servicioRecetaMock;

    @BeforeEach
    public void setup() {
        servicioRecetaMock = mock(ServicioReceta.class);
        controladorReceta = new ControladorReceta(servicioRecetaMock);
        controladorDetalleReceta = new ControladorDetalleReceta(servicioRecetaMock);
    }

    @Test
    public void QueRetorneLaVistaRecetaCuandoSeEjecutaElMetodoIrARecetas(){
        //Dado
        String categoria = "cena";
        String tiempo = "1";
        //Cuando
        ModelAndView modelAndView = controladorReceta.irARecetas(categoria, tiempo);
        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
    }

    @Test
    public void QueRetorneLaVistaDetalleRecetaCuandoSeEjecutaElMetodoMostrarDetalleReceta(){
        //Dado
        int id = 1;
        //Cuando
        ModelAndView modelAndView = controladorDetalleReceta.mostrarDetalleReceta(id);
        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("detalleReceta"));
    }

    @Test
    public void QueRetorneTodasLasRecetasCuandoNoHayNingunFiltroSeleccionadoEnCategorias() {
        //Dado
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(new Receta("Milanesa napolitana", 30.0, "almuerzo", "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", "Carne, Huevo, Pan rallado, Perejil, Papas", "No vayas más al club de la milanesa, traelo a tu casa.", "Aplasta la carne y condimenta. Bate un huevo y mezcla pan rallado con perejil. Pasa cada filete por el huevo y luego por el pan rallado. Fríe hasta dorar. Sirve con papas y salsa de tomate, jamón y queso."));
        recetasMock.add(new Receta("Tarta jamón y queso", 60.0, "almuerzo", "https://i.postimg.cc/XYXRZ1Mq/tarta-jamon-queso.jpg", "Jamón, Queso, Tapa pascualina, Huevo, Tomate", "Para comer con tus amigos y familia.", "Precalienta el horno a 180 grados. Extiende una tapa de pascualina en un molde. Mezcla jamón picado, queso y tomate. Bate un huevo y agrégalo. Vierte sobre la base, cubre con otra tapa si deseas y haz cortes. Hornea 30-35 minutos hasta dorar."));
        recetasMock.add(new Receta("Café cortado con tostadas", 10.0, "desayuno", "https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg", "Café, Leche, Pan lactal, Mermelada", "Un clásico de las mañanas.", "Prepara el café a tu gusto y añade un chorrito de leche caliente. Tuesta las rebanadas de pan lactal hasta dorarlas. Unta mermelada en las tostadas. Sirve el café cortado en una taza y acompáñalo con las tostadas."));

        //Cuando
        when(servicioRecetaMock.getTodasLasRecetas()).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.irARecetas(null, null);

        //Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(3));  // Verificamos que hay 3 recetas
        assertThat(recetas.get(0).getTitulo(), equalTo("Milanesa napolitana"));
        assertThat(recetas.get(1).getTitulo(), equalTo("Tarta jamón y queso"));
        assertThat(recetas.get(2).getTitulo(), equalTo("Café cortado con tostadas"));
    }


    @Test
    public void QueRetorneLasRecetasDeAlmuerzoCuandoElFiltroDeCategoriaEsteSeleccionadoEnAlmmuerzo() {
        //Dado
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(new Receta("Milanesa napolitana", 30.0, "almuerzo", "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", "Carne, Huevo, Pan rallado, Perejil, Papas", "No vayas más al club de la milanesa, traelo a tu casa.", "Aplasta la carne y condimenta. Bate un huevo y mezcla pan rallado con perejil. Pasa cada filete por el huevo y luego por el pan rallado. Fríe hasta dorar. Sirve con papas y salsa de tomate, jamón y queso."));
        recetasMock.add(new Receta("Tarta jamón y queso", 60.0, "almuerzo", "https://i.postimg.cc/XYXRZ1Mq/tarta-jamon-queso.jpg", "Jamón, Queso, Tapa pascualina, Huevo, Tomate", "Para comer con tus amigos y familia.", "Precalienta el horno a 180 grados. Extiende una tapa de pascualina en un molde. Mezcla jamón picado, queso y tomate. Bate un huevo y agrégalo. Vierte sobre la base, cubre con otra tapa si deseas y haz cortes. Hornea 30-35 minutos hasta dorar."));
        
        //Cuando
        when(servicioRecetaMock.getRecetasPorCategoria("almuerzo")).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.irARecetas("almuerzo", null);

        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");

        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(2));
        assertThat(recetas.get(0).getTitulo(), equalTo("Milanesa napolitana"));
        assertThat(recetas.get(1).getTitulo(), equalTo("Tarta jamón y queso"));
        assertThat(recetas.get(0).getCategoria(), equalTo("almuerzo"));
        assertThat(recetas.get(1).getCategoria(), equalTo("almuerzo"));
    }
}