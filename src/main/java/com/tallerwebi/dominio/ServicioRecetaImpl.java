package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
public class ServicioRecetaImpl implements ServicioReceta {

    private final RepositorioReceta repositorioReceta;
    private final RepositorioIngrediente repositorioIngrediente;

    @Autowired
    public ServicioRecetaImpl(RepositorioReceta repositorioReceta, RepositorioIngrediente repositorioIngrediente) {
        this.repositorioReceta = repositorioReceta;
        this.repositorioIngrediente = repositorioIngrediente;
    }

    @Override
    public List<Receta> getTodasLasRecetas() {
        return this.ordenarPorPopularidad(repositorioReceta.getRecetas());
    }

    @Override
    public void guardarReceta(Receta receta, MultipartFile imagen) {
        for (Ingrediente ingrediente : receta.getIngredientes()) {
            ingrediente.setReceta(receta);
        }
        if (imagen != null && !imagen.isEmpty()){
            try {
                receta.setImagen(imagen.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar la imagen", e);
            }
        }
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
        Receta receta = this.repositorioReceta.getRecetaPorId(id);

        if (receta != null && receta.getImagen() != null && receta.getImagen().length > 0) {
            String imagenBase64 = Base64.getEncoder().encodeToString(receta.getImagen());
            receta.setImagenBase64(imagenBase64);
        }

        return receta;
    }

    @Transactional
    @Override
    public void eliminarReceta(Receta receta) {
        Receta recetaExistente = repositorioReceta.getRecetaPorId(receta.getId());

        if (recetaExistente != null) {
            // Eliminar los ingredientes relacionados con la receta
            for (Ingrediente ingrediente : recetaExistente.getIngredientes()) {
                repositorioIngrediente.eliminar(ingrediente);
            }

            // Luego eliminar la receta en sí
            repositorioReceta.eliminar(recetaExistente);
        }
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

            // Crear un mapa de ingredientes existentes para búsqueda rápida
            Map<String, Ingrediente> ingredientesExistentes = new HashMap<>();
            for (Ingrediente ingredienteExistente : recetaExistente.getIngredientes()) {
                ingredientesExistentes.put(ingredienteExistente.getNombre(), ingredienteExistente);
            }

            // Lista para nuevos ingredientes que se agregarán a la receta
            List<Ingrediente> nuevosIngredientes = new ArrayList<>();

            for (Ingrediente ingredienteNuevo : receta.getIngredientes()) {
                if (ingredienteNuevo.getNombre() != null && !ingredienteNuevo.getNombre().isEmpty() &&
                        ingredienteNuevo.getCantidad() > 0.0 && ingredienteNuevo.getUnidad_de_medida() != null &&
                        ingredienteNuevo.getTipo() != null) {

                    Ingrediente ingredienteExistente = ingredientesExistentes.get(ingredienteNuevo.getNombre());

                    if (ingredienteExistente != null) {
                        // Actualizar los detalles del ingrediente existente
                        ingredienteExistente.setCantidad(ingredienteNuevo.getCantidad());
                        ingredienteExistente.setUnidad_de_medida(ingredienteNuevo.getUnidad_de_medida());
                        ingredienteExistente.setTipo(ingredienteNuevo.getTipo());
                        ingredienteExistente.setReceta(recetaExistente);  // Actualizar la referencia a la receta
                        ingredientesExistentes.remove(ingredienteNuevo.getNombre());
                    } else {
                        // Si el ingrediente no existe, agregarlo como nuevo
                        ingredienteNuevo.setReceta(recetaExistente);
                        nuevosIngredientes.add(ingredienteNuevo);
                    }
                }
            }

            // Eliminar los ingredientes que no están en la lista de nuevos ingredientes
            for (Ingrediente ingredienteRestante : ingredientesExistentes.values()) {
                repositorioIngrediente.eliminar(ingredienteRestante);
            }

            // Agregar los nuevos ingredientes
            for (Ingrediente ingredienteNuevo : nuevosIngredientes) {
                recetaExistente.getIngredientes().add(ingredienteNuevo);
                repositorioIngrediente.guardar(ingredienteNuevo);
            }

            // Guardar la receta actualizada en el repositorio
            repositorioReceta.actualizar(recetaExistente);
        }
    }



    @Override
    public List<Receta> buscarRecetasPorTitulo(String titulo) {
        return this.ordenarPorPopularidad(repositorioReceta.buscarRecetasPorTitulo(titulo));
    }

    @Transactional
    @Override
    public void actualizarVisitasDeReceta(Receta receta) {
        receta.incrementarVisitas();
        repositorioReceta.actualizar(receta);
    }

    private List<Receta> ordenarPorPopularidad(List<Receta> recetas) {
        Collections.shuffle(recetas); //mezcla la lista en orden aleatorio

        recetas.sort(Comparator.comparingInt(Receta::getContadorVisitas).reversed() //ordena de mayor a menor por clicks
                .thenComparing(this::ordenarAleatoriamenteSiCoincidenVisitas)); //otro criterio de ordenamiento si coinciden
        return recetas;
    }
    private int ordenarAleatoriamenteSiCoincidenVisitas(Receta receta1, Receta receta2) {
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

    @Transactional
    @Override
    public List<Ingrediente> getIngredientesDeRecetaPorId(int id) {
        return repositorioReceta.getIngredientesDeRecetaPorId(id);
    }
    @Override
    public List<Receta> buscarRecetaPorAutor(String autor) {
        return repositorioReceta.buscarPorAutor(autor);
    }
}
