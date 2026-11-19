package cl.louaraya.solicitudes.service;

import cl.louaraya.solicitudes.dto.SolicitudRequest;
import cl.louaraya.solicitudes.dto.SolicitudResponse;
import cl.louaraya.solicitudes.exception.RecursoNoEncontradoException;
import cl.louaraya.solicitudes.exception.ReglaNegocioException;
import cl.louaraya.solicitudes.model.EstadoSolicitud;
import cl.louaraya.solicitudes.model.Solicitud;
import cl.louaraya.solicitudes.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository repository;

    public SolicitudServiceImpl(SolicitudRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudResponse> listar(Long userId, String status) {
        List<Solicitud> solicitudes;
        if (userId != null) solicitudes = repository.findByUserIdOrderByIdDesc(userId);
        else if (status != null && !status.isBlank()) solicitudes = repository.findByStatusOrderByIdDesc(parsearEstado(status));
        else solicitudes = repository.findAllByOrderByIdDesc();
        return solicitudes.stream().map(SolicitudResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SolicitudResponse obtener(Long id) {
        return SolicitudResponse.from(buscar(id));
    }

    @Override
    @Transactional
    public SolicitudResponse crear(SolicitudRequest request) {
        Solicitud solicitud = new Solicitud();
        solicitud.setUserId(request.userId());
        aplicar(solicitud, request);
        solicitud.setStatus(EstadoSolicitud.PENDIENTE);
        return SolicitudResponse.from(repository.save(solicitud));
    }

    @Override
    @Transactional
    public SolicitudResponse actualizar(Long id, SolicitudRequest request) {
        Solicitud solicitud = buscar(id);
        if (!solicitud.getStatus().esEditable()) {
            throw new ReglaNegocioException("Solo se pueden editar solicitudes pendientes");
        }
        aplicar(solicitud, request);
        return SolicitudResponse.from(repository.save(solicitud));
    }

    @Override
    @Transactional
    public SolicitudResponse cambiarEstado(Long id, String status) {
        Solicitud solicitud = buscar(id);
        EstadoSolicitud nuevo = parsearEstado(status);
        if (!solicitud.getStatus().puedeCambiarA(nuevo)) {
            throw new ReglaNegocioException("No se puede pasar de " + solicitud.getStatus() + " a " + nuevo);
        }
        solicitud.setStatus(nuevo);
        return SolicitudResponse.from(repository.save(solicitud));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private Solicitud buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud " + id + " no encontrada"));
    }

    private EstadoSolicitud parsearEstado(String status) {
        try {
            return EstadoSolicitud.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("Estado desconocido: " + status);
        }
    }

    private void aplicar(Solicitud s, SolicitudRequest r) {
        s.setUserName(r.userName() == null ? "" : r.userName().trim());
        s.setProductId(r.productId());
        s.setProductName(r.productName().trim());
        s.setEventType(r.eventType().trim());
        s.setDimensions(r.dimensions().trim());
        s.setMaterial(r.material().trim());
        s.setColors(r.colors().trim());
        s.setRequiredDate(r.requiredDate().trim());
        s.setObservations(r.observations() == null ? "" : r.observations().trim());
    }
}
