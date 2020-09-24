package no.nav.tps.forvalteren.service.command.exceptions;

public class HttpInternalServerErrorException extends RuntimeException {

    public HttpInternalServerErrorException(String message) {
        super(message);
    }

    public HttpInternalServerErrorException(Exception exception) {
        super(exception);
    }
}
