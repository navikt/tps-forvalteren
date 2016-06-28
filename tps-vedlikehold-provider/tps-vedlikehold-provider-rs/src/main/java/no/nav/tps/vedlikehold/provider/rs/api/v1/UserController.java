package no.nav.tps.vedlikehold.provider.rs.api.v1;

import no.nav.tps.vedlikehold.domain.rs.User;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * Endpoint for user related requests in the REST API
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600) //This should be handled properly
@RequestMapping(value = "api/v1")
public class UserController {

    @Autowired
    public UserContextHolder userContextHolder;

    /**
     * Get an object representing the user
     *
     * @param session current HTTP session
     * @return user object representing the current user
     */

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(@ApiIgnore HttpSession session) {

        /* Convert user roles to a set of strings */
        Set<String> roles = new HashSet<String>();
        for (GrantedAuthority role :  userContextHolder.getRoles()) {
            if ( role == null ) {
                continue;
            }

            roles.add( role.getAuthority() );
        }

        return new User(
                userContextHolder.getDisplayName(),
                userContextHolder.getUsername(),
                roles,
                session.getId()
        );
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public void logout(@ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) {
        userContextHolder.logout(request, response);
    }
}
