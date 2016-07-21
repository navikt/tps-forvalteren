package no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class HttpInternalServerErrorException extends HttpException {
    public HttpInternalServerErrorException(String message, String path) {
        super(message, path);
    }
}
