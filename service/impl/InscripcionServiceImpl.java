package com.universidad.service.impl;

import com.universidad.model.Inscripcion;
import com.universidad.dto.InscripcionDTO;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.repository.EstudianteRepository;
import com.universidad.service.IInscripcionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class InscripcionServiceImpl implements IInscripcionService {

    @Autowired private InscripcionRepository inscripcionRepository;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private MateriaRepository materiaRepository;

    private InscripcionDTO mapToDTO(Inscripcion inscripcion) {
        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .idEstudiante(inscripcion.getEstudiante().getId())
                .idMateria(inscripcion.getMateria().getId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .estado(inscripcion.getEstado())
                .build();
    }

    @Cacheable(value = "inscripciones")
    @Override
    public List<InscripcionDTO> obtenerTodas() {
        return inscripcionRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Cacheable(value = "inscripcion", key = "#id")
    @Override
    public InscripcionDTO obtenerPorId(Long id) {
        return inscripcionRepository.findById(id).map(this::mapToDTO)
            .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));
    }

    @CachePut(value = "inscripcion", key = "#result.id")
    @CacheEvict(value = "inscripciones", allEntries = true)
    @Override
    public InscripcionDTO crearInscripcion(InscripcionDTO dto) {
        Inscripcion inscripcion = Inscripcion.builder()
                .estudiante(estudianteRepository.findById(dto.getIdEstudiante())
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado")))
                .materia(materiaRepository.findById(dto.getIdMateria())
                    .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada")))
                .fechaInscripcion(dto.getFechaInscripcion() != null ? dto.getFechaInscripcion() : LocalDate.now())
                .estado(dto.getEstado())
                .build();
        return mapToDTO(inscripcionRepository.save(inscripcion));
    }

    @CachePut(value = "inscripcion", key = "#id")
    @CacheEvict(value = "inscripciones", allEntries = true)
    @Override
    public InscripcionDTO actualizar(Long id, InscripcionDTO dto) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));
        if (dto.getEstado() != null) inscripcion.setEstado(dto.getEstado());
        return mapToDTO(inscripcionRepository.save(inscripcion));
    }

    @CacheEvict(value = { "inscripcion", "inscripciones" }, allEntries = true)
    @Override
    public void eliminar(Long id) {
        inscripcionRepository.deleteById(id);
    }
}
