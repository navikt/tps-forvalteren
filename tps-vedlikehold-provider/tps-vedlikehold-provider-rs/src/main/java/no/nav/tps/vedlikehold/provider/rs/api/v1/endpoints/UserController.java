package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import no.nav.freg.metrics.annotations.Metrics;
import no.nav.tps.vedlikehold.domain.service.user.User;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static no.nav.tps.vedlikehold.provider.rs.config.ProviderConstants.OPERATION;
import static no.nav.tps.vedlikehold.provider.rs.config.ProviderConstants.RESTSERVICE;


@RestController
@RequestMapping(value = "api/v1")
public class UserController {

    private static final String REST_SERVICE_NAME = "user";

    @Autowired
    public UserContextHolder userContextHolder;

    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "getUser")})
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(@ApiIgnore HttpSession session) {
        User user = userContextHolder.getUser();
        user.setToken(session.getId());
        return user;
    }

    @Metrics(value = "provider", tags = {@Metrics.Tag(key = RESTSERVICE, value = REST_SERVICE_NAME), @Metrics.Tag(key = OPERATION, value = "logout")})
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public void logout(@ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) {
        userContextHolder.logout(request, response);
    }
}
