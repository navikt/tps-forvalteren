package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.advices;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.util.Date;

import no.nav.tps.forvalteren.service.command.exceptions.ExceptionInformation;
import no.nav.tps.forvalteren.service.command.exceptions.HttpBadRequestException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpUnauthorisedException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;


@RunWith(MockitoJUnitRunner.class)
public class HttpExceptionAdviceTest {

    private static final String EXCEPTION_MESSAGE = "This is a message";
    private static final String EXCEPTION_PATH    = "this/is/a/path";

    @InjectMocks
    HttpExceptionAdvice exceptionController;

    @Test
    public void unauthorisedAccessReturnsCorrectInformation() {
        HttpException httpException = new HttpUnauthorisedException(EXCEPTION_MESSAGE, EXCEPTION_PATH);

        ExceptionInformation exceptionInformation = exceptionController.unauthorisedAccess(httpException);

        assertThat(exceptionInformation.getError(), is(HttpStatus.UNAUTHORIZED.getReasonPhrase()));
        assertThat(exceptionInformation.getStatus(), is(HttpStatus.UNAUTHORIZED.value()));
        assertThat(exceptionInformation.getPath(), is(EXCEPTION_PATH));
        assertThat(exceptionInformation.getMessage(), is(EXCEPTION_MESSAGE));
        assertThat(exceptionInformation.getTimestamp(), lessThanOrEqualTo(new Date().getTime()));
        assertThat(exceptionInformation.getTimestamp(), greaterThanOrEqualTo(new Date().getTime()-200));
    }

    @Test
    public void internalServerErrorReturnsCorrectInformation() {
        HttpException httpException = new HttpInternalServerErrorException(EXCEPTION_MESSAGE, EXCEPTION_PATH);

        ExceptionInformation exceptionInformation = exceptionController.internalServerError(httpException);

        assertThat(exceptionInformation.getError(), is(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        assertThat(exceptionInformation.getStatus(), is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        assertThat(exceptionInformation.getPath(), is(EXCEPTION_PATH));
        assertThat(exceptionInformation.getMessage(), is(EXCEPTION_MESSAGE));
        assertThat(exceptionInformation.getTimestamp(), lessThanOrEqualTo(new Date().getTime()));
        assertThat(exceptionInformation.getTimestamp(), greaterThanOrEqualTo(new Date().getTime()-200));
    }

    @Test
    public void badRequestReturnsCorrectInformation() {
        HttpException httpException = new HttpBadRequestException(EXCEPTION_MESSAGE, EXCEPTION_PATH);

        ExceptionInformation exceptionInformation = exceptionController.badRequest(httpException);

        assertThat(exceptionInformation.getError(), is(HttpStatus.BAD_REQUEST.getReasonPhrase()));
        assertThat(exceptionInformation.getStatus(), is(HttpStatus.BAD_REQUEST.value()));
        assertThat(exceptionInformation.getPath(), is(EXCEPTION_PATH));
        assertThat(exceptionInformation.getMessage(), is(EXCEPTION_MESSAGE));
        assertThat(exceptionInformation.getTimestamp(), lessThanOrEqualTo(new Date().getTime()));
        assertThat(exceptionInformation.getTimestamp(), greaterThanOrEqualTo(new Date().getTime()-200));
    }

}
