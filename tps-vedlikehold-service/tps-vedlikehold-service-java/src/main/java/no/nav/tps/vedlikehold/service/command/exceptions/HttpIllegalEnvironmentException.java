package no.nav.tps.vedlikehold.service.command.exceptions;

public class HttpIllegalEnvironmentException extends HttpException{

    public HttpIllegalEnvironmentException(String message, String path) {
        super(message, path);
    }

    public HttpIllegalEnvironmentException(Exception exception, String path) {
        super(exception, path);
    }
}
