package no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class HttpException extends Exception {

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
