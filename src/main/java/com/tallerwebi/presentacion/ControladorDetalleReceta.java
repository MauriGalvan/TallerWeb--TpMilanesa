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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
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
    public ModelAndView mostrarDetalleReceta(Integer id,HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        Receta receta = servicioReceta.getUnaRecetaPorId(id);

            // Obtener el usuarioNombre desde la sesión
        HttpSession session = request.getSession();
        String usuarioNombre = (String) session.getAttribute("usuarioNombre");
        //cuenta las visitas
        servicioReceta.actualizarVisitasDeReceta(receta);

        if (receta.getImagen() != null && receta.getImagen().length > 0) {
            String imagenBase64 = Base64.getEncoder().encodeToString(receta.getImagen());
            receta.setImagenBase64(imagenBase64);
        }
        modelo.put("usuarioNombre", usuarioNombre);
        modelo.put("unaReceta", receta);
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

        boolean esProfesional = usuario != null && usuario.getRol().equals(Rol.PROFESIONAL);
        modelo.put("esProfesional", Optional.of(esProfesional));

        if (!esProfesional) {
            modelo.put("mensajeError", "Solo los usuarios con rol PROFESIONAL pueden eliminar recetas.");
            modelo.put("unaReceta", receta);
            return new ModelAndView("detalleReceta", modelo);
        }

        servicioReceta.eliminarReceta(receta);
        return new ModelAndView("redirect:/vista-receta");
    }


    @PostMapping("/modificarReceta")
    public ModelAndView modificarReceta(@ModelAttribute Receta receta,
                                        @RequestParam(value = "imagenArchivo", required = false) MultipartFile imagenArchivo,
                                        HttpServletRequest request) {
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");
        ModelMap modelo = new ModelMap();

        boolean esProfesional = usuario != null && usuario.getRol().equals(Rol.PROFESIONAL);
        modelo.put("esProfesional", Optional.of(esProfesional));

        if (!esProfesional) {
            modelo.put("mensajeError", "Solo los usuarios con rol PROFESIONAL pueden modificar recetas.");
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

        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            try {
                receta.setImagen(imagenArchivo.getBytes());
            } catch (IOException e) {
                modelo.put("unaReceta", receta);
                modelo.put("mensajeError", "Hubo un problema al cargar la imagen.");
                return new ModelAndView("detalleReceta", modelo);
            }
        } else {
            Receta recetaExistente = servicioReceta.getUnaRecetaPorId(receta.getId());
            receta.setImagen(recetaExistente.getImagen());
        }

        servicioReceta.actualizarReceta(receta);

        if (receta.getImagen() != null && receta.getImagen().length > 0) {
            String imagenBase64 = Base64.getEncoder().encodeToString(receta.getImagen());
            receta.setImagenBase64(imagenBase64);
        }

        return new ModelAndView("redirect:/detalleReceta?id=" + receta.getId());
    }



}
