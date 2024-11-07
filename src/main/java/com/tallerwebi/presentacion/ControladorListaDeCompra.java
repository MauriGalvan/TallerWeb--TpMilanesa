package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorListaDeCompra {

    @GetMapping("/listaCompras")
    public ModelAndView irAListaDeCompras() {

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
        recetas.add("ensalada 1");
        recetas.add("ensalada 2");
        recetas.add("ensalada 3");
        recetas.add("ensalada 4");

        modelo.put("recetas", recetas);

        List<String> ingredientes = new ArrayList<>();
        ingredientes.add("lechuga");
        ingredientes.add("sal");
        ingredientes.add("Tomate");
        modelo.put("ingredientes", ingredientes);

        if (recetas.isEmpty()) {
            modelo.put("mensajeError", "Tu lista de compras está vacía.");
            return new ModelAndView("vistaListaDeCompras", modelo);
        }


        return new ModelAndView("vistaListaDeCompras", modelo);
    }
}
