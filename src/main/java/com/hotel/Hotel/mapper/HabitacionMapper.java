package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Habitacion;
import com.hotel.Hotel.domain.HabitacionEstandar;
import com.hotel.Hotel.domain.SuitePresidencial;
import com.hotel.Hotel.dto.request.CrearHabitacionEstandarRequest;
import com.hotel.Hotel.dto.response.HabitacionEstandarResponse;
import com.hotel.Hotel.dto.response.HabitacionResponse;
import com.hotel.Hotel.dto.request.CrearSuitePresidencialRequest;
import com.hotel.Hotel.dto.response.SuitePresidencialResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HabitacionMapper {

    default HabitacionResponse toResponse(Habitacion habitacion) {
        if (habitacion instanceof HabitacionEstandar estandar) {
            return toEstandarResponse(estandar);
        }
        if (habitacion instanceof SuitePresidencial suite) {
            return toSuiteResponse(suite);
        }
        throw new IllegalArgumentException("Tipo de habitación no soportado: " + habitacion.getClass().getName());
    }

    HabitacionEstandarResponse toEstandarResponse(HabitacionEstandar habitacion);
    SuitePresidencialResponse toSuiteResponse(SuitePresidencial suite);

    default HabitacionEstandar toEntity(CrearHabitacionEstandarRequest r) {
        return new HabitacionEstandar(r.numero(), r.capacidadMaxima(), r.precioPorNoche(), r.camasIndividuales());
    }

    default SuitePresidencial toEntity(CrearSuitePresidencialRequest r) {
        return new SuitePresidencial(r.numero(), r.capacidadMaxima(), r.precioPorNoche(),
                r.incluyeMayordomo(), r.jacuzziPrivado());
    }
}
