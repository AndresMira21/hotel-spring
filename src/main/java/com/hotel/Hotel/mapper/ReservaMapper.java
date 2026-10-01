package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Reserva;
import com.hotel.Hotel.dto.response.ReservaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(target = "estado", expression = "java(reserva.getEstado().name())")
    @Mapping(target = "nombreHuesped", expression = "java(reserva.getCliente().getNombre())")
    @Mapping(target = "habitacionNumero", expression = "java(reserva.getHabitacion().getNumero())")
    @Mapping(target = "fechaInicio", expression = "java(reserva.getPeriodo().fechaInicio().toLocalDate())")
    @Mapping(target = "fechaFin", expression = "java(reserva.getPeriodo().fechaFin().toLocalDate())")
    ReservaResponse toResponse(Reserva reserva);

    List<ReservaResponse> toResponseList(List<Reserva> reservas);
}