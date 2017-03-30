package no.nav.tps.vedlikehold.service.command.exceptions;


public class SelftestFailureException extends RuntimeException {
    public SelftestFailureException(String message) {
        super(message);
    }
}
