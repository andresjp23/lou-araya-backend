package cl.louaraya.solicitudes.repository;

import cl.louaraya.solicitudes.model.EstadoSolicitud;
import cl.louaraya.solicitudes.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findAllByOrderByIdDesc();
    List<Solicitud> findByUserIdOrderByIdDesc(Long userId);
    List<Solicitud> findByStatusOrderByIdDesc(EstadoSolicitud status);
}
