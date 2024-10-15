package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
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

    private Receta recetaMilanesaNapolitanaDeTreintaMinCreada(){
        byte[] imagen = new byte[]{0, 1};
        return new Receta ("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                imagen, ".", "Esto es una descripción de mila napo", ".");
    }
    private Receta recetaMilanesaConPapasDeVeinteMinCreada(){
        byte[] imagen = new byte[]{0, 1};
        return new Receta ("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                imagen, ".", "Milanesa con guarnición de papas fritas", ".");
    }
    private Receta recetaCafeConLecheDeDiezMinCreada(){
        byte[] imagen = new byte[]{0, 1};
        return new Receta ("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                imagen, ".", "Un clásico de las mañanas.", ".");
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
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        //Cuando
        when(servicioRecetaMock.getUnaRecetaPorId(receta.getId())).thenReturn(receta);
        ModelAndView modelAndView = controladorDetalleReceta.mostrarDetalleReceta(receta.getId());
        //Entonces
        verify(servicioRecetaMock, times(1)).getUnaRecetaPorId(receta.getId());
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("detalleReceta"));
    }



    @Test
    public void QueRetorneTodasLasRecetasCuandoNoHayNingunFiltroSeleccionadoEnCategorias() {
        //Dado
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(this.recetaMilanesaConPapasDeVeinteMinCreada());
        recetasMock.add(this.recetaCafeConLecheDeDiezMinCreada());
        recetasMock.add(this.recetaMilanesaNapolitanaDeTreintaMinCreada());

        //Cuando
        when(servicioRecetaMock.getTodasLasRecetas()).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.irARecetas(null, null);

        //Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(3));
        assertThat(recetas, hasItem(hasProperty("titulo", equalTo("Milanesa napolitana"))));
        assertThat(recetas, hasItem(hasProperty("titulo", equalTo("Milanesa con papas fritas"))));
        assertThat(recetas, hasItem(hasProperty("titulo", equalTo("Café cortado con tostadas"))));
    }


    @Test
    public void QueRetorneLasRecetasDeAlmuerzoCuandoElFiltroDeCategoriaEsteSeleccionadoEnAlmmuerzo() {
        //Dado
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(this.recetaMilanesaConPapasDeVeinteMinCreada());
        recetasMock.add(this.recetaMilanesaNapolitanaDeTreintaMinCreada());

        //Cuando
        when(servicioRecetaMock.getRecetasPorCategoria(Categoria.ALMUERZO_CENA)).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.irARecetas("ALMUERZO_CENA", null);

        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");

        //Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(2));
        assertThat(recetas, hasItem(hasProperty("titulo", equalTo("Milanesa napolitana"))));
        assertThat(recetas, hasItem(hasProperty("titulo", equalTo("Milanesa con papas fritas"))));
        assertThat(recetas, everyItem(hasProperty("categoria", equalTo(Categoria.ALMUERZO_CENA))));
    }

    @Test
    public void QueRetorneRecetasCuandoSeBuscaPorTitulo() {
        // Dado
        String tituloBuscado = "Milanesa";
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(this.recetaMilanesaNapolitanaDeTreintaMinCreada());

        // Cuando
        when(servicioRecetaMock.buscarRecetasPorTitulo(tituloBuscado)).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.buscarRecetasPorTitulo(tituloBuscado, null, null);

        // Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(1));
        assertThat(recetas.get(0).getTitulo(), containsString(tituloBuscado));
    }

    @Test
    public void QueSePuedaCargarUnaReceta() throws IOException {
        String titulo = "Milanesa napolitana";
        TiempoDePreparacion tiempo = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", // nombre del campo
                "imagen.jpg", // nombre del archivo
                "image/jpeg", // tipo de contenido
                new byte[]{0, 1, 2, 3} // contenido de la imagen
        );
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";

        ModelAndView modelAndView = controladorReceta.guardarReceta(titulo, pasos, tiempo, categoria, ingredientes, descripcion, imagen);

        Receta recetaEsperada = new Receta(titulo, tiempo, categoria, imagen.getBytes(), ingredientes, descripcion, pasos);
        verify(servicioRecetaMock, times(1)).guardarReceta(recetaEsperada);

        assertEquals("redirect:/vista-receta", modelAndView.getViewName());
    }

}