package no.nav.tps.vedlikehold.provider.rs.security.csrf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.web.util.WebUtils.getCookie;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
//https://raw.githubusercontent.com/aditzel/spring-security-csrf-filter/master/src/test/java/com/allanditzel/springframework/security/web/csrf/CsrfTokenResponseHeaderBindingFilterTest.java
//https://github.com/aditzel/spring-security-csrf-filter/blob/master/src/main/java/com/allanditzel/springframework/security/web/csrf/CsrfTokenResponseHeaderBindingFilter.java

@RunWith(MockitoJUnitRunner.class)
public class CsrfHeaderFilterTest {

    private CsrfHeaderFilter csrfHeaderFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpSession session;

    @Mock
    private CsrfToken token;

    @Before
    public void setUp() {
        csrfHeaderFilter = new CsrfHeaderFilter("path");
    }

    /**
     * csrf == null
     */
    @Test
    public void shouldContinueProcessingFilterChainWithoutAddingCookieToResponseIfCsrfTokenIsNull() throws ServletException, IOException {
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(null);

        csrfHeaderFilter.doFilterInternal(request, response, filterChain);

        verify(request).getAttribute(CsrfToken.class.getName());
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }

    /**
     * csrf != null
     * cookie != null
     * token != null
     * token === cookie.value
     * Failed inner if statement
     */
    @Test
    public void shouldContinueProcessingFilterChainWithoutAddingCookieToResponse1() throws ServletException, IOException {
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(new DefaultCsrfToken("header", "parameter", "X-CSRF"));
        Cookie cookie = new Cookie("XSRF-TOKEN", "X-CSRF");
        cookie.setPath("path");
        Cookie[] c = {cookie};
        when(request.getCookies()).thenReturn(c);

        csrfHeaderFilter.doFilterInternal(request, response, filterChain);

        verify(request).getAttribute(CsrfToken.class.getName());
        verify(request).getCookies();
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }

    /**
     * csrf != null
     * cookie != null
     * token != null
     * token != cookie.value
     * Failed inner if statement
     */
    @Test
    public void shouldContinueProcessingFilterChainAfterAddingCookieToResponse1() throws ServletException, IOException {
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(new DefaultCsrfToken("header", "parameter", "X-CSRF"));
        Cookie cookie = new Cookie("XSRF-TOKEN", "X-CSRF-1");
        cookie.setPath("path");
        Cookie[] c = {cookie};
        when(request.getCookies()).thenReturn(c);

        csrfHeaderFilter.doFilterInternal(request, response, filterChain);

        verify(request).getAttribute(CsrfToken.class.getName());
        verify(request).getCookies();
        verify(response).addCookie(any(Cookie.class));
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }

    /**
     * csrf != null
     * cookie == null
     * token != null
     * token != cookie.value
     * Failed inner if statement
     */
    @Test
    public void shouldContinueProcessingFilterChainAfterAddingCookieToResponse2() throws ServletException, IOException {
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(new DefaultCsrfToken("header", "parameter", "X-CSRF"));
        Cookie cookie = new Cookie("XSRF-TOKEN", "X-CSRF-1");
        cookie.setPath("path");
        Cookie[] c = {cookie};
        when(request.getCookies()).thenReturn(null);

        csrfHeaderFilter.doFilterInternal(request, response, filterChain);

        verify(request).getAttribute(CsrfToken.class.getName());
        verify(request).getCookies();
        verify(response).addCookie(any(Cookie.class));
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }
}
