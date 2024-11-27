package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.lang.model.type.DeclaredType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ControladorListaDeCompra {

    private final ServicioPlanificador servicioPlanificador;
    private final ServicioIngrediente servicioIngrediente;

    @Autowired
    public ControladorListaDeCompra(ServicioPlanificador servicioPlanificador, ServicioIngrediente servicioIngrediente) {
        this.servicioPlanificador = servicioPlanificador;
        this.servicioIngrediente = servicioIngrediente;
    }

    @GetMapping("/listaCompras")
    public ModelAndView irAListaDeCompras(@SessionAttribute(value = "ROL", required = false) String rol) {

        ModelMap modelo = new ModelMap();

        if (rol == null || !rol.equals("USUARIO_PREMIUM")) {
            modelo.put("accesoDenegado", true); // Flag para indicar acceso denegado
            return new ModelAndView("vistaPlanificador", modelo);
        }

        List<String> dias = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        List<DetallePlanificador> detalles = servicioPlanificador.obtenerDetallesDelPlanificador();


        modelo.put("dias", dias);
        modelo.put("detalles", detalles);
        modelo.put("accesoDenegado", false);

        return new ModelAndView("vistaListaDeCompras", modelo);
    }

    @RequestMapping(value = "/encargarProductos", method = RequestMethod.POST)
    public ModelAndView irAEncargarProducto(@RequestParam("ingredientes") String ingredientesStr) {

        ModelMap modelo = new ModelMap();
        List<Ingrediente> ingredientes = new ArrayList<>();
        List<String> ingredientesIds = Arrays.asList(ingredientesStr.split(","));

        for (int i = 0; i < ingredientesIds.size(); i++) {
            int ingredienteId = Integer.parseInt(ingredientesIds.get(i));

            Ingrediente ingrediente = servicioIngrediente.getIngredientePorId(ingredienteId);
            ingredientes.add(ingrediente);
        }

        modelo.put("ingredientes", ingredientes);

        return new ModelAndView("vistaEncargarProducto", modelo);
    }
}