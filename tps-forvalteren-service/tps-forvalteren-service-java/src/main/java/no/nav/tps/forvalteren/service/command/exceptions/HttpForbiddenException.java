package no.nav.tps.forvalteren.service.command.exceptions;

public class HttpForbiddenException extends RuntimeException {

    public HttpForbiddenException(String message) {
        super(message);
    }

    public HttpForbiddenException(Exception exception) {
        super(exception);
    }
}
