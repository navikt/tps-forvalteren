package no.nav.tps.vedlikehold.provider.rs.security.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * Abstraction of the user context for testability and reusability
 *
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public interface UserContextHolder {
    String getDistinguishedName();
    String getUsername();

    Authentication getAuthentication();
    Boolean isAuthenticated();

    Collection<? extends GrantedAuthority> getRoles();

    void logout(HttpServletRequest request, HttpServletResponse response);
}
