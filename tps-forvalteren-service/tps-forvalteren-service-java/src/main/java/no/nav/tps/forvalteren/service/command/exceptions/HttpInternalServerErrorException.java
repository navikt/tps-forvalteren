package no.nav.tps.forvalteren.service.command.exceptions;


public class HttpInternalServerErrorException extends HttpException {
    public HttpInternalServerErrorException(String message, String path) {
        super(message, path);
    }

    public HttpInternalServerErrorException(Exception exception, String path) {
        super(exception, path);
    }
}
