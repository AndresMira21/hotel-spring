package com.hotel.Hotel.service;

import com.hotel.Hotel.mapper.HabitacionMapper;
import com.hotel.Hotel.dto.request.CrearSuitePresidencialRequest;
import com.hotel.Hotel.dto.request.CrearHabitacionEstandarRequest;
import com.hotel.Hotel.repository.HabitacionRepository;
import com.hotel.Hotel.dto.response.HabitacionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    public HabitacionService(HabitacionRepository habitacionRepository, HabitacionMapper habitacionMapper) {
        this.habitacionRepository = habitacionRepository;
        this.habitacionMapper = habitacionMapper;
    }

    @Transactional
    public UUID crearEstandar(CrearHabitacionEstandarRequest request) {
        return habitacionRepository.save(habitacionMapper.toEntity(request)).getId();
    }

    @Transactional
    public UUID crearSuite(CrearSuitePresidencialRequest request) {
        return habitacionRepository.save(habitacionMapper.toEntity(request)).getId();
    }

    public List<HabitacionResponse> listarTodos() {
        return habitacionRepository.findAll().stream()
                .map(habitacionMapper::toResponse)
                .toList();
    }

    public HabitacionResponse obtenerPorId(UUID id) {
        return habitacionMapper.toResponse(habitacionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada")));
    }
}
