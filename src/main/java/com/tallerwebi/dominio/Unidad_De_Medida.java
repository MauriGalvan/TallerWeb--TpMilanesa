package com.tallerwebi.dominio;

public enum Unidad_De_Medida {
    UNIDAD ("unidades"),
    MILILITROS ("ml"),
    GRAMOS ("g"),
    KILOGRAMOS ("kg");

    private final String abreviatura;

    Unidad_De_Medida(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getAbreviatura() {
        return abreviatura;
    }
}
