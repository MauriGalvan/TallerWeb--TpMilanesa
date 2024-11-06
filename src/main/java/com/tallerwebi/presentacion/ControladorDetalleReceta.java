package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Ingrediente;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorDetalleReceta {

    private ServicioReceta servicioReceta;

    @Autowired
    public ControladorDetalleReceta(ServicioReceta servicioReceta) {
        this.servicioReceta = servicioReceta;
    }


    @RequestMapping("/detalleReceta")
    public ModelAndView mostrarDetalleReceta(Integer id) {
        ModelMap modelo = new ModelMap();
        Receta receta = servicioReceta.getUnaRecetaPorId(id);

        //este método para ver si funcionaba con .LAZY
        //List<Ingrediente> ingredientes = servicioReceta.getIngredientesDeRecetaPorId(id);


        //cuenta las visitas
        servicioReceta.actualizarVisitasDeReceta(receta);

        modelo.put("unaReceta", receta);
        //modelo.put("ingredientes", ingredientes);
        return new ModelAndView("detalleReceta", modelo);
    }

//    @PostMapping("/confirmarEliminarReceta")
//    public ModelAndView confirmarEliminarReceta(int id) {
//        ModelMap modelo = new ModelMap();
//        Receta receta = servicioReceta.getUnaRecetaPorId(id);
//        modelo.put("unaReceta", receta);
//        modelo.put("mostrarConfirmacion", true);
//        return new ModelAndView("detalleReceta", modelo);
//    }

    @PostMapping("/eliminarReceta")
    public ModelAndView eliminarReceta(@RequestParam("id") int id) {
        Receta receta = servicioReceta.getUnaRecetaPorId(id);
        servicioReceta.eliminarReceta(receta);
        return new ModelAndView("redirect:/vista-receta");
    }

    @PostMapping("/modificarReceta")
    public ModelAndView modificarReceta(@ModelAttribute Receta receta) {
        ModelMap modelo = new ModelMap();

        if (receta.getTitulo() == null || receta.getTitulo().isEmpty() ||
                receta.getIngredientes() == null || receta.getIngredientes().isEmpty() ||
                receta.getPasos() == null || receta.getPasos().isEmpty()) {
            modelo.put("unaReceta", receta);
            modelo.put("mensajeError", "La receta no fue modificada, verifique que los campos no estén vacíos.");
            return new ModelAndView("detalleReceta", modelo);
        }
        for(Ingrediente ingrediente : receta.getIngredientes()){
            System.out.println(ingrediente.getNombre() + ", su id: " + ingrediente.getId());
        }

        servicioReceta.actualizarReceta(receta);

        modelo.put("unaReceta", receta);
        modelo.put("mensajeExito", "La receta fue modificada correctamente.");
        return new ModelAndView("detalleReceta", modelo);
    }


}
