package com.universidad.controller;

import com.universidad.dto.MateriaDTO;
import com.universidad.service.IMateriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/materias")
@Tag(name = "Materias", description = "Operaciones para gestionar materias y asignar docentes")
public class MateriaController {

    private final IMateriaService materiaService;

    @Autowired
    public MateriaController(IMateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @Operation(summary = "Obtener todas las materias registradas")
    @GetMapping
    public ResponseEntity<List<MateriaDTO>> obtenerTodasLasMaterias() {
        return ResponseEntity.ok(materiaService.obtenerTodasLasMaterias());
    }

    @Operation(summary = "Obtener una materia por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MateriaDTO> obtenerMateriaPorId(@PathVariable Long id) {
        MateriaDTO materia = materiaService.obtenerMateriaPorId(id);
        return materia != null ? ResponseEntity.ok(materia) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Obtener una materia por su código único")
    @GetMapping("/codigo/{codigoUnico}")
    public ResponseEntity<MateriaDTO> obtenerMateriaPorCodigoUnico(@PathVariable String codigoUnico) {
        MateriaDTO materia = materiaService.obtenerMateriaPorCodigoUnico(codigoUnico);
        return materia != null ? ResponseEntity.ok(materia) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear una nueva materia")
    @PostMapping
    public ResponseEntity<MateriaDTO> crearMateria(@RequestBody @Valid MateriaDTO materia) {
        MateriaDTO nueva = materiaService.crearMateria(materia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Actualizar los datos de una materia existente")
    @PutMapping("/{id}")
    public ResponseEntity<MateriaDTO> actualizarMateria(@PathVariable Long id, @RequestBody @Valid MateriaDTO materia) {
        MateriaDTO actualizada = materiaService.actualizarMateria(id, materia);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Eliminar una materia por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMateria(@PathVariable Long id) {
        materiaService.eliminarMateria(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar si agregar un prerequisito formaría un ciclo")
    @GetMapping("/formaria-circulo/{materiaId}/{prerequisitoId}")
    @Transactional
    public ResponseEntity<Boolean> formariaCirculo(@PathVariable Long materiaId, @PathVariable Long prerequisitoId) {
        MateriaDTO materiaDTO = materiaService.obtenerMateriaPorId(materiaId);
        if (materiaDTO == null) {
            return ResponseEntity.notFound().build();
        }
        boolean circulo = new com.universidad.model.Materia(
            materiaDTO.getId(), 
            materiaDTO.getNombreMateria(), 
            materiaDTO.getCodigoUnico()
        ).formariaCirculo(prerequisitoId);
        return circulo ? ResponseEntity.badRequest().body(true) : ResponseEntity.ok(false);
    }
}
