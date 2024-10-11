package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
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
            @RequestParam(value = "titulo", required = false) String titulo) {

        ModelMap modelo = new ModelMap();
        List<Receta> recetas;

        if (titulo != null && !titulo.isEmpty()) {
            recetas = servicioReceta.buscarRecetasPorTitulo(titulo);
            if (recetas.isEmpty()) {
                modelo.put("mensajeError", "No se encontró ninguna receta con esa referencia");
            }
        } else {
            recetas = servicioReceta.getTodasLasRecetas();
        }

        modelo.put("todasLasRecetas", recetas);
        modelo.put("tituloBuscado", titulo);

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

        modelo.put("todasLasRecetas", recetas);
        modelo.put("categoriaSeleccionada", categoria);
        modelo.put("tiempoSeleccionado", tiempo);

        return new ModelAndView("vistaReceta", modelo);
    }

    @RequestMapping(value = "/guardarReceta", method = RequestMethod.POST)
    public ModelAndView guardarReceta(
            @RequestParam("titulo") String titulo,
            @RequestParam("pasos") String pasos,
            @RequestParam("tiempoPreparacion") TiempoDePreparacion tiempoPreparacion,
            @RequestParam("categoria") Categoria categoria,
            @RequestParam("ingredientes") String ingredientes,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("imagen") String imagen) {

        System.out.println("Título recibido: " + titulo);
    //valida que los campos no esten vacios o solo con espacios vacios
        ModelAndView errorModelAndView = validarCampos(titulo, ingredientes, imagen, descripcion);
        if (errorModelAndView != null) {
            return errorModelAndView;
        }

        Receta nuevaReceta = new Receta(titulo, tiempoPreparacion, categoria, imagen, ingredientes, descripcion, pasos);
        servicioReceta.guardarReceta(nuevaReceta);

        return new ModelAndView("redirect:/vista-receta");
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/vista-receta");
    }





    private ModelAndView validarCampos(String titulo, String ingredientes, String imagen, String descripcion) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return crearModeloConError("El título no puede estar vacío o contener solo espacios.");
        }
        if (ingredientes == null || ingredientes.trim().isEmpty()) {
            return crearModeloConError("Los Ingredientes no pueden estar vacíos o contener solo espacios.");
        }
        if (imagen == null || imagen.trim().isEmpty()) {
            return crearModeloConError("La imagen no puede estar vacía o contener solo espacios.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return crearModeloConError("La descripción no puede estar vacía o contener solo espacios.");
        }
        return null; // Si no hay errores, retorna null
    }

    private ModelAndView crearModeloConError(String mensajeError) {

        ModelAndView modelAndView = new ModelAndView("vistaReceta");
        modelAndView.addObject("error", mensajeError);
        return modelAndView;
    }
}


