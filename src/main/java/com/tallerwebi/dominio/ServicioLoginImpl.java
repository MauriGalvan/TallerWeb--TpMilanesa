package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private HttpSession session;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return repositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente {
        // Utiliza el método buscar para validar si el email ya existe
        if (repositorioUsuario.buscar(usuario.getEmail()) != null) {
            throw new UsuarioExistente(); // Lanza excepción si ya existe un usuario con ese email
        }
        repositorioUsuario.guardar(usuario); // Guarda el usuario si no existe
    }


    @Override
    public boolean existeEmail(String email) {
        return repositorioUsuario.buscar(email) != null; // Reutiliza el método buscar del repositorio
    }

    @Override
    public Usuario obtenerUsuarioActual() {
        // Devuelve el usuario guardado en la sesión
        return (Usuario) session.getAttribute("usuarioActual");
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        repositorioUsuario.modificar(usuario);  // Llama a modificar para actualizar en BD
        // Actualiza el usuario en la sesión si es el usuario actual
        session.setAttribute("usuarioActual", usuario);
    }

}

