package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void queSePuedaGuardarUnaReceta() {
        String titulo = "Milanesa napolitana";
        TiempoDePreparacion tiempo_preparacion = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";

        Receta receta = new Receta(titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion, pasos);
        int idBuscado = receta.getId();

        servicioReceta.guardarReceta(receta);

        Mockito.when(repositorioReceta.getRecetaPorId(idBuscado)).thenReturn(receta);
        Mockito.verify(repositorioReceta, times(1)).guardar(receta);
    }

    @Test
    public void queSePuedaFiltrarPorCategoriaYSeMuestrenSoloLosDesayunosYMeriendas(){
        Categoria categoria = Categoria.ALMUERZO_CENA;
        Categoria categoria1 = Categoria.DESAYUNO_MERIENDA;
        Receta receta = new Receta();
        receta.setCategoria(categoria);
        Receta receta1 = new Receta();
        receta1.setCategoria(categoria1);

        List<Receta> todasLasRecetasYaFiltradas = new ArrayList<>();
        todasLasRecetasYaFiltradas.add(receta1);

        Mockito.when(repositorioReceta.getRecetasPorCategoria(categoria1)).thenReturn(todasLasRecetasYaFiltradas);

        servicioReceta.guardarReceta(receta);
        servicioReceta.guardarReceta(receta1);

        List<Receta> recetasFiltradas = servicioReceta.getRecetasPorCategoria(categoria1);

        Mockito.verify(repositorioReceta, times(1)).getRecetasPorCategoria(categoria1);

        assertEquals(1, recetasFiltradas.size());
    }

    @Test
    public void queSePuedaFiltrarPorTiempoDePreparacionYSeMuestrenSoloLosQueTienen30Min(){
        TiempoDePreparacion tiempo_preparacion = TiempoDePreparacion.TREINTA_MIN;
        TiempoDePreparacion tiempo_preparacion1 = TiempoDePreparacion.UNA_HORA;
        Receta receta = new Receta();
        receta.setTiempo_preparacion(tiempo_preparacion);
        Receta receta1 = new Receta();
        receta1.setTiempo_preparacion(tiempo_preparacion1);

        List<Receta> todasLasRecetasYaFiltradas = new ArrayList<>();
        todasLasRecetasYaFiltradas.add(receta);

        Mockito.when(repositorioReceta.getRecetasPorTiempoDePreparacion(tiempo_preparacion)).thenReturn(todasLasRecetasYaFiltradas);

        servicioReceta.guardarReceta(receta);
        servicioReceta.guardarReceta(receta1);

        List<Receta> recetasFiltradas = servicioReceta.getRecetasPorTiempoDePreparacion(tiempo_preparacion);

        Mockito.verify(repositorioReceta, times(1)).getRecetasPorTiempoDePreparacion(tiempo_preparacion);

        assertEquals(1, recetasFiltradas.size());
    }

    @Test
    public void queSePuedaFiltrarPorCategoriaYPorTiempoYSeMuestreElAlmuerzoOCenaDe30Min(){
        TiempoDePreparacion tiempo_preparacion = TiempoDePreparacion.TREINTA_MIN;
        TiempoDePreparacion tiempo_preparacion1 = TiempoDePreparacion.UNA_HORA;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        Categoria categoria1 = Categoria.DESAYUNO_MERIENDA;
        Receta receta = new Receta();
        receta.setCategoria(categoria);
        receta.setTiempo_preparacion(tiempo_preparacion);
        Receta receta1 = new Receta();
        receta1.setCategoria(categoria);
        receta1.setTiempo_preparacion(tiempo_preparacion1);
        Receta receta2 = new Receta();
        receta2.setCategoria(categoria1);
        receta2.setTiempo_preparacion(tiempo_preparacion);


        List<Receta> todasLasRecetasYaFiltradas = new ArrayList<>();
        todasLasRecetasYaFiltradas.add(receta);

        Mockito.when(repositorioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempo_preparacion)).thenReturn(todasLasRecetasYaFiltradas);

        servicioReceta.guardarReceta(receta);
        servicioReceta.guardarReceta(receta1);

        List<Receta> recetasFiltradas = servicioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempo_preparacion);

        Mockito.verify(repositorioReceta, times(1)).getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempo_preparacion);

        assertEquals(1, recetasFiltradas.size());
    }

    @Test
    public void queSePuedaBuscarRecetasPorTituloYSeEncuentrenLasCorrectas() {
        // Definir algunos títulos y recetas
        String tituloBuscado = "Milanesa";
        Receta receta1 = new Receta("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", "Jamón, Queso, Tapa pascualina, Huevo, Tomate",
                "Esto es una descripción de mila napo", ".");
        Receta receta2 = new Receta("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/mila-papas.jpg", "Carne, Pan rallado, Papas",
                "Milanesa con guarnición de papas fritas", ".");
        Receta receta3 = new Receta("Ensalada Cesar", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/cesar.jpg", "Lechuga, Pollo, Croutones, Queso",
                "Fresca ensalada con aderezo cesar.", ".");

        List<Receta> recetasFiltradas = new ArrayList<>();
        recetasFiltradas.add(receta1);
        recetasFiltradas.add(receta2);

        Mockito.when(repositorioReceta.buscarRecetasPorTitulo(tituloBuscado)).thenReturn(recetasFiltradas);

        servicioReceta.guardarReceta(receta1);
        servicioReceta.guardarReceta(receta2);
        servicioReceta.guardarReceta(receta3);

        List<Receta> recetasEncontradas = servicioReceta.buscarRecetasPorTitulo(tituloBuscado);

        Mockito.verify(repositorioReceta, times(1)).buscarRecetasPorTitulo(tituloBuscado);

        assertEquals(2, recetasEncontradas.size());

        assertEquals(receta1, recetasEncontradas.get(0));
        assertEquals(receta2, recetasEncontradas.get(1));
    }

    @Test
    public void queSePuedaBuscarRecetasPorTituloYNoSeEncuentrenCoincidencias() {
        String tituloBuscado = "Pizza";

        Mockito.when(repositorioReceta.buscarRecetasPorTitulo(tituloBuscado)).thenReturn(new ArrayList<>());

        List<Receta> recetasEncontradas = servicioReceta.buscarRecetasPorTitulo(tituloBuscado);

        Mockito.verify(repositorioReceta, times(1)).buscarRecetasPorTitulo(tituloBuscado);

        assertEquals(0, recetasEncontradas.size());
    }


}
