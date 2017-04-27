package no.nav.tps.forvalteren.service.command.exceptions;

/**
 * Object containing information about the exception.
 * This is the response type for exceptions in the REST provider
 */
public final class ExceptionInformation {

    private String message;
    private String error;
    private String path;
    private Integer status;
    private long timestamp;

    private ExceptionInformation() {}

    public String getMessage() {
        return message;
    }

    public ExceptionInformation setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getError() {
        return error;
    }

    public ExceptionInformation setError(String error) {
        this.error = error;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ExceptionInformation setPath(String path) {
        this.path = path;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public ExceptionInformation setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ExceptionInformation setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public static ExceptionInformation create() {
        return new ExceptionInformation();
    }
}
