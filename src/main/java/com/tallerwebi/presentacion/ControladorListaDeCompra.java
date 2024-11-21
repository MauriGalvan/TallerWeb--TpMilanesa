package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorListaDeCompra {

    @GetMapping("/listaCompras")
    public ModelAndView irAListaDeCompras(HttpServletRequest request) {

        ArrayList<String> dias = new ArrayList<>();
        dias.add("Lunes");
        dias.add("Martes");
        dias.add("Miércoles");
        dias.add("Jueves");
        dias.add("Viernes");
        dias.add("Sábado");
        dias.add("Domingo");

        ModelAndView modelAndView = new ModelAndView();
        ModelMap modelo = new ModelMap();
        modelo.put("dias", dias);

        List<String> recetas = new ArrayList<>();
        recetas.add("Torta de manzana");
        recetas.add("Ensalada");
        recetas.add("Yogurt");
        recetas.add("Pizza");
        modelo.put("recetas", recetas);

        List<String> ingredientes = new ArrayList<>();
        ingredientes.add("Harina");
        ingredientes.add("lechuga");
        ingredientes.add("sal");
        ingredientes.add("Tomate");
        modelo.put("ingredientes", ingredientes);

        // Obtener el usuarioNombre desde la sesión
        HttpSession session = request.getSession();
        String usuarioNombre = (String) session.getAttribute("usuarioNombre");
        modelo.put("usuarioNombre", usuarioNombre);
        return new ModelAndView("vistaListaDeCompras", modelo);
    }
}
