package no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *  @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class HttpExceptionControllerTest {

    private static final String EXCEPTION_MESSAGE = "This is a message";
    private static final String EXCEPTION_PATH    = "this/is/a/path";

    @InjectMocks
    HttpExceptionController exceptionController;

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

}
