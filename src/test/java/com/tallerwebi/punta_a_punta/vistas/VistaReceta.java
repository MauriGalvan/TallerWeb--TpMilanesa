package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;

public class VistaReceta extends VistaWeb {

    public VistaReceta(Page page) {
        super(page);
        page.navigate("localhost:8080/spring/login");
    }

    public String obtenerTextoDeLaBarraDeNavegacion(){
        return this.obtenerTextoDelElemento("nav a.navbar-brand");
    }

    public String obtenerMensajeDeError(){
        return this.obtenerTextoDelElemento("p.alert.alert-danger");
    }

    public void escribirEMAIL(String email){
        this.escribirEnElElemento("#email", email);
    }

    public void escribirClave(String clave){
        this.escribirEnElElemento("#password", clave);
    }

    public void darClickEnIniciarSesion(){
        this.darClickEnElElemento("#btn-login");
    }

    public String obtenerTituloDeLaPagina() {
        return this.obtenerTextoDelElemento("h1.titulo-header");
    }

    public void darClickEnCargarReceta() {
        this.darClickEnElElemento("button[data-bs-toggle='modal']");
    }

    public boolean modalEstaVisible() {
        return this.obtenerTextoDelElemento("#modalRecetaLabel").equals("Cargar nueva receta");
    }

    public void completarFormularioReceta(String titulo, String descripcion, String tiempoPreparacion, String categoria, String ingrediente, String cantidad, String nombreArchivo) {
        this.escribirEnElElemento("#titulo", titulo);
        this.escribirEnElElemento("#descripcion", descripcion);
        this.seleccionarOpcion("#tiempoPreparacion", tiempoPreparacion);
        this.seleccionarOpcion("#categoria", categoria);
        this.escribirEnElElemento("#ingredientesContainer .ingrediente input[name='ingredientes[0].nombre']", ingrediente);
        this.escribirEnElElemento("#ingredientesContainer .ingrediente input[name='ingredientes[0].cantidad']", cantidad);
        this.cargarArchivo("#imagen", nombreArchivo);
    }

    public void confirmarCargaDeReceta() {
        this.page.locator("button:has-text('Aceptar')").click();
    }

    public boolean recetaEstaCargada(String titulo) {
        List<String> recetasMostradas = page.locator(".titulo").allInnerTexts();
        return recetasMostradas.stream().anyMatch(titulo::equalsIgnoreCase);
    }

}
