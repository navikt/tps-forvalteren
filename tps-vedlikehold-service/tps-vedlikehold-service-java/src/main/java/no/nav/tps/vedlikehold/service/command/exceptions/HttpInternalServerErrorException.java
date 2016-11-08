package no.nav.tps.vedlikehold.service.command.exceptions;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class HttpInternalServerErrorException extends HttpException {
    public HttpInternalServerErrorException(String message, String path) {
        super(message, path);
    }

    public HttpInternalServerErrorException(Exception exception, String path) {
        super(exception, path);
    }
}
