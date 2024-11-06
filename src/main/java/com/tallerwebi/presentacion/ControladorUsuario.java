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
            return new ModelAndView("perfil", modelo); // Muestra la vista "perfil"
        } else {
            return new ModelAndView("redirect:/login"); // Redirige a la vista de login si no hay usuario en sesión
        }
    }

    @RequestMapping("/pago-premium")
    public ModelAndView mostrarPagoPremium(HttpServletRequest request) {
        ModelMap modelo = new ModelMap();

        // Obtener el usuario actual de la sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");

        if (usuario != null) {
            modelo.put("usuario", usuario);
            return new ModelAndView("pago-premium", modelo); // Muestra la vista de pago
        } else {
            return new ModelAndView("redirect:/login"); // Redirige a login si no hay usuario en sesión
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

        // Obtener el usuario actual de la sesión
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioActual");

        if (usuario != null) {
            // Simular validación de pago
            boolean pagoExitoso = validarPago(metodoPago, numeroTarjeta, fechaExpiracion, cvv);

            if (pagoExitoso) {
                usuario.setRol(Rol.USUARIO_PREMIUM); // Cambia el rol del usuario a USUARIO_PREMIUM
                servicioLogin.actualizarUsuario(usuario); // Guarda los cambios usando el servicio de login
                request.getSession().setAttribute("ROL", Rol.USUARIO_PREMIUM); // Actualiza el rol en la sesión
                modelo.put("mensaje", "¡Pago realizado exitosamente! Ya eres un usuario Premium.");
                modelo.put("usuario", usuario);
                request.getSession().setAttribute("usuarioActual", usuario); // Actualiza la sesión con el nuevo rol
            } else {
                modelo.put("mensaje", "Error en el pago. Verifica tus datos e intenta nuevamente.");
            }

            return new ModelAndView("pago-premium", modelo);
        } else {
            return new ModelAndView("redirect:/login"); // Redirige a login si no hay usuario en sesión
        }
    }


    // Método de validación simulado
    private boolean validarPago(String metodoPago, String numeroTarjeta, String fechaExpiracion, String cvv) {
        // Validar que la longitud del número de tarjeta sea 16
        if (numeroTarjeta.length() != 16 || !numeroTarjeta.matches("\\d+")) {
            return false;
        }
        // Validar formato de fecha de expiración (MM/AA)
        if (!fechaExpiracion.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            return false;
        }
        // Validar que CVV tenga 3 dígitos
        if (cvv.length() != 3 || !cvv.matches("\\d+")) {
            return false;
        }
        // Puedes simular una validación adicional aquí
        return true; // En un caso real, llamaría a un sistema de pago externo
    }


}

