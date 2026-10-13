package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.*;
import com.hotel.Hotel.dto.response.ReservaResponse;
import com.hotel.Hotel.exception.RecursoNoEncontradoException;
import com.hotel.Hotel.mapper.ReservaMapper;
import com.hotel.Hotel.repository.ClienteRepository;
import com.hotel.Hotel.repository.HabitacionRepository;
import com.hotel.Hotel.repository.ReservaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceOperacionesTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaService reservaService;

    @Nested
    @DisplayName("Operaciones de Cancelación")
    class CancelacionTests {

        @Test
        @DisplayName("Debe cancelar la reserva y liberar la habitación")
        void debeCancelarReservaYLiberarHabitacion() {
            // Arrange
            UUID reservaId = UUID.randomUUID();
            Cliente cliente = new Cliente("Sofía Ramírez", "sofia@correo.com");
            Habitacion habitacion = new HabitacionEstandar("202", 2, 150.0, 1);
            habitacion.asignarAReserva();
            assertEquals(EstadoHabitacion.OCUPADA, habitacion.getEstado());

            LocalDateTime inicio = LocalDateTime.now().plusDays(1);
            RangoFechas fechas = new RangoFechas(inicio, inicio.plusDays(2));
            Reserva reserva = new Reserva(cliente, habitacion, fechas);

            ReservaResponse responseEsperado = new ReservaResponse(
                    reservaId, "Sofía Ramírez", "202",
                    fechas.fechaInicio(), fechas.fechaFin(), 300.0, "CANCELADA");

            // Stubs
            when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
            when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reservaMapper.toResponse(any(Reserva.class))).thenReturn(responseEsperado);

            // Act
            ReservaResponse resultado = reservaService.cancelar(reservaId);

            // Assert
            assertNotNull(resultado);
            assertEquals("CANCELADA", resultado.estado());
            assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
            assertEquals(EstadoHabitacion.DISPONIBLE, habitacion.getEstado());

            verify(reservaRepository, times(1)).save(reserva);
            verify(reservaMapper, times(1)).toResponse(reserva);
        }
    }

    @Nested
    @DisplayName("Reglas de Confirmación y Consulta")
    class ReglasTests {

        @Test
        @DisplayName("No debe confirmar una reserva cuando la habitación ya está ocupada")
        void noDebeConfirmarSiLaHabitacionYaEstaOcupada() {
            // Arrange
            UUID reservaId = UUID.randomUUID();
            Habitacion habitacion = new HabitacionEstandar("303", 2, 120.0, 1);

            LocalDateTime inicio = LocalDateTime.now().plusDays(1);
            RangoFechas fechas = new RangoFechas(inicio, inicio.plusDays(2));

            // Primera reserva ya confirmada: deja la habitación OCUPADA
            Reserva primera = new Reserva(new Cliente("Pedro Díaz", "pedro@correo.com"), habitacion, fechas);
            primera.confirmar();

            // Segunda reserva pendiente sobre la misma habitación
            Reserva segunda = new Reserva(new Cliente("Lucía Vega", "lucia@correo.com"), habitacion, fechas);

            // Stubs
            when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(segunda));

            // Act & Assert
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> reservaService.confirmar(reservaId));
            assertTrue(ex.getMessage().contains("no está disponible"));
            assertEquals(EstadoReserva.PENDIENTE, segunda.getEstado());

            verify(reservaRepository, never()).save(any(Reserva.class));
            verifyNoInteractions(reservaMapper);
        }

        @Test
        @DisplayName("Debe lanzar RecursoNoEncontradoException cuando la reserva no existe")
        void debeFallarAlObtenerReservaInexistente() {
            // Arrange
            UUID reservaId = UUID.randomUUID();
            when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

            // Act & Assert
            RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class,
                    () -> reservaService.obtenerPorId(reservaId));
            assertTrue(ex.getMessage().contains("Reserva"));
            assertTrue(ex.getMessage().contains(reservaId.toString()));

            verify(reservaRepository, times(1)).findById(reservaId);
            verifyNoInteractions(reservaMapper);
        }
    }
}