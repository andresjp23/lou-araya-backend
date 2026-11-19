package cl.louaraya.catalogo.exception;

/** Recurso no encontrado (404). */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}
