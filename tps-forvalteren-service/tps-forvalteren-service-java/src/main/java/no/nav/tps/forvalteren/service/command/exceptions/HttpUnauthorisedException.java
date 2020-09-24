package no.nav.tps.forvalteren.service.command.exceptions;

public class HttpUnauthorisedException extends RuntimeException {

    public HttpUnauthorisedException(String message) {
        super(message);
    }
}
