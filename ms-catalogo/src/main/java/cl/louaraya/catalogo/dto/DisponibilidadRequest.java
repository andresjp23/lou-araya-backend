package cl.louaraya.catalogo.dto;

import jakarta.validation.constraints.NotNull;

public record DisponibilidadRequest(@NotNull(message = "Indica el valor") Boolean value) {}
