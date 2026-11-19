package cl.louaraya.solicitudes;

import cl.louaraya.solicitudes.dto.SolicitudRequest;
import cl.louaraya.solicitudes.exception.RecursoNoEncontradoException;
import cl.louaraya.solicitudes.exception.ReglaNegocioException;
import cl.louaraya.solicitudes.model.EstadoSolicitud;
import cl.louaraya.solicitudes.model.Solicitud;
import cl.louaraya.solicitudes.repository.SolicitudRepository;
import cl.louaraya.solicitudes.service.SolicitudServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SolicitudServiceImplTest {

    private SolicitudRepository repository;
    private SolicitudServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(SolicitudRepository.class);
        service = new SolicitudServiceImpl(repository);
        when(repository.save(any(Solicitud.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    private SolicitudRequest request() {
        return new SolicitudRequest(1L, "Lou", 3L, "Arco de ceremonia", "Matrimonio", "2 x 3 m", "Algodón", "Blanco", "2026-12-20", null);
    }

    private Solicitud existente(EstadoSolicitud estado) {
        Solicitud s = new Solicitud();
        s.setUserId(1L);
        s.setStatus(estado);
        when(repository.findById(7L)).thenReturn(Optional.of(s));
        return s;
    }

    @Test
    void crearQuedaPendiente() {
        var r = service.crear(request());
        assertThat(r.status()).isEqualTo("PENDIENTE");
        assertThat(r.observations()).isEmpty();
        assertThat(r.productName()).isEqualTo("Arco de ceremonia");
    }

    @Test
    void listarPorUsuarioEstadoOTodas() {
        when(repository.findByUserIdOrderByIdDesc(1L)).thenReturn(List.of(new Solicitud()));
        when(repository.findByStatusOrderByIdDesc(EstadoSolicitud.APROBADA)).thenReturn(List.of());
        when(repository.findAllByOrderByIdDesc()).thenReturn(List.of(new Solicitud(), new Solicitud()));
        assertThat(service.listar(1L, null)).hasSize(1);
        assertThat(service.listar(null, "aprobada")).isEmpty();
        assertThat(service.listar(null, null)).hasSize(2);
    }

    @Test
    void cambiarEstadoValido() {
        existente(EstadoSolicitud.PENDIENTE);
        assertThat(service.cambiarEstado(7L, "APROBADA").status()).isEqualTo("APROBADA");
    }

    @Test
    void cambiarEstadoInvalidoSeRechaza() {
        existente(EstadoSolicitud.COMPLETADA);
        assertThatThrownBy(() -> service.cambiarEstado(7L, "PENDIENTE")).isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void estadoDesconocido() {
        existente(EstadoSolicitud.PENDIENTE);
        assertThatThrownBy(() -> service.cambiarEstado(7L, "VOLANDO")).isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void soloSeEditanPendientes() {
        existente(EstadoSolicitud.APROBADA);
        assertThatThrownBy(() -> service.actualizar(7L, request())).isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void editarPendienteYEliminar() {
        Solicitud s = existente(EstadoSolicitud.PENDIENTE);
        assertThat(service.actualizar(7L, request()).colors()).isEqualTo("Blanco");
        service.eliminar(7L);
        verify(repository).delete(s);
    }

    @Test
    void solicitudInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.obtener(99L)).isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void transicionesDelEnum() {
        assertThat(EstadoSolicitud.PENDIENTE.puedeCambiarA(EstadoSolicitud.EN_REVISION)).isTrue();
        assertThat(EstadoSolicitud.APROBADA.puedeCambiarA(EstadoSolicitud.COMPLETADA)).isTrue();
        assertThat(EstadoSolicitud.RECHAZADA.puedeCambiarA(EstadoSolicitud.APROBADA)).isFalse();
        assertThat(EstadoSolicitud.PENDIENTE.esEditable()).isTrue();
        assertThat(EstadoSolicitud.EN_REVISION.esEditable()).isFalse();
    }
}
