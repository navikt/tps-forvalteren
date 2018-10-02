package no.nav.tps.forvalteren.service.command.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TpsfFunctionalException extends HttpException {

    public TpsfFunctionalException(String message) {
        super(message, null);
    }

    public TpsfFunctionalException(String message, Throwable throwable) {
        this(message);
    }
}