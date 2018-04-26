package no.nav.tps.forvalteren.service.command.exceptions;

public class TpsfFunctionalException extends RuntimeException {
    public TpsfFunctionalException() {
    }
    
    public TpsfFunctionalException(String message) {
        super(message);
    }
    
    public TpsfFunctionalException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TpsfFunctionalException(Throwable cause) {
        super(cause);
    }
    
    public TpsfFunctionalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
