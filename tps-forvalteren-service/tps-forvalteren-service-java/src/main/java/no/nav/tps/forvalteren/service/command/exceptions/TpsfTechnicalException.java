package no.nav.tps.forvalteren.service.command.exceptions;

public class TpsfTechnicalException extends RuntimeException {
    
    public TpsfTechnicalException(String message) {
        super(message);
    }
    
    public TpsfTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
