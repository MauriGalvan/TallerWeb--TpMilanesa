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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Transactional
    public void dadoQueNoExistenRecetasCuandoConsultoEntoncesObtengoUnaListaVacia(){
        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        ArrayList<Receta> recetas = (ArrayList<Receta>) query.getResultList();

        assertThat(recetas.size(), equalTo(0));
    }

    @Test
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


}


