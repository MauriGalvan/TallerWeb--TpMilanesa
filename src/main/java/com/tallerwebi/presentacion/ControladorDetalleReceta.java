package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorDetalleReceta {

    private ServicioReceta servicioReceta;

    @Autowired
    public ControladorDetalleReceta(ServicioReceta servicioReceta) {
        this.servicioReceta = servicioReceta;
    }

    @RequestMapping("/detalleReceta")
    public ModelAndView mostrarDetalleReceta(int id) {
        ModelMap modelo = new ModelMap();
        Receta r = servicioReceta.getUnaRecetaPorId(id);
        modelo.put("unaReceta", r);
        return new ModelAndView("detalleReceta", modelo);
    }
}
