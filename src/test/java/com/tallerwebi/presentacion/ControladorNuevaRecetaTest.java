package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.ServicioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControladorNuevaRecetaTest {

    private ControladorNuevaReceta controladorNuevaReceta;
    private ServicioReceta servicioRecetaMock;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        servicioRecetaMock = mock(ServicioReceta.class);
        controladorNuevaReceta = new ControladorNuevaReceta(servicioRecetaMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controladorNuevaReceta).build();
    }

    @Test
    public void testCrearReceta() throws Exception {
        Receta receta = new Receta();
        receta.setTitulo("Milanesa");
        receta.setTiempo_preparacion(TiempoDePreparacion.TREINTA_MIN);
        receta.setCategoria(Categoria.ALMUERZO_CENA);
        receta.setIngredientes("Carne, pan rallado, huevo");
        receta.setDescripcion("Milanesa crocante de carne.");
        receta.setImagen("imagen_milanesa.jpg");


        when(servicioRecetaMock.guardarReceta(any(Receta.class))).thenReturn(receta);

        // Realizar la llamada al controlador
        mockMvc.perform(post("/guardar-receta")
                        .param("titulo", receta.getTitulo())
                        .param("tiempo_preparacion", "TREINTA_MIN")
                        .param("categoria", "ALMUERZO_CENA")
                        .param("ingredientes", receta.getIngredientes())
                        .param("descripcion", receta.getDescripcion())
                        .param("imagen", receta.getImagen()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("receta"))
                .andExpect(model().attribute("receta", receta));

        // Verifica que el método guardarReceta fue llamado en el servicio
        verify(servicioRecetaMock, times(1)).guardarReceta(any(Receta.class));
    }
}

