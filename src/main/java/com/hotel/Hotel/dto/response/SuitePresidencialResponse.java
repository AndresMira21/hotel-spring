package com.hotel.Hotel.dto.response;

import java.util.UUID;

public class SuitePresidencialResponse extends HabitacionResponse {

    private final boolean incluyeMayordomo;
    private final boolean jacuzziPrivado;

    public SuitePresidencialResponse(
            UUID id,
            String numero,
            double precioPorNoche,
            int capacidadMaxima,
            boolean incluyeMayordomo,
            boolean jacuzziPrivado
    ) {
        super(id, numero, precioPorNoche, capacidadMaxima);
        this.incluyeMayordomo = incluyeMayordomo;
        this.jacuzziPrivado = jacuzziPrivado;
    }

    @Override
    public String getTipo() {
        return "SUITE";
    }

    public boolean isIncluyeMayordomo() {
        return incluyeMayordomo;
    }

    public boolean isJacuzziPrivado() {
        return jacuzziPrivado;
    }
}
