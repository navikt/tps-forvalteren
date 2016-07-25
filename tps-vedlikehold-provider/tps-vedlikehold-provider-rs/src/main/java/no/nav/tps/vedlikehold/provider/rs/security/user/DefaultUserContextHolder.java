package no.nav.tps.vedlikehold.provider.rs.security.user;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

/**
 * Implementation of the UserContextHolder interface using spring security
 * 
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Service
public class DefaultUserContextHolder implements UserContextHolder {

    /**
     * Get the display name of the currently authenticated user
     *
     * @return display name of the authenticated user
     */

    @Override
    public String getDisplayName() {
        return getUserDetails().getDn();
    }

    /**
     * Get the username of the currently authenticated user
     *
     * @return username of the authenticated user
     */

    @Override
    public String getUsername() {
        return getUserDetails().getUsername();
    }

    /**
     * Get an object containing information about the authenticated user
     *
     * @return An Authentication object
     */

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Check if the current user is authenticated
     *
     * @return boolean indicating whether the user is authenticated
     */

    @Override
    public Boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * Get all roles for the authenticated user
     *
     * @return collection of the authenticated users roles
     */

    @Override
    public Collection<? extends GrantedAuthority> getRoles() {
        return getUserDetails().getAuthorities();
    }

    /**
     * Logout the authenticated user
     */

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        if ( isAuthenticated() ) {
            new SecurityContextLogoutHandler().logout(request, response, getAuthentication());
        }
    }

    /**
     * Retrieve user details for the authenticated user from LDAP
     *
     * @throws RuntimeException if the user details retrieved is not an LdapUserDetails object
     *
     * @return User details as an LdapUserDetails object
     */

    private LdapUserDetails getUserDetails() {
        Object userDetails = getAuthentication().getPrincipal();

        Boolean isUserDetails = userDetails instanceof LdapUserDetails;

        if ( !isUserDetails ) {
            throw new RuntimeException("User details is an incorrect type: " + userDetails.getClass());
        }

        return (LdapUserDetails) userDetails;
    }
}
