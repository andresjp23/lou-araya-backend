package cl.louaraya.solicitudes.model;

import java.util.Set;

/**
 * Estados de una solicitud de cotización. Cada estado define a cuáles puede avanzar,
 * así la regla de negocio vive en el modelo y no en el controlador.
 */
public enum EstadoSolicitud {
    PENDIENTE {
        @Override public Set<EstadoSolicitud> siguientes() { return Set.of(EN_REVISION, APROBADA, RECHAZADA); }
    },
    EN_REVISION {
        @Override public Set<EstadoSolicitud> siguientes() { return Set.of(APROBADA, RECHAZADA, PENDIENTE); }
    },
    APROBADA {
        @Override public Set<EstadoSolicitud> siguientes() { return Set.of(COMPLETADA, RECHAZADA); }
    },
    RECHAZADA {
        @Override public Set<EstadoSolicitud> siguientes() { return Set.of(); }
    },
    COMPLETADA {
        @Override public Set<EstadoSolicitud> siguientes() { return Set.of(); }
    };

    public abstract Set<EstadoSolicitud> siguientes();

    public boolean puedeCambiarA(EstadoSolicitud nuevo) {
        return this == nuevo || siguientes().contains(nuevo);
    }

    public boolean esEditable() {
        return this == PENDIENTE;
    }
}
