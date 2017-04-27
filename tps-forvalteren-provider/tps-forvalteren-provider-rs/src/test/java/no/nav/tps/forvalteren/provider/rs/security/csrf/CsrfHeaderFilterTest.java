package no.nav.tps.forvalteren.provider.rs.security.csrf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CsrfHeaderFilterTest {

    private static final String PATH = "path";

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
        csrfHeaderFilter = new CsrfHeaderFilter(PATH);
    }

    @Test
    public void shouldNotAddCookieToResponseIfCsrfTokenIsNull() throws ServletException, IOException {
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(null);

        csrfHeaderFilter.doFilterInternal(request, response, filterChain);

        verify(request).getAttribute(CsrfToken.class.getName());
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }

    @Test
    public void shouldNotAddCookieToResponseIfCsrfTokenIsNotNullAndCookieIsNotNullAndTokenIsNotNullAndTokenEqualsCookie() throws ServletException, IOException {
        Cookie cookie = new Cookie("XSRF-TOKEN", "X-CSRF");
        cookie.setPath(PATH);

        Cookie[] cookies = {cookie};

        when(request.getCookies()).thenReturn(cookies);
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(new DefaultCsrfToken("header", "parameter", "X-CSRF"));

        csrfHeaderFilter.doFilterInternal(request, response, filterChain);

        verify(request).getAttribute(CsrfToken.class.getName());
        verify(request).getCookies();
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }

    @Test
    public void shouldAddCookieToResponseWhenCSRFTokenIsNotNullAndCookieIsNotNullAndTokenNotNullAndTokenNotEqualToCookie() throws ServletException, IOException {
        Cookie cookie = new Cookie("XSRF-TOKEN", "X-CSRF-1");
        cookie.setPath(PATH);

        Cookie[] cookies = {cookie};

        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(new DefaultCsrfToken("header", "parameter", "X-CSRF"));
        when(request.getCookies()).thenReturn(cookies);

        csrfHeaderFilter.doFilterInternal(request, response, filterChain);

        verify(request).getAttribute(CsrfToken.class.getName());
        verify(request).getCookies();
        verify(response).addCookie(any(Cookie.class));
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }

    @Test
    public void shouldAddCookieToResponseWhenCSRFTokenIsNotNullAndCookieIsNull() throws ServletException, IOException {
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(new DefaultCsrfToken("header", "parameter", "X-CSRF"));
        when(request.getCookies()).thenReturn(null);

        csrfHeaderFilter.doFilterInternal(request, response, filterChain);

        verify(request).getAttribute(CsrfToken.class.getName());
        verify(request).getCookies();
        verify(response).addCookie(any(Cookie.class));
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(request, response, filterChain);
    }
}
