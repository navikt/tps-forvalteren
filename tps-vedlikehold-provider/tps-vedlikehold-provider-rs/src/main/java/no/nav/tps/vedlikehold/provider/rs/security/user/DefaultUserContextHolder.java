package no.nav.tps.vedlikehold.provider.rs.security.user;

import no.nav.tps.vedlikehold.domain.service.user.User;
import no.nav.tps.vedlikehold.service.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.user.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the UserContextHolder interface using spring security
 */

@Service
public class DefaultUserContextHolder implements UserContextHolder {

    @Override
    public String getDisplayName() {
        return getUserDetails().getDn();
    }

    @Override
    public String getUsername() {
        return getUserDetails().getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    @Override
    public User getUser() {
        return new User(getDisplayName(), getUsername());
    }

    @Override
    public Set<UserRole> getRoles() {
        return new HashSet<>((Collection<UserRole>)getAuthentication().getAuthorities());
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private LdapUserDetails getUserDetails() {
        Object userDetails = getAuthentication().getPrincipal();

        Boolean isUserDetails = userDetails instanceof LdapUserDetails;

        if ( !isUserDetails ) {
            throw new RuntimeException("User details is an incorrect type: " + userDetails.getClass());
        }

        return (LdapUserDetails) userDetails;
    }
}
