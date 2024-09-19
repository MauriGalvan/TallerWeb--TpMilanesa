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
        double tiempo_preparacion = 1.0;
        String categoria = "almuerzo";
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
        String titulo = "Milanesa napolitana";
        double tiempo_preparacion = 1.0;
        String categoria = "ALMUERZO_CENA";
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";
        Receta receta = new Receta(titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion, pasos);
        String titulo1 = "Cafe con medialunas";
        String categoria1 = "DESAYUNO_MERIENDA";
        Receta receta1 = new Receta(titulo1, tiempo_preparacion, categoria1, imagen, ingredientes, descripcion, pasos);

        List<Receta> todasLasRecetas = new ArrayList<>();
        todasLasRecetas.add(receta);
        todasLasRecetas.add(receta1);

        Mockito.when(repositorioReceta.getRecetas()).thenReturn(todasLasRecetas);

        servicioReceta.guardarReceta(receta);
        servicioReceta.guardarReceta(receta1);

        List<Receta> recetasFiltradas = servicioReceta.getRecetasPorCategoria(categoria1);

        Mockito.verify(repositorioReceta, times(1)).getRecetas();

        assertEquals(1, recetasFiltradas.size());
    }

    @Test
    public void queSePuedaFiltrarPorTiempoDePreparacionYSeMuestrenSoloLosQueTienen30Min(){
        String titulo = "Milanesa napolitana";
        double tiempo_preparacion = 30.0;
        String categoria = "ALMUERZO_CENA";
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";
        Receta receta = new Receta(titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion, pasos);
        String titulo1 = "Cafe con medialunas";
        String categoria1 = "DESAYUNO_MERIENDA";
        double tiempo_preparacion1 = 60.0;
        Receta receta1 = new Receta(titulo1, tiempo_preparacion1, categoria1, imagen, ingredientes, descripcion, pasos);

        List<Receta> todasLasRecetas = new ArrayList<>();
        todasLasRecetas.add(receta);
        todasLasRecetas.add(receta1);

        Mockito.when(repositorioReceta.getRecetas()).thenReturn(todasLasRecetas);

        servicioReceta.guardarReceta(receta);
        servicioReceta.guardarReceta(receta1);

        List<Receta> recetasFiltradas = servicioReceta.getRecetasPorTiempoDePreparacion(tiempo_preparacion);

        Mockito.verify(repositorioReceta, times(1)).getRecetas();

        assertEquals(1, recetasFiltradas.size());
    }

    @Test
    public void queSePuedaFiltrarPorCategoriaYPorTiempoYSeMuestreElAlmuerzoOCenaDe30Min(){
        String titulo = "Milanesa napolitana";
        String titulo1 = "Cafe con medialunas";
        String titulo2 = "Pastel de Papa";
        double tiempo_preparacion = 30.0;
        double tiempo_preparacion1 = 60.0;
        String categoria = "ALMUERZO_CENA";
        String categoria1 = "DESAYUNO_MERIENDA";
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";
        Receta receta = new Receta(titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion, pasos);
        Receta receta1 = new Receta(titulo1, tiempo_preparacion, categoria1, imagen, ingredientes, descripcion,pasos);
        Receta receta2 = new Receta(titulo2, tiempo_preparacion1, categoria, imagen, ingredientes, descripcion, pasos);

        List<Receta> todasLasRecetas = new ArrayList<>();
        todasLasRecetas.add(receta);
        todasLasRecetas.add(receta1);
        todasLasRecetas.add(receta2);

        Mockito.when(repositorioReceta.getRecetas()).thenReturn(todasLasRecetas);

        servicioReceta.guardarReceta(receta);
        servicioReceta.guardarReceta(receta1);
        servicioReceta.guardarReceta(receta2);

        List<Receta> recetasFiltradas = servicioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempo_preparacion);

        Mockito.verify(repositorioReceta, times(1)).getRecetas();

        assertEquals(1, recetasFiltradas.size());
    }

}
