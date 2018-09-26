package no.nav.tps.forvalteren.provider.rs.security.user;

import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
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
        if(getUserDetails() == null) {
            return "ananomys";
        }
        return getUserDetails().getDn();
    }

    @Override
    public String getUsername() {
        if(getUserDetails() == null) {
            return "ananomys_user";
        }
        return getUserDetails().getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    @Override
    public User getUser() {
        if(getAuthentication() == null || getAuthentication().getPrincipal().equals("anonymousUser")){
            return new User("anonymousUser", "anonymousUser");
        }
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
        if(getAuthentication() == null){
            return null;
        }

        Object userDetails = getAuthentication().getPrincipal();

        Boolean isUserDetails = userDetails instanceof LdapUserDetails;

        if ( !isUserDetails ) {
            return null;
//            throw new TpsfFunctionalException("User details is an incorrect type: " + userDetails.getClass());
        }

        return (LdapUserDetails) userDetails;
    }
}
