package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorDetalleReceta {

    @RequestMapping(value = "/detalleReceta")
    public ModelAndView mostrarDetalleReceta(@ModelAttribute Receta receta) {
        ModelMap modelo = new ModelMap();
        modelo.put("titulo", receta.getTitulo());
        modelo.put("imagen", receta.getImagen());
        modelo.put("ingredientes", receta.getIngredientes());

        return new ModelAndView("detalleReceta", modelo);
    }
}
