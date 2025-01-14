package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import static org.mockito.Mockito.any;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControladorRecetaTest {

    private ControladorReceta controladorReceta;
    private ControladorDetalleReceta controladorDetalleReceta;
    private ServicioReceta servicioRecetaMock;

    @BeforeEach
    public void setup() {
        servicioRecetaMock = mock(ServicioReceta.class);
        controladorReceta = new ControladorReceta(servicioRecetaMock);
        controladorDetalleReceta = new ControladorDetalleReceta(servicioRecetaMock);
    }

    private List<Ingrediente> unosIngredientes(){
        return Arrays.asList(
                new Ingrediente("Carne", 1, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Papas", 10, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA),
                new Ingrediente("Pan rallado", 200, Unidad_De_Medida.GRAMOS, Tipo_Ingrediente.CEREAL_O_GRANO)
        );
    }
    private Receta recetaMilanesaNapolitanaDeTreintaMinCreada(){
        byte[] imagen = new byte[]{0, 1};
        return new Receta ("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                imagen, this.unosIngredientes(), "Esto es una descripción de mila napo", ".");
    }
    private Receta recetaMilanesaConPapasDeVeinteMinCreada(){
        byte[] imagen = new byte[]{0, 1};
        return new Receta ("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                imagen, this.unosIngredientes(), "Milanesa con guarnición de papas fritas", ".");
    }
    private Receta recetaCafeConLecheDeDiezMinCreada(){
        byte[] imagen = new byte[]{0, 1};
        return new Receta ("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                imagen, this.unosIngredientes(), "Un clásico de las mañanas.", ".");
    }

    @Test
    public void QueRetorneLaVistaRecetaCuandoSeEjecutaElMetodoIrARecetas(){
        //Dado
        String categoria = "ALMUERZO_CENA";
        String tiempo = "UNA_HORA";

        // Mockear HttpServletRequest y HttpSession
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        // Configurar el valor que retorna el atributo "ROL" en la sesión
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("ROL")).thenReturn(null);  // O asigna Rol.PROFESIONAL si lo necesitas

        // Cuando
        ModelAndView modelAndView = controladorReceta.irARecetas(categoria, tiempo, null, requestMock);

        // Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
    }

    @Test
    public void QueRetorneLaVistaDetalleRecetaCuandoSeEjecutaElMetodoMostrarDetalleReceta(){
        //Dado
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();

        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioNombre")).thenReturn("UsuarioMock");
        //Cuando
        when(servicioRecetaMock.getUnaRecetaPorId(receta.getId())).thenReturn(receta);
        ModelAndView modelAndView = controladorDetalleReceta.mostrarDetalleReceta(Integer.valueOf(receta.getId()), requestMock);
        //Entonces
        verify(servicioRecetaMock, times(1)).getUnaRecetaPorId(receta.getId());
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("detalleReceta"));
    }



    @Test
    public void QueRetorneRecetasCuandoSeBuscaPorTitulo() {
        // Dado
        String tituloBuscado = "Milanesa";
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(this.recetaMilanesaNapolitanaDeTreintaMinCreada());

        // Cuando
        when(servicioRecetaMock.buscarRecetasPorTitulo(tituloBuscado)).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.buscarRecetasPorTitulo(tituloBuscado, null, null);

        //Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(1));
        assertThat(recetas.get(0).getTitulo(), containsString(tituloBuscado));
    }

    @Test
    public void QueSePuedaCargarUnaReceta(){
        String titulo = "Milanesa napolitana";
        String autor = "UsuarioMock";
        TiempoDePreparacion tiempo = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", // nombre del campo
                "imagen.jpg", // nombre del archivo
                "image/jpeg", // tipo de contenido
                new byte[]{0, 1, 2, 3} // contenido de la imagen
        );
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("autor")).thenReturn(autor);
        ModelAndView modelAndView = controladorReceta.guardarReceta(titulo, pasos, tiempo, categoria, descripcion, autor,imagen,request);


//        verify(servicioRecetaMock, times(1)).guardarReceta(any(Receta.class));

        assertEquals("redirect:/vista-receta", modelAndView.getViewName());
    }


    @Test
    public void QueRetorneRecetasCuandoSeBuscaPorTituloYCategoria() {
        // Dado
        String tituloBuscado = "Milanesa";
        Categoria categoriaBuscada = Categoria.ALMUERZO_CENA;
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(this.recetaMilanesaConPapasDeVeinteMinCreada());

        // Cuando
        when(servicioRecetaMock.buscarRecetasPorTituloYCategoria(tituloBuscado, categoriaBuscada)).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.buscarRecetasPorTitulo(tituloBuscado, categoriaBuscada.name(), null);

        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(1));
        assertThat(recetas.get(0).getTitulo(), containsString(tituloBuscado));
        assertThat(recetas.get(0).getCategoria(), is(categoriaBuscada));
    }

    @Test
    public void QueRetorneRecetasCuandoSeBuscaPorTituloYTiempo() {
        // Dado
        String tituloBuscado = "Milanesa";
        TiempoDePreparacion tiempoBuscado = TiempoDePreparacion.TREINTA_MIN;
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(this.recetaMilanesaNapolitanaDeTreintaMinCreada());

        // Cuando
        when(servicioRecetaMock.buscarRecetasPorTituloYTiempo(tituloBuscado, tiempoBuscado)).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.buscarRecetasPorTitulo(tituloBuscado, null, tiempoBuscado.name());

        // Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(1));
        assertThat(recetas.get(0).getTitulo(), containsString(tituloBuscado));
        assertThat(recetas.get(0).getTiempo_preparacion(), is(tiempoBuscado));
    }

    @Test
    public void QueRetorneRecetasCuandoSeBuscaPorTituloCategoriaYTiempo() {
        // Dado
        String tituloBuscado = "Milanesa";
        Categoria categoriaBuscada = Categoria.ALMUERZO_CENA;
        TiempoDePreparacion tiempoBuscado = TiempoDePreparacion.TREINTA_MIN;
        List<Receta> recetasMock = new ArrayList<>();
        recetasMock.add(this.recetaMilanesaNapolitanaDeTreintaMinCreada());

        // Cuando
        when(servicioRecetaMock.buscarRecetasPorTituloCategoriaYTiempo(tituloBuscado, categoriaBuscada, tiempoBuscado)).thenReturn(recetasMock);
        ModelAndView modelAndView = controladorReceta.buscarRecetasPorTitulo(tituloBuscado, categoriaBuscada.name(), tiempoBuscado.name());

        // Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(recetas, hasSize(1));
        assertThat(recetas.get(0).getTitulo(), containsString(tituloBuscado));
        assertThat(recetas.get(0).getCategoria(), is(categoriaBuscada));
        assertThat(recetas.get(0).getTiempo_preparacion(), is(tiempoBuscado));
    }

    @Test
    public void QueRetorneVistaErrorCuandoNoSeEncuentranRecetasPorTitulo() {
        // Dado
        String tituloBuscado = "PlatoInexistente";

        // Simular que no se encuentran recetas
        when(servicioRecetaMock.buscarRecetasPorTitulo(tituloBuscado)).thenReturn(new ArrayList<>());

        // Cuando se llama al método con solo el título
        ModelAndView modelAndView = controladorReceta.buscarRecetasPorTitulo(tituloBuscado, null, null);

        // Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta")); // Verificar que se devuelve "vistaReceta"
        assertThat(recetas, hasSize(0)); // No debe haber recetas en la lista
        assertThat(modelAndView.getModel().get("mensajeError"), is("No se encontró ninguna receta con esa referencia")); // Verificar el mensaje de error
    }


    @Test
    public void QueRetorneVistaErrorCuandoNoSeEncuentranRecetasPorTituloYCategoria() {
        // Dado
        String tituloBuscado = "PlatoInexistente";
        Categoria categoriaBuscada = Categoria.ALMUERZO_CENA;

        // Simular que no se encuentran recetas
        when(servicioRecetaMock.buscarRecetasPorTituloYCategoria(tituloBuscado, categoriaBuscada)).thenReturn(new ArrayList<>());

        // Cuando
        ModelAndView modelAndView = controladorReceta.buscarRecetasPorTitulo(tituloBuscado, categoriaBuscada.name(), null);

        // Entonces
        List<Receta> recetas = (List<Receta>) modelAndView.getModel().get("todasLasRecetas");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta")); // Verificar que devuelve "vistaReceta"
        assertThat(recetas, hasSize(0)); // No debe haber recetas en la lista
        assertThat(modelAndView.getModel().get("mensajeError"), is("No se encontró ninguna receta con esa referencia")); // Verificar el mensaje de error
    }

    @Test
    public void QueElUsuarioProfesionalPuedaVerCargarReceta() {
        // Dado
        String categoria = "ALMUERZO_CENA";
        String tiempo = "UNA_HORA";

        // Mockear HttpServletRequest y HttpSession
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        // Configurar el valor que retorna el atributo "ROL" en la sesión
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("ROL")).thenReturn(Rol.PROFESIONAL);

        // Cuando
        ModelAndView modelAndView = controladorReceta.irARecetas(categoria, tiempo, null, requestMock);

        // Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(modelAndView.getModel().get("esProfesionalOPremium"), is(true));
        assertThat(modelAndView.getModel().containsKey("todasLasRecetas"), is(true));
    }

    @Test
    public void QueElUsuarioPremiumPuedaVerCargarReceta() {
        // Dado
        String categoria = "ALMUERZO_CENA";
        String tiempo = "UNA_HORA";

        // Mockear HttpServletRequest y HttpSession
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        // Configurar el valor que retorna el atributo "ROL" en la sesión
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("ROL")).thenReturn(Rol.USUARIO_PREMIUM);

        // Cuando
        ModelAndView modelAndView = controladorReceta.irARecetas(categoria, tiempo, null, requestMock);

        // Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(modelAndView.getModel().get("esProfesionalOPremium"), is(true));
        assertThat(modelAndView.getModel().containsKey("todasLasRecetas"), is(true));
    }

    @Test
    public void QueElUsuarioNoPuedaVerCargarReceta() {
        // Dado
        String categoria = "ALMUERZO_CENA";
        String tiempo = "UNA_HORA";


        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);


        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("ROL")).thenReturn(Rol.USUARIO);

        // Cuando
        ModelAndView modelAndView = controladorReceta.irARecetas(categoria, tiempo, null, requestMock);

        // Entonces
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("vistaReceta"));
        assertThat(modelAndView.getModel().get("esProfesionalOPremium"), is(false));
        assertThat(modelAndView.getModel().containsKey("todasLasRecetas"), is(true));
    }

    @Test
    public void QueUsuarioPremiumPuedaCargarReceta() throws IOException {
        // Dado
        String titulo = "Tarta de manzana";
        String pasos = "1. Mezclar ingredientes\n2. Hornear";
        TiempoDePreparacion tiempoPreparacion = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        String descripcion = "Una deliciosa tarta de manzana casera.";


        byte[] imagenBytes = new byte[]{0, 1, 2};
        MultipartFile imagenMock = new MockMultipartFile("imagen", "tarta.jpg", "image/jpeg", imagenBytes);


        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);

        String autor = "usuarioPremium123"; // Define un autor válido


        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("ROL")).thenReturn(Rol.USUARIO_PREMIUM);
        when(requestMock.getParameter("ingredientes[0].nombre")).thenReturn("Manzana");
        when(requestMock.getParameter("ingredientes[0].cantidad")).thenReturn("3");
        when(requestMock.getParameter("ingredientes[0].unidad_de_medida")).thenReturn("UNIDAD");
        when(requestMock.getParameter("ingredientes[0].tipo")).thenReturn("FRUTA");
        when(requestMock.getParameter("ingredientes[1].nombre")).thenReturn(null);

        // Cuando
        ModelAndView modelAndView = controladorReceta.guardarReceta(
                titulo,
                pasos,
                tiempoPreparacion,
                categoria,
                descripcion,
                autor,
                imagenMock,
                requestMock
        );

        // Entonces
        verify(servicioRecetaMock, times(1)).guardarReceta(any(Receta.class), eq(imagenMock));
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/vista-receta"));
    }

    @Test
    public void QueUsuarioProfesionalPuedaCargarReceta() throws IOException {
        // Dado
        String titulo = "Tarta de frutillas";
        String pasos = "1. Preparar masa\n2. Agregar frutillas y crema";
        TiempoDePreparacion tiempoPreparacion = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        String descripcion = "Una tarta fresca y deliciosa.";


        byte[] imagenBytes = new byte[]{1, 2, 3};
        MultipartFile imagenMock = new MockMultipartFile("imagen", "tarta.jpg", "image/jpeg", imagenBytes);


        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpSession sessionMock = mock(HttpSession.class);


        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("ROL")).thenReturn(Rol.PROFESIONAL);
        when(requestMock.getParameter("ingredientes[0].nombre")).thenReturn("Frutilla");
        when(requestMock.getParameter("ingredientes[0].cantidad")).thenReturn("200");
        when(requestMock.getParameter("ingredientes[0].unidad_de_medida")).thenReturn("GRAMOS");
        when(requestMock.getParameter("ingredientes[0].tipo")).thenReturn("FRUTA");
        when(requestMock.getParameter("ingredientes[1].nombre")).thenReturn(null);
        String autor = "usuarioPremium123"; // Define un autor válido

        // Cuando
        ModelAndView modelAndView = controladorReceta.guardarReceta(
                titulo,
                pasos,
                tiempoPreparacion,
                categoria,
                descripcion,
                autor,
                imagenMock,
                requestMock
        );

        // Entonces
        verify(servicioRecetaMock, times(1)).guardarReceta(any(Receta.class), eq(imagenMock));
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/vista-receta"));
    }

}