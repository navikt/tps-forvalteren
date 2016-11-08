package no.nav.tps.vedlikehold.service.command.exceptions;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class HttpUnauthorisedException extends HttpException {

    public static final String messageKey = "rest.service.request.exception.Unauthorized";

    public HttpUnauthorisedException(String message, String path) {
        super(message, path);
    }
}
