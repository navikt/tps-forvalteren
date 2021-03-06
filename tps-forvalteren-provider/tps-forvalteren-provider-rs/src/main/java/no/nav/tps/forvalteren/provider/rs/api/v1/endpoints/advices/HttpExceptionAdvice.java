package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.advices;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UrlPathHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tps.forvalteren.service.command.exceptions.HttpCantSatisfyRequestException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class HttpExceptionAdvice {

    private final HttpServletRequest httpServletRequest;
    private final UrlPathHelper urlPathHelper;

    @ResponseBody
    @ExceptionHandler(HttpUnauthorisedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    ExceptionInformation unauthorisedAccess(RuntimeException exception) {
        return informationForException(exception, HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(HttpForbiddenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    ExceptionInformation forbiddenAccess(RuntimeException exception) {
        return informationForException(exception, HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler({ HttpInternalServerErrorException.class, HttpCantSatisfyRequestException.class })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    ExceptionInformation internalServerError(RuntimeException exception) {
        return informationForException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler({ TpsfFunctionalException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    ExceptionInformation badRequest(RuntimeException exception) {
        return informationForException(exception, HttpStatus.BAD_REQUEST);
    }

    private ExceptionInformation informationForException(RuntimeException exception, HttpStatus status) {
        return ExceptionInformation.builder()
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(exception.getMessage())
                .path(getPath())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExceptionInformation {

        private String message;
        private String error;
        private String path;
        private Integer status;
        private LocalDateTime timestamp;
    }

    private String getPath() {

        try {
            return urlPathHelper.getPathWithinApplication(httpServletRequest);
        } catch (RuntimeException e) {
            log.error("Feilet å hente path: ", e);
            return null;
        }
    }
}
