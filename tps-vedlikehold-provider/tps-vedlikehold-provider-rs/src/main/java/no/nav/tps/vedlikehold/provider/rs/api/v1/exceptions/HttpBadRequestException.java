package no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class HttpBadRequestException extends HttpException {
    public HttpBadRequestException(String message, String path) {
        super(message, path);
    }
}