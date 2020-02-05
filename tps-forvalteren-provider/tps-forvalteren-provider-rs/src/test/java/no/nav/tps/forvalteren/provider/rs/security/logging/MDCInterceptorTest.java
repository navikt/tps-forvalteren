package no.nav.tps.forvalteren.provider.rs.security.logging;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

import no.nav.tps.forvalteren.service.user.UserContextHolder;



@RunWith(MockitoJUnitRunner.class)
public class MDCInterceptorTest {

    private static final String USERNAME = "username";

    @Mock
    private UserContextHolder userContextHolderMock;

    @InjectMocks
    private MDCInterceptor interceptor;

    @Test
    public void preHandleFailsGracefullyWhenUserIsAnonymous() {
        when(userContextHolderMock.getUsername()).thenThrow(new RuntimeException());

        boolean result = interceptor.preHandle(null, null, null);

        assertThat(result, is(true));
    }

    @Test
    public void preHandleReturnsTrue() {
        when(userContextHolderMock.getUsername()).thenReturn(USERNAME);

        boolean result = interceptor.preHandle(null, null, null);

        assertThat(result, is(true));
    }

    @Test
    public void preHandlePopulatesMdc() {
        when(userContextHolderMock.getUsername()).thenReturn(USERNAME);

        interceptor.preHandle(null, null, null);

        String userId = MDC.get(MDCInterceptor.USER_KEY);
        assertThat(userId, is(USERNAME));
    }

    @Test
    public void postHandleDoesNothing() {
        interceptor.postHandle(null, null, null, null);

        verify(userContextHolderMock.getUsername(), never());
    }

    @Test
    public void afterCompletionRemovesFromMdc() {
        interceptor.afterCompletion(null, null, null, null);

        String userId = MDC.get(MDCInterceptor.USER_KEY);
        assertThat(userId, is(nullValue()));
    }
}