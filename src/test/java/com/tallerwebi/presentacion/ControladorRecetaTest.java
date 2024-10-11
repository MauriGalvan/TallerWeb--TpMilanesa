package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.web.servlet.ModelAndView;
import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
        String categoria = "ALMUERZO_CENA";
        String tiempo = "UNA_HORA";
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
        recetasMock.add(new Receta("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA, "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", "Carne, Huevo, Pan rallado, Perejil, Papas", "No vayas más al club de la milanesa, traelo a tu casa.", "Aplasta la carne y condimenta. Bate un huevo y mezcla pan rallado con perejil. Pasa cada filete por el huevo y luego por el pan rallado. Fríe hasta dorar. Sirve con papas y salsa de tomate, jamón y queso."));
        recetasMock.add(new Receta("Tarta jamón y queso", TiempoDePreparacion.UNA_HORA, Categoria.ALMUERZO_CENA, "https://i.postimg.cc/XYXRZ1Mq/tarta-jamon-queso.jpg", "Jamón, Queso, Tapa pascualina, Huevo, Tomate", "Para comer con tus amigos y familia.", "Precalienta el horno a 180 grados. Extiende una tapa de pascualina en un molde. Mezcla jamón picado, queso y tomate. Bate un huevo y agrégalo. Vierte sobre la base, cubre con otra tapa si deseas y haz cortes. Hornea 30-35 minutos hasta dorar."));
        recetasMock.add(new Receta("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA, "https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg", "Café, Leche, Pan lactal, Mermelada", "Un clásico de las mañanas.", "Prepara el café a tu gusto y añade un chorrito de leche caliente. Tuesta las rebanadas de pan lactal hasta dorarlas. Unta mermelada en las tostadas. Sirve el café cortado en una taza y acompáñalo con las tostadas."));

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
        recetasMock.add(new Receta("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA, "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", "Carne, Huevo, Pan rallado, Perejil, Papas", "No vayas más al club de la milanesa, traelo a tu casa.", "Aplasta la carne y condimenta. Bate un huevo y mezcla pan rallado con perejil. Pasa cada filete por el huevo y luego por el pan rallado. Fríe hasta dorar. Sirve con papas y salsa de tomate, jamón y queso."));
        recetasMock.add(new Receta("Tarta jamón y queso", TiempoDePreparacion.UNA_HORA, Categoria.ALMUERZO_CENA, "https://i.postimg.cc/XYXRZ1Mq/tarta-jamon-queso.jpg", "Jamón, Queso, Tapa pascualina, Huevo, Tomate", "Para comer con tus amigos y familia.", "Precalienta el horno a 180 grados. Extiende una tapa de pascualina en un molde. Mezcla jamón picado, queso y tomate. Bate un huevo y agrégalo. Vierte sobre la base, cubre con otra tapa si deseas y haz cortes. Hornea 30-35 minutos hasta dorar."));

        //Cuando
        when(servicioRecetaMock.getRecetasPorCategoria(Categoria.ALMUERZO_CENA)).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.irARecetas("ALMUERZO_CENA", null);

        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");

        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(2));
        assertThat(recetas.get(0).getTitulo(), equalTo("Milanesa napolitana"));
        assertThat(recetas.get(1).getTitulo(), equalTo("Tarta jamón y queso"));
        assertThat(recetas.get(0).getCategoria(), equalTo(Categoria.ALMUERZO_CENA));
        assertThat(recetas.get(1).getCategoria(), equalTo(Categoria.ALMUERZO_CENA));
    }

    @Test
    public void QueRetorneRecetasCuandoSeBuscaPorTitulo() {
        // Dado
        String tituloBuscado = "Milanesa";
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(new Receta("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA, "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", "Carne, Huevo, Pan rallado, Perejil, Papas", "No vayas más al club de la milanesa, traelo a tu casa.", "Aplasta la carne y condimenta. Bate un huevo y mezcla pan rallado con perejil. Pasa cada filete por el huevo y luego por el pan rallado. Fríe hasta dorar. Sirve con papas y salsa de tomate, jamón y queso."));

        // Cuando
        when(servicioRecetaMock.buscarRecetasPorTitulo(tituloBuscado)).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.buscarRecetasPorTitulo(tituloBuscado);

        // Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(1));
        assertThat(recetas.get(0).getTitulo(), containsString(tituloBuscado));
    }

    @Test
    public void QueSePuedaCargarUnaReceta(){
        String titulo = "Milanesa napolitana";
        TiempoDePreparacion tiempo = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";

        ModelAndView modelAndView = controladorReceta.guardarReceta(titulo, pasos, tiempo, categoria, ingredientes, descripcion, imagen);

        Receta recetaEsperada = new Receta(titulo, tiempo, categoria, imagen, ingredientes, descripcion, pasos);
        verify(servicioRecetaMock, times(1)).guardarReceta(recetaEsperada);

        assertEquals("redirect:/vista-receta", modelAndView.getViewName());
    }
    @Test
    public void QueNoSePuedaCargarUnaRecetaConTituloVacioOconEspacioEnBlanco(){
        String titulo = "  ";
        TiempoDePreparacion tiempo = TiempoDePreparacion.UNA_HORA;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";

        ModelAndView modelAndView =  controladorReceta.guardarReceta(titulo, pasos, tiempo, categoria, ingredientes, descripcion, imagen);

        verify(servicioRecetaMock, times(0)).guardarReceta(any(Receta.class));

        // Verifica si se añadio un mensaje de error al modelo
        assertTrue(modelAndView.getModel().containsKey("error"));
        assertEquals("El título no puede estar vacío o contener solo espacios.", modelAndView.getModel().get("error"));
    }
    @Test
    public void QueNoSePuedaCargarUnaRecetaConIngredientesVacios() {
        String titulo = "Receta titulo";
        String ingredientes = "   "; // Ingredientes vacíos
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String descripcion = "Descripción de la receta";
        String pasos = "Paso 1, Paso 2";
        TiempoDePreparacion tiempo = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;

        ModelAndView modelAndView = controladorReceta.guardarReceta(titulo, pasos, tiempo, categoria, ingredientes, descripcion, imagen);

        assertTrue(modelAndView.getModel().containsKey("error"));
        assertEquals("Los Ingredientes no pueden estar vacíos o contener solo espacios.", modelAndView.getModel().get("error"));
        verify(servicioRecetaMock, times(0)).guardarReceta(any(Receta.class));
    }
}