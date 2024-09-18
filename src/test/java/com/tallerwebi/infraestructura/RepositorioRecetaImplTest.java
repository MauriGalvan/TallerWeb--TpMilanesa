package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Receta;
import com.tallerwebi.dominio.RepositorioReceta;
import com.tallerwebi.infraestructura.config.HibernateInfraestructuraTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        double tiempo_preparacion = 1.0;
        String categoria = "almuerzo";
        String imagen = "https://i.postimg.cc/7hbGvN2c/mila-napo.webp";
        String ingredientes = "Jamón, Queso, Tapa pascualina, Huevo, Tomate";
        String descripcion = "Esto es una descripción de mila napo";

        Receta receta = new Receta(titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion);

        this.repositorioReceta.guardar(receta);

        String hql = "FROM Receta WHERE titulo = :titulo";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("titulo", "Milanesa napolitana");
        Receta recetaObtenida = (Receta)query.getSingleResult();

        assertEquals(receta, recetaObtenida);

    }

    @Test
    @Transactional
    public void dadoQueExisteUnRepositorioRecetaCuandoGuardoVariasRecetasEntoncesLasEncuentroEnLaBaseDeDatos(){
        Receta receta1 = new Receta("Tarta de jamón y queso", 1.5, "almuerzo",
                "https://i.postimg.cc/tarta.jpg", "Jamón, Queso, Tapa pascualina, Huevo",
                "Deliciosa tarta de jamón y queso.");
        Receta receta2 = new Receta("Ensalada Cesar", 0.5, "cena",
                "https://i.postimg.cc/cesar.jpg", "Lechuga, Pollo, Croutones, Queso",
                "Fresca ensalada con aderezo cesar.");

        this.repositorioReceta.guardar(receta1);
        this.repositorioReceta.guardar(receta2);

        String hql = "FROM Receta WHERE titulo IN (:titulos)";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("titulos", Arrays.asList("Tarta de jamón y queso", "Ensalada Cesar"));
        ArrayList<Receta> recetas = (ArrayList<Receta>) query.getResultList();

        assertThat(recetas.size(), equalTo(2));
        assertThat(recetas.get(0), equalTo(receta1));
        assertThat(recetas.get(1), equalTo(receta2));
    }

    @Test
    @Transactional
    public void dadoQueExisteUnaRecetaCuandoLaEliminoEntoncesYaNoSeEncuentraEnLaBaseDeDatos(){
        Receta receta = new Receta("Empanadas de carne", 1.0, "almuerzo",
                "https://i.postimg.cc/empanada.jpg", "Carne, Masa de empanada, Cebolla, Pimentón",
                "Empanadas caseras de carne.");

        this.repositorioReceta.guardar(receta);
        this.repositorioReceta.eliminar(receta);

        String hql = "FROM Receta WHERE titulo = :titulo";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("titulo", "Empanadas de carne");

        assertThat(query.getResultList().size(), equalTo(0));
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
    public void dadoQueExisteUnRepositorioRecetaCuandoConsultoPorRecetasPorCategoriaEntoncesObtengoLasRecetasCorrectas(){
        Receta receta1 = new Receta("Bife con ensalada", 0.8, "almuerzo",
                "https://i.postimg.cc/bife.jpg", "Bife de chorizo, Ensalada",
                "Bife jugoso acompañado de ensalada.");
        Receta receta2 = new Receta("Sopa de calabaza", 0.6, "cena",
                "https://i.postimg.cc/sopa.jpg", "Calabaza, Cebolla, Ajo",
                "Sopa cremosa de calabaza.");

        this.repositorioReceta.guardar(receta1);
        this.repositorioReceta.guardar(receta2);

        String hql = "FROM Receta WHERE categoria = :categoria";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("categoria", "cena");
        ArrayList<Receta> recetasCena = (ArrayList<Receta>) query.getResultList();

        assertThat(recetasCena.size(), equalTo(1));
        assertThat(recetasCena.get(0), equalTo(receta2));
    }

    @Test
    @Transactional
    public void dadoQueUnaRecetaNoExisteCuandoIntentoEliminarlaEntoncesNoSeHaceNada(){
        Receta recetaInexistente = new Receta("Sándwich de miga", 0.2, "almuerzo",
                "https://i.postimg.cc/sandwich.jpg", "Pan de miga, Jamón, Queso",
                "Delicioso sándwich de miga.");

        repositorioReceta.eliminar(recetaInexistente);

        String hql = "FROM Receta WHERE titulo = :titulo";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("titulo", "Sándwich de miga");
        assertThat(query.getResultList().size(), equalTo(0));
    }


    @Test
    @Transactional
    public void dadoQueUnaRecetaEsNullCuandoIntentoGuardarlaEntoncesSeLanzaNullPointerException(){
        Receta recetaNula = null;

        assertThrows(NullPointerException.class, () -> {
            repositorioReceta.guardar(recetaNula);
        });
    }


    @Test
    @Transactional
    public void dadoQueUnaRecetaNoExisteCuandoLaBuscoPorTituloEntoncesNoSeEncuentra(){
        String tituloInexistente = "Receta inexistente";

        String hql = "FROM Receta WHERE titulo = :titulo";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        query.setParameter("titulo", tituloInexistente);

        assertThat(query.getResultList().size(), equalTo(0));
    }


}


