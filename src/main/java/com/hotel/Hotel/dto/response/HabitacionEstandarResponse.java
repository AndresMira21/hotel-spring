package com.hotel.Hotel.dto.response;

import java.util.UUID;

public class HabitacionEstandarResponse extends HabitacionResponse {

    private final int camasIndividuales;

    public HabitacionEstandarResponse(
            UUID id,
            String numero,
            double precioPorNoche,
            int capacidadMaxima,
            int camasIndividuales
    ) {
        super(id, numero, precioPorNoche, capacidadMaxima);
        this.camasIndividuales = camasIndividuales;
    }

    @Override
    public String getTipo() {
        return "ESTANDAR";
    }

    public int getCamasIndividuales() {
        return camasIndividuales;
    }
}
