package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Controller
@Transactional
public class ControladorNuevaReceta {


    private final ServicioReceta servicioReceta;



    public ControladorNuevaReceta(ServicioReceta servicioReceta) {
        this.servicioReceta = servicioReceta;
    }

    @GetMapping("/crear-receta")
    public String mostrarForm(Model model) {

        Receta receta = new Receta();
        model.addAttribute("receta", receta);
        return "crearReceta";
    }


    @RequestMapping(path = "/guardar-receta", method = RequestMethod.POST)
    public ModelAndView guardarReceta(@ModelAttribute("receta") Receta receta) throws IOException {
        // si me devuelve la misma pagina no esta devolviendo nada
        if (receta.getTitulo() == null || receta.getIngredientes() == null || receta.getDescripcion() == null) {
            ModelAndView modelAndView = new ModelAndView("crearReceta");

            return modelAndView;
        }
    // si me devuelve datos se guarda la receta y va a la vista receta
        Receta nueva = servicioReceta.guardarReceta(receta);


        ModelAndView modelAndView = new ModelAndView("vistaReceta");
        modelAndView.addObject("receta", nueva);
        return modelAndView;
    }


}
