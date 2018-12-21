package no.nav.tps.forvalteren.service.command.exceptions;

public class SkdEndringsmeldingGruppeTooLargeException extends RuntimeException {

    public SkdEndringsmeldingGruppeTooLargeException(String melding) {
        super(melding);
    }
}
