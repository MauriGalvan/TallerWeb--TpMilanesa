package com.tallerwebi.dominio;

public enum TiempoDePreparacion {
    DIEZ_MIN(10),
    VEINTE_MIN(20),
    TREINTA_MIN(30),
    UNA_HORA(60);

    private final int minutos;

    TiempoDePreparacion(int minutos) {
        this.minutos = minutos;
    }

    public int getMinutos() {
        return minutos;
    }

    @Override
    public String toString() {
        if (minutos < 60) {
            return minutos + " min";
        } else {
            return (minutos / 60) + " hr";
        }
    }
}
