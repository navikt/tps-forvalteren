package no.nav.tps.forvalteren.service.command.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TpsfFunctionalException extends RuntimeException {

    public TpsfFunctionalException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public TpsfFunctionalException(String message) {
        this(message, null);
    }

    public TpsfFunctionalException(Throwable cause) {
        super(cause);
    }
}