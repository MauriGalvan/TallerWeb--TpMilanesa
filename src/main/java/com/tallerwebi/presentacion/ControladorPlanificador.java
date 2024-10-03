package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class ControladorPlanificador {


    private final ServicioReceta servicioReceta;

    @Autowired
    public ControladorPlanificador(ServicioReceta servicioReceta) {
        this.servicioReceta = servicioReceta;
    }
    //Carga la vista principal del planificador
    @RequestMapping("/vista-planificador")
    public ModelAndView irAPlanificador() {
        //carga inicial de dias y categorias para mostrar en el planificador mmmmm?
        List<String> dias = Arrays.asList("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado" , "Domingo");
        List<String> categorias = Arrays.asList("Desayuno", "Almuerzo", "Merienda", "Cena");
        ModelMap modelo = new ModelMap();
        modelo.put("dias", dias);
        modelo.put("categorias", categorias);
        return new ModelAndView("vistaPlanificador", modelo);
    }

    //Obtiene las recetas de una categoria especifica para mostrar en el modal
    @RequestMapping("/recetasModal")
    public ModelAndView obtenerRecetasPorCategoria(@RequestParam("categoria") String categoria, @RequestParam("dia") String dia) {
        ModelMap modelo = new ModelMap();

        List<Receta> recetas = servicioReceta.getRecetasPorCategoria(categoria);

        modelo.put("recetas", recetas);
        modelo.put("categoriaSeleccionada", categoria);
        modelo.put("dia", dia);

        return new ModelAndView("recetasModal", modelo);
    }



}
