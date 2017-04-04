package no.nav.tps.vedlikehold.provider.rs.security.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component
public class MDCInterceptor implements HandlerInterceptor {

    static final String USER_KEY = "userId";

    @Autowired
    private UserContextHolder userContextHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            MDC.put(USER_KEY, userContextHolder.getUsername());
        } catch (RuntimeException exception){
            return true;                            // Exception is thrown if this is accessed before the user context is configured
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(USER_KEY);
    }
}