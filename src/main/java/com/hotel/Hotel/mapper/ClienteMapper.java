package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.domain.Reserva;
import com.hotel.Hotel.dto.request.CrearClienteRequest;
import com.hotel.Hotel.dto.response.ClienteResponse;
import com.hotel.Hotel.dto.response.ClienteResumenResponse;
import com.hotel.Hotel.dto.response.ReservaItemResponse;
import org.mapstruct.*;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponse toResponse(Cliente cliente);

    List<ClienteResponse> toResponseList(List<Cliente> clientes);

    default Cliente toEntity(CrearClienteRequest request) {
        return new Cliente(request.nombre(), request.email());
    }

    @Mapping(target = "email", source = "email")
    @Mapping(target = "totalReservasRealizadas", expression = "java(cliente.getReservas().size())")
    @Mapping(target = "montoTotalGastado", source = "reservas", qualifiedByName = "sumarMonto")
    @Mapping(target = "reservasRecientes", source = "reservas", qualifiedByName = "aItems")
    ClienteResumenResponse toResumen(Cliente cliente);

    @Mapping(target = "idReserva", source = "id")
    @Mapping(target = "numeroHabitacion", source = "habitacion.numero")
    @Mapping(target = "fechaInicio", source = "periodo.fechaInicio")
    @Mapping(target = "fechaFin", source = "periodo.fechaFin")
    @Mapping(target = "costoTotal", expression = "java(reserva.getCostoTotal())")
    @Mapping(target = "estado",
            expression = "java(reserva.getEstado() == null ? null : reserva.getEstado().name())")
    ReservaItemResponse toItem(Reserva reserva);

    @Named("sumarMonto")
    default double sumarMonto(List<Reserva> reservas) {
        return reservas.stream().mapToDouble(Reserva::getCostoTotal).sum();
    }

    @Named("aItems")
    default List<ReservaItemResponse> aItems(List<Reserva> reservas) {
        return reservas.stream()
                .sorted(Comparator.comparing((Reserva r) -> r.getPeriodo().fechaInicio()).reversed())
                .map(this::toItem)
                .toList();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "penalizaciones", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    void updateClienteFromDto(CrearClienteRequest dto, @MappingTarget Cliente entity);

}
