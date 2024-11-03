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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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

    private Receta recetaTartaJamonYQuesoDeVeinteMinCreada(){
        return new Receta("Tarta de jamón y queso", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/tarta.jpg", ".", "Deliciosa tarta de jamón y queso.", ".");
    }
    private Receta recetaMilanesaNapolitanaDeTreintaMinCreada(){
        return new Receta ("Milanesa napolitana", TiempoDePreparacion.TREINTA_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/7hbGvN2c/mila-napo.webp", ".", "Esto es una descripción de mila napo", ".");
    }
    private Receta recetaMilanesaConPapasDeVeinteMinCreada(){
        return new Receta ("Milanesa con papas fritas", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                "https://i.postimg.cc/mila-papas.jpg", ".", "Milanesa con guarnición de papas fritas", ".");
    }
    private Receta recetaCafeConLecheDeDiezMinCreada(){
        return new Receta ("Café cortado con tostadas", TiempoDePreparacion.DIEZ_MIN, Categoria.DESAYUNO_MERIENDA,
                "https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg", ".", "Un clásico de las mañanas.", ".");
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnRepositorioRecetaQueSePuedaGuardarUnaReceta(){
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();

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
        Receta receta1 = this.recetaTartaJamonYQuesoDeVeinteMinCreada();
        Receta receta2 = this.recetaMilanesaConPapasDeVeinteMinCreada();

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
    public void dadoQueExisteDosRecetasCuandoEliminoUnaEntoncesSoloHayUnaEnLaBaseDeDatos(){
        Receta receta1 = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta2 = this.recetaMilanesaConPapasDeVeinteMinCreada();

        this.sessionFactory.getCurrentSession().save(receta1);
        this.sessionFactory.getCurrentSession().save(receta2);

        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        ArrayList<Receta> recetas = (ArrayList<Receta>) query.getResultList();

        assertThat(recetas, hasItem(receta1));
        assertThat(recetas, hasItem(receta2));

        this.repositorioReceta.eliminar(receta1);

        String hql1 = "FROM Receta";
        Query query1 = this.sessionFactory.getCurrentSession().createQuery(hql1);
        ArrayList<Receta> recetas1 = (ArrayList<Receta>) query1.getResultList();

        assertThat(recetas1, hasItem(receta2));
        assertThat(recetas1, not(hasItem(receta1)));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueSeGuardanDosRecetasCuandoConsultoEntoncesObtengoLasDosRecetas(){
        Receta receta1 = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta2 = this.recetaMilanesaConPapasDeVeinteMinCreada();

        this.sessionFactory.getCurrentSession().save(receta1);
        this.sessionFactory.getCurrentSession().save(receta2);

        List<Receta> recetas = this.repositorioReceta.getRecetas();

        assertThat(recetas, hasItem(receta1));
        assertThat(recetas, hasItem(receta2));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueNoExistenRecetasCuandoConsultoEntoncesObtengoUnaListaVacia(){
        List<Receta> recetas = this.repositorioReceta.getRecetas();

        assertThat(recetas.size(), equalTo(0));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueSeGuardaUnaRecetaCuandoConsultoPorSuIdEntoncesObtengoLaReceta(){
        Receta receta = this.recetaMilanesaConPapasDeVeinteMinCreada();

        this.sessionFactory.getCurrentSession().save(receta);
        int idBuscado = receta.getId(); //iría después de que se guarde en la base, porque antes no lo inicializa

        Receta recetaBuscada = this.repositorioReceta.getRecetaPorId(idBuscado);

        assertEquals(receta, recetaBuscada);
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExisteUnaRecetaCuandoLaModificoEntoncesLosCambiosSeReflejanEnLaBaseDeDatos() {
        Receta receta = this.recetaMilanesaNapolitanaDeTreintaMinCreada();

        this.sessionFactory.getCurrentSession().save(receta);

        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        Receta recetaObtenida = (Receta)query.getSingleResult();

        assertEquals(receta, recetaObtenida);

        receta.setTitulo("Pizza Napolitana");
        receta.setIngredientes("Harina, Queso, Tomate, Anchoas");
        receta.setDescripcion("Pizza napolitana con anchoas");
        this.repositorioReceta.actualizar(receta);

        String hql1 = "FROM Receta";
        Query query1 = this.sessionFactory.getCurrentSession().createQuery(hql1);
        Receta recetaModificada = (Receta)query1.getSingleResult();

        assertThat(recetaModificada.getTitulo(), equalTo("Pizza Napolitana"));
        assertThat(recetaModificada.getIngredientes(), equalTo("Harina, Queso, Tomate, Anchoas"));
        assertThat(recetaModificada.getDescripcion(), equalTo("Pizza napolitana con anchoas"));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenDosRecetasCuandoBuscoPorCategoriaEntoncesObtengoSoloLasRecetasDeEsaCategoria() {
        Receta receta1 = this.recetaTartaJamonYQuesoDeVeinteMinCreada();
        Receta receta2 = this.recetaCafeConLecheDeDiezMinCreada();

        this.sessionFactory.getCurrentSession().save(receta1);
        this.sessionFactory.getCurrentSession().save(receta2);

        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        ArrayList<Receta> recetas = (ArrayList<Receta>) query.getResultList();

        assertThat(recetas, hasItem(receta1));
        assertThat(recetas, hasItem(receta2));

        List<Receta> recetasFiltradas = this.repositorioReceta.getRecetasPorCategoria(Categoria.ALMUERZO_CENA);

        assertThat(recetasFiltradas, hasItem(receta1));
        assertThat(recetasFiltradas, not(hasItem(receta2)));
        assertThat(recetasFiltradas.get(0).getCategoria(), equalTo(Categoria.ALMUERZO_CENA));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenDosRecetasCuandoBuscoPorTiempoDePreparacionEntoncesObtengoSoloLasQueCoincidenConElTiempo() {
        Receta receta1 = this.recetaTartaJamonYQuesoDeVeinteMinCreada();
        Receta receta2 = this.recetaCafeConLecheDeDiezMinCreada();

        this.sessionFactory.getCurrentSession().save(receta1);
        this.sessionFactory.getCurrentSession().save(receta2);

        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        ArrayList<Receta> recetas = (ArrayList<Receta>) query.getResultList();

        assertThat(recetas, hasItem(receta1));
        assertThat(recetas, hasItem(receta2));

        List<Receta> recetasFiltradas = this.repositorioReceta.getRecetasPorTiempoDePreparacion(TiempoDePreparacion.VEINTE_MIN);

        assertThat(recetasFiltradas, hasItem(receta1));
        assertThat(recetasFiltradas, not(hasItem(receta2)));
        assertThat(recetas.get(0).getTiempo_preparacion(), equalTo(TiempoDePreparacion.VEINTE_MIN));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenRecetasCuandoBuscoPorCategoriaYTiempoDePreparacionEntoncesObtengoSoloLasCoincidentes() {
        Receta receta1 = this.recetaTartaJamonYQuesoDeVeinteMinCreada();
        Receta receta2 = this.recetaCafeConLecheDeDiezMinCreada();

        this.sessionFactory.getCurrentSession().save(receta1);
        this.sessionFactory.getCurrentSession().save(receta2);

        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        ArrayList<Receta> recetas = (ArrayList<Receta>) query.getResultList();

        assertThat(recetas, hasItem(receta1));
        assertThat(recetas, hasItem(receta2));

        List<Receta> recetasFiltradas = this.repositorioReceta.getRecetasPorCategoriaYTiempoDePreparacion(Categoria.ALMUERZO_CENA, TiempoDePreparacion.VEINTE_MIN);

        assertThat(recetasFiltradas, hasItem(receta1));
        assertThat(recetasFiltradas, not(hasItem(receta2)));
        assertThat(recetasFiltradas.get(0).getCategoria(), equalTo(Categoria.ALMUERZO_CENA));
        assertThat(recetasFiltradas.get(0).getTiempo_preparacion(), equalTo(TiempoDePreparacion.VEINTE_MIN));
    }

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenRecetasCuandoBuscoPorTituloEntoncesObtengoRecetasCuyoTituloCoincideParcialmente() {
        Receta receta1 = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta2 = this.recetaCafeConLecheDeDiezMinCreada();
        Receta receta3 = this.recetaMilanesaConPapasDeVeinteMinCreada();

        this.sessionFactory.getCurrentSession().save(receta1);
        this.sessionFactory.getCurrentSession().save(receta2);
        this.sessionFactory.getCurrentSession().save(receta3);

        String hql = "FROM Receta";
        Query query = this.sessionFactory.getCurrentSession().createQuery(hql);
        ArrayList<Receta> recetas = (ArrayList<Receta>) query.getResultList();

        assertThat(recetas, hasItem(receta1));
        assertThat(recetas, hasItem(receta2));
        assertThat(recetas, hasItem(receta3));

        List<Receta> recetasFiltradas = this.repositorioReceta.buscarRecetasPorTitulo("milanesa");

        assertThat(recetasFiltradas.size(), equalTo(2));
        assertThat(recetasFiltradas, hasItem(receta1));
        assertThat(recetasFiltradas, hasItem(receta3));
        assertThat(recetasFiltradas, not(hasItem(receta2)));
        assertThat(recetasFiltradas, hasItem(hasProperty("titulo", equalTo("Milanesa napolitana"))));
        assertThat(recetasFiltradas, hasItem(hasProperty("titulo", equalTo("Milanesa con papas fritas"))));
    }

}


