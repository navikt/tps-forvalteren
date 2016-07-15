package no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions;

import no.nav.tps.vedlikehold.domain.service.User;
import no.nav.tps.vedlikehold.provider.rs.api.v1.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.java.authorisation.AuthorisationService;
import no.nav.tps.vedlikehold.service.java.service.rutine.ServiceRutineService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

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
    }

}
