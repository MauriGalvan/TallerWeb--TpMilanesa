package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ServicioRecetaImpl implements ServicioReceta {

    private final RepositorioReceta repositorioReceta;

    @Autowired
    public ServicioRecetaImpl(RepositorioReceta repositorioReceta) {
        this.repositorioReceta = repositorioReceta;
    }

    @Override
    public List<Receta> getTodasLasRecetas() {
        return this.ordenarPorPopularidad(repositorioReceta.getRecetas());
    }

    @Override
    public void guardarReceta(Receta receta) {
        this.repositorioReceta.guardar(receta);
    }

    @Override
    @Transactional
    public List<Receta> getRecetasPorCategoria(Categoria categoria) {
        return this.ordenarPorPopularidad(repositorioReceta.getRecetasPorCategoria(categoria));
    }

    @Override
    public List<Receta> getRecetasPorTiempoDePreparacion(TiempoDePreparacion tiempoPreparacion) {
        return this.ordenarPorPopularidad(repositorioReceta.getRecetasPorTiempoDePreparacion(tiempoPreparacion));
    }

    @Override
    public List<Receta> getRecetasPorCategoriaYTiempoDePreparacion(Categoria categoria, TiempoDePreparacion tiempoPreparacion) {
        return this.ordenarPorPopularidad(repositorioReceta.getRecetasPorCategoriaYTiempoDePreparacion(categoria, tiempoPreparacion));
    }

    @Override
    public Receta getUnaRecetaPorId(int id) {
        return this.repositorioReceta.getRecetaPorId(id);
    }

    @Transactional
    @Override
    public void eliminarReceta(Receta receta) {
        this.repositorioReceta.eliminar(receta);
    }

    @Transactional
    @Override
    public void actualizarReceta(Receta receta) {
        Receta recetaExistente = repositorioReceta.getRecetaPorId(receta.getId());
        if (recetaExistente != null) {
            recetaExistente.setTitulo(receta.getTitulo());
            recetaExistente.setTiempo_preparacion(receta.getTiempo_preparacion());
            recetaExistente.setIngredientes(receta.getIngredientes());
            recetaExistente.setPasos(receta.getPasos());
            recetaExistente.setImagen(receta.getImagen());
            repositorioReceta.actualizar(recetaExistente);
        }
    }



    @Override
    public List<Receta> buscarRecetasPorTitulo(String titulo) {
        return this.ordenarPorPopularidad(repositorioReceta.buscarRecetasPorTitulo(titulo));
    }

    @Transactional
    @Override
    public void actualizarClicksDeReceta(Receta receta) {
        receta.incrementarClicks();
        repositorioReceta.actualizar(receta);
    }

    private List<Receta> ordenarPorPopularidad(List<Receta> recetas) {
        Collections.shuffle(recetas); //mezcla la lista en orden aleatorio

        recetas.sort(Comparator.comparingInt(Receta::getContadorClicks).reversed() //ordena de mayor a menor por clicks
                .thenComparing(this::ordenarAleatoriamenteSiCoincidenClicks)); //otro criterio de ordenamiento si coinciden
        return recetas;
    }
    private int ordenarAleatoriamenteSiCoincidenClicks(Receta receta1, Receta receta2) {
        int random1 = (int) (Math.random() * Integer.MAX_VALUE);
        int random2 = (int) (Math.random() * Integer.MAX_VALUE);
        return Integer.compare(random1, random2);
    }

    @Override
    public List<Receta> buscarRecetasPorTituloCategoriaYTiempo(String titulo, Categoria categoriaEnum, TiempoDePreparacion tiempoEnum) {
        return repositorioReceta.buscarRecetasPorTituloCategoriaYTiempo(titulo, categoriaEnum, tiempoEnum);
    }

    @Override
    public List<Receta> buscarRecetasPorTituloYCategoria(String titulo, Categoria categoriaEnum) {
        return repositorioReceta.buscarRecetasPorTituloYCategoria(titulo, categoriaEnum);
    }

    @Override
    public List<Receta> buscarRecetasPorTituloYTiempo(String titulo, TiempoDePreparacion tiempoEnum) {
        return repositorioReceta.buscarRecetasPorTituloYTiempo(titulo, tiempoEnum);
    }

}
