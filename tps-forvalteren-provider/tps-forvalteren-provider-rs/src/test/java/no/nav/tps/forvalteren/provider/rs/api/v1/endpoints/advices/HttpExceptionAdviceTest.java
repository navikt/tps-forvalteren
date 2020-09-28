package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.advices;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UrlPathHelper;

import no.nav.tps.forvalteren.service.command.exceptions.HttpInternalServerErrorException;
import no.nav.tps.forvalteren.service.command.exceptions.HttpUnauthorisedException;


@RunWith(MockitoJUnitRunner.class)
public class HttpExceptionAdviceTest {

    private static final String EXCEPTION_MESSAGE = "This is a message";

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private UrlPathHelper urlPathHelper;

    @InjectMocks
    HttpExceptionAdvice exceptionController;

    @Test
    public void unauthorisedAccessReturnsCorrectInformation() {

        RuntimeException httpException = new HttpUnauthorisedException(EXCEPTION_MESSAGE);

        HttpExceptionAdvice.ExceptionInformation exceptionInformation = exceptionController.unauthorisedAccess(httpException);

        assertThat(exceptionInformation.getError(), is(HttpStatus.UNAUTHORIZED.getReasonPhrase()));
        assertThat(exceptionInformation.getStatus(), is(HttpStatus.UNAUTHORIZED.value()));
        assertThat(exceptionInformation.getMessage(), is(EXCEPTION_MESSAGE));
        assertThat(exceptionInformation.getTimestamp(), lessThanOrEqualTo(LocalDateTime.now()));
    }

    @Test
    public void internalServerErrorReturnsCorrectInformation() {

        RuntimeException httpException = new HttpInternalServerErrorException(EXCEPTION_MESSAGE);

        HttpExceptionAdvice.ExceptionInformation exceptionInformation = exceptionController.internalServerError(httpException);

        assertThat(exceptionInformation.getError(), is(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        assertThat(exceptionInformation.getStatus(), is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        assertThat(exceptionInformation.getMessage(), is(EXCEPTION_MESSAGE));
        assertThat(exceptionInformation.getTimestamp(), lessThanOrEqualTo(LocalDateTime.now()));
    }
}
