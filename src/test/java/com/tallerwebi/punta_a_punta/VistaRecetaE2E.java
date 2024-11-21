package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import com.tallerwebi.punta_a_punta.vistas.VistaReceta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class VistaRecetaE2E {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    VistaReceta vistaReceta;

    @BeforeAll
    static void abrirNavegador() {
        playwright = Playwright.create();
        //browser = playwright.chromium().launch();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(600));
    }

    @AfterAll
    static void cerrarNavegador() {
        playwright.close();
    }

    @BeforeEach
    void crearContextoYPagina() {
        context = browser.newContext();
        Page page = context.newPage();
        vistaReceta = new VistaReceta(page);
    }

    @AfterEach
    void cerrarContexto() {
        context.close();
    }

    @Test
    void deberiaMostrarTituloRecetasEnLaPagina() {
        String titulo = vistaReceta.obtenerTituloDeLaPagina();
        assertThat(titulo, containsStringIgnoringCase("Recetas"));
    }

    @Test
    void deberiaAbrirModalAlCargarReceta() {
        vistaReceta.darClickEnCargarReceta();

        assertTrue("El modal debería estar visible con el texto 'Cargar nueva receta'", vistaReceta.modalEstaVisible());
    }

    @Test
    void deberiaCargarUnaRecetaConImagenYRedirigir() {
        vistaReceta.darClickEnCargarReceta();
        vistaReceta.completarFormularioReceta(
                "Receta Test con Imagen",
                "Descripción de prueba con imagen",
                "DIEZ_MIN",
                "ALMUERZO_CENA",
                "src/test/java/com/tallerwebi/punta_a_punta/imagen/prueba.jpg"
        );
        vistaReceta.confirmarCargaDeReceta();
        String url = vistaReceta.obtenerURLActual();
        assertThat(url, containsStringIgnoringCase("/spring/vista-receta"));
    }
}
