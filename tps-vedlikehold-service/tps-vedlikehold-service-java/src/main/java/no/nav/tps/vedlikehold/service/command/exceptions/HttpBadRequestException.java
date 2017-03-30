package no.nav.tps.vedlikehold.service.command.exceptions;


public class HttpBadRequestException extends HttpException {
    public HttpBadRequestException(String message, String path) {
        super(message, path);
    }
}