package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ControladorHome {
    private final ServicioReceta servicioReceta;

    @Autowired
    public ControladorHome(ServicioReceta servicioReceta) {
        this.servicioReceta = servicioReceta;
    }


    @GetMapping("/inicio")
    public ModelAndView mostrarHome() {
        // Crea una nueva instancia de ModelAndView
        ModelAndView modelAndView = new ModelAndView("home");

        // Carga las recetas recomendadas
        List<Receta> recetasRecomendadas = servicioReceta.obtenerRecetasRecomendadas();


        modelAndView.addObject("recetasRecomendadas", recetasRecomendadas);

        return modelAndView;
    }




}
