package no.nav.tps.vedlikehold.provider.rs.security.user;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.nav.tps.vedlikehold.domain.service.user.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Abstraction of the user context for testability and reusability
 */

public interface UserContextHolder {
    String getDisplayName();
    String getUsername();

    Authentication getAuthentication();
    boolean isAuthenticated();

    User getUser();

    Collection<? extends GrantedAuthority> getRoles();

    void logout(HttpServletRequest request, HttpServletResponse response);
}
