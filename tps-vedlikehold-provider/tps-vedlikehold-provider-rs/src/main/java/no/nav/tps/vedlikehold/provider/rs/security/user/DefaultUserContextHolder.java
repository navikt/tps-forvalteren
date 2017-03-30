package no.nav.tps.vedlikehold.provider.rs.security.user;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.nav.tps.vedlikehold.domain.service.User.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

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
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    @Override
    public User getUser() {
        Set<String> roles = getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toSet());

        return new User(getDisplayName(), getUsername(), roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles() {
        return getUserDetails().getAuthorities();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        if ( isAuthenticated() ) {
            new SecurityContextLogoutHandler().logout(request, response, getAuthentication());
        }
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
