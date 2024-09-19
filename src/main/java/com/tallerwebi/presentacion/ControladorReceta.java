package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
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

//    @RequestMapping("/vista-receta")
//    public ModelAndView irARecetas() {
//        ModelMap modelo = new ModelMap();
//        List<Receta> todasLasRecetas = servicioReceta.getTodasLasRecetas();
//        modelo.put("todasLasRecetas", todasLasRecetas);
//        return new ModelAndView("vistaReceta", modelo);
//    }

    @RequestMapping("/vista-receta")
    public ModelAndView irARecetas(@RequestParam(value = "categoria", required = false) String categoria) {
        ModelMap modelo = new ModelMap();

        List<Receta> recetas;

        if (categoria != null && !categoria.equals("todos")) {
            recetas = servicioReceta.getRecetasPorCategoria(categoria);
        } else {
            recetas = servicioReceta.getTodasLasRecetas();
        }

        modelo.put("todasLasRecetas", recetas);
        modelo.put("categoriaSeleccionada", categoria); // Para mantener el estado del filtro en la vista

        return new ModelAndView("vistaReceta", modelo);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/vista-receta");
    }
}