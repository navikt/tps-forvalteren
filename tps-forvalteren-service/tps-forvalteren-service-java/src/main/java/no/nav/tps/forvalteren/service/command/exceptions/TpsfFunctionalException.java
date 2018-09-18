package no.nav.tps.forvalteren.service.command.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TpsfFunctionalException extends RuntimeException {
    
    public TpsfFunctionalException(String message) {
        super(message);
    }

    public TpsfFunctionalException(Throwable cause) {
        super(cause);
    }
}