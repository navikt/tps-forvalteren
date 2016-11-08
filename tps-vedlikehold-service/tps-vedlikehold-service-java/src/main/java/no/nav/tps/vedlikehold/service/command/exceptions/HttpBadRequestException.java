package no.nav.tps.vedlikehold.service.command.exceptions;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class HttpBadRequestException extends HttpException {
    public HttpBadRequestException(String message, String path) {
        super(message, path);
    }
}