package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
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
    public ModelAndView irAPlanificador() {
        List<String> dias = Arrays.asList("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado" , "Domingo");
        List<String> categorias = Arrays.asList("Desayuno", "Almuerzo", "Merienda", "Cena");
        ModelMap modelo = new ModelMap();
        modelo.put("dias", dias);
        modelo.put("categorias", categorias);
        return new ModelAndView("vistaPlanificador", modelo);
    }

    @RequestMapping("/recetasModal")
    public ModelAndView obtenerRecetasPorCategoria(@RequestParam("categoria") String categoria, @RequestParam("dia") String dia) {
        ModelMap modelo = new ModelMap();

        // Lógica de mapeo para convertir la categoría a un valor del enum
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
            case "POSTRE":
                categoriaEnum = Categoria.POSTRE;
                break;
            default:
                throw new IllegalArgumentException("Categoría no válida: " + categoria);
        }

        List<Receta> recetas = servicioReceta.getRecetasPorCategoria(categoriaEnum);

        modelo.put("recetas", recetas);
        modelo.put("categoriaSeleccionada", categoriaEnum);
        modelo.put("dia", dia);

        return new ModelAndView("recetasModal", modelo);
    }
    @RequestMapping(value = "/guardarPlanificador", method = RequestMethod.POST)
    public ModelAndView guardarPlanificador(@RequestParam List<String> dias, List<String> recetas) {
        System.out.println("Recibido: dias=" + dias + ", recetas=" + recetas);

        Planificador planificador = servicioPlanificador.obtenerPlanificador();
        if(planificador == null){
            planificador = new Planificador();
            servicioPlanificador.guardar(planificador);
        }

        for (int i = 0; i < dias.size(); i++) {
            String diaString = dias.get(i);
            Dia diaEnum = Dia.valueOf(diaString);  // Convierte el string a enum
            int recetaId = Integer.parseInt(recetas.get(i));  // Convierte el ID a int

            Receta receta = servicioReceta.getUnaRecetaPorId(recetaId);

            DetallePlanificador nuevoDetalle = new DetallePlanificador(diaEnum, receta.getCategoria(), receta);

            servicioPlanificador.agregarDetalle(planificador, nuevoDetalle);
        }

        servicioPlanificador.actualizar(planificador);

        return new ModelAndView("redirect:/vistaPlanificador");
    }

//    @RequestMapping(value = "/guardarPlanificador", method = RequestMethod.POST)
//    public ModelAndView guardarReceta(@RequestParam("detallePlanificador") String detallePlanificadorJson) {
//        System.out.println("kaka");
//        List<DetallePlanificador> detalles = new ArrayList<>();
//
//        Planificador planificador = servicioPlanificador.obtenerPlanificador();
//        if(planificador == null){
//            planificador = new Planificador();
//            servicioPlanificador.guardar(planificador);
//        }
//        JSONArray jsonArray = new JSONArray(detallePlanificadorJson);
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject jsonDetalle = jsonArray.getJSONObject(i);
//
//            // Obtener los valores del JSON
//            String dia = jsonDetalle.getString("dia");
//            String categoria = jsonDetalle.getString("categoria");
//            int recetaId = jsonDetalle.getInt("recetaId");
//
//            // Llenamos los detalles de planificador
//            DetallePlanificador detalle = new DetallePlanificador(dia, categoria, recetaId);
//            detalles.add(detalle);
//        }
//
//        for(DetallePlanificador detalle : detalles){
//            Receta receta = servicioReceta.getUnaRecetaPorId(detalle.getId());
//            String diaString = detalle.getDia().toString();
//            Dia diaEnum = Dia.valueOf(diaString);
//            Categoria categoriaEnum = receta.getCategoria();
//
//            DetallePlanificador nuevoDetalle = new DetallePlanificador(diaEnum, categoriaEnum, receta);
//
//            servicioPlanificador.agregarDetalle(planificador, nuevoDetalle);
//        }
//        servicioPlanificador.actualizar(planificador);
//
//        return new ModelAndView("redirect:/vistaPlanificador");
//    }










}
