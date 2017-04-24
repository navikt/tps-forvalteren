package no.nav.tps.forvalteren.provider.rs.security.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.addHeader("WWW-Authenticate", "xBasic realm=\"tps-forvalteren\"");
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not authenticated");
    }
}
