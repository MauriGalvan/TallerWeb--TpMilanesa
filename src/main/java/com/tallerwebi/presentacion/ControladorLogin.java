package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Rol;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin){
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping("/login")
    public ModelAndView irALogin() {
        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }


    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {
        ModelMap model = new ModelMap();

        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
        if (usuarioBuscado != null) {
            request.getSession().setAttribute("usuarioActual", usuarioBuscado);
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            request.getSession().setAttribute("usuarioNombre", usuarioBuscado.getUsername());
            return new ModelAndView("redirect:/vista-receta");
        } else {
            model.put("error", "Usuario o clave incorrecta");
        }
        return new ModelAndView("login", model);
    }





    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario) {
        ModelMap model = new ModelMap();

        try {
            // Intenta registrar el usuario
            servicioLogin.registrar(usuario);
        } catch (UsuarioExistente e) {
            // Si el email ya está registrado, muestra un error específico
            model.put("error", "Mail ya registrado, intente ingresando uno nuevo.");
            model.put("usuario", usuario); // Mantiene los datos ingresados en el formulario
            model.put("roles", Rol.values()); // Para el select de roles
            return new ModelAndView("nuevo-usuario", model);
        } catch (Exception e) {
            // Manejo genérico de errores
            model.put("error", "Error al registrar el nuevo usuario");
            model.put("usuario", usuario); // Mantiene los datos ingresados en el formulario
            model.put("roles", Rol.values());
            return new ModelAndView("nuevo-usuario", model);
        }

        // Redirige al login si el registro fue exitoso
        return new ModelAndView("redirect:/login");
    }




    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        model.put("roles", Rol.values());
        return new ModelAndView("nuevo-usuario", model);
    }



    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }

}

