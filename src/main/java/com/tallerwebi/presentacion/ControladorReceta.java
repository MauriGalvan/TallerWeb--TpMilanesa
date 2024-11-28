package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

        recetas.forEach(receta -> {
            if (receta.getImagen() != null) {
                String imagenBase64 = Base64.getEncoder().encodeToString(receta.getImagen());
                receta.setImagenBase64(imagenBase64);
            }
        });

        modelo.put("todasLasRecetas", recetas);
        modelo.put("tituloBuscado", titulo);
        modelo.put("categoriaSeleccionada", categoria);
        modelo.put("tiempoSeleccionado", tiempo);

        return new ModelAndView("vistaReceta", modelo);
    }




    @RequestMapping("/vista-receta")
    public ModelAndView irARecetas(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "tiempo", required = false) String tiempo,
            @SessionAttribute(value = "usuarioNombre", required = false) String usuarioNombre,
            HttpServletRequest request) {

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

        if (categoriaEnum != null) {
            if (tiempoEnum != null) {
                recetas = servicioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoriaEnum, tiempoEnum);
            } else {
                recetas = servicioReceta.getRecetasPorCategoria(categoriaEnum);
            }
        } else if (tiempoEnum != null) {
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

        Rol rolUsuario = (Rol) request.getSession().getAttribute("ROL");
        boolean esProfesionalOPremium = rolUsuario != null && (rolUsuario.equals(Rol.PROFESIONAL) || rolUsuario.equals(Rol.USUARIO_PREMIUM));

        modelo.put("todasLasRecetas", recetas);
        modelo.put("usuarioNombre", usuarioNombre);
        modelo.put("categoriaSeleccionada", categoria);
        modelo.put("tiempoSeleccionado", tiempo);
        modelo.put("esProfesionalOPremium", esProfesionalOPremium);


        return new ModelAndView("vistaReceta", modelo);
    }

    @RequestMapping(value = "/guardarReceta", method = RequestMethod.POST)
    public ModelAndView guardarReceta(
            @RequestParam("titulo") String titulo,
            @RequestParam("pasos") String pasos,
            @RequestParam("tiempoPreparacion") TiempoDePreparacion tiempoPreparacion,
            @RequestParam("categoria") Categoria categoria,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("autor") String autor,
            @RequestParam("imagen") MultipartFile imagen,
            HttpServletRequest request) { //se pasan los ingredientes por este parámetro, porque hay errores con el List<>

        List<Ingrediente> ingredientes = new ArrayList<>();

        int index = 0;
        while (request.getParameter("ingredientes[" + index + "].nombre") != null) {
            String nombre = request.getParameter("ingredientes[" + index + "].nombre");
            double cantidad = Double.parseDouble(request.getParameter("ingredientes[" + index + "].cantidad"));
            Unidad_De_Medida unidad_de_medida = Unidad_De_Medida.valueOf(request.getParameter("ingredientes[" + index + "].unidad_de_medida"));
            Tipo_Ingrediente tipo = Tipo_Ingrediente.valueOf(request.getParameter("ingredientes[" + index + "].tipo"));

            Ingrediente ingrediente = new Ingrediente(nombre, cantidad, unidad_de_medida, tipo);
            ingredientes.add(ingrediente);
            index++;
        }

        ModelMap model = new ModelMap();
        try {
            byte[] imagenBytes = null;
            if (imagen != null && !imagen.isEmpty()) {
                imagenBytes = imagen.getBytes();
            }
            Receta nuevaReceta = new Receta(titulo, tiempoPreparacion, categoria, imagenBytes, ingredientes, descripcion, pasos);
            nuevaReceta.setAutor(autor);
            servicioReceta.guardarReceta(nuevaReceta, imagen);

            return new ModelAndView("redirect:/vista-receta");
        } catch (Exception e) {
            model.put("error", "Error al cargar receta.");
            return new ModelAndView("redirect:/vista-receta", model);
        }
    }



}