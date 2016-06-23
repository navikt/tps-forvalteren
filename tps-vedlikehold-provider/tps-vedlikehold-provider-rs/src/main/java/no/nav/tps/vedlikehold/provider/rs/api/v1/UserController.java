package no.nav.tps.vedlikehold.provider.rs.api.v1;

import no.nav.tps.vedlikehold.domain.rs.User;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * Endpoint for user related requests in the REST API
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@RestController
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
    public User getUser(HttpSession session) {

        /* Convert user roles to a set of strings */
        Set<String> roles = new HashSet<String>();
        for (GrantedAuthority role :  userContextHolder.roles()) {
            if ( role == null ) continue;
            roles.add( role.getAuthority() );
        }

        return new User(
                userContextHolder.getDisplayName(),
                userContextHolder.getUsername(),
                roles,
                session.getId()
        );
    }
}
