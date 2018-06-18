package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.provider.rs.config.ProviderConstants;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "api/v1")
public class UserController {

    private static final String REST_SERVICE_NAME = "user";

    @Autowired
    private UserContextHolder userContextHolder;

    @Metrics(value = "provider", tags = { @Metrics.Tag(key = ProviderConstants.RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = ProviderConstants.OPERATION, value = "getUser") })
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(@ApiIgnore HttpSession session) {
        return new User("Test", "Bruker");
//        User user = userContextHolder.getUser();
//        user.setToken(session.getId());
//        return user;
    }

    @Metrics(value = "provider", tags = { @Metrics.Tag(key = ProviderConstants.RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = ProviderConstants.OPERATION, value = "logout") })
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public void logout(@ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) {
        logoutUser(request, response);
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        if (userContextHolder.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        }
    }
}
