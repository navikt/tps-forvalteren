package no.nav.tps.forvalteren.service.command.exceptions;


public class HttpUnauthorisedException extends HttpException {

    public HttpUnauthorisedException(String message, String path) {
        super(message, path);
    }

    public HttpUnauthorisedException(Exception exception, String path) {
        super(exception, path);
    }
}