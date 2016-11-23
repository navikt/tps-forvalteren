package no.nav.tps.vedlikehold.service.command.exceptions;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class HttpException extends RuntimeException {

    private final String path;

    public HttpException(String message, String path) {
        super(message);
        this.path = path;
    }

    public HttpException(Exception exception, String path) {
        super(exception);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
