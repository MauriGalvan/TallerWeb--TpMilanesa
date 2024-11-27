package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioIngredienteImpl implements ServicioIngrediente{

    private RepositorioIngrediente repositorioIngrediente;

    @Autowired
    public ServicioIngredienteImpl(RepositorioIngrediente repositorioIngrediente) {this.repositorioIngrediente = repositorioIngrediente;}

    @Transactional
    @Override
    public Ingrediente getIngredientePorId(int ingredienteId) {
        return repositorioIngrediente.getIngredientePorId(ingredienteId);
    }
}
