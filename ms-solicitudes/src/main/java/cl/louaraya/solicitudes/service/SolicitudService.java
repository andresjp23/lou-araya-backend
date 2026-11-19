package cl.louaraya.solicitudes.service;

import cl.louaraya.solicitudes.dto.SolicitudRequest;
import cl.louaraya.solicitudes.dto.SolicitudResponse;

import java.util.List;

public interface SolicitudService {
    List<SolicitudResponse> listar(Long userId, String status);
    SolicitudResponse obtener(Long id);
    SolicitudResponse crear(SolicitudRequest request);
    SolicitudResponse actualizar(Long id, SolicitudRequest request);
    SolicitudResponse cambiarEstado(Long id, String status);
    void eliminar(Long id);
}
