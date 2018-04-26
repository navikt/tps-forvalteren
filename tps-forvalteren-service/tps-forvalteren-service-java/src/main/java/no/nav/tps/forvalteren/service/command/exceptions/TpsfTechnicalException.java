package no.nav.tps.forvalteren.service.command.exceptions;

public class TpsfTechnicalException extends RuntimeException {
    public TpsfTechnicalException() {
    }
    
    public TpsfTechnicalException(String message) {
        super(message);
    }
    
    public TpsfTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TpsfTechnicalException(Throwable cause) {
        super(cause);
    }
    
    public TpsfTechnicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
