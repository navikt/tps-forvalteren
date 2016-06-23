package no.nav.tps.vedlikehold.provider.rs.api.v1;

import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by G148232 on 22.06.2016.
 */
@RestController
@RequestMapping(value = "api/v1")
public class UserController {

    @Autowired
    public UserContextHolder userContextHolder;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Set<String> getUser() {
        Set<String> roles = new HashSet<String>();

        for (GrantedAuthority role :  userContextHolder.roles()) {
            if ( role == null ) continue;

            roles.add( role.getAuthority() );
        }

        return roles;
    }
}
