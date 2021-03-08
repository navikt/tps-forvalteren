package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import no.nav.tps.forvalteren.common.logging.LogExceptions;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1")
public class UserController {

    private final UserContextHolder userContextHolder;
    private final HttpSession httpSession;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @LogExceptions
    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser() {
        User user = userContextHolder.getUser();
        user.setToken(httpSession.getId());
        return user;
    }

    @LogExceptions
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public void logout() {
        if (userContextHolder.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse,
                    SecurityContextHolder.getContext().getAuthentication());
        }
    }}
