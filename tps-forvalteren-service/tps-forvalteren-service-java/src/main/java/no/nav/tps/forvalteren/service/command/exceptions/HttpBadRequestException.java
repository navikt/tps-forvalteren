package no.nav.tps.forvalteren.service.command.exceptions;


public class HttpBadRequestException extends HttpException {
    public HttpBadRequestException(String message, String path) {
        super(message, path);
    }
}