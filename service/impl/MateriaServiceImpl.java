package com.universidad.service.impl;

import com.universidad.model.Materia;
import com.universidad.model.Docente; // ✅ Indicación agregada
import com.universidad.repository.MateriaRepository;
import com.universidad.repository.DocenteRepository; // ✅ Indicación agregada
import com.universidad.service.IMateriaService;

import org.springframework.transaction.annotation.Transactional;


import com.universidad.dto.MateriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = false)
public class MateriaServiceImpl implements IMateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private DocenteRepository docenteRepository; // ✅ Indicación agregada

    // Método utilitario para mapear Materia a MateriaDTO
    private MateriaDTO mapToDTO(Materia materia) {
        if (materia == null) return null;
        return MateriaDTO.builder()
                .id(materia.getId())
                .nombreMateria(materia.getNombreMateria())
                .codigoUnico(materia.getCodigoUnico())
                .creditos(materia.getCreditos())
                .prerequisitos(materia.getPrerequisitos() != null ?
                    materia.getPrerequisitos().stream().map(Materia::getId).collect(Collectors.toList()) : null)
                .esPrerequisitoDe(materia.getEsPrerequisitoDe() != null ?
                    materia.getEsPrerequisitoDe().stream().map(Materia::getId).collect(Collectors.toList()) : null)
                .idDocente(materia.getDocente() != null ? materia.getDocente().getId() : null) // ✅ Indicación agregada
                .build();
    }

    @Override
    @Cacheable(value = "materias")
    public List<MateriaDTO> obtenerTodasLasMaterias() {
        return materiaRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "materia", key = "#id")
    public MateriaDTO obtenerMateriaPorId(Long id) {
        return materiaRepository.findById(id).map(this::mapToDTO).orElse(null);
    }

    @Override
    @Cacheable(value = "materia", key = "#codigoUnico")
    public MateriaDTO obtenerMateriaPorCodigoUnico(String codigoUnico) {
        Materia materia = materiaRepository.findByCodigoUnico(codigoUnico);
        return mapToDTO(materia);
    }

    @Override
    @CachePut(value = "materia", key = "#result.id")
    @CacheEvict(value = "materias", allEntries = true)
    @Transactional
    public MateriaDTO crearMateria(MateriaDTO materiaDTO) {
        Materia materia = new Materia();
        materia.setNombreMateria(materiaDTO.getNombreMateria());
        materia.setCodigoUnico(materiaDTO.getCodigoUnico());
        materia.setCreditos(materiaDTO.getCreditos());

        // ✅ Indicación agregada - Asignar docente si se proporciona un ID de docente
        if (materiaDTO.getIdDocente() != null) {
            Docente docente = docenteRepository.findById(materiaDTO.getIdDocente())
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));
            materia.setDocente(docente);
        }

        Materia savedMateria = materiaRepository.save(materia);
        return mapToDTO(savedMateria);
    }

    @Override
    @CachePut(value = "materia", key = "#id")
    @CacheEvict(value = "materias", allEntries = true)
    @Transactional
    public MateriaDTO actualizarMateria(Long id, MateriaDTO materiaDTO) {
        Materia materia = materiaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));

        materia.setNombreMateria(materiaDTO.getNombreMateria());
        materia.setCodigoUnico(materiaDTO.getCodigoUnico());
        materia.setCreditos(materiaDTO.getCreditos());

        // ✅ Indicación agregada - Actualizar docente si se proporciona un ID de docente
        if (materiaDTO.getIdDocente() != null) {
            Docente docente = docenteRepository.findById(materiaDTO.getIdDocente())
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));
            materia.setDocente(docente);
        }

        Materia updatedMateria = materiaRepository.save(materia);
        return mapToDTO(updatedMateria);
    }

    @Override
    @CacheEvict(value = {"materia", "materias"}, allEntries = true)
    @Transactional
    public void eliminarMateria(Long id) {
        materiaRepository.deleteById(id);
    }
}
