package no.nav.tps.vedlikehold.provider.rs.security.csrf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
//https://raw.githubusercontent.com/aditzel/spring-security-csrf-filter/master/src/test/java/com/allanditzel/springframework/security/web/csrf/CsrfTokenResponseHeaderBindingFilterTest.java
//https://github.com/aditzel/spring-security-csrf-filter/blob/master/src/main/java/com/allanditzel/springframework/security/web/csrf/CsrfTokenResponseHeaderBindingFilter.java

@RunWith(MockitoJUnitRunner.class)
public class CsrfHeaderFilterTest {

    private CsrfHeaderFilter csrfHeaderFilter;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    HttpSession session;

    @Mock
    CsrfToken token;

    @Before
    public void setUp() {
        csrfHeaderFilter = new CsrfHeaderFilter("path");
    }

    @Test
    public void shouldContinueProcessingFilterChainIfTokenNotPresentInRequest() throws ServletException, IOException {
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(null);

        csrfHeaderFilter.doFilterInternal(request, response, filterChain);

        verify(request).getAttribute(CsrfToken.class.getName());
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }
}
