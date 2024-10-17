package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ControladorHome {
    private final ServicioReceta servicioReceta;

    @Autowired
    public ControladorHome(ServicioReceta servicioReceta) {
        this.servicioReceta = servicioReceta;
    }


    @GetMapping("/inicio")
    public ModelAndView mostrarHome(
            @SessionAttribute(value = "usuarioNombre", required = false) String usuarioNombre,
            HttpServletRequest request) {
        // Crea una nueva instancia de ModelAndView
        ModelAndView modelAndView = new ModelAndView("home");

        // Carga las recetas recomendadas
        List<Receta> recetasRecomendadas = servicioReceta.obtenerRecetasRecomendadas();
        modelAndView.addObject("recetasRecomendadas", recetasRecomendadas);

        Rol rolUsuario = (Rol) request.getSession().getAttribute("ROL");
        boolean esProfesional = rolUsuario != null && rolUsuario.equals(Rol.PROFESIONAL);

        modelAndView.addObject("usuarioNombre", usuarioNombre);
        modelAndView.addObject("esProfesional", esProfesional);

        return modelAndView;
    }




}
