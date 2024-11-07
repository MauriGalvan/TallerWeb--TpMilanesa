package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Rol;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorUsuario {

    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorUsuario(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping("/perfil")
    public ModelAndView verPerfil(HttpServletRequest request) {
        ModelMap modelo = new ModelMap();

        // Obtener el usuario actual de la sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");

        if (usuario != null) {
            modelo.put("usuario", usuario);
            return new ModelAndView("perfil", modelo);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    @RequestMapping("/pago-premium")
    public ModelAndView mostrarPagoPremium(HttpServletRequest request) {
        ModelMap modelo = new ModelMap();


        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");

        if (usuario != null) {
            modelo.put("usuario", usuario);
            return new ModelAndView("pago-premium", modelo);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    @RequestMapping(value = "/pago-premium", method = RequestMethod.POST)
    public ModelAndView procesarPagoPremium(
            @ModelAttribute("metodoPago") String metodoPago,
            @ModelAttribute("numeroTarjeta") String numeroTarjeta,
            @ModelAttribute("fechaExpiracion") String fechaExpiracion,
            @ModelAttribute("cvv") String cvv,
            HttpServletRequest request) {
        ModelMap modelo = new ModelMap();


        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");

        if (usuario != null) {
            // Simular validación de pago
            boolean pagoExitoso = validarPago(metodoPago, numeroTarjeta, fechaExpiracion, cvv);

            if (pagoExitoso) {
                usuario.setRol(Rol.USUARIO_PREMIUM);
                servicioLogin.actualizarUsuario(usuario);
                request.getSession().setAttribute("ROL", Rol.USUARIO_PREMIUM);
                modelo.put("mensaje", "¡Pago realizado exitosamente! Ya eres un usuario Premium.");
                modelo.put("usuario", usuario);
                request.getSession().setAttribute("usuarioActual", usuario);
            } else {
                modelo.put("mensaje", "Error en el pago. Verifica tus datos e intenta nuevamente.");
            }

            return new ModelAndView("pago-premium", modelo);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }


//    MétodoDeValidaciónSimulado
    private boolean validarPago(String metodoPago, String numeroTarjeta, String fechaExpiracion, String cvv) {

        if (numeroTarjeta.length() != 16 || !numeroTarjeta.matches("\\d+")) {
            return false;
        }

        if (!fechaExpiracion.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            return false;
        }

        if (cvv.length() != 3 || !cvv.matches("\\d+")) {
            return false;
        }

        return true;
    }


}
