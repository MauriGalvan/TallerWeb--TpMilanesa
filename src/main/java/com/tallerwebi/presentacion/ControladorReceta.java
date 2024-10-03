package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Transactional
public class ControladorReceta {

    private final ServicioReceta servicioReceta;

    @Autowired
    public ControladorReceta(ServicioReceta servicioReceta) {
        this.servicioReceta = servicioReceta;
    }

    @RequestMapping("/vista-receta")
    public ModelAndView irARecetas(
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "tiempo", required = false) String tiempo){

        ModelMap modelo = new ModelMap();
        List<Receta> recetas;

//        double tiempoDouble = 0.0;

//        if (tiempo != null && !tiempo.equals("-")){
//            switch(tiempo){
//                case "10min":
//                    tiempoDouble = 10.0;
//                    break;
//                case "20min":
//                    tiempoDouble = 20.0;
//                    break;
//                case "30min":
//                    tiempoDouble = 30.0;
//                    break;
//                case "60min":
//                    tiempoDouble = 60.0;
//                    break;
//            }
//        }

        Categoria categoriaEnum = null;
        TiempoDePreparacion tiempoEnum = null;

        if (categoria != null && !categoria.equals("todos")) {
            categoriaEnum = Categoria.valueOf(categoria);
        }

        if (tiempo != null && !tiempo.equals("-")) {
            tiempoEnum = TiempoDePreparacion.valueOf(tiempo);
        }

//        if (categoria != null && !categoria.equals("todos")) {
//            if (tiempoDouble > 0.0){
//                recetas = servicioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempoDouble);
//            } else{
//                recetas = servicioReceta.getRecetasPorCategoria(categoria);
//            }
//        } else if (tiempoDouble > 0.0){
//            recetas = servicioReceta.getRecetasPorTiempoDePreparacion(tiempoDouble);
//        } else {
//            recetas = servicioReceta.getTodasLasRecetas();
//        }

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

        Receta nuevaReceta = new Receta(titulo, tiempoPreparacion, categoria, imagen, ingredientes, descripcion, pasos);
        servicioReceta.guardarReceta(nuevaReceta);

        return new ModelAndView("redirect:/vista-receta");
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/vista-receta");
    }


}