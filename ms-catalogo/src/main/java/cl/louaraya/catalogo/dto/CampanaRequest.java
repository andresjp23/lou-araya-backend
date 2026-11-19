package cl.louaraya.catalogo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CampanaRequest(
        @NotBlank(message = "Ingresa el nombre de la campaña") String title,
        String description,
        @NotNull(message = "Ingresa la fecha de inicio") LocalDate startDate,
        @NotNull(message = "Ingresa la fecha de término") LocalDate endDate,
        Boolean active
) {}
