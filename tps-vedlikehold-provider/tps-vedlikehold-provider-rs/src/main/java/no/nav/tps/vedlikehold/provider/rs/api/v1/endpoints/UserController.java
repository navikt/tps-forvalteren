package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import no.nav.tps.vedlikehold.provider.rs.api.v1.strategies.user.UserContextUserFactoryStrategy;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.user.DefaultUserFactory;
import no.nav.tps.vedlikehold.service.command.user.UserFactory;
import no.nav.tps.vedlikehold.service.command.user.UserFactoryStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

/**
 * Endpoint for user related requests in the REST API
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@RestController
@RequestMapping(value = "api/v1")
public class UserController {

    @Autowired
    public UserContextHolder userContextHolder;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(@ApiIgnore HttpSession session) {
        UserFactoryStrategy strategy = new UserContextUserFactoryStrategy(userContextHolder, session);
        UserFactory userFactory      = new DefaultUserFactory();

        return userFactory.createUser(strategy);
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public void logout(@ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) {
        userContextHolder.logout(request, response);
    }
}
