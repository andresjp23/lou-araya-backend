package cl.louaraya.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadoRequest(@NotBlank(message = "Indica el nuevo estado") String status) {}
