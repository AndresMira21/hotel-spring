package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.Habitacion;
import com.hotel.Hotel.domain.HabitacionEstandar;
import com.hotel.Hotel.dto.request.CrearHabitacionEstandarRequest;
import com.hotel.Hotel.mapper.HabitacionMapper;
import com.hotel.Hotel.repository.HabitacionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitacionServiceTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private HabitacionMapper habitacionMapper;

    @InjectMocks
    private HabitacionService habitacionService;

    @Test
    @DisplayName("Debe crear una habitación estándar y devolver el ID de la entidad guardada")
    void debeCrearHabitacionEstandarYRetornarId() {
        // Arrange
        CrearHabitacionEstandarRequest request = new CrearHabitacionEstandarRequest("401", 180.0, 3, 2);
        HabitacionEstandar entidad = new HabitacionEstandar("401", 3, 180.0, 2);

        // Stubs
        when(habitacionMapper.toEntity(request)).thenReturn(entidad);
        when(habitacionRepository.save(any(Habitacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UUID idGenerado = habitacionService.crearEstandar(request);

        // Assert
        assertNotNull(idGenerado);
        assertEquals(entidad.getId(), idGenerado);

        verify(habitacionMapper, times(1)).toEntity(request);
        verify(habitacionRepository, times(1)).save(entidad);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando la habitación no existe")
    void debeFallarAlObtenerHabitacionInexistente() {
        // Arrange
        UUID habitacionId = UUID.randomUUID();
        when(habitacionRepository.findById(habitacionId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> habitacionService.obtenerPorId(habitacionId));
        assertEquals("Habitación no encontrada", ex.getMessage());

        verify(habitacionRepository, times(1)).findById(habitacionId);
        verifyNoInteractions(habitacionMapper);
    }
}