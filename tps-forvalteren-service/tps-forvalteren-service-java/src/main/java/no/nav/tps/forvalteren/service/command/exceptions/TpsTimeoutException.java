package no.nav.tps.forvalteren.service.command.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GATEWAY_TIMEOUT)
public class TpsTimeoutException extends RuntimeException {
    public TpsTimeoutException(String message) {
        super(message);
    }
    
    public TpsTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TpsTimeoutException(Throwable cause) {
        super(cause);
    }
    
    public TpsTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
