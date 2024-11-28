package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Controller
public class ControladorPlanificador {


    private final ServicioReceta servicioReceta;
    private ServicioPlanificador servicioPlanificador;

    @Autowired
    public ControladorPlanificador(ServicioReceta servicioReceta, ServicioPlanificador servicioPlanificador) {
        this.servicioReceta = servicioReceta;
        this.servicioPlanificador = servicioPlanificador;
    }


    @RequestMapping("/vista-planificador")
    public ModelAndView irAPlanificador(@SessionAttribute(value = "ROL", required = false) String rol, HttpServletRequest request) {

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
        List<String> categorias = Arrays.asList("Desayuno", "Almuerzo", "Merienda", "Cena");

        Planificador planificador = servicioPlanificador.obtenerPlanificador();
        List<DetallePlanificador> detalles = planificador.obtenerDetalles();

        modelo.put("dias", dias);
        modelo.put("categorias", categorias);
        modelo.put("accesoDenegado", false);
        modelo.put("planificador", planificador);
        modelo.put("detalles", detalles);

        return new ModelAndView("vistaPlanificador", modelo);
    }


    @RequestMapping("/recetasModal")
    public ModelAndView obtenerRecetasPorCategoria(@RequestParam("categoria") String categoria, @RequestParam("dia") String dia) {
        ModelMap modelo = new ModelMap();

        Categoria categoriaEnum;
        switch (categoria.toUpperCase()) {
            case "DESAYUNO":
            case "MERIENDA":
                categoriaEnum = Categoria.DESAYUNO_MERIENDA;
                break;
            case "ALMUERZO":
            case "CENA":
                categoriaEnum = Categoria.ALMUERZO_CENA;
                break;
            default:
                throw new IllegalArgumentException("Categoría no válida: " + categoria);
        }

        List<Receta> recetas = servicioReceta.getRecetasPorCategoria(categoriaEnum);

        for (Receta receta : recetas) {
            if (receta.getImagen() != null) {
                String imagenBase64 = Base64.getEncoder().encodeToString(receta.getImagen());
                receta.setImagenBase64(imagenBase64);
            }
        }

        modelo.put("recetas", recetas);
        modelo.put("categoriaSeleccionada", categoriaEnum);
        modelo.put("dia", dia);

        return new ModelAndView("recetasModal", modelo);
    }

    @RequestMapping("/vista-lista-compra")
    public ModelAndView irAListaCompra() {
        return new ModelAndView("vistaLista");
    }

    @RequestMapping(value = "/guardarPlanificador", method = RequestMethod.POST)
    public ModelAndView guardarPlanificador(@RequestParam("dias") String diasStr, @RequestParam("recetas") String recetasStr, @RequestParam("categorias") String categoriasStr) {
        List<String> dias = Arrays.asList(diasStr.split(","));
        List<String> recetas = Arrays.asList(recetasStr.split(","));
        List<String> categorias = Arrays.asList(categoriasStr.split(","));

        Planificador planificador = servicioPlanificador.obtenerPlanificador();

        for (int i = 0; i < dias.size(); i++) {
            Dia diaEnum = Dia.valueOf(dias.get(i).toUpperCase().trim());
            int recetaId = Integer.parseInt(recetas.get(i));;
            String categoriaDelPlanificador = categorias.get(i);

            Receta receta = servicioReceta.getUnaRecetaPorId(recetaId);

            DetallePlanificador nuevoDetalle = new DetallePlanificador(diaEnum, receta.getCategoria(), receta, categoriaDelPlanificador);
            servicioPlanificador.agregarDetalle(planificador, nuevoDetalle);
        }

        servicioPlanificador.actualizar(planificador);

        return new ModelAndView("redirect:vista-planificador");
    }
    @RequestMapping(value = "/borrarPlanificador", method = RequestMethod.POST)
    public ModelAndView borrarPlanificador() {
        Planificador planificador = servicioPlanificador.obtenerPlanificador();

        planificador.obtenerDetalles().clear();

        servicioPlanificador.actualizar(planificador);

        return new ModelAndView("redirect:vista-planificador");
    }

}