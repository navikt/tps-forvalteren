package no.nav.tps.vedlikehold.provider.rs.security.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class MDCInterceptorTest {

    private static final String USERNAME = "username";

    @Mock
    private UserContextHolder userContextHolderMock;

    @InjectMocks
    private MDCInterceptor interceptor;

    @Before
    public void setUp() {

    }

    @Test
    public void prehandleFailsGracefullyWhenUserIsAnonymous() throws Exception {
        when(userContextHolderMock.getUsername()).thenThrow(RuntimeException.class);

        Boolean result = interceptor.preHandle(mock(HttpServletRequest.class), mock(HttpServletResponse.class), mock(Object.class));

        assertThat(result, is(true));
    }

    @Test(expected = Exception.class)
    public void prehandleFailsForGeneralExceptions() throws Exception {
        when(userContextHolderMock.getUsername()).thenThrow(Exception.class);

        interceptor.preHandle(mock(HttpServletRequest.class), mock(HttpServletResponse.class), mock(Object.class));
    }

    @Test
    public void prehandleReturnsTrue() throws Exception {
        when(userContextHolderMock.getUsername()).thenReturn(USERNAME);

        Boolean result = interceptor.preHandle(mock(HttpServletRequest.class), mock(HttpServletResponse.class), mock(Object.class));

        assertThat(result, is(true));
    }


}
