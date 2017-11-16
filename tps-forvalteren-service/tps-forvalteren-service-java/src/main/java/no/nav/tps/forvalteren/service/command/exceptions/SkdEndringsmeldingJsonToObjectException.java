package no.nav.tps.forvalteren.service.command.exceptions;

public class SkdEndringsmeldingJsonToObjectException extends RuntimeException {

    public SkdEndringsmeldingJsonToObjectException(String message, Exception exception) {
        super(message, exception);
    }
}
