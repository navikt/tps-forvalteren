package no.nav.tps.forvalteren.service.command.exceptions;

public class HttpForbiddenException extends HttpException{

    public HttpForbiddenException(String message, String path) {
        super(message, path);
    }

    public HttpForbiddenException(Exception exception, String path) {
        super(exception, path);
    }
}
