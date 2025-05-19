package com.universidad.dto;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaDTO implements Serializable {

    private Long id;

    @NotBlank(message = "El nombre de la materia no puede estar vacío")
    private String nombreMateria;

    @NotBlank(message = "El código único de la materia no puede estar vacío")
    private String codigoUnico;

    @NotNull(message = "Los créditos de la materia no pueden ser nulos")
    @Min(value = 1, message = "La materia debe tener al menos 1 crédito")
    private Integer creditos;

    // Nuevo atributo con validación
    @NotNull(message = "El ID del docente no puede ser nulo")
    private Long idDocente;

    /**
     * Lista de IDs de materias que son prerequisitos para esta materia.
     */
    private List<Long> prerequisitos;

    /**
     * Lista de IDs de materias para las que esta materia es prerequisito.
     */
    private List<Long> esPrerequisitoDe;
}
