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


}
