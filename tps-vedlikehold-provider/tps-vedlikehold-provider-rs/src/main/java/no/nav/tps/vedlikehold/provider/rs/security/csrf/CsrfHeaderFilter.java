package no.nav.tps.vedlikehold.provider.rs.security.csrf;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for handling: Cross-Site Request Forgery (CSRF) token used by AngularJs
 */


public class CsrfHeaderFilter extends OncePerRequestFilter {
    private String cookiePath;

    public CsrfHeaderFilter(String cookiePath) {
        super();
        this.cookiePath = cookiePath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrf != null) {
            Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
            String token  = csrf.getToken();

            if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                cookie = new Cookie("XSRF-TOKEN", token);
                cookie.setPath(cookiePath);
                response.addCookie(cookie);
            }
        }

        filterChain.doFilter(request, response);
    }
}
