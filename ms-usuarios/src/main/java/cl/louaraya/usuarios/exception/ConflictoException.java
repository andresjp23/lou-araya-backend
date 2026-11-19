package cl.louaraya.usuarios.exception;

/** Dato duplicado o en conflicto (409). */
public class ConflictoException extends RuntimeException {
    public ConflictoException(String message) {
        super(message);
    }
}
