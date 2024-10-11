package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ServicioRecetaTest {

    @Mock
    private RepositorioReceta repositorioReceta;
    private ServicioReceta servicioReceta;

    @BeforeEach
    public void inicializar(){
        this.repositorioReceta = mock(RepositorioReceta.class);
        this.servicioReceta = new ServicioRecetaImpl(repositorioReceta);
    }

    private Receta recetaMilanesaNapolitanaDeTreintaMinCreada(){
        return new Receta ("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", ".", "Esto es una descripción de mila napo", ".");
    }
    private Receta recetaMilanesaConPapasDeVeinteMinCreada(){
        return new Receta ("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/mila-papas.jpg", ".", "Milanesa con guarnición de papas fritas", ".");
    }
    private Receta recetaCafeConLecheDeDiezMinCreada(){
        return new Receta ("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg", ".", "Un clásico de las mañanas.", ".");
    }

    @Test
    public void queSePuedaGuardarUnaReceta() {
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        int idBuscado = receta.getId();

        servicioReceta.guardarReceta(receta);

        Mockito.when(repositorioReceta.getRecetaPorId(idBuscado)).thenReturn(receta);
        Mockito.verify(repositorioReceta, times(1)).guardar(receta);
    }

    @Test
    public void queSePuedaFiltrarPorCategoriaYSeMuestrenSoloLosDesayunosYMeriendas(){
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta1 = this.recetaCafeConLecheDeDiezMinCreada();
        Categoria categoria = Categoria.DESAYUNO_MERIENDA;

        List<Receta> todasLasRecetasYaFiltradas = new ArrayList<>();
        todasLasRecetasYaFiltradas.add(receta1);

        Mockito.when(repositorioReceta.getRecetasPorCategoria(categoria)).thenReturn(todasLasRecetasYaFiltradas);

        List<Receta> recetasFiltradas = servicioReceta.getRecetasPorCategoria(categoria);

        Mockito.verify(repositorioReceta, times(1)).getRecetasPorCategoria(categoria);

        assertEquals(1, recetasFiltradas.size());
    }

    @Test
    public void queSePuedaFiltrarPorTiempoDePreparacionYSeMuestrenSoloLosQueTienen30Min(){
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta1 = this.recetaCafeConLecheDeDiezMinCreada();
        TiempoDePreparacion tiempo_preparacion = TiempoDePreparacion.TREINTA_MIN;

        List<Receta> todasLasRecetasYaFiltradas = new ArrayList<>();
        todasLasRecetasYaFiltradas.add(receta);

        Mockito.when(repositorioReceta.getRecetasPorTiempoDePreparacion(tiempo_preparacion)).thenReturn(todasLasRecetasYaFiltradas);

        List<Receta> recetasFiltradas = servicioReceta.getRecetasPorTiempoDePreparacion(tiempo_preparacion);

        Mockito.verify(repositorioReceta, times(1)).getRecetasPorTiempoDePreparacion(tiempo_preparacion);

        assertEquals(1, recetasFiltradas.size());
    }

    @Test
    public void queSePuedaFiltrarPorCategoriaYPorTiempoYSeMuestreElAlmuerzoOCenaDe30Min(){
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta1 = this.recetaCafeConLecheDeDiezMinCreada();
        Receta receta2 = this.recetaMilanesaConPapasDeVeinteMinCreada();
        TiempoDePreparacion tiempo_preparacion = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;

        List<Receta> todasLasRecetasYaFiltradas = new ArrayList<>();
        todasLasRecetasYaFiltradas.add(receta);

        Mockito.when(repositorioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempo_preparacion)).thenReturn(todasLasRecetasYaFiltradas);

        List<Receta> recetasFiltradas = servicioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempo_preparacion);

        Mockito.verify(repositorioReceta, times(1)).getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempo_preparacion);

        assertEquals(1, recetasFiltradas.size());
    }

    @Test
    public void queSePuedaBuscarRecetasPorTituloYSeEncuentrenLasCorrectas() {
        // Definir algunos títulos y recetas
        String tituloBuscado = "Milanesa";
        Receta receta1 = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta2 = this.recetaMilanesaConPapasDeVeinteMinCreada();
        Receta receta3 = this.recetaCafeConLecheDeDiezMinCreada();

        List<Receta> recetasFiltradas = new ArrayList<>();
        recetasFiltradas.add(receta1);
        recetasFiltradas.add(receta2);

        Mockito.when(repositorioReceta.buscarRecetasPorTitulo(tituloBuscado)).thenReturn(recetasFiltradas);

        List<Receta> recetasEncontradas = servicioReceta.buscarRecetasPorTitulo(tituloBuscado);

        Mockito.verify(repositorioReceta, times(1)).buscarRecetasPorTitulo(tituloBuscado);

        assertEquals(2, recetasEncontradas.size());
        assertThat(recetasEncontradas, hasItem(hasProperty("titulo", equalTo("Milanesa napolitana"))));
        assertThat(recetasEncontradas, hasItem(hasProperty("titulo", equalTo("Milanesa con papas fritas"))));
    }

    @Test
    public void queSePuedaBuscarRecetasPorTituloYNoSeEncuentrenCoincidencias() {
        String tituloBuscado = "Pizza";

        Mockito.when(repositorioReceta.buscarRecetasPorTitulo(tituloBuscado)).thenReturn(new ArrayList<>());

        List<Receta> recetasEncontradas = servicioReceta.buscarRecetasPorTitulo(tituloBuscado);

        Mockito.verify(repositorioReceta, times(1)).buscarRecetasPorTitulo(tituloBuscado);

        assertEquals(0, recetasEncontradas.size());
    }

    @Test
    public void queSePuedanActualizarLosClicksOVisitasDeLasRecetas() {
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta1 = this.recetaCafeConLecheDeDiezMinCreada();

        this.servicioReceta.actualizarVisitasDeReceta(receta);
        this.servicioReceta.actualizarVisitasDeReceta(receta);
        this.servicioReceta.actualizarVisitasDeReceta(receta1);

        Mockito.verify(repositorioReceta, times(2)).actualizar(receta);
        Mockito.verify(repositorioReceta, times(1)).actualizar(receta1);
        assertEquals(receta.getContadorVisitas(), 2);
        assertEquals(receta1.getContadorVisitas(), 1);
    }

    @Test
    public void queSePuedanBuscarTodasLasRecetasYSePuedanOrdenarPorPopularidadDependiendoLaCantidadDeVisitas() {
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta1 = this.recetaCafeConLecheDeDiezMinCreada();
        Receta receta2 = this.recetaMilanesaConPapasDeVeinteMinCreada();
        List<Receta> todasLasRecetas = new ArrayList<>();
        todasLasRecetas.add(receta);
        todasLasRecetas.add(receta1);
        todasLasRecetas.add(receta2);

        Mockito.when(repositorioReceta.getRecetas()).thenReturn(todasLasRecetas);
        this.servicioReceta.actualizarVisitasDeReceta(receta);
        this.servicioReceta.actualizarVisitasDeReceta(receta);
        this.servicioReceta.actualizarVisitasDeReceta(receta1);

        Mockito.verify(repositorioReceta, times(2)).actualizar(receta);
        Mockito.verify(repositorioReceta, times(1)).actualizar(receta1);
        assertEquals(receta.getContadorVisitas(), 2);
        assertEquals(receta1.getContadorVisitas(), 1);
        //la posición
        assertEquals(receta, todasLasRecetas.get(0));
        assertEquals(receta1, todasLasRecetas.get(1));
        assertEquals(receta2, todasLasRecetas.get(2));
    }

    @Test
    public void queCuandoSeBusquenTodasLasRecetasYTenganLaMismaCantidadDeVisitasNoHayaCriterioDeOrdenamiento(){
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta1 = this.recetaCafeConLecheDeDiezMinCreada();
        Receta receta2 = this.recetaMilanesaConPapasDeVeinteMinCreada();
        List<Receta> todasLasRecetas = new ArrayList<>();
        todasLasRecetas.add(receta);
        todasLasRecetas.add(receta1);
        todasLasRecetas.add(receta2);

        Mockito.when(repositorioReceta.getRecetas()).thenReturn(todasLasRecetas);
        assertEquals(receta.getContadorVisitas(), 0);
        assertEquals(receta1.getContadorVisitas(), 0);
        assertEquals(receta2.getContadorVisitas(), 0);
        assertThat(todasLasRecetas, hasItem(receta));
        assertThat(todasLasRecetas, hasItem(receta1));
        assertThat(todasLasRecetas, hasItem(receta2));
    }
}
