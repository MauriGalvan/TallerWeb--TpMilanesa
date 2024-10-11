package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Categoria;
import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.RepositorioReceta;
import com.tallerwebi.dominio.TiempoDePreparacion;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateInfraestructuraTestConfig.class})

public class RepositorioRecetaImplTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioReceta repositorioReceta;

    @BeforeEach
    public void inicializar(){
        this.repositorioReceta = new RepositorioRecetaImpl(sessionFactory);
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnRepositorioRecetaCuandoGuardoUnaRecetaEntoncesLoEncuentroEnLaBaseDeDatos(){
        String titulo = "Milanesa napolitana";
        TiempoDePreparacion tiempo_preparacion = TiempoDePreparacion.TREINTA_MIN;
        Categoria categoria = Categoria.ALMUERZO_CENA;
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";
        String pasos = ".";

        Receta receta = new Receta(titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion, pasos);

        this.repositorioReceta.guardar(receta);

        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        Receta recetaObtenida = (Receta)query.getSingleResult();

        assertEquals(receta, recetaObtenida);

    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnRepositorioRecetaCuandoGuardoVariasRecetasEntoncesLasEncuentroEnLaBaseDeDatos(){
        Receta receta1 = new Receta("Tarta de jamón y queso", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/tarta.jpg", "Jamón, Queso, Tapa pascualina, Huevo",
                "Deliciosa tarta de jamón y queso.", ".");
        Receta receta2 = new Receta("Ensalada Cesar", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/cesar.jpg", "Lechuga, Pollo, Croutones, Queso",
                "Fresca ensalada con aderezo cesar.", ".");

        this.repositorioReceta.guardar(receta1);
        this.repositorioReceta.guardar(receta2);

        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        ArrayList<Receta> recetas = (ArrayList<Receta>) query.getResultList();

        assertThat(recetas.size(), equalTo(2));
        assertThat(recetas.get(0), equalTo(receta1));
        assertThat(recetas.get(1), equalTo(receta2));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnaRecetaCuandoLaEliminoEntoncesYaNoSeEncuentraEnLaBaseDeDatos(){
        Receta receta = new Receta("Empanadas de carne", TiempoDePreparacion.UNA_HORA, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/empanada.jpg", "Carne, Masa de empanada, Cebolla, Pimentón",
                "Empanadas caseras de carne.", ".");

        this.repositorioReceta.guardar(receta);
        this.repositorioReceta.eliminar(receta);

        this.repositorioReceta.getRecetaPorId(receta.getId());

        assertEquals(0, this.repositorioReceta.getRecetas().size());
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueNoExistenRecetasCuandoConsultoEntoncesObtengoUnaListaVacia(){
        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        ArrayList<Receta> recetas = (ArrayList<Receta>) query.getResultList();

        assertThat(recetas.size(), equalTo(0));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnaRecetaCuandoLaModificoEntoncesLosCambiosSeReflejanEnLaBaseDeDatos() {
        Receta receta = new Receta("Pizza Margarita", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/pizza.jpg", "Harina, Queso, Tomate, Albahaca",
                "Pizza clásica italiana", ".");

        this.repositorioReceta.guardar(receta);

        receta.setTitulo("Pizza Napolitana");
        receta.setIngredientes("Harina, Queso, Tomate, Anchoas");
        receta.setDescripcion("Pizza napolitana con anchoas");

        this.repositorioReceta.actualizar(receta);

        Receta recetaModificada = this.repositorioReceta.getRecetaPorId(receta.getId());

        assertThat(recetaModificada.getTitulo(), equalTo("Pizza Napolitana"));
        assertThat(recetaModificada.getIngredientes(), equalTo("Harina, Queso, Tomate, Anchoas"));
        assertThat(recetaModificada.getDescripcion(), equalTo("Pizza napolitana con anchoas"));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenDosRecetasCuandoBuscoPorCategoriaEntoncesObtengoSoloLasRecetasDeEsaCategoria() {
        Receta receta1 = new Receta("Pasta Carbonara", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/pasta.jpg", "Pasta, Huevo, Queso", "Deliciosa pasta carbonara.", ".");
        Receta receta2 = new Receta("Tarta de frutilla", TiempoDePreparacion.TREINTA_MIN, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/tarta-frutilla.jpg", "Frutilla, Masa, Crema", "Tarta de frutilla casera.", ".");

        this.repositorioReceta.guardar(receta1);
        this.repositorioReceta.guardar(receta2);

        List<Receta> recetas = this.repositorioReceta.getRecetasPorCategoria(Categoria.ALMUERZO_CENA);

        assertThat(recetas.size(), equalTo(1));
        assertThat(recetas.get(0), equalTo(receta1));
        assertThat(recetas.get(0).getCategoria(), equalTo(Categoria.ALMUERZO_CENA));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenDosRecetasCuandoBuscoPorTiempoDePreparacionEntoncesObtengoSoloLasQueCoincidenConElTiempo() {
        Receta receta1 = new Receta("Sopa de verduras", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/sopa.jpg", "Zanahoria, Papa, Apio", "Sopa saludable.", ".");
        Receta receta2 = new Receta("Budín de vainilla", TiempoDePreparacion.UNA_HORA, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/budin.jpg", "Harina, Azúcar, Huevo", "Budín casero.", ".");

        this.repositorioReceta.guardar(receta1);
        this.repositorioReceta.guardar(receta2);

        List<Receta> recetas = this.repositorioReceta.getRecetasPorTiempoDePreparacion(TiempoDePreparacion.VEINTE_MIN);

        assertThat(recetas.size(), equalTo(1));
        assertThat(recetas.get(0), equalTo(receta1));
        assertThat(recetas.get(0).getTiempo_preparacion(), equalTo(TiempoDePreparacion.VEINTE_MIN));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenRecetasCuandoBuscoPorCategoriaYTiempoDePreparacionEntoncesObtengoSoloLasCoincidentes() {
        Receta receta1 = new Receta("Tortilla de papa", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/tortilla.jpg", "Papa, Huevo, Cebolla", "Clásica tortilla de papa.", ".");
        Receta receta2 = new Receta("Helado de chocolate", TiempoDePreparacion.TREINTA_MIN, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/helado.jpg", "Chocolate, Crema, Azúcar", "Helado casero.", ".");

        this.repositorioReceta.guardar(receta1);
        this.repositorioReceta.guardar(receta2);

        List<Receta> recetas = this.repositorioReceta.getRecetasPorCategoriaYTiempoDePreparacion(Categoria.ALMUERZO_CENA, TiempoDePreparacion.TREINTA_MIN);

        assertThat(recetas.size(), equalTo(1));
        assertThat(recetas.get(0), equalTo(receta1));
        assertThat(recetas.get(0).getCategoria(), equalTo(Categoria.ALMUERZO_CENA));
        assertThat(recetas.get(0).getTiempo_preparacion(), equalTo(TiempoDePreparacion.TREINTA_MIN));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenRecetasCuandoBuscoPorTituloEntoncesObtengoRecetasCuyoTituloCoincideParcialmente() {
        Receta receta1 = new Receta("Tarta de manzana", TiempoDePreparacion.TREINTA_MIN, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/tarta-manzana.jpg", "Manzana, Harina, Azúcar", "Tarta clásica de manzana.", ".");
        Receta receta2 = new Receta("Carne al horno con papas", TiempoDePreparacion.UNA_HORA, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/carne-papas.jpg", "Carne, Papas, Cebolla", "Condimentalo como vos quieras", ".");
        Receta receta3 = new Receta("Tarta de frutilla", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/tarta-frutilla.jpg", "Frutilla, Harina, Crema", "Tarta casera de frutilla.", ".");

        this.repositorioReceta.guardar(receta1);
        this.repositorioReceta.guardar(receta2);
        this.repositorioReceta.guardar(receta3);

        List<Receta> recetas = this.repositorioReceta.buscarRecetasPorTitulo("tarta");

        assertThat(recetas.size(), equalTo(2));
        assertThat(recetas.get(0).getTitulo().toLowerCase(), equalTo("tarta de manzana"));
        assertThat(recetas.get(1).getTitulo().toLowerCase(), equalTo("tarta de frutilla"));
    }
}


