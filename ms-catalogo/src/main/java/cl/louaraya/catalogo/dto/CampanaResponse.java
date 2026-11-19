package cl.louaraya.catalogo.dto;

import cl.louaraya.catalogo.model.Campana;

import java.time.LocalDate;

public record CampanaResponse(Long id, String title, String description, LocalDate startDate, LocalDate endDate, Boolean active) {
    public static CampanaResponse from(Campana c) {
        return new CampanaResponse(c.getId(), c.getTitle(), c.getDescription(), c.getStartDate(), c.getEndDate(), c.getActive());
    }
}
