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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public ModelAndView irAListaDeCompras(@SessionAttribute(value = "ROL", required = false) String rol, HttpServletRequest request) {

        ModelMap modelo = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");



        if (usuario == null) {
            usuario = new Usuario();
            usuario.setUsername("Invitado");
        }

        modelo.put("usuario", usuario);


        if (rol == null || !rol.equals("USUARIO_PREMIUM")) {
            modelo.put("accesoDenegado", true);
            return new ModelAndView("vistaPlanificador", modelo);
        }

        List<String> dias = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        List<DetallePlanificador> detalles = servicioPlanificador.obtenerDetallesDelPlanificador();
        HttpSession session = request.getSession();
        String usuarioNombre = (String) session.getAttribute("usuarioNombre");
        modelo.put("usuarioNombre", usuarioNombre);

        modelo.put("dias", dias);
        modelo.put("detalles", detalles);
        modelo.put("accesoDenegado", false);

        return new ModelAndView("vistaListaDeCompras", modelo);
    }

    @RequestMapping(value = "/encargarProductos", method = RequestMethod.POST)
    public ModelAndView irAEncargarProducto(@RequestParam("ingredientes") String ingredientesStr, @SessionAttribute(value = "ROL", required = false) String rol, HttpServletRequest request) {

        ModelMap modelo = new ModelMap();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setUsername("Invitado");
        }

        modelo.put("usuario", usuario);

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