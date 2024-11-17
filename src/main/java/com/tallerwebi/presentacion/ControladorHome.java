package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
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

        // Convertir las imágenes a Base64
        recetasRecomendadas.forEach(receta -> {
            if (receta.getImagen() != null) {
                String imagenBase64 = Base64.getEncoder().encodeToString(receta.getImagen());
                receta.setImagenBase64(imagenBase64);
            }
        });

        Rol rolUsuario = (Rol) request.getSession().getAttribute("ROL");
        boolean esProfesional = rolUsuario != null && rolUsuario.equals(Rol.PROFESIONAL);

        modelAndView.addObject("usuarioNombre", usuarioNombre);
        modelAndView.addObject("esProfesional", esProfesional);

        return modelAndView;
    }


    @GetMapping("/categorias/{nombreCategoria}")
    public ModelAndView verRecetasPorCategoria(@PathVariable String nombreCategoria) {
        Categoria categoria = Categoria.valueOf(nombreCategoria.toUpperCase());
        List<Receta> recetas = servicioReceta.getRecetasPorCategoria(categoria);

        // Convertir las imágenes a Base64
        recetas.forEach(receta -> {
            if (receta.getImagen() != null) {
                String imagenBase64 = Base64.getEncoder().encodeToString(receta.getImagen());
                receta.setImagenBase64(imagenBase64);
            }
        });

        ModelAndView mav = new ModelAndView("recetasPorCategoria");
        mav.addObject("recetas", recetas);
        mav.addObject("categoria", categoria);

        return mav;
    }

    @GetMapping("/profesionales/{nombreAutor}")
    public ModelAndView verRecetasPorAutor(@PathVariable String nombreAutor) {
        List<Receta> recetas = servicioReceta.buscarRecetaPorAutor(nombreAutor);

        // Convertir las imágenes a Base64
        recetas.forEach(receta -> {
            if (receta.getImagen() != null) {
                String imagenBase64 = Base64.getEncoder().encodeToString(receta.getImagen());
                receta.setImagenBase64(imagenBase64);
            }
        });

        ModelAndView mav = new ModelAndView("recetasPorAutor");
        mav.addObject("recetas", recetas);
        mav.addObject("autor", nombreAutor);

        return mav;
    }


}