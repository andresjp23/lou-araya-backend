package cl.louaraya.solicitudes.exception;

/** Regla de negocio no cumplida (422). */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String message) {
        super(message);
    }
}
