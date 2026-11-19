package cl.louaraya.usuarios.exception;

/** Credenciales inválidas (401). */
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String message) {
        super(message);
    }
}
