package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
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

    private List<Ingrediente> unosIngredientes(){
        return Arrays.asList(
                new Ingrediente("Carne", Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Papas", 10, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA),
                new Ingrediente("Pan rallado", 200, Unidad_De_Medida.GRAMOS, Tipo_Ingrediente.CEREAL_O_GRANO)
        );
    }
    private Receta recetaMilanesaNapolitanaDeTreintaMinCreada(){
        return new Receta ("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", this.unosIngredientes(), "Esto es una descripción de mila napo", ".");
    }
    private Receta recetaMilanesaConPapasDeVeinteMinCreada(){
        return new Receta ("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/mila-papas.jpg", this.unosIngredientes(), "Milanesa con guarnición de papas fritas", ".");
    }
    private Receta recetaCafeConLecheDeDiezMinCreada(){
        return new Receta ("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg", this.unosIngredientes(), "Un clásico de las mañanas.", ".");
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

    //esto no iria en controlador detalle?
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
        recetasMock.add(this.recetaMilanesaNapolitanaDeTreintaMinCreada());
        recetasMock.add(this.recetaCafeConLecheDeDiezMinCreada());
        recetasMock.add(this.recetaMilanesaConPapasDeVeinteMinCreada());

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
        recetasMock.add(this.recetaMilanesaNapolitanaDeTreintaMinCreada());
        recetasMock.add(this.recetaMilanesaConPapasDeVeinteMinCreada());

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
    public void QueSePuedaCargarUnaReceta(){
        String titulo = "Milanesa napolitana";
        TiempoDePreparacion tiempo = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        List<Ingrediente> ingredientes = Arrays.asList(
                new Ingrediente("Carne", Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Papas", 10, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA),
                new Ingrediente("Pan rallado", 200, Unidad_De_Medida.GRAMOS, Tipo_Ingrediente.CEREAL_O_GRANO));
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";

        ModelAndView modelAndView = controladorReceta.guardarReceta(titulo, pasos, tiempo, categoria, ingredientes, descripcion, imagen);

        Receta recetaEsperada = new Receta(titulo, tiempo, categoria, imagen, ingredientes, descripcion, pasos);
        verify(servicioRecetaMock, times(1)).guardarReceta(recetaEsperada);

        assertEquals("redirect:/vista-receta", modelAndView.getViewName());
    }

}