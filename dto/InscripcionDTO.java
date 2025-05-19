package com.universidad.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionDTO implements Serializable {
    private Long id;
    @NotNull(message = "Debe especificar el ID del estudiante")
    private Long idEstudiante;
    @NotNull(message = "Debe especificar el ID de la materia")
    private Long idMateria;
    private LocalDate fechaInscripcion;
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
