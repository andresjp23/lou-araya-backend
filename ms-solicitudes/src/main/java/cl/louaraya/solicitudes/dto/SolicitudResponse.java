package cl.louaraya.solicitudes.dto;

import cl.louaraya.solicitudes.model.Solicitud;

import java.time.format.DateTimeFormatter;

public record SolicitudResponse(Long id, Long userId, String userName, Long productId, String productName,
                                String eventType, String dimensions, String material, String colors,
                                String requiredDate, String observations, String status, String createdAt) {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static SolicitudResponse from(Solicitud s) {
        return new SolicitudResponse(s.getId(), s.getUserId(), s.getUserName(), s.getProductId(), s.getProductName(),
                s.getEventType(), s.getDimensions(), s.getMaterial(), s.getColors(), s.getRequiredDate(),
                s.getObservations(), s.getStatus().name(),
                s.getCreatedAt() == null ? "" : s.getCreatedAt().format(FORMATO));
    }
}
