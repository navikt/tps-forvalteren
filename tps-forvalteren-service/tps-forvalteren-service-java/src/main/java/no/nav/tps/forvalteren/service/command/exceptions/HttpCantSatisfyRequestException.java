package no.nav.tps.forvalteren.service.command.exceptions;

public class HttpCantSatisfyRequestException extends HttpException{

    public HttpCantSatisfyRequestException(String message, String path) {
        super(message, path);
    }
}
