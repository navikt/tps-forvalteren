package no.nav.tps.forvalteren.provider.rs.security.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the UserContextHolder interface using spring security
 */

@Service
public class DefaultUserContextHolder implements UserContextHolder {

    private static final String ANONYMOUS_USER = "anonymousUser";

    @Override
    public String getDisplayName() {
        return Optional.ofNullable(getUserDetails()).map(userDetails -> userDetails.getDn()).orElse("anonymous");
    }

    @Override
    public String getUsername() {
        return Optional.ofNullable(getUserDetails()).map(userDetails -> userDetails.getUsername()).orElse("anonymous_user");
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    @Override
    public User getUser() {
        Authentication authentication = getAuthentication();
        return authentication == null || ANONYMOUS_USER.equals(authentication.getPrincipal()) ?
                new User(ANONYMOUS_USER, ANONYMOUS_USER) :
                new User(getDisplayName(), getUsername());
    }

    @Override
    public Set<UserRole> getRoles() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.getAuthorities() != null ?
                new HashSet<>((Collection<UserRole>) authentication.getAuthorities()) : null;
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext() != null ?
                SecurityContextHolder.getContext().getAuthentication() : null;
    }

    private LdapUserDetails getUserDetails() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof LdapUserDetails ?
                (LdapUserDetails) authentication.getPrincipal() : null;
    }
}
