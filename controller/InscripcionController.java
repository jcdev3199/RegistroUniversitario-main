package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.service.IInscripcionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inscripciones")
@Tag(name = "Inscripciones", description = "Gestión de inscripciones de estudiantes a materias")
public class InscripcionController {

    @Autowired
    private IInscripcionService inscripcionService;

    @Operation(summary = "Registrar una nueva inscripción")
    @PostMapping
    public ResponseEntity<InscripcionDTO> registrar(@RequestBody @Valid InscripcionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.crearInscripcion(dto));
    }

    @Operation(summary = "Listar todas las inscripciones registradas")
    @GetMapping
    public ResponseEntity<List<InscripcionDTO>> listar() {
        return ResponseEntity.ok(inscripcionService.obtenerTodas());
    }

    @Operation(summary = "Obtener una inscripción por ID")
    @GetMapping("/{id}")
    public ResponseEntity<InscripcionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inscripcionService.obtenerPorId(id));
    }

    @Operation(summary = "Actualizar los datos de una inscripción")
    @PutMapping("/{id}")
    public ResponseEntity<InscripcionDTO> actualizar(@PathVariable Long id, @RequestBody @Valid InscripcionDTO dto) {
        return ResponseEntity.ok(inscripcionService.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar una inscripción por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
