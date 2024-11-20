package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
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
import java.util.Arrays;
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

    private List<Ingrediente> unosIngredientes(){
        return Arrays.asList(
                new Ingrediente("Carne", 1, Unidad_De_Medida.KILOGRAMOS, Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Huevo", 2, Unidad_De_Medida.UNIDAD,Tipo_Ingrediente.PROTEINA_ANIMAL),
                new Ingrediente("Papas", 10, Unidad_De_Medida.UNIDAD, Tipo_Ingrediente.VERDURA),
                new Ingrediente("Pan rallado", 200, Unidad_De_Medida.GRAMOS, Tipo_Ingrediente.CEREAL_O_GRANO)
        );
    }
    private Receta recetaTartaJamonYQuesoDeVeinteMinCreada(){
        byte[] imagen = new byte[]{0, 1};
        return new Receta("Tarta de jamón y queso", TiempoDePreparacion.VEINTE_MIN, Categoria.ALMUERZO_CENA,
                imagen, this.unosIngredientes(), "Deliciosa tarta de jamón y queso.", ".");
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
    @Rollback
    @Transactional
    public void cuandoGuardoUnaRecetaEntoncesLoEncuentroEnLaBaseDeDatos(){
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
    public void cuandoGuardoVariasRecetasEntoncesLasEncuentroEnLaBaseDeDatos(){
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
        int idBuscado = receta.getId();

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

        Ingrediente ingrediente1 = receta.getIngredientes().get(0);
        Ingrediente ingrediente2 = receta.getIngredientes().get(1);
        Ingrediente ingrediente3 = receta.getIngredientes().get(2);
        Ingrediente ingrediente4 = receta.getIngredientes().get(3);

        receta.setTitulo("Pizza Napolitana");
        ingrediente1.setNombre("Masa para pizza");
        ingrediente1.setCantidad(1);
        ingrediente1.setUnidad_de_medida(Unidad_De_Medida.UNIDAD);
        ingrediente1.setTipo(Tipo_Ingrediente.CEREAL_O_GRANO);
        ingrediente2.setNombre("Salsa de tomate");
        ingrediente2.setCantidad(150);
        ingrediente2.setUnidad_de_medida(Unidad_De_Medida.MILILITROS);
        ingrediente2.setTipo(Tipo_Ingrediente.VERDURA);
        ingrediente3.setNombre("Mozzarella");
        ingrediente3.setCantidad(200);
        ingrediente3.setUnidad_de_medida(Unidad_De_Medida.GRAMOS);
        ingrediente3.setTipo(Tipo_Ingrediente.LACTEO);
        ingrediente4.setNombre("Albahaca fresca");
        ingrediente4.setCantidad(10);
        ingrediente4.setUnidad_de_medida(Unidad_De_Medida.UNIDAD);
        ingrediente4.setTipo(Tipo_Ingrediente.ESPECIA);
        receta.setDescripcion("Pizza napolitana con anchoas");
        this.repositorioReceta.actualizar(receta);

        String hql1 = "FROM Receta";
        Query query1 = this.sessionFactory.getCurrentSession().createQuery(hql1);
        Receta recetaModificada = (Receta)query1.getSingleResult();

        assertThat(recetaModificada.getTitulo(), equalTo("Pizza Napolitana"));
        assertThat(recetaModificada.getDescripcion(), equalTo("Pizza napolitana con anchoas"));

        Ingrediente ingredienteModificado1 = recetaModificada.getIngredientes().get(0);
        Ingrediente ingredienteModificado2 = recetaModificada.getIngredientes().get(1);
        Ingrediente ingredienteModificado3 = recetaModificada.getIngredientes().get(2);
        Ingrediente ingredienteModificado4 = recetaModificada.getIngredientes().get(3);

        assertThat(ingredienteModificado1.getNombre(), equalTo("Masa para pizza"));
        assertThat(ingredienteModificado1.getCantidad(), equalTo(1.0));
        assertThat(ingredienteModificado1.getUnidad_de_medida(), equalTo(Unidad_De_Medida.UNIDAD));
        assertThat(ingredienteModificado1.getTipo(), equalTo(Tipo_Ingrediente.CEREAL_O_GRANO));

        assertThat(ingredienteModificado2.getNombre(), equalTo("Salsa de tomate"));
        assertThat(ingredienteModificado2.getCantidad(), equalTo(150.0));
        assertThat(ingredienteModificado2.getUnidad_de_medida(), equalTo(Unidad_De_Medida.MILILITROS));
        assertThat(ingredienteModificado2.getTipo(), equalTo(Tipo_Ingrediente.VERDURA));

        assertThat(ingredienteModificado3.getNombre(), equalTo("Mozzarella"));
        assertThat(ingredienteModificado3.getCantidad(), equalTo(200.0));
        assertThat(ingredienteModificado3.getUnidad_de_medida(), equalTo(Unidad_De_Medida.GRAMOS));
        assertThat(ingredienteModificado3.getTipo(), equalTo(Tipo_Ingrediente.LACTEO));

        assertThat(ingredienteModificado4.getNombre(), equalTo("Albahaca fresca"));
        assertThat(ingredienteModificado4.getCantidad(), equalTo(10.0));
        assertThat(ingredienteModificado4.getUnidad_de_medida(), equalTo(Unidad_De_Medida.UNIDAD));
        assertThat(ingredienteModificado4.getTipo(), equalTo(Tipo_Ingrediente.ESPECIA));
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

    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenRecetasCuandoBuscoPorTituloYCategoriaEntoncesObtengoRecetasCuyoTituloYCategoriaCoinciden() {
        Receta receta1 = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta2 = this.recetaCafeConLecheDeDiezMinCreada();
        receta2.setTitulo("Milanesa de desayuno"); // ahora es Milanesa de desayuno de 10 min
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

        List<Receta> recetasFiltradas = this.repositorioReceta.buscarRecetasPorTituloYCategoria("milanesa", Categoria.ALMUERZO_CENA);

        assertThat(recetasFiltradas.size(), equalTo(2));
        assertThat(recetasFiltradas, hasItem(receta1));
        assertThat(recetasFiltradas, hasItem(receta3));
        assertThat(recetasFiltradas, not(hasItem(receta2)));
        assertThat(recetasFiltradas, hasItem(hasProperty("titulo", equalTo("Milanesa napolitana"))));
        assertThat(recetasFiltradas, hasItem(hasProperty("titulo", equalTo("Milanesa con papas fritas"))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("titulo", equalTo("Milanesa de desayuno")))));
        assertThat(recetasFiltradas, hasItem(hasProperty("categoria", equalTo(Categoria.ALMUERZO_CENA))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("categoria", equalTo(Categoria.DESAYUNO_MERIENDA)))));
    }
    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenRecetasCuandoBuscoPorTituloYTiempoEntoncesObtengoRecetasCuyoTituloYTiempoCoinciden() {
        Receta receta1 = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta2 = this.recetaCafeConLecheDeDiezMinCreada();
        receta2.setTitulo("Milanesa de desayuno"); // ahora es Milanesa de desayuno de 10 min
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

        List<Receta> recetasFiltradas = this.repositorioReceta.buscarRecetasPorTituloYTiempo("milanesa", TiempoDePreparacion.TREINTA_MIN);

        assertThat(recetasFiltradas.size(), equalTo(1));
        assertThat(recetasFiltradas, hasItem(receta1));
        assertThat(recetasFiltradas, not(hasItem(receta3)));
        assertThat(recetasFiltradas, not(hasItem(receta2)));
        assertThat(recetasFiltradas, hasItem(hasProperty("titulo", equalTo("Milanesa napolitana"))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("titulo", equalTo("Milanesa con papas fritas")))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("titulo", equalTo("Milanesa de desayuno")))));
        assertThat(recetasFiltradas, hasItem(hasProperty("tiempo_preparacion", equalTo(TiempoDePreparacion.TREINTA_MIN))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("tiempo_preparacion", equalTo(TiempoDePreparacion.VEINTE_MIN)))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("tiempo_preparacion", equalTo(TiempoDePreparacion.DIEZ_MIN)))));
    }
    @Test
    @Rollback
    @Transactional
    public void dadoQueExistenRecetasCuandoBuscoPorTituloTiempoYCategoriaEntoncesObtengoRecetasCuyoTituloTiempoYCategoriaCoinciden() {
        Receta receta1 = this.recetaMilanesaNapolitanaDeTreintaMinCreada();
        Receta receta2 = this.recetaCafeConLecheDeDiezMinCreada();
        receta2.setTitulo("Milanesa de desayuno"); // ahora es Milanesa de desayuno de 10 min
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

        List<Receta> recetasFiltradas = this.repositorioReceta.buscarRecetasPorTituloCategoriaYTiempo("milanesa", Categoria.ALMUERZO_CENA, TiempoDePreparacion.VEINTE_MIN);

        assertThat(recetasFiltradas.size(), equalTo(1));
        assertThat(recetasFiltradas, hasItem(receta3));
        assertThat(recetasFiltradas, not(hasItem(receta1)));
        assertThat(recetasFiltradas, not(hasItem(receta2)));
        assertThat(recetasFiltradas, hasItem(hasProperty("titulo", equalTo("Milanesa con papas fritas"))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("titulo", equalTo("Milanesa napolitana")))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("titulo", equalTo("Milanesa de desayuno")))));
        assertThat(recetasFiltradas, hasItem(hasProperty("tiempo_preparacion", equalTo(TiempoDePreparacion.VEINTE_MIN))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("tiempo_preparacion", equalTo(TiempoDePreparacion.TREINTA_MIN)))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("tiempo_preparacion", equalTo(TiempoDePreparacion.DIEZ_MIN)))));
        assertThat(recetasFiltradas, hasItem(hasProperty("categoria", equalTo(Categoria.ALMUERZO_CENA))));
        assertThat(recetasFiltradas, not(hasItem(hasProperty("categoria", equalTo(Categoria.DESAYUNO_MERIENDA)))));
    }



}


