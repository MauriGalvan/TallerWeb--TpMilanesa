package com.tallerwebi.dominio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class ServicioIngredienteTest {

    @Mock
    private RepositorioIngrediente repositorioIngrediente;
    private ServicioIngrediente servicioIngrediente;

    @BeforeEach
    public void inicializar() {
        this.repositorioIngrediente = mock(RepositorioIngrediente.class);
        this.servicioIngrediente = new ServicioIngredienteImpl(repositorioIngrediente);
    }

    @Test
    public void queSePuedaObtenerUnIngredientePorId(){
        Ingrediente ingrediente = new Ingrediente();

        Mockito.when(repositorioIngrediente.getIngredientePorId(ingrediente.getId())).thenReturn(ingrediente);

        servicioIngrediente.getIngredientePorId(ingrediente.getId());

        Mockito.verify(repositorioIngrediente, times(1)).getIngredientePorId(ingrediente.getId());
    }
}
