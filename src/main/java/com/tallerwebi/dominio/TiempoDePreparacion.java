package com.tallerwebi.dominio;

public enum TiempoDePreparacion {
    DIEZ_MIN("10 minutos"),
    VEINTE_MIN("20 minutos"),
    TREINTA_MIN("30 minutos"),
    UNA_HORA("1 hora");

    private final String tiempo;

    TiempoDePreparacion(String tiempoATexto) {
        this.tiempo = tiempoATexto;
    }

    public String getTiempo(){
        return tiempo;
    }
}
