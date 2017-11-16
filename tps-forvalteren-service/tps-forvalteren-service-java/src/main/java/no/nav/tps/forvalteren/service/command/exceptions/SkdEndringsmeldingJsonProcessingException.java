package no.nav.tps.forvalteren.service.command.exceptions;

public class SkdEndringsmeldingJsonProcessingException extends RuntimeException {
    public SkdEndringsmeldingJsonProcessingException(String message, Exception exception) {
        super(message, exception);
    }
}
