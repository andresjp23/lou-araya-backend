package cl.louaraya.catalogo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductoRequest(
        @NotBlank(message = "Ingresa el nombre del producto") String name,
        String description,
        String dimensions,
        String materials,
        @NotBlank(message = "Ingresa una categoría") String category,
        @NotNull(message = "Ingresa un precio") @Min(value = 0, message = "El precio no puede ser negativo") Integer price,
        Boolean available,
        String imageUri,
        String collection
) {}
