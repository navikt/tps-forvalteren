package no.nav.tps.vedlikehold.provider.rs.security.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.addHeader("WWW-Authenticate", "xBasic realm=\"tps-vedlikehold\"");
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not authenticated");
    }
}
