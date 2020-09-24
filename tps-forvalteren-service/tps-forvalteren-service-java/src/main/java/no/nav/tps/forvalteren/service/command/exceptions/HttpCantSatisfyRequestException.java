package no.nav.tps.forvalteren.service.command.exceptions;

public class HttpCantSatisfyRequestException extends RuntimeException {

    public HttpCantSatisfyRequestException(String message) {
        super(message);
    }
}
