package com.hotel.Hotel.controller;

import com.hotel.Hotel.dto.request.CrearHabitacionEstandarRequest;
import com.hotel.Hotel.dto.request.CrearSuitePresidencialRequest;
import com.hotel.Hotel.dto.response.HabitacionResponse;
import com.hotel.Hotel.service.HabitacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @PostMapping("/estandar")
    public ResponseEntity<Map<String, UUID>> crearEstandar(
            @RequestBody CrearHabitacionEstandarRequest request,
            UriComponentsBuilder uriBuilder) {
        UUID id = habitacionService.crearEstandar(request);
        URI uri = uriBuilder.path("/api/habitaciones/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(Map.of("id", id));
    }

    @PostMapping("/suites")
    public ResponseEntity<Map<String, UUID>> crearSuite(
            @RequestBody CrearSuitePresidencialRequest request,
            UriComponentsBuilder uriBuilder) {
        UUID id = habitacionService.crearSuite(request);
        URI uri = uriBuilder.path("/api/habitaciones/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(Map.of("id", id));
    }

    @GetMapping
    public ResponseEntity<List<HabitacionResponse>> listar() {
        return ResponseEntity.ok(habitacionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(habitacionService.obtenerPorId(id));
    }
}