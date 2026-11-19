package cl.louaraya.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SolicitudRequest(
        @NotNull(message = "El usuario es obligatorio") @Positive(message = "Usuario inválido") Long userId,
        String userName,
        Long productId,
        @NotBlank(message = "Selecciona un tipo de producto") String productName,
        @NotBlank(message = "Selecciona el tipo de evento") String eventType,
        @NotBlank(message = "Ingresa las dimensiones") String dimensions,
        @NotBlank(message = "Selecciona un material") String material,
        @NotBlank(message = "Indica los colores") String colors,
        @NotBlank(message = "Selecciona una fecha") String requiredDate,
        String observations
) {}
