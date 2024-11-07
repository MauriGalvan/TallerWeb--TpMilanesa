package com.tallerwebi.punta_a_punta.vistas;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class VistaReceta extends VistaWeb {

    public VistaReceta(Page page) {
        super(page);
        page.navigate("http://localhost:8080/spring/vista-receta");
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

    public void completarFormularioReceta(String titulo, String descripcion, String tiempoPreparacion, String categoria) {
        this.escribirEnElElemento("#titulo", titulo);
        this.escribirEnElElemento("#descripcion", descripcion);
        this.seleccionarOpcion("#tiempoPreparacion", tiempoPreparacion);
        this.seleccionarOpcion("#categoria", categoria);
    }

    public void confirmarCargaDeReceta() {
        this.darClickEnElElemento("button[type='submit']");
    }
}
