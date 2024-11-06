package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@Transactional
public class ControladorReceta {

    private final ServicioReceta servicioReceta;

    @Autowired
    public ControladorReceta(ServicioReceta servicioReceta) {
        this.servicioReceta = servicioReceta;
    }

    @PostMapping("/buscar-receta-titulo")
    public ModelAndView buscarRecetasPorTitulo(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "tiempo", required = false) String tiempo) {

        ModelMap modelo = new ModelMap();
        List<Receta> recetas;

        Categoria categoriaEnum = null;
        TiempoDePreparacion tiempoEnum = null;

        if (categoria != null && !categoria.equals("todos")) {
            categoriaEnum = Categoria.valueOf(categoria);
        }

        if (tiempo != null && !tiempo.equals("-")) {
            tiempoEnum = TiempoDePreparacion.valueOf(tiempo);
        }

        // Aplicar los filtros junto con la búsqueda por título
        if (titulo != null && !titulo.isEmpty()) {
            if (categoriaEnum != null && tiempoEnum != null) {
                recetas = servicioReceta.buscarRecetasPorTituloCategoriaYTiempo(titulo, categoriaEnum, tiempoEnum);
            } else if (categoriaEnum != null) {
                recetas = servicioReceta.buscarRecetasPorTituloYCategoria(titulo, categoriaEnum);
            } else if (tiempoEnum != null) {
                recetas = servicioReceta.buscarRecetasPorTituloYTiempo(titulo, tiempoEnum);
            } else {
                recetas = servicioReceta.buscarRecetasPorTitulo(titulo);
            }

            if (recetas.isEmpty()) {
                modelo.put("mensajeError", "No se encontró ninguna receta con esa referencia");
            }
        } else {
            // Si no se busca por título, solo aplicar los filtros de categoría y tiempo
            if (categoriaEnum != null && tiempoEnum != null) {
                recetas = servicioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoriaEnum, tiempoEnum);
            } else if (categoriaEnum != null) {
                recetas = servicioReceta.getRecetasPorCategoria(categoriaEnum);
            } else if (tiempoEnum != null) {
                recetas = servicioReceta.getRecetasPorTiempoDePreparacion(tiempoEnum);
            } else {
                recetas = servicioReceta.getTodasLasRecetas();
            }
        }

        modelo.put("todasLasRecetas", recetas);
        modelo.put("tituloBuscado", titulo);
        modelo.put("categoriaSeleccionada", categoria);
        modelo.put("tiempoSeleccionado", tiempo);

        return new ModelAndView("vistaReceta", modelo);
    }




    @RequestMapping("/vista-receta")
    public ModelAndView irARecetas(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "tiempo", required = false) String tiempo){

        ModelMap modelo = new ModelMap();
        List<Receta> recetas;

        Categoria categoriaEnum = null;
        TiempoDePreparacion tiempoEnum = null;

        if (categoria != null && !categoria.equals("todos")) {
            categoriaEnum = Categoria.valueOf(categoria);
        }

        if (tiempo != null && !tiempo.equals("-")) {
            tiempoEnum = TiempoDePreparacion.valueOf(tiempo);
        }

        if (categoriaEnum != null){
            if (tiempoEnum != null){
                recetas = servicioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoriaEnum, tiempoEnum);
            } else {
                recetas = servicioReceta.getRecetasPorCategoria(categoriaEnum);
            }
        } else if (tiempoEnum != null){
            recetas = servicioReceta.getRecetasPorTiempoDePreparacion(tiempoEnum);
        } else {
            recetas = servicioReceta.getTodasLasRecetas();
        }

        recetas.forEach(receta -> {
            if (receta.getImagen() != null) {
                String imagenBase64 = Base64.getEncoder().encodeToString(receta.getImagen());
                receta.setImagenBase64(imagenBase64);
            }
        });

        modelo.put("todasLasRecetas", recetas);
        modelo.put("categoriaSeleccionada", categoria);
        modelo.put("tiempoSeleccionado", tiempo);

        return new ModelAndView("vistaReceta", modelo);
    }

    @RequestMapping(value = "/guardarReceta", method = RequestMethod.POST)
    public ModelAndView guardarReceta(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "pasos", required = false) String pasos,
            @RequestParam(value = "tiempoPreparacion", required = false) TiempoDePreparacion tiempoPreparacion,
            @RequestParam(value = "categoria", required = false) Categoria categoria,
            @RequestParam(value = "ingredientes", required = false) String ingredientes,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        ModelMap model = new ModelMap();
        try {
            byte[] imagenBytes = null;
            if (imagen != null && !imagen.isEmpty()) {
                imagenBytes = imagen.getBytes();
            }
            Receta nuevaReceta = new Receta(titulo, tiempoPreparacion, categoria, imagenBytes, ingredientes, descripcion, pasos);
            servicioReceta.guardarReceta(nuevaReceta, imagen);

            return new ModelAndView("redirect:/vista-receta");
        } catch (Exception e) {
            model.put("error", "Error al cargar receta.");
            return new ModelAndView("redirect:/vista-receta", model);
        }
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/vista-receta");
    }


}