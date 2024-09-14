package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ControladorReceta {

    @RequestMapping("/vista-receta")
    public ModelAndView irARecetas(@RequestParam(value = "categoria", required = false) String categoria) {
        ModelMap modelo = new ModelMap();

        // Crear las recetas de ejemplo
        ArrayList<String> ingredientes1 = new ArrayList<>(Arrays.asList
                ("Carne", "Huevo", "Pan rallado", "Perejil", "Papas"));
        Receta receta1 = new Receta("Milanesa napolitana", 1, "almuerzo",
                "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", ingredientes1,
                "Esto es una descripción de mila napo.");
        ArrayList<String> ingredientes2 = new ArrayList<>(Arrays.asList
                ("Jamón", "Queso", "Tapa pascualina", "Huevo", "Tomate"));
        Receta receta2 = new Receta("Tarta jamón y queso", 1.5, "almuerzo",
                "https://i.postimg.cc/XYXRZ1Mq/tarta-jamon-queso.jpg", ingredientes2,
                "Esto es una descripción de tarta de jamón y queso.");
        ArrayList<String> ingredientes3 = new ArrayList<>(Arrays.asList
                ("Café", "Leche", "Pan lactal", "Mermelada"));
        Receta receta3 = new Receta("Café cortado con tostadas", 1, "desayuno",
                "https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg", ingredientes3,
                "Esto es una descripción de prueba.");

        // Lista completa de recetas
        List<Receta> recetas = Arrays.asList(receta1, receta2, receta3);

        // Si se especifica un tipo, filtrar las recetas por ese tipo
        if (categoria != null && !categoria.isEmpty()) {
            recetas = recetas.stream()
                    .filter(receta -> receta.getCategoria().equalsIgnoreCase(categoria))
                    .collect(Collectors.toList());
        }

        modelo.put("recetas", recetas);
        modelo.put("tipoSeleccionado", categoria); // Para reflejar el filtro en la vista

        return new ModelAndView("vistaReceta", modelo);
    }
}