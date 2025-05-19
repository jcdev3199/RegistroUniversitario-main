package com.universidad.service;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Inscripcion;
import java.util.List;


public interface IInscripcionService {
    InscripcionDTO crearInscripcion(InscripcionDTO dto);
    List<InscripcionDTO> obtenerTodas();
    InscripcionDTO obtenerPorId(Long id);
    InscripcionDTO actualizar(Long id, InscripcionDTO dto);
    void eliminar(Long id);
}

