package no.nav.tps.forvalteren.service.command.exceptions;


public class SelftestFailureException extends RuntimeException {
    public SelftestFailureException(String message) {
        super(message);
    }
}
