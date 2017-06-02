package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.advices;

import java.util.Date;

import no.nav.tps.forvalteren.service.command.exceptions.ExceptionInformation;
import no.nav.tps.forvalteren.service.command.exceptions.HttpBadRequestException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpIllegalEnvironmentException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpUnauthorisedException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HttpExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(HttpUnauthorisedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    ExceptionInformation unauthorisedAccess(HttpException exception) {
        return informationForException(exception, HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(HttpInternalServerErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    ExceptionInformation internalServerError(HttpException exception) {
        return informationForException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(HttpBadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ExceptionInformation badRequest(HttpException exception) {
        return informationForException(exception, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(HttpIllegalEnvironmentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ExceptionInformation illegalEnvironment(HttpException exception){
        return informationForException(exception, HttpStatus.BAD_REQUEST);
    }

    private ExceptionInformation informationForException(HttpException exception, HttpStatus status) {
        return ExceptionInformation.create()
                .setError( status.getReasonPhrase() )
                .setStatus( status.value() )
                .setMessage( exception.getMessage() )
                .setPath( exception.getPath() )
                .setTimestamp( new Date().getTime() );
    }
}