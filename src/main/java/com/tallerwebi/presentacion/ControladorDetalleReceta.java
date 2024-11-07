package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Ingrediente;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.Rol;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

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


    @PostMapping("/confirmarEliminarReceta")
    public ModelAndView confirmarEliminarReceta(int id) {
        ModelMap modelo = new ModelMap();
        Receta receta = servicioReceta.getUnaRecetaPorId(id);
        modelo.put("unaReceta", receta);
        modelo.put("mostrarConfirmacion", Optional.of(true));
        return new ModelAndView("detalleReceta", modelo);
    }

    @PostMapping("/eliminarReceta")
    public ModelAndView eliminarReceta(@ModelAttribute Receta receta, HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");
        ModelMap modelo = new ModelMap();

        boolean esProfesionalOPremium = usuario != null &&
                (usuario.getRol().equals(Rol.PROFESIONAL) ||
                        usuario.getRol().equals(Rol.USUARIO_PREMIUM));
        modelo.put("esProfesionalOPremium", Optional.of(esProfesionalOPremium));

        if (!esProfesionalOPremium) {
            modelo.put("mensajeError", "No tienes permisos para eliminar recetas.");
            modelo.put("unaReceta", receta);
            return new ModelAndView("detalleReceta", modelo);
        }

        servicioReceta.eliminarReceta(receta);
        return new ModelAndView("redirect:/vista-receta");
    }



    @PostMapping("/modificarReceta")
    public ModelAndView modificarReceta(@ModelAttribute Receta receta, HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");
        ModelMap modelo = new ModelMap();

        boolean esProfesionalOPremium = usuario != null &&
                (usuario.getRol().equals(Rol.PROFESIONAL) ||
                        usuario.getRol().equals(Rol.USUARIO_PREMIUM));
        modelo.put("esProfesionalOPremium", Optional.of(esProfesionalOPremium));

        if (!esProfesionalOPremium) {
            modelo.put("mensajeError", "No tienes permisos para modificar recetas.");
            modelo.put("unaReceta", receta);
            return new ModelAndView("detalleReceta", modelo);
        }

        // Valida que los campos de la receta no estén vacíos
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



