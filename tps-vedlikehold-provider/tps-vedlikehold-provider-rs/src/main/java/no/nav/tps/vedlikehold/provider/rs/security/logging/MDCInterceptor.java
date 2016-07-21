package no.nav.tps.vedlikehold.provider.rs.security.logging;

import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Component
public class MDCInterceptor implements HandlerInterceptor {

    private static final String USER_ID_ATTRIBUTE_NAME = "userId";

    @Autowired
    private UserContextHolder userContextHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(USER_ID_ATTRIBUTE_NAME, userContextHolder.getUsername());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(USER_ID_ATTRIBUTE_NAME);
    }
}