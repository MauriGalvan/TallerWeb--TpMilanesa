package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);

    void registrar(Usuario usuario) throws UsuarioExistente;

    Usuario obtenerUsuarioActual();

    void actualizarUsuario(Usuario usuario);

    boolean existeEmail(String email);
}
